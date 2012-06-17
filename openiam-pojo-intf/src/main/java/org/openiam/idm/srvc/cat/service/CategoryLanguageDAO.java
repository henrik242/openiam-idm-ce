package org.openiam.idm.srvc.cat.service;

import org.openiam.idm.srvc.cat.dto.CategoryLanguage;

import java.util.List;

public interface CategoryLanguageDAO {

    public abstract void add(CategoryLanguage transientInstance);

    public abstract void remove(CategoryLanguage persistentInstance);

    public abstract CategoryLanguage update(CategoryLanguage detachedInstance);

    public abstract CategoryLanguage findById(
            org.openiam.idm.srvc.cat.dto.CategoryLanguageId id);

    public abstract List<CategoryLanguage> findByExample(
            CategoryLanguage instance);

}