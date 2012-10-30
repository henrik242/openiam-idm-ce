package org.openiam.idm.srvc.user.service;

import org.openiam.idm.srvc.user.domain.SupervisorEntity;
import org.openiam.idm.srvc.user.dto.Supervisor;

import java.util.List;

public interface SupervisorDAO {

    public abstract void add(SupervisorEntity transientInstance);

    public abstract void remove(SupervisorEntity persistentInstance);

    public abstract SupervisorEntity update(SupervisorEntity detachedInstance);

    public abstract SupervisorEntity findById(java.lang.String id);

    public abstract List<SupervisorEntity> findByExample(SupervisorEntity instance);

    /**
     * Returns a list of Supervisor objects that represents the employees or users for this supervisor
     *
     * @param supervisorId
     * @return
     */
    public List<SupervisorEntity> findEmployees(String supervisorId);

    /**
     * Returns a List of supervisor objects that represents the supervisors for this employee or user.
     *
     * @param employeeId
     * @return
     */
    public List<SupervisorEntity> findSupervisors(String employeeId);

    /**
     * Returns the primary supervisor for this employee. Null if no primary is defined.
     *
     * @param employeeId
     * @return
     */
    public SupervisorEntity findPrimarySupervisor(String employeeId);
}