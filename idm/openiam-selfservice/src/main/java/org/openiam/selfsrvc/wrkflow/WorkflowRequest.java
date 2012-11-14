package org.openiam.selfsrvc.wrkflow;

/**
 * Class used to capture request details from the user and pass them to the main controller
 */
public class WorkflowRequest {

    String workflowResId;
    String personId;
    String description;
    String requestorId;

    public WorkflowRequest() {
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkflowResId() {
        return workflowResId;
    }

    public void setWorkflowResId(String workflowResId) {
        this.workflowResId = workflowResId;
    }

    public String getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }
}
