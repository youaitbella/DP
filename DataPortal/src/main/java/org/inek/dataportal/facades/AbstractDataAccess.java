package org.inek.dataportal.facades;

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
import org.eclipse.persistence.jpa.JpaQuery;

public abstract class AbstractDataAccess {

    protected static final Logger _logger = Logger.getLogger("DataAccess");

    @PersistenceContext(unitName = "DataPortalPU")
    private EntityManager _em;


    public static Logger getLogger() {
        return _logger;
    }

    protected EntityManager getEntityManager() {
        return _em;
    }

    void setEntityManager(EntityManager em) {
        _em = em;
    }

    public void persist(Object entity) {
        try {
            _em.persist(entity);
            _em.flush();
        } catch (Exception ex) {
            // EJB wont populate any exection up to a caller. It allways forces a rollback.
            // To check for those kind of problems, we log it and re-throw the exception
            _logger.log(Level.SEVERE, ex.getMessage());
           //_logger.log(Level.SEVERE, ex.getStackTrace().toString());
            throw ex;
        }
    }

    public <T> T merge(T entity) {
        try {
            T mergedEntity = _em.merge(entity);
            _em.flush();
            return mergedEntity;
        } catch (Exception ex) {
            _logger.log(Level.SEVERE, ex.getMessage());
            throw ex;
        }
    }

    protected void remove(Object entity) {
        _em.remove(_em.merge(entity));
        _em.flush();
    }

    public void refresh(Object entity) {
        _em.refresh(entity);
    }

    protected <T> T find(Class<T> entityClass, Object id) {
        return _em.find(entityClass, id);
    }

    protected <T> T findFresh(Class<T> entityClass, Object id) {
        Map<String, Object> props = new HashMap<>();
        props.put("javax.persistence.cache.retrieveMode", "BYPASS");
        return _em.find(entityClass, id, props);
    }

    protected <T> List<T> findAll(Class<T> entityClass) {
        CriteriaBuilder cb = _em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return _em.createQuery(cq).getResultList();
    }

    protected <T> List<T> findAllFresh(Class<T> entityClass) {
        CriteriaBuilder cb = _em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return _em.createQuery(cq).setHint("javax.persistence.cache.retrieveMode", "BYPASS").getResultList();
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
