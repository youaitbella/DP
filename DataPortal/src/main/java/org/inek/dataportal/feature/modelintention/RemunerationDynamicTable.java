package org.inek.dataportal.feature.modelintention;

import org.inek.dataportal.entities.modelintention.ModelIntention;
import org.inek.dataportal.entities.modelintention.Remuneration;

public class RemunerationDynamicTable extends DynamicTable {

    public RemunerationDynamicTable(ModelIntention modelIntention) {
        super(modelIntention, modelIntention.getRemunerations());
    }

    @Override
    protected void addNewEntry() {
        Remuneration remuneration = new Remuneration();
        if (getModelIntention().getId() != null) {
            remuneration.setModelIntentionId(getModelIntention().getId());
        }
        getList().add(remuneration);
    }

    @Override
    protected boolean isEmptyEntry(Object entry) {
        Remuneration remuneration = (Remuneration) entry;
        return remuneration.getCode().length() == 0 && remuneration.getText().length() == 0;
    }

}
