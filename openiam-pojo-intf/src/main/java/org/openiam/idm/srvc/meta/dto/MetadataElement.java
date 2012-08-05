package org.openiam.idm.srvc.meta.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

// Generated Nov 4, 2008 12:11:29 AM by Hibernate Tools 3.2.2.GA

/**
 * <code>MetadataElement</code> represents an attribute of MetadataType.
 * MetadataElement also contains parameters that define validation constraints.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetadataElement", propOrder = {
        "metadataElementId",
        "metadataTypeId",
        "attributeName",
        "description",
        "minLen",
        "maxLen",
        "defaultValue",
        "valueList",
        "label",
        "multiValue",
        "auditable",
        "required",
        "selfEditable",
        "selfViewable",
        "uiType",
        "uiSize",
        "valueSrc",
        "minValue",
        "maxValue",
        "textCase",
        "dataType"
})
public class MetadataElement implements java.io.Serializable {

    private String metadataElementId;
    private String metadataTypeId;
    private String attributeName;
    private String description;
    private Integer minLen = 0;
    private Integer maxLen;
    private String textCase;
    private String dataType;
    private Long minValue;
    private Long maxValue;
    private String defaultValue;
    private String valueList;
    private String label;
    private String multiValue;
    private Integer auditable = 1;
    private Integer required = 0;

    private Integer selfEditable =0;
    private Integer selfViewable = 0;

    private String uiType;
    private String uiSize;
    private String valueSrc;

    public MetadataElement() {
    }

    public MetadataElement(String metadataId) {
        this.metadataElementId = metadataId;
    }

    public MetadataElement(String metadataId, String typeId,
                           String attributeName, String description, Integer minLen,
                           Integer maxLen, String textCase, String dataType, Long minValue,
                           Long maxValue, String defaultValue, String valueList, String label,
                           String multiValue, Integer auditable, Integer required) {
        this.metadataElementId = metadataId;
        this.metadataTypeId = typeId;
        this.attributeName = attributeName;
        this.description = description;
        this.minLen = minLen;
        this.maxLen = maxLen;
        this.textCase = textCase;
        this.dataType = dataType;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
        this.valueList = valueList;
        this.label = label;
        this.multiValue = multiValue;
        this.auditable = auditable;
        this.required = required;
    }

    public String getMetadataElementId() {
        return metadataElementId;
    }

    public void setMetadataElementId(String metadataElementId) {
        this.metadataElementId = metadataElementId;
    }

    public String getMetadataTypeId() {
        return metadataTypeId;
    }

    public void setMetadataTypeId(String metadataTypeId) {
        this.metadataTypeId = metadataTypeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinLen() {
        return minLen;
    }

    public void setMinLen(Integer minLen) {
        this.minLen = minLen;
    }

    public Integer getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(Integer maxLen) {
        this.maxLen = maxLen;
    }

    public String getTextCase() {
        return textCase;
    }

    public void setTextCase(String textCase) {
        this.textCase = textCase;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Long getMinValue() {
        return minValue;
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValueList() {
        return valueList;
    }

    public void setValueList(String valueList) {
        this.valueList = valueList;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMultiValue() {
        return multiValue;
    }

    public void setMultiValue(String multiValue) {
        this.multiValue = multiValue;
    }

    public Integer getAuditable() {
        return auditable;
    }

    public void setAuditable(Integer auditable) {
        this.auditable = auditable;
    }

    public Integer getRequired() {
        return required;
    }

    public void setRequired(Integer required) {
        this.required = required;
    }



    public String getUiType() {
        return uiType;
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    public String getUiSize() {
        return uiSize;
    }

    public void setUiSize(String uiSize) {
        this.uiSize = uiSize;
    }

    public String getValueSrc() {
        return valueSrc;
    }

    public void setValueSrc(String valueSrc) {
        this.valueSrc = valueSrc;
    }

    public boolean isRequire() {
        if ( required == null ||  required == 0) {
            return false;
        }
        return true;
    }

    public Integer getSelfEditable() {
        return selfEditable;
    }

    public void setSelfEditable(Integer selfEditable) {
        this.selfEditable = selfEditable;
    }

    public Integer getSelfViewable() {
        return selfViewable;
    }

    public void setSelfViewable(Integer selfViewable) {
        this.selfViewable = selfViewable;
    }
}
