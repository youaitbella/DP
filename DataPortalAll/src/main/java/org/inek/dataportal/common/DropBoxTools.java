package org.inek.dataportal.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.entities.dropbox.DropBoxType;
import org.inek.dataportal.facades.DropBoxTypeFacade;

/**
 *
 * @author muellermi
 */
public class DropBoxTools {

    private Map<Integer, DropBoxType> _dropBoxTypes;
    @Inject
    private transient DropBoxTypeFacade _dropBoxTypeFacade;

    public DropBoxType getDropBoxType(int id) {
        initDropBoxTypes();
        return _dropBoxTypes.get(id);
    }

    private void initDropBoxTypes() {
        if (_dropBoxTypes == null) {
            _dropBoxTypes = new HashMap<>();
            for (DropBoxType type : _dropBoxTypeFacade.findAll()) {
                _dropBoxTypes.put(type.getId(), type);
            }
        }
    }

    public List<SelectItem> getDropBoxTypeItems() {
        initDropBoxTypes();
        List<SelectItem> dropboxTypeItems;
        dropboxTypeItems = new ArrayList<>();
        dropboxTypeItems.add(new SelectItem(null, Utils.getMessage("lblChooseEntry")));
        for (DropBoxType type : _dropBoxTypes.values()) {
            dropboxTypeItems.add(new SelectItem(type.getId(), type.getName()));
        }
        return dropboxTypeItems;
    }
    
}
