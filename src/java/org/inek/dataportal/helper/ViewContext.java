/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper;

import java.util.Map;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 *
 * @author muellermi
 */
public class ViewContext implements Context {

    @Override
    public Class getScope() {
        return ViewScoped.class;
    }
    
    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        Bean bean = (Bean) contextual;
        Map viewMap = getViewMap();
        if(viewMap.containsKey(bean.getName())) {
            return (T) viewMap.get(bean.getName());
        } else {
            T t = (T) bean.create(creationalContext);
            viewMap.put(bean.getName(), t);
            return t;
        }
    }

    private Map getViewMap() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = fctx.getViewRoot();
        return viewRoot.getViewMap(true);
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        Bean bean = (Bean) contextual;
        Map viewMap = getViewMap();
        if(viewMap.containsKey(bean.getName())) {
            return (T) viewMap.get(bean.getName());
        } else {
            return null;
        }
    }

    @Override
    public boolean isActive() {
        return true;
    }

}
