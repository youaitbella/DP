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
        getBeans().put(featureScopedInstance.getBean().getBeanClass(), featureScopedInstance);
    }

    public void destroyAllBeans() {
        for (FeatureScopedInstance inst : getBeans().values()) {
            destroyBean(inst);
        }
    }

    void destroyBean(FeatureScopedInstance featureScopedInstance) {
        getBeans().remove(featureScopedInstance.getBean().getBeanClass());
        featureScopedInstance.getBean().destroy(featureScopedInstance.getInstance(), featureScopedInstance.getCtx());
    }

    public static class FeatureScopedInstance<T> {

        private Bean<T> _bean;
        private CreationalContext<T> _context;
        private T _instance;

        public Bean<T> getBean() {
            return _bean;
        }

        public void setBean(Bean<T> bean) {
            _bean = bean;
        }

        public CreationalContext<T> getCtx() {
            return _context;
        }

        public void setCtx(CreationalContext<T> ctx) {
            _context = ctx;
        }

        public T getInstance() {
            return _instance;
        }

        public void setInstance(T instance) {
            _instance = instance;
        }

    }

}
