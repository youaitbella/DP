package org.inek.dataportal.common.data.icmt.facade;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.icmt.entities.Customer;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
@Named
public class CustomerFacade extends AbstractDataAccess {

    public Customer getCustomerByIK(int ik) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
        Root<Customer> root = query.from(Customer.class);
        query.select(root).where(cb.equal(root.get("_ik"), ik));
        TypedQuery<Customer> q = getEntityManager().createQuery(query);
        try {
            return q.getSingleResult();
        } catch (Exception e) {
            Customer customer = new Customer();
            customer.setIK(ik);
            String ikString = "" + ik;
            boolean testIk = ikString.length() == 9 && (ikString.startsWith("222222") || ikString.startsWith("70"));
            customer.setName(testIk ? "Test-IK" : "unbekannt");
            customer.setTown(testIk ? "Musterort" : "unbekannt");
            return customer;
        }
    }

    public boolean isValidIK(String ikString) {
        if (!isFormalCorrectIk(ikString)) {
            return false;
        }

        return checkIK(new Integer(ikString));
    }

    public boolean isFormalCorrectIk(String ikString) {
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
        return true;
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
        } catch (Exception e) {
            return false;
        }
    }

    public void isIKValid(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        String ikString = "" + value;
        if (!isFormalCorrectIk(ikString)) {
            throw new ValidatorException(new FacesMessage("Ungültiges IK"));
        }
        if (!checkIK(new Integer(ikString))) {
            throw new ValidatorException(new FacesMessage("Unbekanntes IK"));
        }
    }

}
