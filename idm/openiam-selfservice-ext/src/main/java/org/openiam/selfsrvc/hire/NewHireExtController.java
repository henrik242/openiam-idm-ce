package org.openiam.selfsrvc.hire;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.util.StringUtils;
import org.openiam.base.AttributeOperationEnum;
import org.openiam.base.ws.PropertyMap;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.ws.IdmAuditLogWebDataService;
import org.openiam.idm.srvc.cd.dto.ReferenceData;
import org.openiam.idm.srvc.cd.service.ReferenceDataService;
import org.openiam.idm.srvc.continfo.dto.Address;
import org.openiam.idm.srvc.continfo.dto.ContactConstants;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.continfo.dto.Phone;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.ws.GroupDataWebService;
import org.openiam.idm.srvc.loc.dto.Location;
import org.openiam.idm.srvc.loc.ws.LocationDataWebService;
import org.openiam.idm.srvc.meta.dto.MetadataElement;
import org.openiam.idm.srvc.meta.dto.MetadataType;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
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
import org.openiam.idm.srvc.user.dto.*;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/priv/newhire")
public class NewHireExtController {
    public static final String NEW_PENDING_REQUEST_NOTIFICATION = "NEW_PENDING_REQUEST";

    private static final Log LOG = LogFactory.getLog(NewHireExtController.class);
    public static final String USER_CATEGORY_TYPE = "USER_TYPE";
    public static final String REQUEST_TYPE = "260";

    @Autowired
    private MetadataWebService metadataServiceClient;

    @Autowired
    private OrganizationDataService orgServiceClient;

    @Autowired
    private RoleDataWebService roleServiceClient;

    @Autowired
    private ReferenceDataService referenceDataServiceClient;

    @Autowired
    private GroupDataWebService groupServiceClient;

    @Autowired
    private LocationDataWebService locationServiceClient;

    @Autowired
    private MailService mailServiceClient;

    @Autowired
    private RequestWebService provRequestServiceClient;

    @Autowired
    private ResourceDataService resourceServiceClient;

    @Autowired
    private IdmAuditLogWebDataService auditServiceClient;

    @Autowired
    private UserDataWebService userServiceClient;

