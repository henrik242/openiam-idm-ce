package org.openiam.connector.type;

import org.openiam.idm.srvc.recon.dto.ReconciliationConfig;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemoteReconciliationConfig", propOrder = {
        "scriptHandler"
})
public class RemoteReconciliationConfig extends ReconciliationConfig {
    private static final long serialVersionUID = -5366866165175132136L;
    @XmlElement
    private String scriptHandler;

    public RemoteReconciliationConfig() {
    }

    public RemoteReconciliationConfig(ReconciliationConfig config) {
       this(config.getReconConfigId(), config.getResourceId(), null, config.getFrequency(), config.getStatus(), null, null);
    }

    public RemoteReconciliationConfig(String reconConfigId) {
        super(reconConfigId);
    }

    public RemoteReconciliationConfig(String reconConfigId, String resourceId, String mode, String frequency, String status, Integer attributeLevelCheck, Integer updateChangedAttribute) {
        super(reconConfigId, resourceId, mode, frequency, status, attributeLevelCheck, updateChangedAttribute);
    }

    public String getScriptHandler() {
        return scriptHandler;
    }

    public void setScriptHandler(String scriptHandler) {
        this.scriptHandler = scriptHandler;
    }
}
