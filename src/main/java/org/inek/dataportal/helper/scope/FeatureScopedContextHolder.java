package org.inek.dataportal.helper.scope;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.spi.Contextual;
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

    public Map<String, FeatureScopedInstance> getFeatureScopedMap() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
        Map<String, FeatureScopedInstance> featureBeans;
        if (map.containsKey(FeatureKey)) {
            featureBeans = (Map<String, FeatureScopedInstance>) map.get(FeatureKey);
        } else {
            featureBeans = new HashMap<>();
            map.put(FeatureKey, featureBeans);
        }
        return featureBeans;
    }

    private String getScopeKey(Bean bean) {
        return getScopeKey(bean.getBeanClass());
    }

    private String getScopeKey(Class type) {
        String className = type.getSimpleName(); // named beans will be accessed by their simple name. Thus, this is sufficient.
        FeatureScoped scope = (FeatureScoped) type.getAnnotation(FeatureScoped.class);
        String scopeName = scope.name().isEmpty() ? className : scope.name();
        String key = scopeName + "|" + className;
        return key;
    }

    /**
     * Gets the FeaturedScoped bean of the given type for the session, if any
     *
     * @param type
     * @return
     */
    public <T> T getBean(Class<T> type) {
        return (T) getFeatureScopedMap().get(getScopeKey(type)).getBean();
    }

    public void putBean(FeatureScopedInstance featureScopedInstance) {
        destroyAllBeans();
        String key = getScopeKey(featureScopedInstance.getBean());
        getFeatureScopedMap().put(key, featureScopedInstance);
    }

    public void destroyAllBeans() {
        for (FeatureScopedInstance inst : getFeatureScopedMap().values()) {
            destroyBean(inst);
        }
    }

    private void destroyBean(FeatureScopedInstance featureScopedInstance) {
        String key = getScopeKey(featureScopedInstance.getBean());
        getFeatureScopedMap().remove(key);
        featureScopedInstance.getBean().destroy(featureScopedInstance.getInstance(), featureScopedInstance.getCtx());
    }

    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        Bean bean = (Bean) contextual;
        String key = getScopeKey(bean);
        if (getFeatureScopedMap().containsKey(key)) {
            return (T) getBean(bean.getBeanClass());
        } else {
            T t = (T) bean.create(creationalContext);
            FeatureScopedInstance customInstance = new FeatureScopedInstance();
            customInstance.setBean(bean);
            customInstance.setCtx(creationalContext);
            customInstance.setInstance(t);
            putBean(customInstance);
            return t;
        }
    }

    public <T> T get(Contextual<T> contextual) {
        Bean bean = (Bean) contextual;
        String key = getScopeKey(bean);
        if (getFeatureScopedMap().containsKey(key)) {
            return (T) getBean(bean.getBeanClass());
        } else {
            return null;
        }
    }

    public static class FeatureScopedInstance<T> {

        private Bean<T> _bean;
        public Bean<T> getBean() {
            return _bean;
        }

        public void setBean(Bean<T> bean) {
            _bean = bean;
        }

        private CreationalContext<T> _context;
        public CreationalContext<T> getCtx() {
            return _context;
        }

        public void setCtx(CreationalContext<T> ctx) {
            _context = ctx;
        }

        private T _instance;
        public T getInstance() {
            return _instance;
        }

        public void setInstance(T instance) {
            _instance = instance;
        }

    }

}
