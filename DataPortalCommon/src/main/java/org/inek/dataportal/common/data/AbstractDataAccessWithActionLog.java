package org.inek.dataportal.common.data;

import javax.inject.Inject;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.adm.facade.LogFacade;
import org.inek.dataportal.common.data.iface.StatusEntity;
import org.inek.dataportal.common.enums.WorkflowStatus;

public abstract class AbstractDataAccessWithActionLog extends AbstractDataAccess{
    
        // <editor-fold defaultstate="collapsed" desc="Property LogFacade">
    private LogFacade _logFacade;

    @Inject
    public void setLogFacade(LogFacade logFacade) {
        _logFacade = logFacade;
    }
    // </editor-fold>

    private void logAction(StatusEntity entity) {
        _logFacade.saveActionLog(Feature.CALCULATION_HOSPITAL,
                entity.getClass().getSimpleName(),
                entity.getId(),
                entity.getStatus());
    }

    protected void persist(StatusEntity entity) {
        super.persist(entity);
        logAction(entity);
    }

    protected <T extends StatusEntity> T merge(T entity) {
        logAction(entity);
        return super.merge(entity);
    }

    protected void remove(StatusEntity entity) {
        super.remove(entity);
        entity.setStatus(WorkflowStatus.Deleted);
        logAction(entity);
    }
    

}
