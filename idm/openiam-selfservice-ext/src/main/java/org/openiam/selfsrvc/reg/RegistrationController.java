package org.openiam.selfsrvc.reg;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ws.PropertyMap;
import org.openiam.idm.srvc.continfo.dto.Address;
import org.openiam.idm.srvc.continfo.dto.ContactConstants;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.continfo.dto.Phone;
import org.openiam.idm.srvc.mngsys.dto.ApproverAssociation;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.msg.service.MailService;
import org.openiam.idm.srvc.msg.service.MailTemplateParameters;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.prov.request.dto.RequestApprover;
import org.openiam.idm.srvc.prov.request.dto.RequestUser;
import org.openiam.idm.srvc.prov.request.ws.RequestWebService;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.openiam.idm.srvc.user.dto.DelegationFilterSearch;
import org.openiam.idm.srvc.user.dto.Supervisor;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.provision.dto.ProvisionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.tags.form.OptionTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/pub/registration")
public class RegistrationController {
    private static final Log LOG = LogFactory.getLog(RegistrationController.class);

    private static final String NEW_PENDING_REQUEST_NOTIFICATION = "NEW_PENDING_REQUEST";
    private static final String REQUEST_TYPE = "255";

    @Autowired
    private OrganizationDataService orgServiceClient;

    @Autowired
    private RoleDataWebService roleServiceClient;

    @Autowired
    private UserDataWebService userServiceClient;

    @Autowired
    private MailService mailServiceClient;

    @Autowired
    private RequestWebService provRequestServiceClient;

    @Autowired
    private ResourceDataService resourceServiceClient;

