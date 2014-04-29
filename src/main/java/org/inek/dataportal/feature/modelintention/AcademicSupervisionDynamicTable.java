package org.inek.dataportal.feature.modelintention;

import org.inek.dataportal.entities.modelintention.AcademicSupervision;
import org.inek.dataportal.entities.modelintention.ModelIntention;
import org.inek.dataportal.helper.Utils;

public class AcademicSupervisionDynamicTable extends DynamicTable {

    public AcademicSupervisionDynamicTable(ModelIntention modelIntention) {
        super(modelIntention, modelIntention.getAcademicSupervisions());
    }

    @Override
    protected void addNewEntry() {
        AcademicSupervision supervision = new AcademicSupervision();
        if (getModelIntention().getId() != null) {
            supervision.setModelIntentionId(getModelIntention().getId());
        }
        getList().add(supervision);
    }

    @Override
    protected boolean isEmptyEntry(Object entry) {
        AcademicSupervision supervision = (AcademicSupervision) entry;
        return Utils.isNullOrEmpty(supervision.getContractor()) && Utils.isNullOrEmpty(supervision.getRemitter());
    }

}
