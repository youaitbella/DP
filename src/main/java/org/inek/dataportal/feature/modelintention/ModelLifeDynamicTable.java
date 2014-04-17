package org.inek.dataportal.feature.modelintention;

import org.inek.dataportal.entities.modelintention.ModelIntention;
import org.inek.dataportal.entities.modelintention.ModelLife;

public class ModelLifeDynamicTable extends DynamicTable {

    public ModelLifeDynamicTable(ModelIntention modelIntention) {
        super(modelIntention, modelIntention.getModelLifes());
    }

    @Override
    protected void addNewEntry() {
        ModelLife modelLife = new ModelLife();
        if (getModelIntention().getId() != null) {
            modelLife.setModelIntentionId(getModelIntention().getId());
        }
        getList().add(modelLife);
    }

    @Override
    protected boolean isEmptyEntry(Object entry) {
        ModelLife modelLife = (ModelLife) entry;
        return modelLife.getStartDate() == null && modelLife.getMonthDuration() == null;
    }

}
