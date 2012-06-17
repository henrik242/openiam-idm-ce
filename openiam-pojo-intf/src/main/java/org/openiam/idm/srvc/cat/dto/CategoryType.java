package org.openiam.idm.srvc.cat.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CategoryType", propOrder = {
        "categoryId",
        "typeId"
})
public class CategoryType implements Serializable {
    private CategoryTypeId id;

    public CategoryTypeId getId() {
        return id;
    }

    public void setId(CategoryTypeId id) {
        this.id = id;
    }
}
