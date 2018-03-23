package org.inek.dataportal.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.feature.dropbox.entities.DropBoxType;
import org.inek.dataportal.feature.dropbox.facade.DropBoxTypeFacade;

/**
 *
 * @author muellermi
 */
@Named
@SessionScoped
public class DropBoxTools implements Serializable{

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
