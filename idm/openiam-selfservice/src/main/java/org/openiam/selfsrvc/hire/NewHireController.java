package org.openiam.selfsrvc.hire;


import com.thoughtworks.xstream.XStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.openiam.idm.srvc.menu.ws.NavigatorDataWebService;
import org.openiam.idm.srvc.meta.dto.MetadataType;
import org.openiam.idm.srvc.meta.ws.MetadataWebService;
import org.openiam.idm.srvc.mngsys.dto.ApproverAssociation;
import org.openiam.idm.srvc.mngsys.service.ManagedSystemDataService;
import org.openiam.idm.srvc.msg.service.MailService;
import org.openiam.idm.srvc.msg.service.MailTemplateParameters;
import org.openiam.idm.srvc.org.dto.Organization;
import org.openiam.idm.srvc.org.service.OrganizationDataService;
import org.openiam.idm.srvc.policy.service.PolicyDataService;
import org.openiam.idm.srvc.prov.request.dto.ProvisionRequest;
import org.openiam.idm.srvc.prov.request.dto.RequestApprover;
import org.openiam.idm.srvc.prov.request.dto.RequestUser;
import org.openiam.idm.srvc.prov.request.ws.RequestWebService;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.service.ResourceDataService;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.role.ws.RoleDataWebService;
import org.openiam.idm.srvc.secdomain.service.SecurityDomainDataService;
import org.openiam.idm.srvc.user.dto.*;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.ProvisionService;
import org.openiam.selfsrvc.AppConfiguration;
import org.openiam.selfsrvc.IdToObjectHelper;
import org.openiam.selfsrvc.pswd.PasswordConfiguration;
import org.openiam.selfsrvc.usradmin.DelegationFilterHelper;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Controller for the new hire form.
 *
 * @author suneet
 */
public class NewHireController extends AbstractWizardFormController {

    public static final String NEW_PENDING_REQUEST_NOTIFICATION = "NEW_PENDING_REQUEST";
    protected GroupDataWebService groupManager;
    protected RoleDataWebService roleDataService;
    protected ResourceDataService resourceDataService;
    protected SecurityDomainDataService secDomainService;
    protected OrganizationDataService orgManager;
    protected LocationDataWebService locationService;
    protected NavigatorDataWebService navigatorDataService;
    protected ManagedSystemDataService managedSysService;
    protected UserDataWebService userMgr;
    protected MailService mailService;

    protected MetadataWebService metadataService;

    protected AppConfiguration appConfiguration;

    protected RequestWebService provRequestService;

    protected ReferenceDataService refDataService;
    protected PasswordConfiguration configuration;
    protected PolicyDataService policyDataService;

    protected ProvisionService provisionService;
    protected IdmAuditLogWebDataService auditService;
    private IdToObjectHelper listToObject;
    protected String requestType;
    protected String cancelView;

    String defaultDomainId;
    String menuGroup;


    private static final Log log = LogFactory.getLog(NewHireController.class);


    public NewHireController() {
        super();
    }

    /* ----- overridden methods from the springframework ----------------- */

    @Override
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {

        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
    }

    @Override
    protected void validatePage(Object command, Errors errors, int page) {
        log.debug("Validate page:" + page);
        NewHireValidator validator = (NewHireValidator) getValidator();
        switch (page) {
            case 0:
                validator.validateNewUserType(command, errors);
                break;
            case 1:
                validator.validateNewHireForm(command, errors);
                break;

        }

    }

    protected ModelAndView processCancel(HttpServletRequest request,
                                         HttpServletResponse response, Object command, BindException errors)
            throws Exception {

        return new ModelAndView(new RedirectView(this.getCancelView(), true));


    }


    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        List<Resource> resourceList = resourceDataService.getAllResources();

        NewHireCommand cmd = new NewHireCommand();
        cmd.setResourceList(resourceList);

