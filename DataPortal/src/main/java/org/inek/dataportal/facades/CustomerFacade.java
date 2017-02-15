package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.icmt.Customer;

/**
 *
 * @author muellermi
 */
@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {

    public CustomerFacade() {
        super(Customer.class);
    }

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
            Customer customer = new Customer();
            String ikString = "" + ik;
            if (ikString.length() == 9 && (ikString.startsWith("222222") || ikString.startsWith("70"))) {
                customer.setName("Test-IK");
                customer.setTown("Musterort");
            }
            return customer;
        }
    }

    public boolean isValidIK(String ikString) {
        Integer ik;
        try {
            ik = new Integer(ikString);
        } catch (NumberFormatException e) {
            return false;
        }
        if (ik < 100000000 || ik > 999999999) {
            return false;
        }
        if (ikString.startsWith("222222") || ikString.startsWith("70")) {
            // special numbers for testing: his manufactorers / training calc
            return true;
        }
        int checkSum = 0;
        int coreIk = (ik / 10) % 1000000;
        while (coreIk > 0) {
            checkSum += coreIk % 10;
            coreIk = coreIk / 10;
            int digit = 2 * (coreIk % 10);
            checkSum += digit - (digit < 10 ? 0 : 9);
            coreIk = coreIk / 10;
        }
        checkSum = checkSum % 10;
        if (ik % 10 != checkSum) {
            return false;
        }
        return checkIK(ik);
    }

    public boolean checkIK(int ik) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
        Root<Customer> root = query.from(Customer.class);
        query.select(root).where(cb.equal(root.get("_ik"), ik));
        TypedQuery<Customer> q = getEntityManager().createQuery(query);
        try {
            q.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

}
