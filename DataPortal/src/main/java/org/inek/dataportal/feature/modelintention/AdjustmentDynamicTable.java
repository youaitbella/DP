package org.inek.dataportal.feature.modelintention;

import org.inek.dataportal.entities.modelintention.Adjustment;
import org.inek.dataportal.entities.modelintention.ModelIntention;

public class AdjustmentDynamicTable extends DynamicTable {

    public AdjustmentDynamicTable(ModelIntention modelIntention) {
        super(modelIntention, modelIntention.getAdjustments());
    }

    @Override
    protected void addNewEntry() {
        Adjustment adjustment = new Adjustment();
        if (getModelIntention().getId() != null) {
            adjustment.setModelIntentionId(getModelIntention().getId());
        }
        getList().add(adjustment);
    }

    @Override
    protected boolean isEmptyEntry(Object entry) {
        Adjustment adjustment = (Adjustment) entry;
        return adjustment.getAdjustmentTypeId() == 0l && adjustment.getDateFrom() == null && adjustment.getDateTo() == null;
    }

}