    @Autowired
    private ManagedSystemDataService managedSysServiceClient;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edit")
    public String getEditForm(Model model, HttpServletRequest request) {
        LOG.info("Edit registration form called.");
        editFormInitialization(model);
        model.addAttribute("registrationCommand", new RegistrationCommand());
        return "/pub/selfreg/edit";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/confirm")
    public String getConfirmForm(Model model, HttpServletRequest request) {
        LOG.info("Confirmation form called after registration.");
        return "/pub/confirm";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit")
    public String save(Model model, RegistrationCommand command, BindingResult result, HttpServletRequest request) {
        RegistrationValidator.validate(command, result);
        if (!result.hasErrors()) {
            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("userId");

            User user = prepareObject(command, userId);

            LOG.info("User=" + user);

            ProvisionUser pUser = new ProvisionUser(user);

            setAddress(command, pUser);
            setPhone(command, pUser);


            if (StringUtils.isNotEmpty(command.getRoleId())) {
                pUser.setMemberOfRoles(getRoleList(command, user));
            }

            /* serialize the object into xml */
            String userAsXML = toXML(pUser);
            LOG.info("pUser serialized to XML:" + userAsXML);
            ProvisionRequest pReq = createRequest(userId, user, userAsXML, pUser);

            return "/pub/confirm";
        } else {
            model.addAttribute("registrationCommand", command);
            model.addAttribute("errors",result);
            editFormInitialization(model);
            return "/pub/selfreg/edit";
        }
    }

    private ProvisionRequest createRequest(String userId, User usr, String asXML, ProvisionUser pUser) {
        String approverId = null;

        Resource newUserResource = resourceServiceClient.getResource(REQUEST_TYPE);

        Date curDate = new Date(System.currentTimeMillis());


        ProvisionRequest req = new ProvisionRequest();
        req.setRequestorId(null);
        req.setRequestorLastName("ANONYMOUS");
        req.setStatus("PENDING");
        req.setStatusDate(curDate);
        req.setRequestDate(curDate);
        req.setRequestType(newUserResource.getResourceId());
        req.setWorkflowName(newUserResource.getName());
        req.setRequestTitle(newUserResource.getDescription() + " FOR:" + usr.getFirstName() + " " + usr.getLastName());
        req.setRequestorFirstName(usr.getFirstName());
        req.setRequestorLastName(usr.getLastName());


        if (usr.getCompanyId() != null && usr.getCompanyId().length() > 0) {
            req.setRequestForOrgId(usr.getCompanyId());
        }

        req.setRequestXML(asXML);


        // add a user to the request - this is the person we are making the request for
        Set<RequestUser> reqUserSet = req.getRequestUsers();
        RequestUser reqUser = new RequestUser();
        reqUser.setFirstName(usr.getFirstName());
        reqUser.setLastName(usr.getLastName());
        reqUserSet.add(reqUser);

        String approverRole = null;
        String userOrg = null;
        int applyDelegationFilter = 0;

        // add the approver to the request object.
        List<ApproverAssociation> apList = managedSysServiceClient.getApproverByRequestType(REQUEST_TYPE, 1);
        if (apList != null) {

            for (ApproverAssociation ap : apList) {
                String approverType;
                String roleDomain = null;

                if (ap != null) {
                    approverType = ap.getAssociationType();
                    LOG.info("Approver =" + ap.getApproverRoleId());

                    if (ap.getAssociationType().equalsIgnoreCase("SUPERVISOR")) {
                        Supervisor supVisor = pUser.getSupervisor();
                        approverId = supVisor.getSupervisor().getUserId();


                    } else if (ap.getAssociationType().equalsIgnoreCase("ROLE")) {
                        approverId = ap.getApproverRoleId();
                        roleDomain = ap.getApproverRoleDomain();

                        approverRole = ap.getApproverRoleId();
                        if (ap.getApplyDelegationFilter() != null) {
                            applyDelegationFilter = ap.getApplyDelegationFilter().intValue();
                        }
                        if (usr.getCompanyId() != null) {
                            userOrg = usr.getCompanyId();
                        }


                    } else {
                        approverId = ap.getApproverUserId();
                    }


                    RequestApprover reqApprover = new RequestApprover(approverId, ap.getApproverLevel(),
                            ap.getAssociationType(), "PENDING");
                    reqApprover.setApproverType(approverType);
                    reqApprover.setRoleDomain(roleDomain);

                    req.getRequestApprovers().add(reqApprover);
                }

            }
        }

        provRequestServiceClient.addRequest(req);

        notifyApprover(req, reqUser, usr, approverRole, userOrg, applyDelegationFilter);

        return req;
    }

    private String toXML(ProvisionUser pUser) {
        XStream xstream = new XStream();
        return xstream.toXML(pUser);

    }

    private List<Role> getRoleList(RegistrationCommand newHireCmd, User user) {

        List<Role> roleList = new ArrayList<Role>();
        String cmdRole = newHireCmd.getRoleId();


        StringTokenizer rl = new StringTokenizer(cmdRole, ",");
        while (rl.hasMoreTokens()) {

            String roleElement = rl.nextToken();

            /* parse the role */
            String domainId = null;
            String roleId = null;

            StringTokenizer st = new StringTokenizer(roleElement, "*");
            if (st.hasMoreTokens()) {
                domainId = st.nextToken();
            }
            if (st.hasMoreElements()) {
                roleId = st.nextToken();
            }
            RoleId id = new RoleId(domainId, roleId);
            Role r = new Role();
            r.setId(id);
            roleList.add(r);
        }

        return roleList;

    }

    private void editFormInitialization(Model model) {
        // get the organizations
        List<Organization> orgList = orgServiceClient.getOrganizationList(null, "ACTIVE");
        model.addAttribute("orgList", orgList);
        // get the list of roles that this user belongs to
        List<Role> roleList = roleServiceClient.getAllRoles().getRoleList();
        List<OptionTag> roles = getOptionTags(roleList);
        model.addAttribute("roleList", roles);
    }

    private List<OptionTag> getOptionTags(List<Role> roleList) {
        List<OptionTag> roles = new ArrayList<OptionTag>(roleList.size());
        for(Role role : roleList) {
            OptionTag ot = new OptionTag();
            ot.setLabel(role.getRoleName());
            ot.setValue(role.getId().getRoleId()+"*"+role.getId().getServiceId());
            roles.add(ot);
        }
        return roles;
    }

    private User prepareObject(RegistrationCommand command, String userId) {
        // need userId to be null so that persistence layer will generate a uid for the user
        User user = new User();
        user.setUserId(null);
        user.setFirstName(command.getFirstName());
        user.setLastName(command.getLastName());
        user.setMaidenName(command.getMaidenName());
        user.setAddress1(command.getAddress1());
        user.setAddress2(command.getAddress2());
        user.setBirthdate(command.getBirthdate());
        user.setNickname(command.getNickname());
        user.setCity(command.getCity());
        user.setCompanyId(command.getCompanyId());
        user.setCreateDate(new Date(System.currentTimeMillis()));
        user.setCreatedBy(userId);
        EmailAddress em1 = new EmailAddress();
        em1.setEmailAddress(command.getEmail1());
        em1.setParentType(ContactConstants.PARENT_TYPE_USER);
        em1.setName("EMAIL1");
        user.setEmail(command.getEmail1());
        user.getEmailAddresses().add(em1);
        user.setStatus(UserStatusEnum.PENDING_APPROVAL);
        return user;
    }

    private Phone buildPhone(ProvisionUser usr, String name,
                             String areaCode, String phone) {
        Phone ph = new Phone();

        ph.setAreaCd(areaCode);
        ph.setPhoneNbr(phone);
        ph.setDescription(name);
        ph.setParentType(ContactConstants.PARENT_TYPE_USER);
        ph.setName(name);
        ph.setParentId(usr.getUserId());

        return ph;
    }

    private void setPhone(RegistrationCommand cmd, ProvisionUser usr) {
       Phone ph = buildPhone(usr, "DESK PHONE", cmd.getWorkAreaCode(), cmd.getWorkPhone());
        if (cmd.getWorkPhone() != null && cmd.getWorkPhone().length() > 0) {
            ph.setPhoneId(cmd.getWorkPhone());
        }
        usr.setAreaCd(ph.getAreaCd());
        usr.setPhoneNbr(ph.getPhoneNbr());
        usr.getPhones().add(ph);

        ph = buildPhone(usr, "CELL PHONE", cmd.getCellAreaCode(), cmd.getCellPhone());
        LOG.info("CELL PHONE: " + cmd.getCellPhone());
        if (cmd.getCellPhone() != null && cmd.getCellPhone().length() > 0) {
            ph.setPhoneId(cmd.getCellPhone());
        }
        usr.getPhones().add(ph);

        ph = buildPhone(usr, "FAX", cmd.getFaxAreaCode(), cmd.getFaxPhone());
        if (cmd.getFaxPhone() != null && cmd.getFaxPhone().length() > 0) {
            ph.setPhoneId(cmd.getFaxPhone());
        }
        usr.getPhones().add(ph);

        ph = buildPhone(usr, "HOME PHONE", cmd.getHomePhoneAreaCode(), cmd.getHomePhoneNbr());
        if (cmd.getHomePhoneNbr() != null && cmd.getHomePhoneNbr().length() > 0) {
            ph.setPhoneId(cmd.getHomePhoneNbr());
        }
        usr.getPhones().add(ph);

        ph = buildPhone(usr, "ALT CELL PHONE", cmd.getAltCellAreaCode(), cmd.getAltCellNbr());
        if (cmd.getAltCellNbr() != null && cmd.getAltCellNbr().length() > 0) {
            ph.setPhoneId(cmd.getAltCellNbr());
        }
        usr.getPhones().add(ph);
    }

    private void setAddress(RegistrationCommand cmd, ProvisionUser pUser) {
        LOG.info("setAddress called.");
        Address adr = new Address();
        adr.setAddress1(cmd.getAddress1());
        adr.setAddress2(cmd.getAddress2());
        adr.setCity(cmd.getCity());
        adr.setState(cmd.getState());
        adr.setName("DEFAULT ADR");
        adr.setParentId(pUser.getUser().getUserId());
        adr.setParentType(ContactConstants.PARENT_TYPE_USER);
        adr.setPostalCd(cmd.getPostalCd());
        pUser.getAddresses().add(adr);
    }

    private void notifyApprover(ProvisionRequest pReq, RequestUser reqUser, User usr,
                                String approverRole, String userOrg, int applyDelegationFilter) {


        Set<RequestApprover> approverList = pReq.getRequestApprovers();
        for (RequestApprover ra : approverList) {




            if (!ra.getApproverType().equalsIgnoreCase("ROLE")) {

                User approver =  userServiceClient.getUserWithDependent(ra.getApproverId(), false).getUser();

                HashMap<String, String> mailParameters = new HashMap<String, String>();
                mailParameters.put(MailTemplateParameters.USER_ID.value(), ra.getApproverId());
                mailParameters.put(MailTemplateParameters.REQUEST_ID.value(), pReq.getRequestId());
                mailParameters.put(MailTemplateParameters.REQUESTER.value(), usr.getFirstName() + " " + usr.getLastName());
                mailParameters.put(MailTemplateParameters.REQUEST_REASON.value(), pReq.getRequestTitle());
                mailParameters.put(MailTemplateParameters.TARGET_USER.value(), reqUser.getFirstName() + " " + reqUser.getLastName());

                if (approver != null && approver.getEmail() != null  ) {
                    mailParameters.put(MailTemplateParameters.TO.value(), approver.getEmail());
                }

                mailServiceClient.sendNotification(NEW_PENDING_REQUEST_NOTIFICATION, new PropertyMap(mailParameters));
            } else {
                // approverType is ROLE
                // get

                LOG.info("notifyApprover: Approver type = " + ra.getApproverType());

                DelegationFilterSearch search = new DelegationFilterSearch();
                search.setRole(approverRole);
                search.setDelAdmin(applyDelegationFilter);
                search.setOrgFilter("%" + userOrg + "%");


                List<User> roleApprovers = userServiceClient.searchByDelegationProperties(search).getUserList();

                if (roleApprovers != null && !roleApprovers.isEmpty()) {
                    for (User u : roleApprovers) {

                        LOG.info("notifyApprover:: Sending message to:   " + u);

                        HashMap<String, String> mailParameters = new HashMap<String, String>();
                        mailParameters.put(MailTemplateParameters.USER_ID.value(), u.getUserId());
                        mailParameters.put(MailTemplateParameters.REQUEST_ID.value(), pReq.getRequestId());
                        mailParameters.put(MailTemplateParameters.REQUESTER.value(), usr.getFirstName() + " " + usr.getLastName());
                        mailParameters.put(MailTemplateParameters.REQUEST_REASON.value(), pReq.getRequestReason());
                        mailParameters.put(MailTemplateParameters.TARGET_USER.value(), reqUser.getFirstName() + " " + reqUser.getLastName());

                        if (u.getEmail() != null) {
                            mailParameters.put(MailTemplateParameters.TO.value(), u.getEmail());
                        }

                        mailServiceClient.sendNotification(NEW_PENDING_REQUEST_NOTIFICATION, new PropertyMap(mailParameters));

                    }

                }

            }

        }


    }

}
