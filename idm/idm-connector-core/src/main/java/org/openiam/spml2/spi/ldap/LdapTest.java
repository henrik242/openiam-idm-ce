package org.openiam.spml2.spi.ldap;

import org.openiam.idm.srvc.mngsys.dto.ManagedSys;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.LdapName;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import javax.naming.directory.SearchControls;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.ldap.*;
import javax.naming.directory.SearchResult;
/**
 * Created by IntelliJ IDEA.
 * User: suneetshah
 * Date: 8/30/11
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class LdapTest {

    /*
    ldapsearch -x -h 172.17.2.116 -p 389 -D "cn=Manager,dc=gtawestdir,dc=com" -w secret -b "ou=affiliations,dc=gtawestdir,dc=com" cn=CGI*
ldapsearch -x -h 172.17.2.116 -p 389 -D "cn=Manager,dc=gtawestdir,dc=com" -w secret -b "ou=users,dc=gtawestdir,dc=com" uid=Suneet.LDAP-TEST-1*
ldapsearch -x -h 172.17.2.116 -p 389 -D "cn=Manager,dc=gtawestdir,dc=com" -w secret -b "ou=roles,dc=gtawestdir,dc=com" cn=LOCAL*

 ldapsearch -x -h localhost -p 389 -D "cn=Manager,dc=openiam,dc=org" -w openiam -b "ou=group,dc=openiam,dc=org" cn=*

     */

    public LdapTest() {
    }

    public static LdapContext connect()  throws NamingException {

		//LdapContext ctxLdap = null;
		Hashtable<String, String> envDC = new Hashtable();


		envDC.put(Context.PROVIDER_URL,"ldap://192.168.149.138:389");
		envDC.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		envDC.put(Context.SECURITY_AUTHENTICATION, "simple" ); // simple
		envDC.put(Context.SECURITY_PRINCIPAL,"cn=Manager,dc=openiam,dc=org");  //"administrator@diamelle.local"
		envDC.put(Context.SECURITY_CREDENTIALS, "openiam");


		return (new InitialLdapContext(envDC,null));


	}

    public static LdapContext ssConnect()  throws NamingException {

        //LdapContext ctxLdap = null;
        Hashtable<String, String> envDC = new Hashtable();


        envDC.put(Context.PROVIDER_URL,"ldaps://192.168.149.135:636");
        envDC.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        envDC.put(Context.SECURITY_AUTHENTICATION, "simple" ); // simple
        envDC.put(Context.SECURITY_PRINCIPAL,"iamdev\\Administrator");  //"administrator@diamelle.local"
        envDC.put(Context.SECURITY_CREDENTIALS, "sasny$257");


        return (new InitialLdapContext(envDC,null));


    }

    public static void createADUser() {

        int UF_ACCOUNTDISABLE = 0x0002;
        int UF_PASSWD_NOTREQD = 0x0020;
        int UF_PASSWD_CANT_CHANGE = 0x0040;
        int UF_NORMAL_ACCOUNT = 0x0200;
        int UF_DONT_EXPIRE_PASSWD = 0x10000;
        int UF_PASSWORD_EXPIRED = 0x800000;
        
        try {

        LdapContext ctx = ssConnect();

        BasicAttributes attrs = new BasicAttributes();
        attrs.put(new BasicAttribute("objectClass", "user"));
        attrs.put(new BasicAttribute("sn", "Shouah"));
        attrs.put(new BasicAttribute("givenName", "Ameet"));
        attrs.put(new BasicAttribute("ou", "CN=Users,DC=iamdev,DC=local"));
        attrs.put(new BasicAttribute("displayName", "Shah, Ameet"));
            attrs.put(new BasicAttribute("sAMAccountName", "Ameet.Shah"));
            attrs.put(new BasicAttribute("title", "VP"));
            attrs.put(new BasicAttribute("telephoneNumber", "916-343-3434"));
            attrs.put(new BasicAttribute("company", "OpenIAM"));
            attrs.put(new BasicAttribute("mail", "ameet@gmail.com"));
            attrs.put(new BasicAttribute("sAMAccountName", "Ameet.Shah"));
            attrs.put(new BasicAttribute("userAccountControl", Integer.toString(UF_NORMAL_ACCOUNT + UF_PASSWD_CANT_CHANGE)));
            // password must meet password policy or we get a "will not perform" exception
            attrs.put(generateActiveDirectoryPassword("sasny$257"));

        Context result = ctx.createSubcontext("CN=Ameet Shah,CN=Users,DC=iamdev,DC=local", attrs);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected static Attribute generateActiveDirectoryPassword(String cleartextPassword) {
        try {
            byte[] password = ("\"" + cleartextPassword + "\"").getBytes("UTF-16LE");
            return new BasicAttribute("unicodePwd", password);
        } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
            return null;
        }
    }
    
    public static void membership() {
        int totalResults = 0;

        //Use an LDAP Compare operation on the group, checking for uniquemember matching the user's DN.

        /*
         *
         public List getGroups(String username) throws NamingException {
        List groups = new LinkedList();

        // Set up criteria to search on
        String filter = new StringBuffer()
            .append("(&")
            .append("(objectClass=groupOfForethoughtNames)")
            .append("(uniqueMember=")
            .append(getUserDN(username))
            .append(")")
            .append(")")
            .toString();

        // Set up search constraints
        SearchControls cons = new SearchControls();
        cons.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        NamingEnumeration results =
            context.search(GROUPS_OU, filter, cons);

        while (results.hasMore()) {
            SearchResult result = (SearchResult)results.next();
            groups.add(getGroupCN(result.getName()));
        }

        return groups;
    }
         */

        //String userSearchFilter = "(objectClass=*)";

        String userSearchFilter = "(&(objectclass=*)(uniqueMember=uid=AMEET.SHAH,ou=people,dc=openiam,dc=org))";

        String searchBase = "cn=HELPDESK,ou=group,dc=openiam,dc=org";


        try {
            LdapContext ctx = connect();

            SearchControls ctls = new SearchControls();

            String userReturnedAtts[]={"uniqueMember"};
            ctls.setReturningAttributes(userReturnedAtts);
            ctls.setSearchScope(SearchControls.OBJECT_SCOPE); // Search object only

            NamingEnumeration answer = ctx.search(searchBase, userSearchFilter, ctls);


            //Loop through the search results
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult)answer.next();

                System.out.println(">>>" + sr.getName());

                //Print out the groups

                Attributes attrs = sr.getAttributes();
                if (attrs != null) {

                    try {
                        for (NamingEnumeration ae = attrs.getAll();ae.hasMore();) {
                            Attribute attr = (Attribute)ae.next();
                            System.out.println("Attribute: " + attr.getID());
                            for (NamingEnumeration e = attr.getAll();e.hasMore();totalResults++) {

                                System.out.println(" " +  totalResults + ". " +  e.next());
                            }

                        }

                    }
                    catch (NamingException e)	{
                        System.err.println("Problem listing membership: " + e);
                    }

                }
            }

        }catch (Exception e) {
            e.printStackTrace();
            
        }
           
        
    }

    public static void ListofUsersMembership() {
        int totalResults = 0;



        //String userSearchFilter = "(objectClass=*)";

        String userSearchFilter = "(&(objectclass=*)(uniqueMember=uid=AMEET.SHAH,ou=people,dc=openiam,dc=org))";

        String searchBase = "dc=openiam,dc=org";


        try {
            LdapContext ctx = connect();

            SearchControls ctls = new SearchControls();

            String userReturnedAtts[]={"uniqueMember"};
            ctls.setReturningAttributes(userReturnedAtts);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE); // Search object only

            NamingEnumeration answer = ctx.search(searchBase, userSearchFilter, ctls);


            //Loop through the search results
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult)answer.next();

                System.out.println(">>>" + sr.getName());


            }

        }catch (Exception e) {
            e.printStackTrace();

        }


    }

    public static  void getMembership() {
        System.out.println("GetMembership...");
         try {
            LdapContext ctx = connect();

             //Create the search controls
			SearchControls searchCtls = new SearchControls();

			//Specify the search scope
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			//specify the LDAP search filter
			String searchFilter = "(&(objectClass=inetOrgPerson)(uid=Suneet.GTAUser6))" ;

			//Specify the Base for the search
			String searchBase = "ou=users,DC=gtawestdir,DC=com";

			//initialize counter to total the group members
			int totalResults = 0;

			//Specify the attributes to return
			String returnedAtts[]={"memberOf", "isMemberOf"};
			searchCtls.setReturningAttributes(returnedAtts);

			//Search for objects using the filter
			NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);

			//Loop through the search results
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult)answer.next();

				System.out.println(">>>" + sr.getName());

				//Print out the groups

				Attributes attrs = sr.getAttributes();
				if (attrs != null) {

					try {
						for (NamingEnumeration ae = attrs.getAll();ae.hasMore();) {
							Attribute attr = (Attribute)ae.next();
							System.out.println("Attribute: " + attr.getID());
							for (NamingEnumeration e = attr.getAll();e.hasMore();totalResults++) {

								System.out.println(" " +  totalResults + ". " +  e.next());
							}

						}

					}
					catch (NamingException e)	{
						System.err.println("Problem listing membership: " + e);
					}

				}
			}



         }catch(Exception e) {

             e.printStackTrace();
         }

    }

    public static void createOrg() {
        try {
            LdapContext ctx = connect();

            BasicAttributes basicAttr =  new BasicAttributes();

            Attribute a = new BasicAttribute("objectclass","organizationalUnit");
            basicAttr.put(a);
            basicAttr.put(new BasicAttribute("businessCategory","Hospital"));
            basicAttr.put(new BasicAttribute("telephoneNumber","917-371-0126"));
             basicAttr.put(new BasicAttribute("description","My description"));
             basicAttr.put(new BasicAttribute("facsimileTelephoneNumber","917-371-0126"));
            basicAttr.put(new BasicAttribute("postalAddress","49 whortleberry rd"));
            basicAttr.put(new BasicAttribute("postalCode","06896"));
             basicAttr.put(new BasicAttribute("st","ct"));
             basicAttr.put(new BasicAttribute("l","redding"));

            String ldapName = "ou=myhospital,ou=organizations,dc=gtawestdir,dc=com";

            Context result = ctx.createSubcontext(ldapName, basicAttr);

        }catch(Exception e) {
           e.printStackTrace();
        }


    }

    public static void createRole() {
        try {
            LdapContext ctx = connect();

            BasicAttributes basicAttr =  new BasicAttributes();

            Attribute a = new BasicAttribute("objectclass","groupOfUniqueNames");
            basicAttr.put(a);
            basicAttr.put(new BasicAttribute("businessCategory","Hospital"));
            basicAttr.put(new BasicAttribute("description","My description"));

            //String ldapName = "cn=myrole,ou=roles,dc=gtawestdir,dc=com";

            String ldapName = "cn=myrole,ou=affiliations,dc=gtawestdir,dc=com";

            Context result = ctx.createSubcontext(ldapName, basicAttr);

        }catch(Exception e) {
           e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        System.out.println("LdapTest called....");

        //LdapTest.getMembership();
     //   LdapTest.ListofUsersMembership();
        try {
           LdapTest.createADUser();
        }catch(Exception e) {
            e.printStackTrace();
            
        }
            
    }

}
