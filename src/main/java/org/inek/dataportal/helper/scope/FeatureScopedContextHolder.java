package org.inek.dataportal.helper.scope;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.faces.context.FacesContext;

/**
 *
 * @author muellermi
 */
public enum FeatureScopedContextHolder {

    Instance;

    private final static String FeatureKey = "FeatureScoped";

    public Map<Class, FeatureScopedInstance> getBeans() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
        Map<Class, FeatureScopedInstance> featureBeans;
        if (map.containsKey(FeatureKey)) {
            featureBeans = (Map<Class, FeatureScopedInstance>) map.get(FeatureKey);
        } else {
            featureBeans = new HashMap<>();
            map.put(FeatureKey, featureBeans);
        }
        return featureBeans;
    }

    public FeatureScopedInstance getBean(Class type) {
        return getBeans().get(type);
    }

    public void putBean(FeatureScopedInstance featureScopedInstance) {
        destroyAllBeans();
        getBeans().put(featureScopedInstance.bean.getBeanClass(), featureScopedInstance);
    }

    public void destroyAllBeans() {
        for (FeatureScopedInstance inst : getBeans().values()) {
            destroyBean(inst);
        }
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
