package org.openiam.idm.srvc.synch.dto;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;




@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SynchConfig", 
		propOrder = { "synchConfigId",
		"name",
		"status", 
		"synchAdapter",
		"fileName",
		"managedSysId",
		"loadMatchOnly",
		"updateAttribute",
		"synchFrequency",
		"synchType",
		"processRule",
		"validationRule",
		"transformationRule",
		"matchFieldName",
		"matchManagedSysId",
		"matchSrcFieldName",
		"srcLoginId",
		"srcPassword", 
		"srcHost", 
		"driver", 
		"connectionUrl", 
		"query", 
		"queryTimeField", 
		"lastExecTime", 
		"customMatchRule",
		"customMatchAttr",
		"customAdatperScript",
		"baseDn",
        "lastRecProcessed",
        "wsScript",
        "wsUrl"
})
/**
 * Object containing the configuration for a synchronization task
 */
public class SynchConfig implements java.io.Serializable {

	private String synchConfigId;
	private String name;
	private String status;
	private String synchAdapter;
	private String fileName;
	private String managedSysId;
	private Integer loadMatchOnly;
	private Integer updateAttribute;
	private String synchFrequency;
	private String synchType;
	//private String deleteRule;
	private String processRule;
	private String validationRule;
	private String transformationRule;
	private String matchFieldName;
	private String matchManagedSysId;
	private String matchSrcFieldName;
	private String srcLoginId;
	private String srcPassword;
	private String srcHost;
	private String driver;
	private String connectionUrl;
	private String query;
	private String queryTimeField;
	@XmlSchemaType(name = "datetime")
	private Date lastExecTime;
    private String lastRecProcessed;
	private String customMatchRule;
	private String customAdatperScript;
	private String customMatchAttr;
	private String baseDn;

    private String wsUrl;
    private String wsScript;


	
	public SynchConfig() {
	}

	public SynchConfig(String synchConfigId) {
		this.synchConfigId = synchConfigId;
	}


	public String getSynchConfigId() {
		return this.synchConfigId;
	}

