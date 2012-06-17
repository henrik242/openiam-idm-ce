package org.openiam.idm.srvc.lang.service;

import org.openiam.idm.srvc.lang.dto.Language;

/**
 * <code>LanguageDataService</code> provides a service to manage the
 * list of languages stored in OpenIAM.
 *
 * @author Suneet Shah
 * @version 2.0
 */


public interface LanguageDataService {

    /**
     * Adds a new language to the list
     *
     * @param lg
     */
    public void addLanguage(Language lg);

    /**
     * Updates an existing language in the list
     *
     * @param lg
     */
    public void updateLanguage(Language lg);

    /**
     * Removes a languages from the list of languages
     *
     * @param langCd
     */
    public void removeLanguage(String langCd);

    /**
     * Returns an array of all languages
     *
     * @return
     */
    public Language[] allLanguages();

    /**
     * Returns the language specified by the language
     *
     * @param languageCd
     * @return
     */
    public Language getLanguage(String languageCd);


}