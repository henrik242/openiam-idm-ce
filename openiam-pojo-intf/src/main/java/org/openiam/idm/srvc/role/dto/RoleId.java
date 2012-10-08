package org.openiam.idm.srvc.role.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for roleId complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="roleId">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="roleId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "roleId", propOrder = {
        "roleId",
        "serviceId"
})
public class RoleId implements java.io.Serializable {

    protected String roleId;
    protected String serviceId;

    // @TODO aps temp added these 2 constructors
    public RoleId(String serviceId, String roleId) {
        this.roleId = roleId;
        this.serviceId = serviceId;
    }

    public RoleId() {
    }


    /**
     * Gets the value of the roleId property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * Sets the value of the roleId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRoleId(String value) {
        this.roleId = value;
    }

    /**
     * Gets the value of the serviceId property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Sets the value of the serviceId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setServiceId(String value) {
        this.serviceId = value;
    }

    @Override
    public String toString() {
        return "RoleId{" +
                "roleId='" + roleId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleId)) return false;

        RoleId roleId1 = (RoleId) o;

        if (roleId != null ? !roleId.equals(roleId1.roleId) : roleId1.roleId != null) return false;
        if (serviceId != null ? !serviceId.equals(roleId1.serviceId) : roleId1.serviceId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = roleId != null ? roleId.hashCode() : 0;
        result = 31 * result + (serviceId != null ? serviceId.hashCode() : 0);
        return result;
    }
}
