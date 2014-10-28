package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.Customer;

/**
 *
 * @author muellermi
 */
@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {

    public CustomerFacade() {
        super(Customer.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Customer getCustomerByIK(Integer ik) {
        if (ik == null) {
            return new Customer();
        }
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
        Root<Customer> root = query.from(Customer.class);
        query.select(root).where(cb.equal(root.get("_ik"), ik));
        TypedQuery<Customer> q = getEntityManager().createQuery(query);
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return new Customer();
        }

    }

}
