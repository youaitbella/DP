package org.inek.dataportal.psy.modelintention;

import org.inek.dataportal.psy.modelintention.entities.Adjustment;
import org.inek.dataportal.psy.modelintention.entities.ModelIntention;

public class AdjustmentDynamicTable extends DynamicTable<Adjustment> {

    public AdjustmentDynamicTable(ModelIntention modelIntention) {
        super(modelIntention, modelIntention.getAdjustments());
    }

    @Override
    protected void addNewEntry() {
        Adjustment adjustment = new Adjustment();
        if (getModelIntention().getId() != -1) {
            adjustment.setModelIntentionId(getModelIntention().getId());
        }
        getList().add(adjustment);
    }

    @Override
    protected boolean isEmptyEntry(Adjustment adjustment) {
        return adjustment.getAdjustmentTypeId() == 0L && adjustment.getDateFrom() == null && adjustment.getDateTo() == null;
    }

}