        return cmd;
    }

    @Override
    protected Map referenceData(HttpServletRequest request, int page) throws Exception {
        switch (page) {
            case 0:
                return loadUserTypes(request);
            case 1:
                return loadUserInformation(request);
            case 2:
                return loadAppInformation(request);
        }
        return null;
    }


    protected ModelAndView processFinish(HttpServletRequest request,
                                         HttpServletResponse response, Object command, BindException arg3)
            throws Exception {

        log.info("In processFinish..");

        NewHireCommand newHireCmd = (NewHireCommand) command;
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        User user = newHireCmd.getUser();
        prepareObject(user, userId);

        List<UserAttribute> attrList = newHireCmd.getAttributeList();
        if (attrList != null) {
            for (UserAttribute ua : attrList) {
                ua.setOperation(AttributeOperationEnum.ADD);
                ua.setUserId(null);
                ua.setId(null);
                log.info("Name=" + ua.getName() + "-" + ua.getValue());
                user.getUserAttributes().put(ua.getName(), ua);
            }
        }


        log.info("User=" + user);

        ProvisionUser pUser = new ProvisionUser(user);
        if (newHireCmd.getSupervisorId() != null && newHireCmd.getSupervisorId().length() > 0) {
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


        if (newHireCmd.getGroup() != null && !newHireCmd.getGroup().isEmpty()) {
            pUser.setMemberOfGroups(getGroupList(newHireCmd, user));
        }
        if (newHireCmd.getRole() != null && !newHireCmd.getRole().isEmpty()) {
            pUser.setMemberOfRoles(getRoleList(newHireCmd, user));
        }


        log.info("User created. New User Id: " + user.getUserId());


        /* serialize the object into xml */
        String userAsXML = toXML(pUser);
        log.info("pUser serialized to XML:" + userAsXML);

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


        auditService.addLog(auditLog);


        return new ModelAndView("pub/confirm");

    }


    /*-------------         Helper methods         ---------------------------*/


    private String toXML(ProvisionUser pUser) {
        XStream xstream = new XStream();
        return xstream.toXML(pUser);

    }


    private List<Group> getGroupList(NewHireCommand newHireCmd, User user) {
        List<Group> groupList = new ArrayList<Group>();
        String groupId = newHireCmd.getGroup();
        Group g = new Group();
        g.setGrpId(groupId);
        groupList.add(g);
        return groupList;
    }

    private List<Role> getRoleList(NewHireCommand newHireCmd, User user) {
        List<Role> roleList = new ArrayList<Role>();
        String cmdRole = newHireCmd.getRole();
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


    protected Map loadUserInformation(HttpServletRequest request) {
        log.info("referenceData called.");

        List<Organization> orgList = null;
        List<Organization> divList = null;
        List<Organization> deptList = null;
        List<Role> roleList = null;
        List<Group> groupList = null;

        HttpSession session = request.getSession();
        User usr = (User) session.getAttribute("userObj");

        if (usr.getDelAdmin() != null && usr.getDelAdmin().intValue() == 1) {
            Map<String, UserAttribute> attrMap = usr.getUserAttributes();

            orgList = listToObject.organizationList(attrMap);

            groupList = listToObject.groupList(attrMap);

            // filter the role
            List<String> roleIdList = DelegationFilterHelper.getRoleFilterFromString(attrMap);
            List<Role> completeRoleList = roleDataService.getAllRoles().getRoleList();


            if (roleIdList == null) {
                roleList = completeRoleList;
            } else {
                // apply filter
                roleList = new ArrayList<Role>();
                for (Role r : completeRoleList) {

                    log.info("Role id = " + r.getId().getRoleId());

                    if (roleIdList.contains(r.getId().getServiceId() + "*" + r.getId().getRoleId())) {

                        log.info("Role found and added to roleList");
                        roleList.add(r);
                    }
                }
            }


        } else {
            // if you are not a delegated admin,then you should only be able to create users for your own organization
            String companyId =  usr.getCompanyId();
            if (companyId != null) {
                orgList = new ArrayList<Organization>();
                orgList.add(orgManager.getOrganization(companyId));
            }
            // get the divisions


            roleList = roleDataService.getAllRoles().getRoleList();
            groupList = groupManager.getAllGroups().getGroupList();


        }

        divList = orgManager.allDivisions(null);
        // load the department list
        deptList = orgManager.allDepartments(null);


        // get the list of job codes
        List<ReferenceData> jobCodeList = refDataService.getRefByGroup("JOB_CODE", "en");

        // get the list of user type codes
        List<ReferenceData> userTypeList = refDataService.getRefByGroup("USER_TYPE", "en");

        // get location list and the address for the user
        Location[] locationAry = locationService.allLocations().getLocationAry();

        Map model = new HashMap();
        model.put("orgList", orgList);
        model.put("divList", divList);
        model.put("deptList", deptList);
        model.put("groupList", groupList);
        model.put("roleList", roleList);
        model.put("jobCodeList", jobCodeList);
        model.put("userTypeList", userTypeList);
        model.put("locationAry", locationAry);

        return model;

    }

    protected Map loadUserTypes(HttpServletRequest request) {
        log.info("referenceData:loadUserTypes called.");

        HttpSession session = request.getSession();

        log.info("User type category =" + appConfiguration.getUserCategoryType());

        MetadataType[] typeAry = metadataService.getTypesInCategory(appConfiguration.getUserCategoryType()).getMetadataTypeAry();


        Map model = new HashMap();
        model.put("metadataTypeAry", typeAry);

        return model;

    }


    protected Map loadAppInformation(HttpServletRequest request) {
        Map model = new HashMap();

        List<Resource> resourceList = resourceDataService.getResourcesByType(this.configuration.getManagedSystemType());
        model.put("resourceList", resourceList);

        return model;

    }

    private void prepareObject(User user, String userId) {
        // need userId to be null so that persistence layer will generate a uid for the user
        user.setUserId(null);
        user.setCreateDate(new Date(System.currentTimeMillis()));
        user.setCreatedBy(userId);
        user.setStatus(UserStatusEnum.PENDING_APPROVAL);

    }


    private ProvisionRequest createRequest(String requestorID, User usr, String asXML, ProvisionUser pUser) {

        String approverId = null;

        Resource newUserResource = resourceDataService.getResource(requestType);

        Date curDate = new Date(System.currentTimeMillis());


        ProvisionRequest req = new ProvisionRequest();
        req.setRequestId(null);

        User requestor = userMgr.getUserWithDependent(requestorID, false).getUser();
        if (requestor != null) {

            req.setRequestorId(requestorID);

        }
        req.setStatus("PENDING");
        req.setStatusDate(curDate);
        req.setRequestDate(curDate);
        req.setRequestType(newUserResource.getResourceId());
        req.setWorkflowName(newUserResource.getName());
        req.setRequestTitle(newUserResource.getDescription() + " FOR:" + usr.getFirstName() + " " + usr.getLastName());
        req.setRequestXML(asXML);
        req.setRequestorFirstName(requestor.getFirstName());
        req.setRequestorLastName(requestor.getLastName());

        if (usr.getCompanyId() != null && usr.getCompanyId().length() > 0) {
            req.setRequestForOrgId(usr.getCompanyId());
        }


        // add a user to the request - this is the person in the New Hire
        Set<RequestUser> reqUserSet = req.getRequestUsers();
        RequestUser reqUser = new RequestUser();
        reqUser.setFirstName(usr.getFirstName());
        reqUser.setLastName(usr.getLastName());
        reqUserSet.add(reqUser);

        String approverRole = null;
        String userOrg = null;
        int applyDelegationFilter = 0;


        // add the approver to the request object.
        List<ApproverAssociation> apList = managedSysService.getApproverByRequestType(requestType, 1);

        if (apList != null) {

            for (ApproverAssociation ap : apList) {
                String approverType;
                String roleDomain = null;

                if (ap != null) {
                    approverType = ap.getAssociationType();


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

        provRequestService.addRequest(req);

        notifyApprover(req, reqUser, requestorID, usr, approverRole, userOrg, applyDelegationFilter);






        return req;


    }


    private void notifyApprover(ProvisionRequest pReq, RequestUser reqUser, String requestorId,
                                User usr, String approverRole, String userOrg, int applyDelegationFilter) {

        // requestor information
        //  User approver = userMgr.getUserWithDependent(approverUserId, false).getUser();

        Set<RequestApprover> approverList = pReq.getRequestApprovers();
        for (RequestApprover ra : approverList) {

            User requestor = userMgr.getUserWithDependent(requestorId, false).getUser();

            if (!ra.getApproverType().equalsIgnoreCase("ROLE")) {
                HashMap<String, String> mailParameters = new HashMap<String, String>();
                mailParameters.put(MailTemplateParameters.USER_ID.value(), ra.getApproverId());
                mailParameters.put(MailTemplateParameters.REQUEST_ID.value(), pReq.getRequestId());
                mailParameters.put(MailTemplateParameters.REQUESTER.value(), requestor.getFirstName() + " " + requestor.getLastName());
                mailParameters.put(MailTemplateParameters.REQUEST_REASON.value(), pReq.getRequestTitle());

                mailService.sendNotification(NEW_PENDING_REQUEST_NOTIFICATION, new PropertyMap(mailParameters));

            } else {
                DelegationFilterSearch search = new DelegationFilterSearch();
                search.setRole(approverRole);
                search.setDelAdmin(applyDelegationFilter);
                search.setOrgFilter("%" + userOrg + "%");

                System.out.println("User Org value =" + userOrg);

                List<User> roleApprovers = userMgr.searchByDelegationProperties(search).getUserList();


                if (roleApprovers != null && !roleApprovers.isEmpty()) {
                    for (User u : roleApprovers) {
                        HashMap<String, String> mailParameters = new HashMap<String, String>();
                        mailParameters.put(MailTemplateParameters.USER_ID.value(), u.getUserId());
                        mailParameters.put(MailTemplateParameters.REQUEST_ID.value(), pReq.getRequestId());
                        mailParameters.put(MailTemplateParameters.REQUESTER.value(), usr.getFirstName() + " " + usr.getLastName());
                        mailParameters.put(MailTemplateParameters.REQUEST_REASON.value(), pReq.getRequestTitle());
                        mailParameters.put(MailTemplateParameters.TARGET_USER.value(), reqUser.getFirstName() + " " + reqUser.getLastName());

                        mailService.sendNotification(NEW_PENDING_REQUEST_NOTIFICATION, new PropertyMap(mailParameters));
                    }

                }
            }
        }


    }


    private void setEmail(NewHireCommand cmd, ProvisionUser pUser) {

        String email = cmd.getEmail1();
        String emailId = cmd.getEmail1Id();
        if (email != null && email.length() > 0) {
            EmailAddress em = buildEmail(emailId, email, "EMAIL1");
            log.info("EmailId 1 = " + em.getEmailId());
            pUser.getEmailAddresses().add(em);
            pUser.setEmail(email);
        }
        email = cmd.getEmail2();
        emailId = cmd.getEmail2Id();
        if (email != null && email.length() > 0) {
            EmailAddress em = buildEmail(emailId, email, "EMAIL2");
            log.info("EmailId 2 = " + em.getEmailId());
            pUser.getEmailAddresses().add(em);
        }
        email = cmd.getEmail3();
        emailId = cmd.getEmail3Id();
        if (email != null && email.length() > 0) {
            EmailAddress em = buildEmail(emailId, email, "EMAIL3");
            pUser.getEmailAddresses().add(em);
        }


    }

    private void setPhone(NewHireCommand cmd, ProvisionUser usr) {
        //	Set<Phone> phSet = usr.getPhone();

        // add obbject


        Phone ph = buildPhone(usr, "DESK PHONE", cmd.getWorkAreaCode(), cmd.getWorkPhone());
        if (cmd.getWorkPhoneId() != null && cmd.getWorkPhoneId().length() > 0) {
            ph.setPhoneId(cmd.getWorkPhoneId());
        }
        usr.getPhones().add(ph);

        ph = buildPhone(usr, "CELL PHONE", cmd.getCellAreaCode(), cmd.getCellPhone());
        log.info("CELL PHONE: " + cmd.getCellPhoneId());
        if (cmd.getCellPhoneId() != null && cmd.getCellPhoneId().length() > 0) {
            ph.setPhoneId(cmd.getCellPhoneId());
        }
        usr.getPhones().add(ph);

        ph = buildPhone(usr, "FAX", cmd.getFaxAreaCode(), cmd.getFaxPhone());
        if (cmd.getFaxPhoneId() != null && cmd.getFaxPhoneId().length() > 0) {
            ph.setPhoneId(cmd.getFaxPhoneId());
        }
        usr.getPhones().add(ph);

        ph = buildPhone(usr, "HOME PHONE", cmd.getHomePhoneAreaCode(), cmd.getHomePhoneNbr());
        if (cmd.getHomePhoneIdr() != null && cmd.getHomePhoneIdr().length() > 0) {
            ph.setPhoneId(cmd.getHomePhoneIdr());
        }
        usr.getPhones().add(ph);

        ph = buildPhone(usr, "ALT CELL PHONE", cmd.getAltCellAreaCode(), cmd.getAltCellNbr());
        if (cmd.getAltCellNbrId() != null && cmd.getAltCellNbrId().length() > 0) {
            ph.setPhoneId(cmd.getAltCellNbrId());
        }
        usr.getPhones().add(ph);

        ph = buildPhone(usr, "PERSONAL PHONE", cmd.getPersonalAreaCode(), cmd.getPersonalNbr());
        if (cmd.getPersonalNbrId() != null && cmd.getPersonalNbrId().length() > 0) {
            ph.setPhoneId(cmd.getPersonalNbrId());
        }
        usr.getPhones().add(ph);


    }

    private void setAddress(NewHireCommand cmd, ProvisionUser pUser) {
        log.info("setAddress called.");

        Address adr = new Address();

        adr.setAddress1(cmd.getUser().getAddress1());
        adr.setAddress2(cmd.getUser().getAddress2());
        adr.setBldgNumber(cmd.getUser().getBldgNum());
        adr.setCity(cmd.getUser().getCity());
        adr.setCountry(cmd.getUser().getCountry());
        adr.setState(cmd.getUser().getState());
        adr.setStreetDirection(cmd.getUser().getStreetDirection());
        adr.setName("DEFAULT ADR");
        adr.setParentId(pUser.getUser().getUserId());
        adr.setParentType(ContactConstants.PARENT_TYPE_USER);
        adr.setPostalCd(cmd.getUser().getPostalCd());
        pUser.getAddresses().add(adr);


    }


    private EmailAddress buildEmail(String emailId, String email, String name) {
        EmailAddress em = new EmailAddress();
        em.setEmailAddress(email);
        if (emailId != null && emailId.length() > 0) {
            em.setEmailId(emailId);
        }
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

    public String getDefaultDomainId() {
        return defaultDomainId;
    }


    public void setDefaultDomainId(String defaultDomainId) {
        this.defaultDomainId = defaultDomainId;
    }


    public String getMenuGroup() {
        return menuGroup;
    }


    public void setMenuGroup(String menuGroup) {
        this.menuGroup = menuGroup;
    }


    public OrganizationDataService getOrgManager() {
        return orgManager;
    }


    public void setOrgManager(OrganizationDataService orgManager) {
        this.orgManager = orgManager;
    }


    public ReferenceDataService getRefDataService() {
        return refDataService;
    }


    public void setRefDataService(ReferenceDataService refDataService) {
        this.refDataService = refDataService;
    }


    public PasswordConfiguration getConfiguration() {
        return configuration;
    }


    public void setConfiguration(PasswordConfiguration configuration) {
        this.configuration = configuration;
    }


    public ResourceDataService getResourceDataService() {
        return resourceDataService;
    }


    public void setResourceDataService(ResourceDataService resourceDataService) {
        this.resourceDataService = resourceDataService;
    }


    public PolicyDataService getPolicyDataService() {
        return policyDataService;
    }


    public void setPolicyDataService(PolicyDataService policyDataService) {
        this.policyDataService = policyDataService;
    }


    public SecurityDomainDataService getSecDomainService() {
        return secDomainService;
    }


    public void setSecDomainService(SecurityDomainDataService secDomainService) {
        this.secDomainService = secDomainService;
    }


    public ProvisionService getProvisionService() {
        return provisionService;
    }


    public void setProvisionService(ProvisionService provisionService) {
        this.provisionService = provisionService;
    }


    public GroupDataWebService getGroupManager() {
        return groupManager;
    }


    public void setGroupManager(GroupDataWebService groupManager) {
        this.groupManager = groupManager;
    }


    public RoleDataWebService getRoleDataService() {
        return roleDataService;
    }


    public void setRoleDataService(RoleDataWebService roleDataService) {
        this.roleDataService = roleDataService;
    }


    public LocationDataWebService getLocationService() {
        return locationService;
    }


    public void setLocationService(LocationDataWebService locationService) {
        this.locationService = locationService;
    }


    public NavigatorDataWebService getNavigatorDataService() {
        return navigatorDataService;
    }


    public void setNavigatorDataService(NavigatorDataWebService navigatorDataService) {
        this.navigatorDataService = navigatorDataService;
    }

    public IdmAuditLogWebDataService getAuditService() {
        return auditService;
    }

    public void setAuditService(IdmAuditLogWebDataService auditService) {
        this.auditService = auditService;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public ManagedSystemDataService getManagedSysService() {
        return managedSysService;
    }

    public void setManagedSysService(ManagedSystemDataService managedSysService) {
        this.managedSysService = managedSysService;
    }

    public UserDataWebService getUserMgr() {
        return userMgr;
    }

    public void setUserMgr(UserDataWebService userMgr) {
        this.userMgr = userMgr;
    }

    public RequestWebService getProvRequestService() {
        return provRequestService;
    }

    public void setProvRequestService(RequestWebService provRequestService) {
        this.provRequestService = provRequestService;
    }

    public String getCancelView() {
        return cancelView;
    }

    public void setCancelView(String cancelView) {
        this.cancelView = cancelView;
    }

    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public MetadataWebService getMetadataService() {
        return metadataService;
    }

    public void setMetadataService(MetadataWebService metadataService) {
        this.metadataService = metadataService;
    }

    public AppConfiguration getAppConfiguration() {
        return appConfiguration;
    }

    public void setAppConfiguration(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    public IdToObjectHelper getListToObject() {
        return listToObject;
    }

    public void setListToObject(IdToObjectHelper listToObject) {
        this.listToObject = listToObject;
    }
}
