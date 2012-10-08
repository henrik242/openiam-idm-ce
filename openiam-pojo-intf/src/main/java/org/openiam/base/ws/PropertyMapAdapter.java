package org.openiam.base.ws;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PropertyMapAdapter extends XmlAdapter<PropertyMap, Map<String, String>>  {

    @Override
    public PropertyMap marshal(Map<String, String> v) throws Exception {
        PropertyMap map = new PropertyMap();
        if (v == null) return map;

        for (Map.Entry<String, String> e : v.entrySet()) {
            PropertyMap.PropertyEntry entry = new PropertyMap.PropertyEntry();
            entry.setValue(e.getValue());
            entry.setKey(e.getKey());
            map.getPropertyEntry().add(entry);
        }
        return map;
    }

    @Override
    public Map<String, String> unmarshal(PropertyMap v) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        if (v == null) return map;

        for (PropertyMap.PropertyEntry e : v.getPropertyEntry()) {
            map.put(e.getKey(), e.getValue());
        }
        return map;
    }
}
