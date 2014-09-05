package org.inek.dataportal.helper.scope;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

    /**
     * Gets the FeaturedScoped bean of the given type for the session, if any
     *
     * @param type
     * @return
     */
    public <T> T getBean(Class<T> type) {
        String key = getScopeKey(type);
        return (T) getFeatureScopedMap().get(key).getInstance();
    }

    /**
     * Destroys all FeatureScoped beans from the current session. Use full to
     * cleanup at logout time or session end
     */
    public void destroyAllBeans() {
        destroyAllOtherBeans("");
    }

    /**
     * Gets a bean according to the context. If no bean exist, a new one i
     * created.
     *
     * @param <T>
     * @param contextual
     * @param creationalContext
     * @return
     */
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

    private Map<String, FeatureScopedInstance> getFeatureScopedMap() {
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

    private void destroyAllOtherBeans(String scopeNameToKeep) {
        Map<String, FeatureScopedInstance> featureMap = getFeatureScopedMap();
        Set<String> keys = new HashSet<>(featureMap.keySet());  // need a copy to avoid concurrent changes!
        keys.stream().filter((key) -> !(key.startsWith(scopeNameToKeep + "|"))).forEach((key) -> {
            FeatureScopedInstance inst = featureMap.get(key);
            inst.getBean().destroy(inst.getInstance(), inst.getCtx());
            featureMap.remove(key);
        });
    }

    private String getScopeName(FeatureScopedInstance inst) {
        Class type = inst.getBean().getBeanClass();
        String className = type.getSimpleName();
        FeatureScoped scope = (FeatureScoped) type.getAnnotation(FeatureScoped.class);
        String scopeName = scope.name().isEmpty() ? className : scope.name();
        return scopeName;
    }

    private void putBean(FeatureScopedInstance featureScopedInstance) {
        destroyAllOtherBeans(getScopeName(featureScopedInstance));
        String key = getScopeKey(featureScopedInstance.getBean());
        getFeatureScopedMap().put(key, featureScopedInstance);
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
