package org.openiam.spml2.spi.mysql;

import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.idm.srvc.res.dto.Resource;
import org.openiam.idm.srvc.res.dto.ResourceProp;
import org.openiam.provision.type.ExtensibleAttribute;
import org.openiam.provision.type.ExtensibleObject;
import org.openiam.spml2.msg.AddRequestType;
import org.openiam.spml2.msg.AddResponseType;
import org.openiam.spml2.msg.ErrorCode;
import org.openiam.spml2.msg.StatusCodeType;
import org.openiam.spml2.spi.jdbc.AppTableAbstractCommand;
import org.openiam.spml2.spi.jdbc.AppTableAddCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * AppTableAddCommand implements the add operation for the AppTableConnector
 */
public class MySQLAddCommand extends MySQLAbstractCommand {



    public AddResponseType add(AddRequestType reqType) {


        AddResponseType response = new AddResponseType();
        response.setStatus(StatusCodeType.SUCCESS);



        return response;
    }


}
