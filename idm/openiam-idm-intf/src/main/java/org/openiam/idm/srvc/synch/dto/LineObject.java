/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License 
 *   version 3 as published by the Free Software Foundation.
 *
 *   OpenIAM is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */

/**
 * 
 */
package org.openiam.idm.srvc.synch.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.sql.Timestamp;

/**
 * @author suneet
 *
 */
public class LineObject {
	private final Map<String,Attribute> columnMap = new HashMap<String, Attribute>();
	private Timestamp lastUpdate = null;

	public Map<String, Attribute> getColumnMap() {
		return columnMap;
	}

	public void put(String key,String name, String lineObject) {
		columnMap.put(key, new Attribute(name,lineObject));
	}
	
	public void put(String key,Attribute attr) {
	
		columnMap.put(key, attr);
	}
	
	public Attribute get(String key) {
		return columnMap.get(key);
		
	}

	public LineObject copy() {
        LineObject obj = new LineObject();
        for (Map.Entry<String, Attribute> entry : this.columnMap.entrySet()) {
            obj.put(entry.getKey(), entry.getValue().getCopy());
        }
        return obj;
	}

    @Override
    public String toString() {
        return "LineObject{" +
                "columnMap=" + columnMap +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineObject)) return false;

        LineObject that = (LineObject) o;

        if (lastUpdate != null ? !lastUpdate.equals(that.lastUpdate) : that.lastUpdate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return lastUpdate != null ? lastUpdate.hashCode() : 0;
    }
}
