package org.openiam.idm.srvc.user.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for userNoteSet complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="userNoteSet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userNoteItem" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="userNote" type="{urn:idm.openiam.org/srvc/user/dto}userNote" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userNoteSet", propOrder = {
        "userNoteItem"
})
public class UserNoteSet {

    protected List<UserNoteSet.UserNoteItem> userNoteItem;

    /**
     * Gets the value of the userNoteItem property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userNoteItem property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserNoteItem().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link UserNoteSet.UserNoteItem }
     */
    public List<UserNoteSet.UserNoteItem> getUserNoteItem() {
        if (userNoteItem == null) {
            userNoteItem = new ArrayList<UserNoteSet.UserNoteItem>();
        }
        return this.userNoteItem;
    }


    /**
     * <p>Java class for anonymous complex type.
     * <p/>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="userNote" type="{urn:idm.openiam.org/srvc/user/dto}userNote" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "userNote"
    })
    public static class UserNoteItem {

        protected UserNote userNote;

        /**
         * Gets the value of the userNote property.
         *
         * @return possible object is
         *         {@link UserNote }
         */
        public UserNote getUserNote() {
            return userNote;
        }

        /**
         * Sets the value of the userNote property.
         *
         * @param value allowed object is
         *              {@link UserNote }
         */
        public void setUserNote(UserNote value) {
            this.userNote = value;
        }

    }

}
