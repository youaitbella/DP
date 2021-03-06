package org.inek.dataportal.psy.modelintention;

import java.util.List;
import org.inek.dataportal.psy.modelintention.entities.ModelIntention;
import org.inek.dataportal.psy.modelintention.entities.Quality;
import org.inek.dataportal.common.helper.Utils;

public class QualityDynamicTable extends DynamicTable<Quality> {
    private final int _typeId;

    public QualityDynamicTable(ModelIntention modelIntention, List<Quality> list, int typeId) {
        super(modelIntention, list);
        _typeId = typeId;
    }

    @Override
    protected void addNewEntry() {
        Quality quality = new Quality();
        if (getModelIntention().getId() != -1) {
            quality.setModelIntentionId(getModelIntention().getId());
        }
        quality.setTypeId(_typeId);
        getList().add(quality);
    }

    @Override
    protected boolean isEmptyEntry(Quality quality) {
        return Utils.isNullOrEmpty(quality.getIndicator()) && Utils.isNullOrEmpty(quality.getDescription());
    }

}
