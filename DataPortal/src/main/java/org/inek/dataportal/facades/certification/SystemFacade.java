package org.inek.dataportal.facades.certification;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.facades.AbstractDataAccess;

/**
 *
 * @author muellermi
 */
@Stateless
public class SystemFacade extends AbstractDataAccess {

    public RemunerationSystem save(RemunerationSystem system) {
        if (system.getId() == -1) {
            persist(system);
            return system;
        }
        return merge(system);
    }

    public List<SelectItem> getRemunerationSystemInfos() {
        List<SelectItem> result = new ArrayList<>();
        for (RemunerationSystem system : findAllFresh(RemunerationSystem.class)) {
            result.add(new SelectItem(system.getId(), system.getDisplayName()));
        }
        return result;
    }
    
    public List<SelectItem> getRemunerationSystemInfosActive(boolean isActive) {
        List<SelectItem> result = new ArrayList<>();
        for (RemunerationSystem system : findAllFresh(RemunerationSystem.class)) {
            if(isActive && !system.isActive()){
                continue;
            }
            result.add(new SelectItem(system.getId(), system.getDisplayName()));
        }
        return result;
    }
    
    public RemunerationSystem findRemunerationSystemByName(String name) {
        List<RemunerationSystem> rs = findAllFresh(RemunerationSystem.class);
        for(RemunerationSystem element : rs) {
            if(element.getDisplayName().equals(name)) {
                return element;
            }
        }
        return null;
    }
    
    public void remove(RemunerationSystem var) {
        super.remove(var);
    }

    public RemunerationSystem findFresh(int id) {
        return super.findFresh(RemunerationSystem.class, id);
    }
    
    public List<RemunerationSystem> findAllFresh() {
        return super.findAllFresh(RemunerationSystem.class);
    }
}
