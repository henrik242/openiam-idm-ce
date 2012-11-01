package org.openiam.idm.srvc.org.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.openiam.idm.srvc.org.domain.OrganizationAttributeEntity;


/**
 * <p>Java class for organizationAttribute complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="organizationAttribute">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attrId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="metadataElement" type=""{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organizationId" type=""{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "organizationAttribute", propOrder = {
        "attrId",
        "metadataElementId",
        "name",
        "organizationId",
        "value"
})

public class OrganizationAttribute implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -231974705360001659L;

    protected String attrId;

    // protected MetadataElement metadataElement;
    protected String metadataElementId;

    protected String name;

    protected String organizationId;

    protected String value;

    // Constructors

    /**
     * default constructor
     */
    public OrganizationAttribute() {
    }

    /**
     * minimal constructor
     */
    public OrganizationAttribute(String companyAttrId) {
        this.attrId = companyAttrId;
    }

    /**
     * full constructor
     */
    public OrganizationAttribute(String companyAttrId,
                                 String metadataId, String organizationId, String name,
                                 String value) {
        this.attrId = companyAttrId;
        this.metadataElementId = metadataId;
        this.organizationId = organizationId;
        this.name = name;
        this.value = value;
    }

    public OrganizationAttribute(OrganizationAttributeEntity attributeEntity) {
        this.attrId = attributeEntity.getAttrId();
        this.metadataElementId = attributeEntity.getMetadataElementId();
        this.name = attributeEntity.getName();
        this.organizationId = attributeEntity.getOrganization() != null ? attributeEntity.getOrganization().getOrgId() : "";
        this.value = attributeEntity.getValue();

    }

    // Property accessors

    /**
     * Gets the value of the attrId property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAttrId() {
        return attrId;
    }

    /**
     * Sets the value of the attrId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAttrId(String value) {
        this.attrId = value;
    }


    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }


    public String getOrganizationId() {
        return this.organizationId;
    }

    /**
     * Gets the value of the value property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setValue(String value) {
        this.value = value;
    }

    public String getMetadataElementId() {
        return metadataElementId;
    }

    public void setMetadataElementId(String metadataElementId) {
        this.metadataElementId = metadataElementId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationAttribute that = (OrganizationAttribute) o;

        if (attrId != null ? !attrId.equals(that.attrId) : that.attrId != null) return false;
        if (metadataElementId != null ? !metadataElementId.equals(that.metadataElementId) : that.metadataElementId != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (organizationId != null ? !organizationId.equals(that.organizationId) : that.organizationId != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = attrId != null ? attrId.hashCode() : 0;
        result = 31 * result + (metadataElementId != null ? metadataElementId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (organizationId != null ? organizationId.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
