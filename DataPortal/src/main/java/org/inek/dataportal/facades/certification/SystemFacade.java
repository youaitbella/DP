package org.inek.dataportal.facades.certification;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.facades.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class SystemFacade extends AbstractFacade<RemunerationSystem> {

    public SystemFacade() {
        super(RemunerationSystem.class);
    }

    public RemunerationSystem save(RemunerationSystem system) {
        if (system.getId() == -1) {
            persist(system);
            return system;
        }
        return merge(system);
    }

    public List<SelectItem> getRemunerationSystemInfos() {
        List<SelectItem> result = new ArrayList<>();
        for (RemunerationSystem system : findAllFresh()) {
            result.add(new SelectItem(system.getId(), system.getDisplayName()));
        }
        return result;
    }
    
    public List<SelectItem> getRemunerationSystemInfosActive(boolean isActive) {
        List<SelectItem> result = new ArrayList<>();
        for (RemunerationSystem system : findAllFresh()) {
            if(isActive && !system.isActive()){
                continue;
            }
            result.add(new SelectItem(system.getId(), system.getDisplayName()));
        }
        return result;
    }
    
    public RemunerationSystem findRemunerationSystemByName(String name) {
        List<RemunerationSystem> rs = findAll();
        for(RemunerationSystem element : rs) {
            if(element.getDisplayName().equals(name)) {
                return element;
            }
        }
        return null;
    }
}
