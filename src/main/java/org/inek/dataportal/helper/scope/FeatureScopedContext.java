/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.scope;

import java.io.Serializable;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder.FeatureScopedInstance;

/**
 *
 * @author muellermi
 */
public class FeatureScopedContext implements Context, Serializable {

    private FeatureScopedContextHolder _contextHolder = FeatureScopedContextHolder.Instance;

    @Override
    public Class getScope() {
        return FeatureScoped.class;
    }

    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        Bean bean = (Bean) contextual;
        if (_contextHolder.getBeans().containsKey(bean.getBeanClass())) {
            return (T) _contextHolder.getBean(bean.getBeanClass()).getInstance();
        } else {
            T t = (T) bean.create(creationalContext);
            FeatureScopedInstance customInstance = new FeatureScopedInstance();
            customInstance.setBean(bean);
            customInstance.setCtx(creationalContext);
            customInstance.setInstance(t);
            _contextHolder.putBean(customInstance);
            return t;
        }
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        Bean bean = (Bean) contextual;
        if (_contextHolder.getBeans().containsKey(bean.getBeanClass())) {
            return (T) _contextHolder.getBean(bean.getBeanClass()).getInstance();
        } else {
            return null;
        }
    }

    @Override
    public boolean isActive() {
        return true;
    }

}
