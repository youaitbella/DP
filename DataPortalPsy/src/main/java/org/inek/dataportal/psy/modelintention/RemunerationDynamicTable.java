package org.inek.dataportal.psy.modelintention;

import org.inek.dataportal.psy.modelintention.entities.ModelIntention;
import org.inek.dataportal.psy.modelintention.entities.Remuneration;

public class RemunerationDynamicTable extends DynamicTable<Remuneration> {

    public RemunerationDynamicTable(ModelIntention modelIntention) {
        super(modelIntention, modelIntention.getRemunerations());
    }

    @Override
    protected void addNewEntry() {
        Remuneration remuneration = new Remuneration();
        if (getModelIntention().getId() != -1) {
            remuneration.setModelIntentionId(getModelIntention().getId());
        }
        getList().add(remuneration);
    }

    @Override
    protected boolean isEmptyEntry(Remuneration remuneration) {
        return remuneration.getCode().length() == 0 && remuneration.getText().length() == 0;
    }

}
