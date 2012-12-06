package org.openiam.base.ws;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Property", propOrder = {
        "entry"
})
public class PropertyMap implements Serializable {

    private static final long serialVersionUID = 950443282629548603L;

    protected List<PropertyEntry> entry;

    public PropertyMap() {
    }

    public PropertyMap(final Map<String,String> map) {
        for(Map.Entry<String, String> entry : map.entrySet()) {
            addProperty(entry.getKey(), entry.getValue());
        }
    }

    public List<PropertyMap.PropertyEntry> getPropertyEntry() {
        if (entry == null) {
            entry = new ArrayList<PropertyEntry>();
        }
        return this.entry;
    }

    public void addEntry(PropertyEntry propertyEntry) {
        getPropertyEntry().add(propertyEntry);
    }

    public void addProperty(String key, String value) {
        getPropertyEntry().add(new PropertyEntry(key, value));
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "Entry")
    public static class PropertyEntry {
        @XmlAttribute
        protected String key;
        @XmlElement
        protected String value;

        public PropertyEntry() {
        }

        public PropertyEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PropertyEntry that = (PropertyEntry) o;

            if (key != null ? !key.equals(that.key) : that.key != null) return false;
            if (value != null ? !value.equals(that.value) : that.value != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }
    }

    public Map<String, String> asMap() {
        Map<String, String> map = new HashMap<String, String>();
        if (this == null) return map;

        for (PropertyMap.PropertyEntry e : this.getPropertyEntry()) {
            map.put(e.getKey(), e.getValue());
        }
        return map;
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder("PropertyMap{\n");
        for(PropertyEntry propertyEntry : this.getPropertyEntry()) {
            content.append("entry=[").append(propertyEntry.getKey()).append(",").append(propertyEntry.getValue()).append("],\n");
        }
        content.append("}");
        return content.toString();
    }
}
