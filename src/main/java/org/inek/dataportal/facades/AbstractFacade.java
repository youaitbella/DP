package org.inek.dataportal.facades;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public abstract class AbstractFacade<T> {

    protected static final Logger _logger = Logger.getLogger("Facade");

    @PersistenceContext(unitName = "DataPortalPU")
    private EntityManager _em;

// If EM should not be container managed
//    @PostConstruct
//    private void init() {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DataPortalPU");
//        _em = emf.createEntityManager();
//    }
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

    void setEntityManager(EntityManager em) {
        _em = em;
    }

    public void persist(T entity) {
        _em.persist(entity);
        _em.flush();
    }

    public T merge(T entity) {
        T savedEntity = _em.merge(entity);
        _em.flush();
        return savedEntity;
    }

    public void remove(T entity) {
        _em.remove(_em.merge(entity));
        _em.flush();
    }

    public void refresh(T entity) {
        _em.refresh(entity);
    }

    public T find(Object id) {
        return _em.find(_entityClass, id);
    }

    public T findFresh(Object id) {
        Map<String, Object> props = new HashMap<>();
        props.put("javax.persistence.cache.retrieveMode", "BYPASS");
        return _em.find(_entityClass, id, props);
// alternative approach:        
//        T entity = find(id);
//        refresh(entity);
//        return entity;
    }

    public List<T> findAll() {
        CriteriaBuilder cb = _em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(_entityClass);
        cq.select(cq.from(_entityClass));
        return _em.createQuery(cq).getResultList();
    }

    /**
     * Finds all whilst by passing the cache
     *
     * @return
     */
    public List<T> findAllFresh() {
        CriteriaBuilder cb = _em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(_entityClass);
        cq.select(cq.from(_entityClass));
        return _em.createQuery(cq).setHint("javax.persistence.cache.retrieveMode", "BYPASS").getResultList();
    }

    public List<T> findRange(int[] range) {
        CriteriaQuery<T> cq = _em.getCriteriaBuilder().createQuery(_entityClass);
        cq.select(cq.from(_entityClass));
        javax.persistence.TypedQuery<T> q = _em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery<Long> cq = _em.getCriteriaBuilder().createQuery(Long.class);
        Root<T> rt = cq.from(_entityClass);
        cq.select(_em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = _em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public void clearCache() {
        _em.flush();
        _em.getEntityManagerFactory().getCache().evictAll();
    }

    public void clearCache(Class clazz) {
        _em.flush();
        _em.getEntityManagerFactory().getCache().evict(clazz);
    }

}
