package org.openiam.idm.srvc.continfo.service;

// Generated Jun 12, 2007 10:46:15 PM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openiam.idm.srvc.continfo.domain.EmailAddressEntity;


/**
 * Implementation for EmailAddresDAO. Associated with a RDBMS.
 * @author Suneet Shah
 *
 */
public class EmailAddressDAOImpl  implements EmailAddressDAO {  

	private static final Log log = LogFactory.getLog(AddressDAOImpl.class);

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

	
	

	public EmailAddressEntity findById(java.lang.String id) {

		try {
			EmailAddressEntity instance = (EmailAddressEntity) sessionFactory
					.getCurrentSession().get(EmailAddressEntity.class, id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/**
	 * Adds a new instance
	 * @param instance
	 */
	public EmailAddressEntity add(EmailAddressEntity instance) {

		try {
			sessionFactory.getCurrentSession().persist(instance);
			log.debug("persist successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}

	}

	/**
	 * Removes an existing instance
	 * @param instance
	 */
	public void remove(EmailAddressEntity instance) {

		try {
			sessionFactory.getCurrentSession().delete(instance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}

	}
	/**
	 * Updates an existing instance
	 * @param instance
	 */
	public void update(EmailAddressEntity instance) {
		log.debug("merging instance");
		try {
			sessionFactory.getCurrentSession().merge(instance);
			log.debug("merge successful");
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}

	}

	

	public EmailAddressEntity findByName(String name, String parentId, String parentType) {


		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(EmailAddressEntity.class)
                .createAlias("parent","p")
                .add(Restrictions.eq("p.userId",parentId))
                .add(Restrictions.eq("parentType",parentType))
                .add(Restrictions.eq("name",name));

		List<EmailAddressEntity> result = (List<EmailAddressEntity>)criteria.list();
		if (result == null || result.size() == 0)
			return null;
		return result.get(0);	

	}

	public Map<String, EmailAddressEntity> findByParent(String parentId, String parentType) {

		
		Map<String, EmailAddressEntity> adrMap = new HashMap<String,EmailAddressEntity>();

		List<EmailAddressEntity> addrList = findByParentAsList(parentId, parentType);
		if (addrList == null)
			return null;
		int size = addrList.size();
		for (int i=0; i<size; i++) {
			EmailAddressEntity adr = addrList.get(i);
			//adrMap.put(adr.getDescription(), adr);
			adrMap.put(adr.getEmailId(), adr);
		}
		if (adrMap.isEmpty())
			return null;
		return adrMap;
	}

	public List<EmailAddressEntity> findByParentAsList(String parentId, String parentType) {


		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(EmailAddressEntity.class)
                .createAlias("parent","p")
                .add(Restrictions.eq("p.userId",parentId))
                .add(Restrictions.eq("parentType",parentType));

		List<EmailAddressEntity> result = (List<EmailAddressEntity>)criteria.list();
		if (result == null || result.size() == 0)
			return null;
		return result;		
	}

	public EmailAddressEntity findDefault(String parentId, String parentType) {


		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(EmailAddressEntity.class)
                .createAlias("parent","p")
                .add(Restrictions.eq("p.userId",parentId))
                .add(Restrictions.eq("parentType",parentType))
                .add(Restrictions.eq("isDefault",1));

		return (EmailAddressEntity)criteria.uniqueResult();
	}

	public void removeByParent(String parentId, String parentType) {
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.continfo.domain.EmailAddressEntity a " +
				" where a.parent.userId = :parentId and   " +
				" 		a.parentType = :parentType");
		qry.setString("parentId", parentId);
		qry.setString("parentType", parentType);
		qry.executeUpdate();	
		
	}

	
	public void saveEmailAddressMap(String parentId, String parentType, Map<String, EmailAddressEntity> adrMap) {
		// get the current map and then compare each record with the map that has been passed in.
		Map<String, EmailAddressEntity> currentMap =  this.findByParent(parentId, parentType);
		if (currentMap != null) {
			Iterator<EmailAddressEntity> it = currentMap.values().iterator();
			while (it.hasNext()) {
				EmailAddressEntity curEmail =  it.next();
				EmailAddressEntity newEmail = adrMap.get(curEmail.getEmailId());
				if (newEmail == null) {
					this.remove(curEmail);
				}else {
					this.update(newEmail);
				}
					
			}
		}
		// add the new records in currentMap that are not in the existing records
		Collection<EmailAddressEntity> adrCol = adrMap.values();
		Iterator<EmailAddressEntity> itr = adrCol.iterator();
		while (itr.hasNext()) {
			EmailAddressEntity newEmail = itr.next();
			EmailAddressEntity curEmail = null;
			if (currentMap != null ) {
				curEmail = currentMap.get(newEmail.getEmailId());
			}
			if (curEmail == null) {
				add(newEmail);
			}
		}		
	}

	public EmailAddressEntity[] findByParentAsArray(String parentId, String parentType) {

		List<EmailAddressEntity> result = this.findByParentAsList(parentId, parentType);
		if (result == null || result.size() == 0)
			return null;
		return (EmailAddressEntity[])result.toArray();
	}



	public void saveEmailAddressArray(String parentId, String parentType,	EmailAddressEntity[] emailAry) {
		int len = emailAry.length;

		Map<String, EmailAddressEntity> currentMap =  this.findByParent(parentId, parentType);
		if (currentMap != null) {
			Set<String> keys = currentMap.keySet();
			Iterator<String> it = keys.iterator();
			int ctr = 0;
			while (it.hasNext()) {
				String key = it.next();
				EmailAddressEntity newEmail = getEmailFromArray(emailAry, key);
				EmailAddressEntity curEmail = currentMap.get(key);
				if (newEmail == null) {
					// address was removed - deleted
					remove(curEmail);
				}else if (!curEmail.equals(newEmail)) {
					// object changed
					this.update(newEmail);
				}
			}
		}
		// add the new records in currentMap that are not in the existing records
		for (int i=0; i<len; i++) {
			EmailAddressEntity curAdr = null;
			EmailAddressEntity  email = emailAry[i];
			String key =  email.getEmailId();
			if (currentMap != null )  {
				curAdr = currentMap.get(key);
			}
			if (curAdr == null) {
				// new address object
				this.add(email);
			}
		}
		
	}

	private EmailAddressEntity getEmailFromArray(EmailAddressEntity[] adrAry, String id) {
		EmailAddressEntity adr = null;
		int len = adrAry.length;
		for (int i=0;i<len;i++) {
			EmailAddressEntity tempAdr = adrAry[i];
			if (tempAdr.getEmailId().equals(id)) {
				return tempAdr;
			}
		}
		return adr;
	}
}
