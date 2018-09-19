package org.inek.dataportal.psy.modelintention;

import org.inek.dataportal.psy.modelintention.entities.Cost;
import org.inek.dataportal.psy.modelintention.entities.ModelIntention;
import org.inek.dataportal.common.helper.Utils;

public class CostDynamicTable extends DynamicTable<Cost> {

    public CostDynamicTable(ModelIntention modelIntention) {
        super(modelIntention, modelIntention.getCosts());
    }

    @Override
    protected void addNewEntry() {
        Cost cost = new Cost();
        if (getModelIntention().getId() != -1) {
            cost.setModelIntentionId(getModelIntention().getId());
        }
        getList().add(cost);
    }

    @Override
    protected boolean isEmptyEntry(Cost cost) {
        return cost.getIk() == null && Utils.isNullOrEmpty(cost.getRemunerationCode());
    }

}
