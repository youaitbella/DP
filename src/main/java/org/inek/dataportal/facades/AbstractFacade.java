package org.inek.dataportal.facades;


import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @param <T>
 * @author muellermi
 */
public abstract class AbstractFacade<T>{
    protected static final Logger _logger = Logger.getLogger("Facade");

    @PersistenceContext(unitName = "DataPortalPU")
    private EntityManager _em;

    private final Class<T> _entityClass;

    public AbstractFacade(Class<T> entityClass) {
        _entityClass = entityClass;
    }

    public static Logger getLogger() {
        return _logger;
    }

    protected EntityManager getEntityManager() {
        return _em;
    }

    void setEntityManager (EntityManager em){
        _em = em;
    }
    
    public void persist(T entity) {
        getEntityManager().persist(entity);
        clearCache();
    }

    public T merge(T entity) {
        T savedEntity=getEntityManager().merge(entity);
        clearCache();
        return savedEntity;
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(_entityClass, id);
    }

    public List<T> findAll() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(_entityClass);
        cq.select(cq.from(_entityClass));
        return getEntityManager().createQuery(cq).setHint("javax.persistence.cache.retrieveMode", "BYPASS").getResultList();
    }

    public List<T> findRange(int[] range) {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(_entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq).setHint("javax.persistence.cache.retrieveMode", "BYPASS");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(_entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq).setHint("javax.persistence.cache.retrieveMode", "BYPASS");
        return ((Long) q.getSingleResult()).intValue();
    }

    public void flush(){
        getEntityManager().flush();
    }
    
    public void clearCache(){
        flush();
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
    }
    
    public void clearCache(Class clazz){
        flush();
        getEntityManager().getEntityManagerFactory().getCache().evict(clazz);
    }
}
