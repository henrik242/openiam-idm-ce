package org.openiam.am.srvc.resattr.dto;

import org.openiam.base.ws.Response;
import org.openiam.base.ws.ResponseStatus;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WebResourceResponse", propOrder = {
        "attributeList"
})
public class WebResourceResponse  extends Response {

    @XmlElementWrapper(name="attributeList")
    @XmlElement(name = "attribute")
    List<Attribute> attributeList = new ArrayList<Attribute>();



    // Constructors

    /**
     * default constructor
     */
    public WebResourceResponse() {
    }

    public WebResourceResponse(ResponseStatus s) {
        super(s);
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }
}
