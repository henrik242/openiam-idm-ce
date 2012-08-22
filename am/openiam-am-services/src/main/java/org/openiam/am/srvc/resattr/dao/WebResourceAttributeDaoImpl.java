package org.openiam.am.srvc.resattr.dao;

import org.hibernate.Query;
import org.openiam.am.srvc.webres.dto.WebResourceAttribute;
import org.openiam.exception.data.DataException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * User: Alexander Duckardt
 * Date: 8/15/12
 */
@Repository
public class WebResourceAttributeDaoImpl extends AbstractGenericDao<WebResourceAttribute, String>
        implements WebResourceAttributeDao {

    @Override
    @Transactional
    public void delete(String id) throws Exception {
        this.sessionFactory.getCurrentSession().createQuery(
                "delete from " + getPersistentClass().getName() + " attr where attr.attributeMapId=:attributeMapId")
                           .setParameter("attributeMapId", id).executeUpdate();
    }

    @Override
    @Transactional
    public int deleteByResourceId(String resourceId) throws Exception {
        return this.sessionFactory.getCurrentSession().createQuery(
                "delete from " + getPersistentClass().getName() + " attr where attr.resourceId=:resourceId")
                                  .setParameter("resourceId", resourceId).executeUpdate();
    }

    @Override
    public WebResourceAttribute getByAttributeNameResource(String resourceId, String targetAttributeName)
            throws Exception {
        List<WebResourceAttribute> result = (List<WebResourceAttribute>) this.sessionFactory.getCurrentSession()
                                                                                            .createQuery("from "
                                                                                                         + getPersistentClass()
                                                                                                    .getName()
                                                                                                         + " attr where attr.resourceId=:resourceId and attr.targetAttributeName=:targetAttributeName")
                                                                                            .setParameter("resourceId",
                                                                                                          resourceId)
                                                                                            .setParameter(
                                                                                                    "targetAttributeName",
                                                                                                    targetAttributeName)
                                                                                            .list();
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public List<WebResourceAttribute> getAttributesByResourceId(String resourceId) throws Exception {
        return (List<WebResourceAttribute>) this.sessionFactory.getCurrentSession().createQuery(
                "from " + getPersistentClass().getName() + " attr where attr.resourceId=:resourceId")
                                                               .setParameter("resourceId", resourceId).list();
    }

    @Override
    @Transactional
    public WebResourceAttribute update(WebResourceAttribute instance) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append("update ").append(getPersistentClass().getName())
                 .append(" obj set obj.amAttributeName=:amAttributeName, obj.amPolicyUrl=:amPolicyUrl");
            if (StringUtils.hasText(instance.getTargetAttributeName())) {
                query.append(", obj.targetAttributeName=:targetAttributeName");
            }
            if (StringUtils.hasText(instance.getResourceId())) {
                query.append(", obj.resourceId=:resourceId");
            }
            query.append(" where obj.attributeMapId=:attributeMapId");

            Query q = sessionFactory.getCurrentSession().createQuery(query.toString());
            q.setParameter("amAttributeName", instance.getAmAttributeName())
             .setParameter("amPolicyUrl", instance.getAmPolicyUrl());
            if (StringUtils.hasText(instance.getTargetAttributeName())) {
                q.setParameter("targetAttributeName", instance.getTargetAttributeName());
            }
            if (StringUtils.hasText(instance.getResourceId())) {
                q.setParameter("resourceId", instance.getResourceId());
            }
            q.setParameter("attributeMapId", instance.getAttributeMapId());
            q.executeUpdate();
            return this.findById(instance.getAttributeMapId());
        } catch (DataAccessException dae) {
            log.error("Update operation failed.", dae);
            throw new DataException(dae.getMessage(), dae.getCause());
        }
    }
}
