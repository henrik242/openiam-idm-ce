package org.openiam.connector.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchRequest", propOrder = {
        "searchValue",
        "searchQuery",
        "returnData",
        "scriptHandler"
})
public class SearchRequest extends RequestType
{
    @XmlElement
    private String scriptHandler;

    @XmlElement(required = true)
    protected String searchValue;                          // the value that we are searching for


    protected String searchQuery;                          // query to search for - if its ldap or AD then this is a search filter

    protected ReturnData returnData;                       // attributes that should be returned.


    /**
     * Gets the value of the returnData property.
     *
     * @return
     *     possible object is
     *     {@link org.openiam.connector.type.ReturnData }
     *
     */
    public ReturnData getReturnData() {
        if (returnData == null) {
            return ReturnData.EVERYTHING;
        } else {
            return returnData;
        }
    }

    /**
     * Sets the value of the returnData property.
     *
     * @param value
     *     allowed object is
     *     {@link org.openiam.connector.type.ReturnData }
     *     
     */
    public void setReturnData(ReturnData value) {
        this.returnData = value;
    }

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

    public String getScriptHandler() {
        return scriptHandler;
    }

    public void setScriptHandler(String scriptHandler) {
        this.scriptHandler = scriptHandler;
    }
}
