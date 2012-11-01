package org.openiam.idm.srvc.continfo.service;


import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;

import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.HibernateException;

import org.hibernate.criterion.Restrictions;
import org.openiam.exception.data.DataException;
import org.openiam.idm.srvc.continfo.domain.AddressEntity;


/**
 * DAO Implementation for domain model class Address.
 * @see org.openiam.idm.srvc.continfo.dto.Address
 */
public class AddressDAOImpl implements AddressDAO {


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


	/**
	 * Return an address object for the id.
	 * @param id
	 */
	public AddressEntity findById(java.lang.String id) {
		log.debug("getting Address instance with id: " + id);
		try {
			AddressEntity instance = (AddressEntity) sessionFactory.getCurrentSession()
					.get(AddressEntity.class, id);
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
	
	
	
	public List findByExample(AddressEntity instance) {
		log.debug("finding Address instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(AddressEntity.class).add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	

	public AddressEntity add(AddressEntity instance) {
		log.debug("persisting Address instance");
		try {
			sessionFactory.getCurrentSession().persist(instance);
			log.debug("persist successful");
			return instance;
		} catch (HibernateException re) {
			log.error("persist failed", re);
			throw new DataException( re.getMessage(), re.getCause() ); 
		}
		
	}
	public void update(AddressEntity instance) {
		log.debug("merging Address instance");
		try {
			//sessionFactory.getCurrentSession().saveOrUpdate(instance);
			sessionFactory.getCurrentSession().merge(instance);
			log.debug("merge successful");
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}		
	}
	public void remove(AddressEntity instance){
		log.debug("deleting Address instance");
		try {
			sessionFactory.getCurrentSession().delete(instance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}		
	}
	
	/**
	 * Persist a map of address objects at one time. Handles add, update, delete.
	 * @param adrMap
	 */
	public void saveAddressMap(String parentId, String parentType, Map<String,AddressEntity> adrMap) {
		// get the current map and then compare each record with the map that has been passed in.
		Map<String,AddressEntity> currentMap =  this.findByParent(parentId, parentType);
		if (currentMap != null) {
			Iterator<AddressEntity> it = currentMap.values().iterator();
			while (it.hasNext()) {
				AddressEntity curAdr =  it.next();
				AddressEntity newAdr = adrMap.get(curAdr.getAddressId());
				if (newAdr == null) {
					this.remove(curAdr);
				}else {
					this.update(newAdr);
				}
					
			}
		}
		
		
	
		Collection<AddressEntity> adrCol = adrMap.values();
		Iterator<AddressEntity> itr = adrCol.iterator();
		while (itr.hasNext()) {
			AddressEntity newAdr = itr.next();
			AddressEntity curAdr = null;
			if (currentMap != null ) {
				curAdr = currentMap.get(newAdr.getAddressId());
			}
			if (curAdr == null) {
				add(newAdr);
			}
		}
		
		
		
	}
	
	/**
	 * Returns a Map of Address objects for the parentId and parentType combination.
	 * The map is keyed on the address.description. Address.description indicates
	 * the type of address that we have; ie. Shipping, Billing, etc.
	 * @param parentId
	 * @param parentType
	 * @return
	 */
	public Map<String, AddressEntity> findByParent(String parentId,String parentType) {

		Map<String, AddressEntity> adrMap = new HashMap<String,AddressEntity>();

		List<AddressEntity> addrList = findByParentAsList(parentId, parentType);
		if (addrList == null)
			return null;
		int size = addrList.size();
		for (int i=0; i<size; i++) {
			AddressEntity adr = addrList.get(i);
			//adrMap.put(adr.getDescription(), adr);
			adrMap.put(adr.getAddressId(), adr);
		}
		if (adrMap.isEmpty())
			return null;
		return adrMap;
		
	}

	/**
	 * Returns a List of Address objects for the parentId and parentType combination.
	 * @param parentId
	 * @param parentType
	 * @return
	 */	
	public List<AddressEntity> findByParentAsList(String parentId,String parentType) {

		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(AddressEntity.class)
                .add(Restrictions.eq("parent.userId",parentId))
                .add(Restrictions.eq("parentType",parentType));

		List<AddressEntity> result = (List<AddressEntity>)criteria.list();
		if (result == null || result.size() == 0)
			return null;
		return result;		
	}
	
	
	/**
	 * Removes all address for a parent
	 * @param parentId
	 * @param parentType
	 */
	public void removeByParent(String parentId,String parentType) {
		Session session = sessionFactory.getCurrentSession();

		Query qry = session.createQuery("delete org.openiam.idm.srvc.continfo.domain.AddressEntity a " +
				" where a.parent.userId = :parentId and   " +
				" 		a.parentType = :parentType");
		qry.setString("parentId", parentId);
		qry.setString("parentType", parentType);
		qry.executeUpdate();	
	}
	/**
	 * Returns a default address for the parentId and parentType combination. 
	 * Returns null if a match is not found.
	 * @return
	 */
	public AddressEntity findDefault(String parentId,String parentType) {

		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(AddressEntity.class)
                .add(Restrictions.eq("parent.userId",parentId))
                .add(Restrictions.eq("parentType",parentType))
                .add(Restrictions.eq("isDefault",1));

		return (AddressEntity)criteria.uniqueResult();
		
	}
	/**
	 * Return an address object that matches the description. Returns null if a match
	 * is not found.
	 * @param name
	 * @param parentId
	 * @param parentType
	 * @return
	 */
	public AddressEntity findByName(String name, String parentId,String parentType) {

		Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(AddressEntity.class)
                .add(Restrictions.eq("parent.userId",parentId))
                .add(Restrictions.eq("parentType",parentType))
                .add(Restrictions.eq("name",name));

		List<AddressEntity> result = (List<AddressEntity>)criteria.list();
		if (result == null || result.size() == 0)
			return null;
		return result.get(0);		
	}

	public AddressEntity[] findByParentAsArray(String parentId, String parentType) {

		List<AddressEntity> result = this.findByParentAsList(parentId, parentType);
		if (result == null || result.size() == 0)
			return null;
		return (AddressEntity[])result.toArray();
	}


	public void saveAddressArray(String parentId, String parentType,	AddressEntity[] adrAry) {
		
		int len = adrAry.length;

		Map<String,AddressEntity> currentMap =  this.findByParent(parentId, parentType);
		if (currentMap != null) {
			Set<String> keys = currentMap.keySet();
			Iterator<String> it = keys.iterator();
			int ctr = 0;
			while (it.hasNext()) {
				String key = it.next();
				AddressEntity newAdr = getAddrFromArray(adrAry, key);
				AddressEntity curAdr = currentMap.get(key);
				if (newAdr == null) {
					// address was removed - deleted
					remove(curAdr);
				}else if (!curAdr.equals(newAdr)) {
					// object changed
					this.update(newAdr);
				}
			}
		}
		// add the new records in currentMap that are not in the existing records
		for (int i=0; i<len; i++) {
			AddressEntity curAdr = null;
			AddressEntity  adr = adrAry[i];
			String key =  adr.getAddressId();
			if (currentMap != null )  {
				curAdr = currentMap.get(key);
			}
			if (curAdr == null) {
				// new address object
				this.add(adr);
			}
		}
	
	}
	
	private AddressEntity getAddrFromArray(AddressEntity[] adrAry, String id) {
		AddressEntity adr = null;
		int len = adrAry.length;
		for (int i=0;i<len;i++) {
			AddressEntity tempAdr = adrAry[i];
			if (tempAdr.getAddressId().equals(id)) {
				return tempAdr;
			}
		}
		return adr;
	}
	
}
