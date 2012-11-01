package org.openiam.idm.srvc.user.service;

import org.openiam.idm.srvc.user.domain.UserNoteEntity;
import org.openiam.idm.srvc.user.dto.UserNote;

import java.util.List;

public interface UserNoteDAO {

    public void persist(UserNoteEntity transientInstance);

    public void attachDirty(UserNoteEntity instance);

    public void attachClean(UserNoteEntity instance);

    public void delete(UserNoteEntity persistentInstance);

    public UserNoteEntity merge(UserNoteEntity detachedInstance);

    public UserNoteEntity findById(java.lang.String id);

    public List findByExample(UserNoteEntity instance);

    List<UserNoteEntity> findUserNotes(String userId);

    /**
     * Delete all the notes associated with a user.
     *
     * @param userId
     */
    void deleteUserNotes(String userId);


}
