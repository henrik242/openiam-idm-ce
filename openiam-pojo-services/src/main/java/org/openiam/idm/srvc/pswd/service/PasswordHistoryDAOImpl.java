package org.openiam.idm.srvc.pswd.service;

// Generated Jan 23, 2010 1:06:13 AM by Hibernate Tools 3.2.2.GA

import java.util.Date;
import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openiam.idm.srvc.pswd.dto.PasswordHistory;
import org.openiam.idm.srvc.pswd.dto.UserIdentityAnswer;
import org.openiam.idm.srvc.pswd.service.PasswordHistoryDAO;
import org.openiam.idm.srvc.continfo.domain.PasswordHistoryEntity;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class PwdHistory.
 * @see org.openiam.idm.srvc.pswd.service.PwdHistory
 * @author Hibernate Tools
 */
public class PasswordHistoryDAOImpl implements PasswordHistoryDAO {

	private static final Log log = LogFactory.getLog(PasswordHistoryDAOImpl.class);


	private SessionFactory sessionFactory;

	
	public void setSessionFactory(SessionFactory session) {
		   this.sessionFactory = session;
	}
	
	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	
	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.pswd.service.PasswordHistoryDAO#add(org.openiam.idm.srvc.pswd.dto.PasswordHistoryEntity)
	 */
	public void add(PasswordHistoryEntity transientInstance) {
		log.debug("persisting PwdHistory instance");
		try {
			if (transientInstance != null && transientInstance.getDateCreated() == null) {
				transientInstance.setDateCreated(new Date(System.currentTimeMillis()));
			}
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (HibernateException re) {
			log.error("persist failed", re);
			throw re;
		}
	}



	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.pswd.service.PasswordHistoryEntityDAO#remove(org.openiam.idm.srvc.pswd.dto.PasswordHistoryEntity)
	 */
	public void remove(PasswordHistoryEntity persistentInstance) {
		log.debug("deleting PwdHistory instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (HibernateException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.pswd.service.PasswordHistoryEntityDAO#update(org.openiam.idm.srvc.pswd.dto.PasswordHistoryEntity)
	 */
	public PasswordHistoryEntity update(PasswordHistoryEntity detachedInstance) {
		log.debug("merging PwdHistory instance");
		try {
			PasswordHistoryEntity result = (PasswordHistoryEntity) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (HibernateException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.pswd.service.PasswordHistoryEntityDAO#findById(java.lang.String)
	 */
	public PasswordHistoryEntity findById(java.lang.String id) {
		log.debug("getting PwdHistory instance with id: " + id);
		try {
			PasswordHistoryEntity instance = (PasswordHistoryEntity) sessionFactory
					.getCurrentSession().get(
							"org.openiam.idm.srvc.continfo.domain.PasswordHistoryEntity", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (HibernateException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.pswd.service.PasswordHistoryEntityDAO#findPasswordHistoryEntityByPrincipal(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<PasswordHistoryEntity> findPasswordHistoryByPrincipal(
			String domainId, String principal, String managedSys, int versions) {
		log.debug("getting PwdHistoryByPrinciPal instance with id: " + principal);
		try {
			Session session = sessionFactory.getCurrentSession();
			Query qry = session.createQuery("from PasswordHistoryEntity ph "
					+ " where ph.serviceId = :domainId and " +
					  " ph.managedSysId = :managedSys and " +
					  " ph.login = :principal " +
					  " order by ph.dateCreated desc ");
			qry.setString("domainId", domainId);
			qry.setString("managedSys", managedSys);
			qry.setString("principal", principal);

			qry.setFetchSize(versions);
			qry.setMaxResults(versions);	
			
			List<PasswordHistoryEntity> result = (List<PasswordHistoryEntity>) qry.list();
			if (result == null || result.size() == 0)
				return null;
			return result;
			
		}catch (HibernateException re) {
			log.error("get failed", re);
			throw re;
		}
	}


}
