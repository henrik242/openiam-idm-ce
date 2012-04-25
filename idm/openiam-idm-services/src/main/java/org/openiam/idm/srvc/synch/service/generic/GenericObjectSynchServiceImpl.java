package org.openiam.idm.srvc.synch.service.generic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.openiam.base.ws.ResponseCode;
import org.openiam.base.ws.ResponseStatus;
import org.openiam.idm.srvc.synch.dto.SyncResponse;
import org.openiam.idm.srvc.synch.dto.SynchConfig;
import org.openiam.idm.srvc.synch.service.SourceAdapter;
import org.openiam.idm.srvc.synch.service.SynchConfigDAO;
import org.openiam.idm.srvc.synch.service.SynchConfigDataMappingDAO;
import org.openiam.idm.srvc.synch.service.generic.SourceAdapterFactory;

import java.sql.Timestamp;

/**
 * Implementation object for <code>GenericObjectSynchService</code> which is used to synchronize objects such as
 * Groups, Roles, Organizations and others. Synchronization for these activities is initiated from
 * startSynchronization()
 */
public class GenericObjectSynchServiceImpl implements GenericObjectSynchService {
    protected MuleContext muleContext;
    protected SynchConfigDAO synchConfigDao;
    protected SynchConfigDataMappingDAO synchConfigMappingDao;
    protected SourceAdapterFactory adaptorFactory;


    private static final Log log = LogFactory.getLog(GenericObjectSynchServiceImpl.class);

    public SyncResponse startSynchronization(SynchConfig config) {
        log.debug("- Generic Object Synchronization started..^^^^^^^^");
        try {
            SourceAdapter adapt = adaptorFactory.create(config);

            long newLastExecTime = System.currentTimeMillis();

            SyncResponse resp = adapt.startSynch(config);

            log.debug("SyncResponse updateTime value=" + resp.getLastRecordTime());

            if (resp.getLastRecordTime() == null) {

                synchConfigDao.updateExecTime(config.getSynchConfigId(), new Timestamp( newLastExecTime ));
            }else {
                synchConfigDao.updateExecTime(config.getSynchConfigId(), new Timestamp( resp.getLastRecordTime().getTime() ));
            }

            if (resp.getLastRecProcessed() != null) {

                synchConfigDao.updateLastRecProcessed(config.getSynchConfigId(),resp.getLastRecProcessed() );
            }


            log.debug("-Generic Object Synchronization COMPLETE.^^^^^^^^");

            return resp;
        }catch( ClassNotFoundException cnfe) {

            cnfe.printStackTrace();

            log.error(cnfe);
            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
            resp.setErrorCode(ResponseCode.CLASS_NOT_FOUND);
            resp.setErrorText(cnfe.getMessage());
            return resp;
        }catch(Exception e) {

            e.printStackTrace();

            log.error(e);
            SyncResponse resp = new SyncResponse(ResponseStatus.FAILURE);
            resp.setErrorText(e.getMessage());
            return resp;
        }

    }


    public void setMuleContext(MuleContext ctx) {

        muleContext = ctx;
    }

    public SynchConfigDAO getSynchConfigDao() {
        return synchConfigDao;
    }

    public void setSynchConfigDao(SynchConfigDAO synchConfigDao) {
        this.synchConfigDao = synchConfigDao;
    }

    public SynchConfigDataMappingDAO getSynchConfigMappingDao() {
        return synchConfigMappingDao;
    }

    public void setSynchConfigMappingDao(SynchConfigDataMappingDAO synchConfigMappingDao) {
        this.synchConfigMappingDao = synchConfigMappingDao;
    }

    public SourceAdapterFactory getAdaptorFactory() {
        return adaptorFactory;
    }

    public void setAdaptorFactory(SourceAdapterFactory adaptorFactory) {
        this.adaptorFactory = adaptorFactory;
    }
}
