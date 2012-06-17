package org.openiam.idm.srvc.cat.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CategoryTypeId", propOrder = {
        "categoryId",
        "typeId"
})
public class CategoryTypeId implements Serializable {
    private String categoryId;
    private String typeId;


    public CategoryTypeId() {
        super();
    }


    public String getCategoryId() {
        return categoryId;
    }


    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    public String getTypeId() {
        return typeId;
    }


    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryTypeId that = (CategoryTypeId) o;

        if (categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null) return false;
        if (typeId != null ? !typeId.equals(that.typeId) : that.typeId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = categoryId != null ? categoryId.hashCode() : 0;
        result = 31 * result + (typeId != null ? typeId.hashCode() : 0);
        return result;
    }
}
