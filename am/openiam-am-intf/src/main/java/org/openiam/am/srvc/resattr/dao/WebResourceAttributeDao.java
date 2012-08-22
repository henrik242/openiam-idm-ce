package org.openiam.am.srvc.resattr.dao;

import org.openiam.am.srvc.webres.dto.WebResourceAttribute;

import java.util.List;

/**
 * User: Alexander Duckardt
 * Date: 8/15/12
 */
public interface WebResourceAttributeDao extends GenericDao<WebResourceAttribute, String> {
    List<WebResourceAttribute> getAttributesByResourceId(String resourceId) throws Exception;

    int deleteByResourceId(String resourceId) throws Exception;

    WebResourceAttribute getByAttributeNameResource(String resourceId, String targetAttributeName) throws Exception;
}
