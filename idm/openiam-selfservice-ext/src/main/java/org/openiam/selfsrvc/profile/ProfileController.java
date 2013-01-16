package org.openiam.selfsrvc.profile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.util.StringUtils;
import org.openiam.idm.srvc.continfo.dto.ContactConstants;
import org.openiam.idm.srvc.continfo.dto.EmailAddress;
import org.openiam.idm.srvc.continfo.dto.Phone;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.ws.UserDataWebService;
import org.openiam.provision.dto.ProvisionUser;
import org.openiam.provision.service.ProvisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

@Controller
@RequestMapping(value = "/profile")
public class ProfileController {
    private static final Log LOG = LogFactory.getLog(ProfileController.class);

    @Autowired
    private ProvisionService provisionServiceClient;

    @Autowired
    public UserDataWebService userServiceClient;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df,true) );
    }

    @RequestMapping(method = RequestMethod.GET, value = "/confirm")
    public String getConfirmForm(Model model, HttpServletRequest request) {
        LOG.info("Confirmation form called after registration.");
        return "/priv/confirm";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edit")
    public String getEditForm(Model model, HttpServletRequest request) {
        LOG.info("Edit user profile form called.");

        ProfileCommand profileCommand = new ProfileCommand();
        editFormInitialization(request, profileCommand);

        model.addAttribute("profileCommand", profileCommand);
        return "/priv/profile";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit")
    public String save(Model model, ProfileCommand command, BindingResult result, HttpServletRequest request) {
        ProfileValidator.validate(command, result);
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        if (!result.hasErrors() && StringUtils.isNotEmpty(userId)) {
            User currentUser = userServiceClient.getUserWithDependent(userId, true).getUser();
            currentUser.setFirstName(command.getFirstName());
            currentUser.setLastName(command.getLastName());
            currentUser.setEmail(command.getEmail1());

            Set<EmailAddress> emailSet =  currentUser.getEmailAddresses();
            Set<Phone> phoneSet = currentUser.getPhones();

            ProvisionUser pUser = new ProvisionUser(currentUser);
            pUser.setEmailAddresses(emailSet);
            pUser.setPhones(phoneSet);

            getPhone(command, pUser);
            getEmail(command, pUser);

            String login = (String)session.getAttribute("login");
            String domain = (String)session.getAttribute("domain");
            pUser.setRequestClientIP(request.getRemoteHost());
            pUser.setRequestorLogin(login);
            pUser.setRequestorDomain(domain);

            provisionServiceClient.modifyUser(pUser);
            LOG.info("User=" + pUser);

            return "redirect:/profile/confirm.jsp";
        } else {
            model.addAttribute("registrationCommand", command);
            model.addAttribute("errors",result);
            editFormInitialization(request, command);
            return "/priv/profile";
        }
    }

    private ProfileCommand editFormInitialization(HttpServletRequest request, ProfileCommand profileCommand) {
        HttpSession session =  request.getSession();
        String userId = (String)session.getAttribute("userId");

        User usr = userServiceClient.getUserWithDependent(userId, true).getUser();

        if(usr != null) {
            profileCommand.setFirstName(usr.getFirstName());
            profileCommand.setLastName(usr.getLastName());
            profileCommand.setEmail1(usr.getEmail());
            profileCommand.setWorkPhone(usr.getPhoneNbr());
            profileCommand.setWorkAreaCode(usr.getPhoneExt());
        }
        return profileCommand;
    }

    private static void getEmail(ProfileCommand profileCommand, User usr) {
        Set<EmailAddress> emailAdrSet =  usr.getEmailAddresses();


        EmailAddress email1 = null, email2 = null, email3 = null;

        Iterator<EmailAddress> emailIterator = emailAdrSet.iterator();
        while (emailIterator.hasNext()) {
            EmailAddress e = emailIterator.next();
            if (e != null) {

                if ("EMAIL1".equalsIgnoreCase(e.getName())) {
                    e.setEmailAddress(profileCommand.getEmail1());
                    System.out.println("EMAIL 1 = MATCH");
                }
            }
        }

        if (!emailExists(emailAdrSet, "EMAIL1")) {
            // add email

            email1 = new EmailAddress();
            email1.setEmailAddress(profileCommand.getEmail1());
            email1.setName("EMAIL1");
            email1.setParentId(usr.getUserId());
            email1.setParentType(ContactConstants.PARENT_TYPE_USER);
            usr.getEmailAddresses().add(email1);
        }
    }

    private static void getPhone(ProfileCommand profileCmd, User usr) {
        Set<Phone> phSet = usr.getPhones();

        Phone deskPhone = null, cellPhone = null, faxPhone=null, homePhone = null, altCellPhone = null, personalPhone = null;

        Iterator<Phone> phoneIterator = phSet.iterator();
        while (phoneIterator.hasNext()) {
            Phone p = phoneIterator.next();
            if ( p.getName() != null) {
                if (p.getName().equalsIgnoreCase("DESK PHONE")) {
                    // update
                    p.setAreaCd(profileCmd.getWorkAreaCode());
                    p.setPhoneNbr(profileCmd.getWorkPhone());
                }
                if (p.getName().equalsIgnoreCase("FAX")) {
                    // update
                    p.setAreaCd(profileCmd.getFaxAreaCode());
                    p.setPhoneNbr(profileCmd.getFaxPhone());
                }

            }
        }
        // add obbject
        if (!phoneExists(phSet, "DESK PHONE")) {
            // add
            deskPhone = new Phone();
            deskPhone.setAreaCd(profileCmd.getWorkAreaCode());
            deskPhone.setPhoneNbr(profileCmd.getWorkPhone());
            deskPhone.setDescription("WORK");
            deskPhone.setParentType(ContactConstants.PARENT_TYPE_USER);
            deskPhone.setName("DESK PHONE");
            deskPhone.setParentId(usr.getUserId());
            phSet.add(deskPhone);
        }

        if (!phoneExists(phSet, "FAX")) {
            // add
            faxPhone = new Phone();
            faxPhone.setAreaCd(profileCmd.getFaxAreaCode());
            faxPhone.setPhoneNbr(profileCmd.getFaxPhone());
            faxPhone.setDescription("FAX");
            faxPhone.setParentType(ContactConstants.PARENT_TYPE_USER);
            faxPhone.setName("FAX");
            faxPhone.setParentId(usr.getUserId());
            phSet.add(faxPhone);
        }
    }

    private static boolean phoneExists(Set<Phone> phSet, String name) {
        Iterator<Phone> phoneIterator = phSet.iterator();
        if (phoneIterator == null) {
            return false;
        }
        Phone p = null;
        while (phoneIterator.hasNext()) {
            p = phoneIterator.next();
            if (p.getName() != null) {
                if (p.getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean emailExists(Set<EmailAddress> emailSet, String name) {
        if (emailSet == null) {
            return false;
        }
        for ( EmailAddress e : emailSet) {
            if (e.getName() != null) {
                if (e.getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }

        }

        return false;

    }

}