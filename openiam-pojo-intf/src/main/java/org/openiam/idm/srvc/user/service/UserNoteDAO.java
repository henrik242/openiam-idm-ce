package org.openiam.idm.srvc.user.service;

import org.openiam.idm.srvc.user.dto.UserNote;

import java.util.List;

public interface UserNoteDAO {

    public void persist(UserNote transientInstance);

    public void attachDirty(UserNote instance);

    public void attachClean(UserNote instance);

    public void delete(UserNote persistentInstance);

    public UserNote merge(UserNote detachedInstance);

    public UserNote findById(java.lang.String id);

    public List findByExample(UserNote instance);

    List<UserNote> findUserNotes(String userId);

    /**
     * Delete all the notes associated with a user.
     *
     * @param userId
     */
    void deleteUserNotes(String userId);


}
