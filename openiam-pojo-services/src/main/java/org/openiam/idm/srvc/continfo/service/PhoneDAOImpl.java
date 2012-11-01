package org.openiam.idm.srvc.continfo.service;


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
import org.openiam.idm.srvc.continfo.domain.PhoneEntity;


/**
 * Implementation for PhoneDAO. Associated with a RDBMS.
 * @author Suneet Shah
 *
 */
public class PhoneDAOImpl implements PhoneDAO {

	private static final Log log = LogFactory.getLog(PhoneDAOImpl.class);

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

	public PhoneEntity findById(java.lang.String id) {

		try {
			PhoneEntity instance = (PhoneEntity) sessionFactory
					.getCurrentSession().get(PhoneEntity.class, id);
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
	public PhoneEntity add(PhoneEntity instance) {

		try {
			sessionFactory.getCurrentSession().persist(instance);
			log.debug("persist successful");
			return instance;
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("persist failed", re);
			throw re;
		}

	}

	/**
	 * Removes an existing instance
	 * @param instance
	 */
	public void remove(PhoneEntity instance) {

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
	public void update(PhoneEntity instance) {

		try {
			sessionFactory.getCurrentSession().merge(instance);
			log.debug("merge successful");
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("merge failed", re);
			throw re;
		}

	}

	

	public PhoneEntity findByName(String name, String parentId, String parentType) {


		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(PhoneEntity.class)
                .add(Restrictions.eq("parent.userId",parentId))
                .add(Restrictions.eq("name",name))
                .add(Restrictions.eq("parentType", parentType));


		List<PhoneEntity> result = (List<PhoneEntity>)criteria.list();
		if (result == null || result.size() == 0)
			return null;
		return result.get(0);		

	}

	public Map<String, PhoneEntity> findByParent(String parentId, String parentType) {

		Map<String, PhoneEntity> adrMap = new HashMap<String,PhoneEntity>();

		List<PhoneEntity> addrList = findByParentAsList(parentId, parentType);
		if (addrList == null)
			return null;
		int size = addrList.size();
		for (int i=0; i<size; i++) {
			PhoneEntity adr = addrList.get(i);
			//adrMap.put(adr.getDescription(), adr);
			adrMap.put(adr.getPhoneId(), adr);
		}
		if (adrMap.isEmpty())
			return null;
		return adrMap;
	}

	public List<PhoneEntity> findByParentAsList(String parentId, String parentType) {

		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(PhoneEntity.class)
                .add(Restrictions.eq("parent.userId",parentId))
                .add(Restrictions.eq("parentType", parentType));

		List<PhoneEntity> result = (List<PhoneEntity>)criteria.list();
		if (result == null || result.size() == 0)
			return null;
		return result;		
	}

	public PhoneEntity findDefault(String parentId, String parentType) {

		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(PhoneEntity.class)
                .add(Restrictions.eq("parent.userId",parentId))
                .add(Restrictions.eq("parentType", parentType))
                .add(Restrictions.eq("isDefault",1));

		return (PhoneEntity)criteria.uniqueResult();
	}

	public void removeByParent(String parentId, String parentType) {
		Session session = sessionFactory.getCurrentSession();
		Query qry = session.createQuery("delete org.openiam.idm.srvc.continfo.domain.PhoneEntity a " +
				" where a.parent.userId = :parentId and   " +
				" 		a.parentType = :parentType");
		qry.setString("parentId", parentId);
		qry.setString("parentType", parentType);
		qry.executeUpdate();	
		
	}


	
	public void savePhoneMap(String parentId, String parentType, Map<String, PhoneEntity> adrMap) {
		// get the current map and then compare each record with the map that has been passed in.
		Map<String,PhoneEntity> currentMap =  this.findByParent(parentId, parentType);
		if (currentMap != null) {
			Iterator<PhoneEntity> it = currentMap.values().iterator();
			while (it.hasNext()) {
				PhoneEntity curPhone =  it.next();
				PhoneEntity newPhone = adrMap.get(curPhone.getPhoneId());
				if (newPhone == null) {
					this.remove(curPhone);
				}else {
					this.update(newPhone);
				}
					
			}			
		}
		Collection<PhoneEntity> adrCol = adrMap.values();
		Iterator<PhoneEntity> itr = adrCol.iterator();
		while (itr.hasNext()) {
			PhoneEntity newPhone = itr.next();
			PhoneEntity curPhone = null;
			if (currentMap != null ) {
				curPhone = currentMap.get(newPhone.getPhoneId());
			}
			if (curPhone == null) {
				add(newPhone);
			}
		}
		
	}

	public PhoneEntity[] findByParentAsArray(String parentId, String parentType) {

		List<PhoneEntity> result = this.findByParentAsList(parentId, parentType);
		if (result == null || result.size() == 0)
			return null;
		return (PhoneEntity[])result.toArray();
	}


	public void savePhoneArray(String parentId, String parentType, PhoneEntity[] phoneAry) {
		int len = phoneAry.length;

		Map<String,PhoneEntity> currentMap =  this.findByParent(parentId, parentType);
		if (currentMap != null) {
			Set<String> keys = currentMap.keySet();
			Iterator<String> it = keys.iterator();
			int ctr = 0;
			while (it.hasNext()) {
				String key = it.next();
				PhoneEntity newPhone = getPhoneFromArray(phoneAry, key);
				PhoneEntity curPhone = currentMap.get(key);
				if (newPhone == null) {
					// address was removed - deleted
					remove(curPhone);
				}else if (!curPhone.equals(newPhone)) {
					// object changed
					this.update(newPhone);
				}
			}
		}
		// add the new records in currentMap that are not in the existing records
		for (int i=0; i<len; i++) {
			PhoneEntity curAdr = null;
			PhoneEntity  phone = phoneAry[i];
			String key =  phone.getPhoneId();
			if (currentMap != null )  {
				curAdr = currentMap.get(key);
			}
			if (curAdr == null) {
				// new address object
				this.add(phone);
			}
		}
		
	}
	
	private PhoneEntity getPhoneFromArray(PhoneEntity[] phoneAry, String id) {
		PhoneEntity phone = null;
		int len = phoneAry.length;
		for (int i=0;i<len;i++) {
			PhoneEntity tempPhone = phoneAry[i];
			if (tempPhone.getPhoneId().equals(id)) {
				return tempPhone;
			}
		}
		return phone;
	}
	
}
