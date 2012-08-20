package org.openiam.spml2.spi.mysql;

import org.openiam.spml2.msg.AddRequestType;
import org.openiam.spml2.msg.AddResponseType;
import org.openiam.spml2.msg.StatusCodeType;

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
