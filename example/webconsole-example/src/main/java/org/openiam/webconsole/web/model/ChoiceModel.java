package org.openiam.webconsole.web.model;

import java.io.Serializable;

/**
 * User: Alexander Duckardt<br/>
 * Date: 09/06/12
 */
public class ChoiceModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String value;
    private String label;

    public ChoiceModel() {
        this("", "");
    }

    public ChoiceModel(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
