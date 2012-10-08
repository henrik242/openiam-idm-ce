package org.openiam.base.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Property", propOrder = {
        "entry"
})
public class PropertyMap {

    protected List<PropertyEntry> entry;

    public List<PropertyMap.PropertyEntry> getPropertyEntry() {
        if (entry == null) {
            entry = new ArrayList<PropertyEntry>();
        }
        return this.entry;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "Entry")
    public static class PropertyEntry {
        @XmlAttribute
        protected String key;
        @XmlElement
        protected String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the key property.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getKey() {
            return key;
        }

        /**
         * Sets the value of the key property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setKey(String value) {
            this.key = value;
        }

    }

}
