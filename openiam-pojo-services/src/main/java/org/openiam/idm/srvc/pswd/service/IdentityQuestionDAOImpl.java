package org.openiam.idm.srvc.pswd.service;

// Generated Aug 23, 2009 12:07:53 AM by Hibernate Tools 3.2.2.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openiam.idm.srvc.pswd.dto.IdentityQuestion;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.continfo.domain.IdentityQuestionEntity;
import static org.hibernate.criterion.Example.create;

/**
 * DAO implementation object for the domain model class IdentityQuestion.
 */
public class IdentityQuestionDAOImpl implements IdentityQuestionDAO {

	private static final Log log = LogFactory
			.getLog(IdentityQuestionDAOImpl.class);

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
	 * @see org.openiam.idm.srvc.pswd.service.IdentityQuestionEntityDAO#add(org.openiam.idm.srvc.pswd.dto.IdentityQuestionEntity)
	 */
	public IdentityQuestionEntity add(IdentityQuestionEntity transientInstance) {
		log.debug("persisting IdentityQuestionEntity instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
			return transientInstance;
		} catch (HibernateException re) {
			log.error("persist failed", re);
			throw re;
		}
	}


	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.pswd.service.IdentityQuestionEntityDAO#remove(org.openiam.idm.srvc.pswd.dto.IdentityQuestionEntity)
	 */
	public void remove(IdentityQuestionEntity persistentInstance) {
		log.debug("deleting IdentityQuestionEntity instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (HibernateException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.pswd.service.IdentityQuestionEntityDAO#update(org.openiam.idm.srvc.pswd.dto.IdentityQuestionEntity)
	 */
	public IdentityQuestionEntity update(IdentityQuestionEntity detachedInstance) {
		log.debug("merging IdentityQuestionEntity instance");
		try {
			IdentityQuestionEntity result = (IdentityQuestionEntity) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (HibernateException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.openiam.idm.srvc.pswd.service.IdentityQuestionEntityDAO#findById(java.lang.String)
	 */
	public IdentityQuestionEntity findById(java.lang.String id) {
		log.debug("getting IdentityQuestionEntity instance with id: " + id);
		try {
			IdentityQuestionEntity instance = (IdentityQuestionEntity) sessionFactory
					.getCurrentSession()
					.get(org.openiam.idm.srvc.continfo.domain.IdentityQuestionEntity.class,
							id);
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
	
	public List<IdentityQuestionEntity> findAllQuestions() {
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("from IdentityQuestionEntity iq "
				+ " order by iq.questionText asc ");
		// enable caching
		qry.setCacheable(true);
		qry.setCacheRegion("query.iq.findAllQuestions");
		
		List<IdentityQuestionEntity> result = (List<IdentityQuestionEntity>) qry.list();
		if (result == null || result.size() == 0)
			return null;

		return result;
	}
	
	public List<IdentityQuestionEntity> findAllQuestionsByQuestionGroup(String questionGroup) {
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("from IdentityQuestionEntity iq "
				+ " where iq.identityQuestGrp.identityQuestGrpId = :questionGroup" +
				   " order by iq.questionText asc ");
		qry.setString("questionGroup", questionGroup);
		// enable caching
		qry.setCacheable(true);
		qry.setCacheRegion("query.iq.findQuestionByQuestionGroup");
		
		List<IdentityQuestionEntity> result = (List<IdentityQuestionEntity>) qry.list();
		if (result == null || result.size() == 0)
			return null;

		return result;		
	}
	public List<IdentityQuestionEntity> findAllQuestionsByUser(String userId) {
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("from IdentityQuestionEntity iq "
				+ " where iq.userId = :userId" +
				  " order by iq.questionText asc ");
		qry.setString("userId", userId);

		
		List<IdentityQuestionEntity> result = (List<IdentityQuestionEntity>) qry.list();
		if (result == null || result.size() == 0)
			return null;

		return result;			
	}
	


}
