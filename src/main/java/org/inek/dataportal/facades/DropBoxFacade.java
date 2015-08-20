/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.facades;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import org.inek.dataportal.entities.dropbox.DropBox;

/**
 *
 * @author muellermi
 */
@Stateless
public class DropBoxFacade extends AbstractFacade<DropBox> {

    public DropBoxFacade() {
        super(DropBox.class);
    }

    
    public List<DropBox> findAll(int accountId, boolean isClosed) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DropBox> cq = cb.createQuery(DropBox.class);
        Root request = cq.from(DropBox.class);
        Predicate criteria = cb.conjunction();
        criteria = cb.and(criteria, cb.isFalse(request.get("_isComplete")));
        criteria = cb.and(criteria, cb.greaterThan(request.get("_validUntil"), Calendar.getInstance().getTime()));
        Predicate isAccount = cb.equal(request.get("_accountId"), accountId);
        if (isClosed) {
            cq.select(request).where(cb.and(isAccount, cb.not(criteria))).orderBy(cb.desc(request.get("_dropBoxId")));
        } else {
            cq.select(request).where(cb.and(isAccount, criteria)).orderBy(cb.asc(request.get("_dropBoxId")));
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    
    public List<DropBox> findInvalid() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DropBox> cq = cb.createQuery(DropBox.class);
        Root request = cq.from(DropBox.class);
        cq.select(request).where(cb.and(cb.isFalse(request.get("_isComplete")), cb.lessThan(request.get("_validUntil"), Calendar.getInstance().getTime())));
        return getEntityManager().createQuery(cq).getResultList();
    }

    
    public DropBox getDropBoxByDir(String dir) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DropBox> query = cb.createQuery(DropBox.class);
        Root<DropBox> root = query.from(DropBox.class);
        query.select(root).where(cb.equal(root.get("_directory"), dir));
        TypedQuery<DropBox> q = getEntityManager().createQuery(query);
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void deleteDropBox(DropBox dropbox) {
        if (dropbox.getDropBoxId() != null) {
            remove(dropbox);
        }
    }

    public DropBox updateDropBox(DropBox dropbox) {
        if (dropbox.getDropBoxId() == null) {
            _logger.log(Level.SEVERE, "attempt to update a non-existing dropbox");
            return null;  // let the client crash or handle
        } else {
            return merge(dropbox);
        }
    }

    public DropBox createDropBox(DropBox dropbox) {
        if (dropbox.getDropBoxId() != null) {
            _logger.log(Level.SEVERE, "attempt to create an existing dropbox");
            return null;  // let the client crash or handle
        }
        String dir;
        do {
            dir = UUID.randomUUID().toString();
        } while (getDropBoxByDir(dir) != null);
        dropbox.setDirectory(dir);
        persist(dropbox);
        if (dropbox.getDropBoxId() == null || dropbox.getDropBoxId() < 0) {
            return null;
        }
        return dropbox;
    }

    @Schedule(hour = "1")
    private void cleanDropBoxes() {
        List<DropBox> dropboxes = findInvalid();
        for (DropBox dropbox : dropboxes) {
            _logger.log(Level.WARNING, "Clean DropBox {0}", dropbox.getDropBoxId());
            remove(dropbox);
        }
    }

}
