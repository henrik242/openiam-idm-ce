 package groovy

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openiam.idm.srvc.synch.service.WSOperationCommand;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.audit.dto.IdmAuditLog;
import org.openiam.idm.srvc.audit.service.AuditHelper;
import org.openiam.idm.srvc.auth.login.LoginDataService;
import org.openiam.idm.srvc.role.service.RoleDataService;
import org.openiam.idm.srvc.synch.dto.Attribute;
import org.openiam.idm.srvc.synch.dto.LineObject;
import org.openiam.idm.srvc.synch.dto.SyncResponse;
import org.openiam.idm.srvc.synch.dto.SynchConfig;
import org.openiam.idm.srvc.user.dto.User;
import org.openiam.idm.srvc.user.dto.UserStatusEnum;
import org.openiam.idm.srvc.user.service.UserDataService;
import org.openiam.provision.dto.ProvisionUser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.xml.sax.SAXException;

import com.sun.xml.txw2.Document;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class WebServiceCommand implements WSOperationCommand {

	//CUSTOM FIELDS
	// entityTag - response tag name of entity
	private static final String entityTag = "user";
	//tags - list of response tags which should be sync
	//tags name should be the same with response tags.
	private static final List <String> tags = Arrays.asList ("ns4:firstName",
	"ns4:lastName",
	"ns4:status",
	"ns4:employeeId",
	"ns4:lastUpdate",
	"ns4:userId"
	);
	//Values of request params.
	private static final List <String> requestParams = Arrays.asList ("Scott", "Nelson");
	//-------------

	/**
	 * CUSTOM METHOD
	 * @param args - request arguments
	 * @return
	 */
	private String getCustomRequest(String[] args) {
		return """<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="urn:idm.openiam.org/srvc/user/service">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:getUserByName>
         <!--Optional:-->
         <firstName>${args[0]}</firstName>
         <!--Optional:-->
         <lastName>${args[1]}</lastName>
      </ser:getUserByName>
   </soapenv:Body>
</soapenv:Envelope>"""
	}
	//END CUSTOM BLOCK^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


	//NOT CUSTOM BLOCK. PLEASE DON'T EDIT CODE BELOW
	//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	protected ProvisionUser pUser = new ProvisionUser();
	public static ApplicationContext ac;
	private String endPoint ;
	private URL url;
	public List<LineObject> execute(SynchConfig config) {
		List<LineObject> rowObjectList = new ArrayList();
		url = new URL(config.getWsUrl());
		endPoint = config.getWsEndPoint();
		for (String entities: this.getTagValue(this.getResponse((String[])requestParams.toArray()), entityTag)) {
			rowObjectList.add(this.parseObject(entities, (String[])tags.toArray()));
		}
		//SyncResponse resp = new SyncResponse(ResponseStatus.SUCCESS);
		return rowObjectList;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ac = applicationContext;
	}


	public static ApplicationContext getAc() {
		return ac;
	}

	public String getResponse(String[] params) {
		return fetch(endPoint, getCustomRequest(params));
	}

	private String fetch(String soapAction, String request) {

		def connection = (HttpURLConnection) url.openConnection()
		connection.setRequestMethod("POST")
		connection.setDoInput(true)
		connection.setDoOutput(true)
		connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8")
		//connection.setRequestProperty("Accept-Encoding", "gzip,deflate")
		connection.setRequestProperty("Content-Length", "" + request.getBytes().size())
		connection.setRequestProperty("SOAPAction", url.toString()+"/${soapAction}")

		DataOutputStream wr = new DataOutputStream(connection.getOutputStream())
		wr.writeBytes(request)
		wr.flush()
		wr.close()

		if (connection.responseCode == 200 || connection.responseCode == 201){

			def result =IOUtils.toString(connection.getInputStream(),"UTF-8");
			System.out.println(result);
			connection.disconnect();
			return result;
		} else {
			throw new RuntimeException("Could not connect, responseCode: ${connection.responseCode}");
		}
	}

	/**
	 * 
	 * @param response - entity non parse text
	 * @param attributesName - attributesName.
	 * @return - LineObject
	 */
	private LineObject parseObject(String entity, String[] tagNames) {
		LineObject result = new LineObject();
		for (String tag : tagNames) {
			Attribute attr = new Attribute();
			attr.populateAttribute(tag, this.getTagValue(entity, tag));
			result.put(tag, attr);
		}
		return result;
	}

	/**
	 * Search text between tags.
	 * @param text
	 * @param tagName
	 * @return - List of such text blocks
	 */
	private List <String> getTagValue(String text, String tagName) {
		List <String> res = new ArrayList<String>(0);
		Matcher matcher = Pattern.compile("<"+tagName+">.*?</"+tagName+">").matcher(text);
		List <String> entities = new ArrayList<String>();
		while (matcher.find()){
			res.add(matcher.group().replace("<"+tagName+">", "").replace("</"+tagName+">", ""));
		}
		if (res.isEmpty())
		{
			res.add("");
		}
		return res;
	}
}

