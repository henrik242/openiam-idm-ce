package org.openiam.provision.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openiam.provision.dto.ProvisionUser;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import java.util.*;
import org.openiam.provision.resp.ProvisionUserResponse;

/**
 * Test the Add User operation in the Provisioning service
 * User: suneetshah
 * Date: 9/30/12
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultProvisioningServiceTest {
    static String BASE_URL = "http://localhost:8080/openiam-idm-esb/idmsrvc/";
    static ProvisionService provisionService;
    static Date curDate = new Date(System.currentTimeMillis());

    @Before
    public void setUp() throws Exception {

        String serviceUrl = BASE_URL + "/ProvisioningService";
        String port ="ProvisionControllerServicePort";
        String nameSpace = "http://www.openiam.org/service/provision";

        Service service = Service.create(QName.valueOf(serviceUrl));

        service.addPort(new QName(nameSpace,port), SOAPBinding.SOAP11HTTP_BINDING,	serviceUrl);

        provisionService = service.getPort(new QName(nameSpace,	port),ProvisionService.class);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddUser() throws Exception {

        ProvisionUser pUser = createSimpleProvision();

        ProvisionUserResponse resp = provisionService.addUser(pUser);

        assertNotNull(resp.getUser());
        assertNotNull(resp.getUser().getUserId());

        ProvisionUser createdUser = resp.getUser();

        assertNotNull(createdUser.getPrincipalList());
        assertEquals(1, createdUser.getPrincipalList().size());

    }


    // build test objects

    private ProvisionUser createSimpleProvision() {
        ProvisionUser pUser = new ProvisionUser();

        pUser.setFirstName("Michael");
        pUser.setLastName("Mouse");
        pUser.setMiddleInit("X");
        pUser.setAddress1("123 Main Street");
        pUser.setAddress2("Row 2");
        pUser.setBirthdate(curDate);
        pUser.setBldgNum("123A");
        pUser.setCompanyId("100");
        pUser.setCountry("USA");
        pUser.setEmail("suneet_shah@Openiam.com");
        pUser.setEmployeeId("EMP-1100");
        pUser.setEmployeeType("FULLTIME");
        pUser.setJobCode("JB-100");
        pUser.setLocationCd("LOC-100");
        pUser.setMailCode("MC-100");
        pUser.setNickname("Mickey");
        pUser.setPostalCd("10166");
        pUser.setPhoneNbr("PHN-123-4567");
        pUser.setPhoneExt("EXT123");
        pUser.setAreaCd("917");
        pUser.setRequestClientIP("localhost");
        pUser.setRequestorLogin("sysadmin");
        pUser.setRequestorDomain("openiam.org");
        pUser.setSecurityDomain("USR_SEC_DOMAIN");
        pUser.setSkipPreprocessor(true);
        pUser.setSkipPostProcessor(true);
        pUser.setEmailCredentialsToNewUsers(true);
        pUser.setSex("M");
        pUser.setState("NY");

        return pUser;
    }
}
