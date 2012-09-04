package org.openiam.webconsole.web.model;

import java.io.Serializable;

public class GroupStatusModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;

    public GroupStatusModel(String status) {
        this.status = status;
    }

    public GroupStatusModel() {
        this("");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
