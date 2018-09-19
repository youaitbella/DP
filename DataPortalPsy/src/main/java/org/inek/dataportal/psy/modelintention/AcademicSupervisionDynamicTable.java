package org.inek.dataportal.psy.modelintention;

import org.inek.dataportal.psy.modelintention.entities.AcademicSupervision;
import org.inek.dataportal.psy.modelintention.entities.ModelIntention;
import org.inek.dataportal.common.helper.Utils;

public class AcademicSupervisionDynamicTable extends DynamicTable<AcademicSupervision> {

    public AcademicSupervisionDynamicTable(ModelIntention modelIntention) {
        super(modelIntention, modelIntention.getAcademicSupervisions());
    }

    @Override
    protected void addNewEntry() {
        AcademicSupervision supervision = new AcademicSupervision();
        if (getModelIntention().getId() != -1) {
            supervision.setModelIntentionId(getModelIntention().getId());
        }
        getList().add(supervision);
    }

    @Override
    protected boolean isEmptyEntry(AcademicSupervision supervision) {
        return Utils.isNullOrEmpty(supervision.getContractor()) && Utils.isNullOrEmpty(supervision.getRemitter());
    }

}