    @Autowired
    private ManagedSystemDataService managedSysServiceClient;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
    }


    @RequestMapping(method = RequestMethod.GET, value = "/edit")
    public String getSelecttypeForm(Model model, HttpServletRequest request) {
        LOG.info("New hire select type of user view form called.");

        MetadataType[] typeAry = metadataServiceClient.getTypesInCategory(USER_CATEGORY_TYPE).getMetadataTypeAry();
        model.addAttribute("metadataTypeAry", typeAry);
        model.addAttribute("newHireCommand", new NewHireExtCommand());

        return "/priv/newhire/selusertype";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit")
    public String getUserDetailsForm(Model model, NewHireExtCommand hireExtCommand, BindingResult result, HttpServletRequest request) {
        LOG.info("New hire select type of user post form called.");
        NewHireExtValidator.validateSelectUserTypeForm(hireExtCommand, result);
        if (!result.hasErrors()) {
            userDetailsFormInitialization(model, hireExtCommand.getMetadataTypeId());
            model.addAttribute("newHireCommand", hireExtCommand);
            return "/priv/newhire/edit";
        } else {
            MetadataType[] typeAry = metadataServiceClient.getTypesInCategory(USER_CATEGORY_TYPE).getMetadataTypeAry();
            model.addAttribute("metadataTypeAry", typeAry);
            model.addAttribute("newHireCommand", new NewHireExtCommand());
            model.addAttribute("errors",result);
            return "/priv/newhire/selusertype";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public String saveForm(Model model, NewHireExtCommand hireExtCommand, BindingResult result, HttpServletRequest request) {
        LOG.info("New hire save post action called.");

        if(StringUtils.isNotEmpty(hireExtCommand.getSubmitAction())) {
           if("_cancel".equals(hireExtCommand.getSubmitAction())) {
               return (String)request.getSession().getAttribute("backUrl");
           } else if("_prev".equals(hireExtCommand.getSubmitAction())) {
               MetadataType[] typeAry = metadataServiceClient.getTypesInCategory(USER_CATEGORY_TYPE).getMetadataTypeAry();
               model.addAttribute("metadataTypeAry", typeAry);
               model.addAttribute("newHireCommand", hireExtCommand);
               return "/priv/newhire/selusertype";
           }
        }
        NewHireExtValidator.validateSaveForm(hireExtCommand, result);
        if (!result.hasErrors()) {
             fireNewHireRequest(hireExtCommand, request);
             return "/priv/confirm";
        } else {
            userDetailsFormInitialization(model, hireExtCommand.getMetadataTypeId());
            model.addAttribute("newHireCommand", hireExtCommand);
            model.addAttribute("errors",result);
            return "/priv/newhire/edit";
        }
    }

    private void fireNewHireRequest(NewHireExtCommand newHireCmd, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        User user = prepareObject(newHireCmd, userId);

        String[] attrNames = newHireCmd.getMetaAttrName();

        for(int i =0; i < attrNames.length; i++) {
            UserAttribute ua = new UserAttribute(newHireCmd.getMetaAttrName()[i],newHireCmd.getMetaAttrValue()[i]);
            ua.setOperation(AttributeOperationEnum.ADD);
            ua.setUserId(null);
            ua.setId(null);
            ua.setRequired(Boolean.parseBoolean(newHireCmd.getMetaAttrRequired()[i]));
            ua.setMetadataElementId(newHireCmd.getMetaAttrId()[i]);
            user.getUserAttributes().put(ua.getName(), ua);
            LOG.info("Name=" + ua.getName() + "-" + ua.getValue());
        }

        LOG.info("User=" + user);

        ProvisionUser pUser = new ProvisionUser(user);
        if (StringUtils.isNotEmpty(newHireCmd.getSupervisorId())) {
            User supervisorUser = new User(newHireCmd.getSupervisorId());
            Supervisor sup = new Supervisor();
            sup.setSupervisor(supervisorUser);
            sup.setStatus("ACTIVE");
            sup.setSupervisor(supervisorUser);
            pUser.setSupervisor(sup);
        }

        // set contact information
        setEmail(newHireCmd, pUser);
        setAddress(newHireCmd, pUser);
        setPhone(newHireCmd, pUser);


        if (StringUtils.isNotEmpty(newHireCmd.getGroupId())) {
            pUser.setMemberOfGroups(getGroupList(newHireCmd, user));
        }
        if (StringUtils.isNotEmpty(newHireCmd.getRoleId())) {
            pUser.setMemberOfRoles(getRoleList(newHireCmd, user));
        }


        LOG.info("User created. New User Id: " + user.getUserId());


        /* serialize the object into xml */
        String userAsXML = toXML(pUser);
        LOG.info("pUser serialized to XML:" + userAsXML);

        ProvisionRequest pReq = createRequest(userId, user, userAsXML, pUser);


        // log the request


        IdmAuditLog auditLog = new IdmAuditLog(
                new Date(System.currentTimeMillis()), "PENDING_REQUEST",
                "SUCCESS", null, null,
                request.getRemoteHost(), 1, null, null,
                null, (String)request.getSession().getAttribute("login"), "NEW_USER_WORFKLOW",
                ProvisionRequest.NEW_USER_WORKFLOW, pReq.getRequestTitle(), null,
                null, null, (String)request.getSession().getAttribute("domain"), pReq.getRequestorId() );

        auditLog.setRequestId(pReq.getRequestId());
        auditLog.setSessionId(request.getSession().getId());


        auditServiceClient.addLog(auditLog);
    }

    private List<Group> getGroupList(NewHireExtCommand newHireCmd, User user) {
        List<Group> groupList = new ArrayList<Group>();
        String groupId = newHireCmd.getGroupId();
        Group g = new Group();
        g.setGrpId(groupId);
        groupList.add(g);
        return groupList;
    }

    private List<Role> getRoleList(NewHireExtCommand newHireCmd, User user) {
        List<Role> roleList = new ArrayList<Role>();
        String cmdRole = newHireCmd.getRoleId();
        /* parse the role */
        String domainId = null;
        String roleId = null;

        StringTokenizer st = new StringTokenizer(cmdRole, "*");
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

        return roleList;
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

    private User prepareObject(NewHireExtCommand command, String userId) {
        User user = new User();
        user.setFirstName(command.getFirstName());
        user.setLastName(command.getLastName());
        user.setMiddleInit(command.getMiddleName());
        user.setAddress1(command.getAddress1());
        user.setAddress2(command.getAddress2());
        user.setCity(command.getCity());
        user.setCompanyId(command.getCompanyId());
        user.setCreateDate(new Date(System.currentTimeMillis()));
        user.setCreatedBy(userId);
        user.setStartDate(command.getStartDate());
        user.setLastDate(command.getLastDate());
        user.setStatus(UserStatusEnum.PENDING_APPROVAL);
        user.setTitle(command.getTitle());
        user.setDeptCd(command.getDeptCd());
        user.setJobCode(command.getJobCode());
        user.setEmployeeType(command.getEmployeeType());
        user.setLocationCd(command.getLocationCd());
        user.setBldgNum(command.getBldgNum());
        user.setAddress1(command.getAddress1());
        user.setAddress2(command.getAddress2());
        user.setCity(command.getCity());
        user.setState(command.getState());
        user.setPostalCd(command.getPostalCd());
        return user;
    }
    private String toXML(ProvisionUser pUser) {
        XStream xstream = new XStream();
        return xstream.toXML(pUser);

    }

    private void userDetailsFormInitialization(Model model, String selectedMetadataTypeId) {
        //get the organizations
        List<Organization> orgList = orgServiceClient.getOrganizationList(null, "ACTIVE");
        model.addAttribute("orgList", orgList);
        // get the list of roles that this user belongs to
        List<Role> roleList = roleServiceClient.getAllRoles().getRoleList();
        List<OptionTag> roles = getRoleOptionTags(roleList);
        model.addAttribute("roleList", roles);
        // load the department list
        List<Organization> deptList = orgServiceClient.allDepartments(null);
        model.addAttribute("deptList", deptList);
        // get the list of job codes
        List<ReferenceData> jobCodeList = referenceDataServiceClient.getRefByGroup("JOB_CODE", "en");
        List<OptionTag> jobCodeOptions = getOptionTagsFromReferencesData(jobCodeList);
        model.addAttribute("jobCodeList", jobCodeOptions);
        // get the list of user type codes
        List<ReferenceData> userTypeList = referenceDataServiceClient.getRefByGroup("USER_TYPE", "en");
        List<OptionTag> userTypeOptions = getOptionTagsFromReferencesData(userTypeList);
        model.addAttribute("userTypeList", userTypeOptions);
        // get the list of user groups
        List<Group> groupList = groupServiceClient.getAllGroups().getGroupList();
        model.addAttribute("groupList", groupList);
        // get the list of metadata
        MetadataElement[] elementAry = metadataServiceClient.getMetadataElementByType(selectedMetadataTypeId).getMetadataElementAry();
        model.addAttribute("attributeList", toAttributeList(elementAry));
        // get location list and the address for the user
        Location[] locationAry = locationServiceClient.allLocations().getLocationAry();
        List<OptionTag> locationOptions = getLocationDataOptions(locationAry);
        model.addAttribute("locationArr", locationOptions);
    }

    private static List<UserAttribute> toAttributeList(MetadataElement[] elementAry) {
        List<UserAttribute> attrList = new ArrayList<UserAttribute>();
        if (elementAry != null) {
            for (MetadataElement elem  :elementAry) {
                UserAttribute attr = new UserAttribute();
                attr.setMetadataElementId(elem.getMetadataElementId());
                attr.setName(elem.getAttributeName());
                attr.setRequired(elem.isRequire());
                attrList.add(attr);
            }
        }
        return attrList;

    }

    private List<OptionTag> getLocationDataOptions(Location[] locationAry) {
        List<OptionTag> locOptions = new LinkedList<OptionTag>();
        if(locationAry != null) {
           for(Location loc : locationAry) {
               OptionTag ot = new OptionTag();
               StringBuilder sb = new StringBuilder(loc.getName());
               sb.append("-").append(loc.getBldgNum())
                       .append("-").append(loc.getCity())
                       .append("-").append(loc.getState())
                       .append("-").append(loc.getPostalCd());
               ot.setLabel(sb.toString());
               ot.setValue(loc.getLocationId());
               locOptions.add(ot);
           }
        }
        return locOptions;
    }

    private List<OptionTag> getOptionTagsFromReferencesData(List<ReferenceData> refsData) {
        List<OptionTag> refsOptions = new LinkedList<OptionTag>();
        if(refsData != null) {
            for(ReferenceData ref : refsData) {
                OptionTag ot = new OptionTag();
                ot.setLabel(ref.getDescription());
                ot.setValue(ref.getId().getCodeGroup());
                refsOptions.add(ot);
            }
        }
        return refsOptions;
    }

    private List<OptionTag> getRoleOptionTags(List<Role> roleList) {
        List<OptionTag> roles = new LinkedList<OptionTag>();
        if(roleList != null) {
            for(Role role : roleList) {
                OptionTag ot = new OptionTag();
                ot.setLabel(role.getRoleName());
                ot.setValue(role.getId().getRoleId()+"*"+role.getId().getServiceId());
                roles.add(ot);
            }
        }
        return roles;
    }

    private void setEmail(NewHireExtCommand cmd, ProvisionUser pUser) {
        String email = cmd.getEmail1();
        if (StringUtils.isNotEmpty(email)) {
            EmailAddress em = buildEmail(email, "EMAIL1");
            LOG.info("EmailId 1 = " + em.getEmailId());
            pUser.getEmailAddresses().add(em);
            pUser.setEmail(email);
        }
    }

    private void setPhone(NewHireExtCommand cmd, ProvisionUser usr) {
        Phone ph = buildPhone(usr, "DESK PHONE", cmd.getWorkAreaCode(), cmd.getWorkPhone());
        usr.getPhones().add(ph);

        ph = buildPhone(usr, "CELL PHONE", cmd.getCellAreaCode(), cmd.getCellPhone());
        usr.getPhones().add(ph);

        ph = buildPhone(usr, "FAX", cmd.getFaxAreaCode(), cmd.getFaxPhone());
        usr.getPhones().add(ph);
    }

    private void setAddress(NewHireExtCommand cmd, ProvisionUser pUser) {
        LOG.info("setAddress called.");
        Address adr = new Address();
        adr.setAddress1(cmd.getAddress1());
        adr.setAddress2(cmd.getAddress2());
        adr.setBldgNumber(cmd.getBldgNum());
        adr.setCity(cmd.getCity());
        adr.setState(cmd.getState());
        adr.setName("DEFAULT ADR");
        adr.setParentId(pUser.getUser().getUserId());
        adr.setParentType(ContactConstants.PARENT_TYPE_USER);
        adr.setPostalCd(cmd.getPostalCd());
        pUser.getAddresses().add(adr);
    }


    private EmailAddress buildEmail(String email, String name) {
        EmailAddress em = new EmailAddress();
        em.setEmailAddress(email);
        em.setParentType(ContactConstants.PARENT_TYPE_USER);
        em.setName(name);
        return em;
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
}
