package org.inek.dataportal.feature.modelintention;

import org.inek.dataportal.feature.modelintention.entities.ModelIntention;
import org.inek.dataportal.feature.modelintention.entities.ModelLife;

public class ModelLifeDynamicTable extends DynamicTable<ModelLife> {

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
    protected boolean isEmptyEntry(ModelLife modelLife) {
        return modelLife.getStartDate() == null && modelLife.getMonthDuration() == null;
    }

}
