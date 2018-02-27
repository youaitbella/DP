package org.inek.dataportal.feature.modelintention;

import org.inek.dataportal.entities.modelintention.Cost;
import org.inek.dataportal.entities.modelintention.ModelIntention;
import org.inek.dataportal.common.helper.Utils;

public class CostDynamicTable extends DynamicTable<Cost> {

    public CostDynamicTable(ModelIntention modelIntention) {
        super(modelIntention, modelIntention.getCosts());
    }

    @Override
    protected void addNewEntry() {
        Cost cost = new Cost();
        if (getModelIntention().getId() != null) {
            cost.setModelIntentionId(getModelIntention().getId());
        }
        getList().add(cost);
    }

    @Override
    protected boolean isEmptyEntry(Cost cost) {
        return cost.getIk() == null && Utils.isNullOrEmpty(cost.getRemunerationCode());
    }

}
