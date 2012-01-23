
import org.openiam.spml2.interf.ConnectorService
import org.openiam.idm.srvc.mngsys.dto.ManagedSys;
import org.openiam.spml2.msg.*;
import org.openiam.spml2.msg.password.*;
import org.openiam.spml2.msg.suspend.ResumeRequestType;
import org.openiam.spml2.msg.suspend.SuspendRequestType;


public class Test_Script_CONNECTOR  implements ConnectorService {

    public ResponseType testConnection(ManagedSys managedSys) {
        return null;
    }


    AddResponseType add(AddRequestType reqType) {
        System.out.println("AddRequest:" + reqType);

        AddResponseType response = new AddResponseType();
        response.setStatus(StatusCodeType.SUCCESS);

        return response;
    }

    ModifyResponseType modify(ModifyRequestType reqType) {
        return null;
    }

    ResponseType delete(DeleteRequestType reqType) {
        return null;
    }

    LookupResponseType lookup(LookupRequestType reqType) {
        return null;
    }


    ResponseType setPassword(SetPasswordRequestType request) {
        return null;
    }


    ResponseType expirePassword(
            ExpirePasswordRequestType request) {
    	return null;        
    }


    ResetPasswordResponseType resetPassword(
            ResetPasswordRequestType request) {
        return null;
    }


    ValidatePasswordResponseType validatePassword(ValidatePasswordRequestType request) {
        return null;
    }


    /**
     * Suspend / disables a user
     *
     * @param request
     * @return
     */

    ResponseType suspend(SuspendRequestType request){
        return null;
    }


    ResponseType resume( ResumeRequestType request) {
        return null;
    }

}




