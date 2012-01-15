package org.openiam.spml2.spi.jdbc;

import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

//import org.openiam.spml2.msg.ExtensibleAttribute;

/**
 * Implements modify capability for the AppTableConnector
 * User: suneetshah
 * Date: 7/30/11
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppTableModifyCommand extends AppTableAbstractCommand {

    public ModifyResponseType modify(ModifyRequestType reqType) {
        String tableName;
        Connection con = null;


        ModifyResponseType response = new ModifyResponseType();
        response.setStatus(StatusCodeType.SUCCESS);

        String principalName = reqType.getPsoID().getID();


        //String requestID = reqType.getRequestID();
        /* PSO - Provisioning Service Object -
*     -  ID must uniquely specify an object on the target or in the target's namespace
*     -  Try to make the PSO ID immutable so that there is consistency across changes. */
        PSOIdentifierType psoID = reqType.getPsoID();
        principalName = psoID.getID();

        /* targetID -  */
        String targetID = psoID.getTargetID();


        /* A) Use the targetID to look up the connection information under managed systems */
        ManagedSys managedSys = managedSysService.getManagedSys(targetID);

        if (managedSys.getResourceId() == null || managedSys.getResourceId().length() == 0) {
            response.setStatus(StatusCodeType.FAILURE);
            response.setError(ErrorCode.INVALID_CONFIGURATION);
            response.addErrorMessage("ResourceID is not defined in the ManagedSys Object");
            return response;
        }

        Resource res = resourceDataService.getResource(managedSys.getResourceId());
        tableName = getTableName(res);
        if (tableName == null || tableName.length() == 0) {
            return populateResp(response, StatusCodeType.FAILURE,
                    ErrorCode.INVALID_CONFIGURATION,
                    "TABLE NAME is not defined.");
        }

        // modificationType contains a collection of objects for each type of operation
        List<ModificationType> modificationList = reqType.getModification();

        log.debug("ModificationList = " + modificationList);
        log.debug("Modificationlist size= " + modificationList.size());


        List<ModificationType> modTypeList = reqType.getModification();

        try {
            con = connectionMgr.connect(managedSys);

            // build sql
            StringBuilder whereBuf = new StringBuilder(" WHERE ");
            StringBuilder updateBuf = new StringBuilder("UPDATE " + tableName);
            updateBuf.append(" SET ");

            for (ModificationType mod : modTypeList) {
                ExtensibleType extType = mod.getData();
                List<ExtensibleObject> extobjectList = extType.getAny();

                int ctr = 0;


                for (ExtensibleObject obj : extobjectList) {

                    if (identityExists(con, tableName, principalName, obj)) {

                        log.debug("Identity found. Modifying identity: " + principalName);

                        List<ExtensibleAttribute> attrList = obj.getAttributes();
                        String principalFieldName = obj.getPrincipalFieldName();
                        String principalFieldDataType = obj.getPrincipalFieldDataType();

                        whereBuf.append(principalFieldName + " = ? ");

                        for (ExtensibleAttribute att : attrList) {


                            if (att.getOperation() != 0 && att.getName() != null) {
                                if (att.getObjectType().equalsIgnoreCase("USER")) {
                                    if (ctr != 0) {
                                        updateBuf.append(",");
                                    }
                                    ctr++;

                                    updateBuf.append(att.getName() + " = ? ");
                                }
                            }
                        }

                        updateBuf.append(whereBuf);
                        log.debug(" SQL=" + updateBuf.toString());

                        // Don't do anything if there were no changes

                        if (ctr > 0) {
                            PreparedStatement statement = con.prepareStatement(updateBuf.toString());

                            ctr = 1;
                            for (ExtensibleAttribute att : attrList) {

                                if (att.getOperation() != 0 && att.getName() != null) {

                                    if (att.getObjectType().equalsIgnoreCase("USER")) {
                                        setStatement(statement, ctr, att);
                                        ctr++;
                                    }

                                }

                            }
                            if (principalFieldName != null) {

                                setStatement(statement, ctr, principalFieldDataType, principalName);
                            }


                            statement.executeUpdate();
                        }

                    } else {

                        // identity does not exist in the target system
                        // identity needs to be re-provisioned
                        addIdentity(con, tableName, principalName, obj);
                    }

                }
            }
        } catch (SQLException se) {
            log.error(se.toString());
            response.setStatus(StatusCodeType.FAILURE);
            response.setError(ErrorCode.SQL_ERROR);
            response.addErrorMessage(se.toString());
            return response;

        } catch (ClassNotFoundException cnfe) {
            log.error(cnfe.toString());
            response.setStatus(StatusCodeType.FAILURE);
            response.setError(ErrorCode.INVALID_CONFIGURATION);
            response.addErrorMessage(cnfe.toString());
            return response;
        } catch (ParseException pe) {
            log.error(pe.toString());
            response.setStatus(StatusCodeType.FAILURE);
            response.setError(ErrorCode.INVALID_CONFIGURATION);
            response.addErrorMessage(pe.toString());
            return response;

        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException s) {
                    log.error(s.toString());
                    response.setStatus(StatusCodeType.FAILURE);
                    response.setError(ErrorCode.SQL_ERROR);
                    response.addErrorMessage(s.toString());
                }
            }
        }


        return response;
    }

    private void addIdentity(Connection con, String tableName, String principalName, ExtensibleObject obj)
            throws SQLException, ParseException {
        // build sql
        StringBuilder insertBuf = new StringBuilder("INSERT INTO " + tableName);
        StringBuilder columnBuf = new StringBuilder("(");
        StringBuilder valueBuf = new StringBuilder(" values (");

        List<ExtensibleAttribute> attrList = obj.getAttributes();

        String principalFieldName = obj.getPrincipalFieldName();
        //String principalFieldDataType = obj.getPrincipalFieldDataType();

        log.debug("Adding identity: " + principalName);
        log.debug("Number of attributes to persist in ADD = " + attrList.size());

        int ctr = 0;
        for (ExtensibleAttribute att : attrList) {
            if (ctr != 0) {
                columnBuf.append(",");
                valueBuf.append(",");
            }
            ctr++;
            columnBuf.append(att.getName());
            valueBuf.append("?");
        }
        // add the primary key
        log.debug("Principal column name=" + obj.getPrincipalFieldName());
        if (principalFieldName != null) {
            if (ctr != 0) {
                columnBuf.append(",");
                valueBuf.append(",");
            }
            columnBuf.append(obj.getPrincipalFieldName());
            valueBuf.append("?");
        }


        columnBuf.append(")");
        valueBuf.append(")");
        //}
        insertBuf.append(columnBuf);
        insertBuf.append(valueBuf);

        log.debug("ADD SQL=" + insertBuf.toString());

        PreparedStatement statement = con.prepareStatement(insertBuf.toString());
        // set the parameters


        ctr = 1;
        for (ExtensibleAttribute att : attrList) {

            setStatement(statement, ctr, att);
            ctr++;
            log.debug("Binding parameter: " + att.getName() + " -> " + att.getValue());

        }


        if (obj.getPrincipalFieldName() != null) {

            setStatement(statement, ctr, obj.getPrincipalFieldDataType(), principalName);
        }


        statement.executeUpdate();


    }


    protected ModifyResponseType populateResp(ModifyResponseType response, StatusCodeType status,
                                              ErrorCode err, String msg) {

        response.setStatus(status);
        response.setError(err);
        response.addErrorMessage(msg);
        return response;
    }


}