	public void setSynchConfigId(String synchConfigId) {
		this.synchConfigId = synchConfigId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getManagedSysId() {
		return this.managedSysId;
	}

	public void setManagedSysId(String managedSysId) {
		this.managedSysId = managedSysId;
	}

	public Integer getLoadMatchOnly() {
		return this.loadMatchOnly;
	}

	public void setLoadMatchOnly(Integer loadMatchOnly) {
		this.loadMatchOnly = loadMatchOnly;
	}

	public Integer getUpdateAttribute() {
		return this.updateAttribute;
	}

	public void setUpdateAttribute(Integer updateAttribute) {
		this.updateAttribute = updateAttribute;
	}

	public String getSynchFrequency() {
		return this.synchFrequency;
	}

	public void setSynchFrequency(String synchFrequency) {
		this.synchFrequency = synchFrequency;
	}


	public String getProcessRule() {
		return this.processRule;
	}

	public void setProcessRule(String processRule) {
		this.processRule = processRule;
	}


	public String getTransformationRule() {
		return this.transformationRule;
	}

	public void setTransformationRule(String transformationRule) {
		this.transformationRule = transformationRule;
	}

	public String getMatchFieldName() {
		return this.matchFieldName;
	}

	public void setMatchFieldName(String matchFieldName) {
		this.matchFieldName = matchFieldName;
	}

	public String getMatchSrcFieldName() {
		return this.matchSrcFieldName;
	}

	public void setMatchSrcFieldName(String matchSrcFieldName) {
		this.matchSrcFieldName = matchSrcFieldName;
	}

	public String getMatchManagedSysId() {
		return matchManagedSysId;
	}

	public void setMatchManagedSysId(String matchManagedSysId) {
		this.matchManagedSysId = matchManagedSysId;
	}

	public String getSrcLoginId() {
		return srcLoginId;
	}

	public void setSrcLoginId(String srcLoginId) {
		this.srcLoginId = srcLoginId;
	}

	public String getSrcPassword() {
		return srcPassword;
	}

	public void setSrcPassword(String srcPassword) {
		this.srcPassword = srcPassword;
	}

	public String getSrcHost() {
		return srcHost;
	}

	public void setSrcHost(String srcHost) {
		this.srcHost = srcHost;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQueryTimeField() {
		return queryTimeField;
	}

	public void setQueryTimeField(String queryTimeField) {
		this.queryTimeField = queryTimeField;
	}



	public String getCustomMatchRule() {
		return customMatchRule;
	}

	public void setCustomMatchRule(String customMatchRule) {
		this.customMatchRule = customMatchRule;
	}

	public String getCustomAdatperScript() {
		return customAdatperScript;
	}

	public void setCustomAdatperScript(String customAdatperScript) {
		this.customAdatperScript = customAdatperScript;
	}

	public String getSynchAdapter() {
		return synchAdapter;
	}

	public void setSynchAdapter(String synchAdapter) {
		this.synchAdapter = synchAdapter;
	}

	public String getValidationRule() {
		return validationRule;
	}

	public void setValidationRule(String validationRule) {
		this.validationRule = validationRule;
	}

	public String getSynchType() {
		return synchType;
	}

	public void setSynchType(String synchType) {
		this.synchType = synchType;
	}

	public String getCustomMatchAttr() {
		return customMatchAttr;
	}

	public void setCustomMatchAttr(String customMatchAttr) {
		this.customMatchAttr = customMatchAttr;
	}

	public Date getLastExecTime() {
		return lastExecTime;
	}

	public void setLastExecTime(Date lastExecTime) {
		this.lastExecTime = lastExecTime;
	}

    public String getBaseDn() {
        return baseDn;
    }

    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    @Override
    public String toString() {
        return "SynchConfig{" +
                "synchConfigId='" + synchConfigId + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", synchAdapter='" + synchAdapter + '\'' +
                ", fileName='" + fileName + '\'' +
                ", managedSysId='" + managedSysId + '\'' +
                ", loadMatchOnly=" + loadMatchOnly +
                ", updateAttribute=" + updateAttribute +
                ", synchFrequency='" + synchFrequency + '\'' +
                ", synchType='" + synchType + '\'' +
                ", processRule='" + processRule + '\'' +
                ", validationRule='" + validationRule + '\'' +
                ", transformationRule='" + transformationRule + '\'' +
                ", matchFieldName='" + matchFieldName + '\'' +
                ", matchManagedSysId='" + matchManagedSysId + '\'' +
                ", matchSrcFieldName='" + matchSrcFieldName + '\'' +
                ", srcLoginId='" + srcLoginId + '\'' +
                ", srcPassword='" + srcPassword + '\'' +
                ", srcHost='" + srcHost + '\'' +
                ", driver='" + driver + '\'' +
                ", connectionUrl='" + connectionUrl + '\'' +
                ", query='" + query + '\'' +
                ", queryTimeField='" + queryTimeField + '\'' +
                ", lastExecTime=" + lastExecTime +
                ", customMatchRule='" + customMatchRule + '\'' +
                ", customAdatperScript='" + customAdatperScript + '\'' +
                ", customMatchAttr='" + customMatchAttr + '\'' +
                ", baseDn='" + baseDn + '\'' +
                '}';
    }

    public String getLastRecProcessed() {
        return lastRecProcessed;
    }

    public void setLastRecProcessed(String lastRecProcessed) {
        this.lastRecProcessed = lastRecProcessed;
    }

    public String getWsUrl() {
        return wsUrl;
    }

    public void setWsUrl(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    public String getWsScript() {
        return wsScript;
    }

    public void setWsScript(String wsScript) {
        this.wsScript = wsScript;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SynchConfig)) return false;

        SynchConfig that = (SynchConfig) o;

        if (baseDn != null ? !baseDn.equals(that.baseDn) : that.baseDn != null) return false;
        if (connectionUrl != null ? !connectionUrl.equals(that.connectionUrl) : that.connectionUrl != null)
            return false;
        if (customAdatperScript != null ? !customAdatperScript.equals(that.customAdatperScript) : that.customAdatperScript != null)
            return false;
        if (customMatchAttr != null ? !customMatchAttr.equals(that.customMatchAttr) : that.customMatchAttr != null)
            return false;
        if (customMatchRule != null ? !customMatchRule.equals(that.customMatchRule) : that.customMatchRule != null)
            return false;
        if (driver != null ? !driver.equals(that.driver) : that.driver != null) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        if (lastExecTime != null ? !lastExecTime.equals(that.lastExecTime) : that.lastExecTime != null) return false;
        if (lastRecProcessed != null ? !lastRecProcessed.equals(that.lastRecProcessed) : that.lastRecProcessed != null)
            return false;
        if (loadMatchOnly != null ? !loadMatchOnly.equals(that.loadMatchOnly) : that.loadMatchOnly != null)
            return false;
        if (managedSysId != null ? !managedSysId.equals(that.managedSysId) : that.managedSysId != null) return false;
        if (matchFieldName != null ? !matchFieldName.equals(that.matchFieldName) : that.matchFieldName != null)
            return false;
        if (matchManagedSysId != null ? !matchManagedSysId.equals(that.matchManagedSysId) : that.matchManagedSysId != null)
            return false;
        if (matchSrcFieldName != null ? !matchSrcFieldName.equals(that.matchSrcFieldName) : that.matchSrcFieldName != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (processRule != null ? !processRule.equals(that.processRule) : that.processRule != null) return false;
        if (query != null ? !query.equals(that.query) : that.query != null) return false;
        if (queryTimeField != null ? !queryTimeField.equals(that.queryTimeField) : that.queryTimeField != null)
            return false;
        if (srcHost != null ? !srcHost.equals(that.srcHost) : that.srcHost != null) return false;
        if (srcLoginId != null ? !srcLoginId.equals(that.srcLoginId) : that.srcLoginId != null) return false;
        if (srcPassword != null ? !srcPassword.equals(that.srcPassword) : that.srcPassword != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (synchAdapter != null ? !synchAdapter.equals(that.synchAdapter) : that.synchAdapter != null) return false;
        if (synchConfigId != null ? !synchConfigId.equals(that.synchConfigId) : that.synchConfigId != null)
            return false;
        if (synchFrequency != null ? !synchFrequency.equals(that.synchFrequency) : that.synchFrequency != null)
            return false;
        if (synchType != null ? !synchType.equals(that.synchType) : that.synchType != null) return false;
        if (transformationRule != null ? !transformationRule.equals(that.transformationRule) : that.transformationRule != null)
            return false;
        if (updateAttribute != null ? !updateAttribute.equals(that.updateAttribute) : that.updateAttribute != null)
            return false;
        if (validationRule != null ? !validationRule.equals(that.validationRule) : that.validationRule != null)
            return false;
        if (wsScript != null ? !wsScript.equals(that.wsScript) : that.wsScript != null) return false;
        if (wsUrl != null ? !wsUrl.equals(that.wsUrl) : that.wsUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = synchConfigId != null ? synchConfigId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (synchAdapter != null ? synchAdapter.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (managedSysId != null ? managedSysId.hashCode() : 0);
        result = 31 * result + (loadMatchOnly != null ? loadMatchOnly.hashCode() : 0);
        result = 31 * result + (updateAttribute != null ? updateAttribute.hashCode() : 0);
        result = 31 * result + (synchFrequency != null ? synchFrequency.hashCode() : 0);
        result = 31 * result + (synchType != null ? synchType.hashCode() : 0);
        result = 31 * result + (processRule != null ? processRule.hashCode() : 0);
        result = 31 * result + (validationRule != null ? validationRule.hashCode() : 0);
        result = 31 * result + (transformationRule != null ? transformationRule.hashCode() : 0);
        result = 31 * result + (matchFieldName != null ? matchFieldName.hashCode() : 0);
        result = 31 * result + (matchManagedSysId != null ? matchManagedSysId.hashCode() : 0);
        result = 31 * result + (matchSrcFieldName != null ? matchSrcFieldName.hashCode() : 0);
        result = 31 * result + (srcLoginId != null ? srcLoginId.hashCode() : 0);
        result = 31 * result + (srcPassword != null ? srcPassword.hashCode() : 0);
        result = 31 * result + (srcHost != null ? srcHost.hashCode() : 0);
        result = 31 * result + (driver != null ? driver.hashCode() : 0);
        result = 31 * result + (connectionUrl != null ? connectionUrl.hashCode() : 0);
        result = 31 * result + (query != null ? query.hashCode() : 0);
        result = 31 * result + (queryTimeField != null ? queryTimeField.hashCode() : 0);
        result = 31 * result + (lastExecTime != null ? lastExecTime.hashCode() : 0);
        result = 31 * result + (lastRecProcessed != null ? lastRecProcessed.hashCode() : 0);
        result = 31 * result + (customMatchRule != null ? customMatchRule.hashCode() : 0);
        result = 31 * result + (customAdatperScript != null ? customAdatperScript.hashCode() : 0);
        result = 31 * result + (customMatchAttr != null ? customMatchAttr.hashCode() : 0);
        result = 31 * result + (baseDn != null ? baseDn.hashCode() : 0);
        result = 31 * result + (wsUrl != null ? wsUrl.hashCode() : 0);
        result = 31 * result + (wsScript != null ? wsScript.hashCode() : 0);
        return result;
    }
}
