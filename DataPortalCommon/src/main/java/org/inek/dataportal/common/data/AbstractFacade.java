package org.inek.dataportal.common.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.eclipse.persistence.jpa.JpaQuery;

/**
 *
 * @param <T>
 * @author muellermi
 */
@Deprecated
public abstract class AbstractFacade<T> {
    private static final long serialVersionUID = 1L;

    protected static final Logger LOGGER = Logger.getLogger("Facade");

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

    public AbstractFacade(Class<T> _entityClass, EntityManager _em) {
        this._em = _em;
        this._entityClass = _entityClass;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    protected EntityManager getEntityManager() {
        return _em;
    }

    protected void persist(T entity) {
        try {
            _em.persist(entity);
            _em.flush();
        } catch (Exception ex) {
            // EJB wont populate any exection up to a caller. It allways forces a rollback.
            // To check for those kind of problems, we log it and re-throw the exception
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw ex;
        }
    }

    public T merge(T entity) {
        try {
            T mergedEntity = _em.merge(entity);
            _em.flush();
            return mergedEntity;
        } catch (Exception ex) {
            // EJB wont populate any exection up to a caller. It allways forces a rollback.
            // To check for those kind of problems, we log it and return a null
            // at the caller level we may chack for null or let it crash and catch an exception there
            LOGGER.log(Level.SEVERE, ex.getMessage());
            return null;
        }
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

    protected void dumpSql(Query query) {
        String sql = query.unwrap(JpaQuery.class).getDatabaseQuery().getSQLString();
        System.out.println(sql);
    }
}
