package org.inek.dataportal.helper.scope;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

/**
 *
 * @author muellermi
 */
public enum FeatureScopedContextHolder {

    Instance;

    private final Map<Class, FeatureScopedInstance> _beans = new HashMap<>();

    public Map<Class, FeatureScopedInstance> getBeans() {
        return _beans;
    }

    public FeatureScopedInstance getBean(Class type) {
        return getBeans().get(type);
    }

    public void putBean(FeatureScopedInstance featureScopedInstance) {
        for (FeatureScopedInstance inst : _beans.values()) {
            destroyBean(inst);
        }
        getBeans().put(featureScopedInstance.bean.getBeanClass(), featureScopedInstance);
    }

    void destroyBean(FeatureScopedInstance featureScopedInstance) {
        getBeans().remove(featureScopedInstance.bean.getBeanClass());
        featureScopedInstance.bean.destroy(featureScopedInstance.instance, featureScopedInstance.ctx);
    }

    public static class FeatureScopedInstance<T> {

        Bean<T> bean;
        CreationalContext<T> ctx;
        T instance;
    }
}
