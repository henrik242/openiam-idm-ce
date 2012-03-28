package org.openiam.idm.srvc.synch.service.generic;

import java.util.*;

/**
 * Provides a map of Adapters for each type of Object.
 * User: suneetshah
 * Date: 3/27/12
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ObjectAdapterMap {
    Map adapterMap;

    public Map getAdapterMap() {
        return adapterMap;
    }

    public void setAdapterMap(Map adapterMap) {
        this.adapterMap = adapterMap;
    }

    @Override
    public String toString() {
        return "ObjectAdapterMap{" +
                "adapterMap=" + adapterMap +
                '}';
    }
}
