package org.inek.dataportal.feature.modelintention;

import org.inek.dataportal.entities.modelintention.Cost;
import org.inek.dataportal.entities.modelintention.ModelIntention;

public class CostDynamicTable extends DynamicTable {

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
    protected boolean isEmptyEntry(Object entry) {
        Cost cost = (Cost) entry;
        return cost.getIk() == null && cost.getRemunerationCode() == null;
    }

}
