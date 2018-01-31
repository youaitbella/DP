/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.scope;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;

/**
 *
 * @author muellermi
 */
public class FeatureScopedContext implements Context, Serializable {

    private final FeatureScopedContextHolder _contextHolder = FeatureScopedContextHolder.Instance;

    @Override
    public Class<? extends Annotation> getScope() {
        return FeatureScoped.class;
    }

    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        return _contextHolder.get(contextual, creationalContext);
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        return _contextHolder.get(contextual);
    }

    @Override
    public boolean isActive() {
        return true;
    }

}
