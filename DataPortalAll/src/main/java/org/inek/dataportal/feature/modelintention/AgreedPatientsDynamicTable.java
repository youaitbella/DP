package org.inek.dataportal.feature.modelintention;

import org.inek.dataportal.entities.modelintention.AgreedPatients;
import org.inek.dataportal.entities.modelintention.ModelIntention;

public class AgreedPatientsDynamicTable extends DynamicTable<AgreedPatients> {

    public AgreedPatientsDynamicTable(ModelIntention modelIntention) {
        super(modelIntention, modelIntention.getAgreedPatients());
    }

    @Override
    protected void addNewEntry() {
        AgreedPatients agreedPatients = new AgreedPatients();
        if (getModelIntention().getId() != null) {
            agreedPatients.setModelIntentionId(getModelIntention().getId());
        }
        getList().add(agreedPatients);
    }

    @Override
    protected boolean isEmptyEntry(AgreedPatients agreedPatients) {
        return agreedPatients.getPatientsFrom() == null && agreedPatients.getPatientsTo()== null;
    }

}
