/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.account.facade;

import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.assertj.core.api.Assertions;
import org.assertj.db.type.Request;
import org.assertj.db.type.Source;
import org.inek.dataportal.common.data.account.entities.AccountRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class AccountRequestFacadeTest {

    private static EntityManager em;
    private static EntityTransaction transaction;
    private static Source source;

    @BeforeAll
    public static void setUpClass() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DataPortalLocalPU");
        em = emf.createEntityManager();
        transaction = em.getTransaction();
        Map<String, Object> emfProps = emf.getProperties();
        System.out.println("emfProps" + emfProps);
        Object get = emfProps.get("javax.persistence.jdbc.url");
        if (get instanceof String) {
            source = new Source(stripParameterFromDbUrl((String) get), "", "");
        } else {
            throw new IllegalStateException("JDBC URL Definition needed in persistence xml: Parameter javax.persistence.jdbc.url not defined");
        }
    }

    private static String stripParameterFromDbUrl(String dbUrl) {
        int indexOf = dbUrl.indexOf(';', 0);
        if (indexOf < 0) {
            indexOf = dbUrl.length();
        }
        return dbUrl.substring(0, indexOf);
    }

    public AccountRequestFacadeTest() {
    }

    @Test
    public void shouldFindExistingAccountRequest() {

        AccountRequestFacade cut = new AccountRequestFacade(em);

        AccountRequest ar = new AccountRequest();
        ar.setEmail("test@test.de");
        ar.setUser("dummdumm");
        em.merge(ar);

        AccountRequest ar1 = new AccountRequest();
        ar1.setEmail("testen@test.de");
        ar1.setUser("dummdumm");
        em.merge(ar1);

        AccountRequest ar2 = new AccountRequest();
        ar2.setEmail("test@tasaest.de");
        ar2.setUser("dummdummas");
        em.merge(ar2);
        transaction.begin();
        transaction.commit();

        Assertions.assertThat(cut.accountRequestExists("test@test.de", "dummdumm")).as("user not found but expected").isTrue();
        Assertions.assertThat(cut.accountRequestExists("test@testen.de", "badummdumm")).as("user found but not expected").isFalse();
        Assertions.assertThat(cut.accountRequestExists("tester@test.de", "dummdummas")).as("user not found but expected").isTrue();

        Request request = new Request(
                source,
                "SELECT a.* FROM AccountRequest a WHERE a.arMail = ? or a.arUser = ?",
                "test@test.de", "dummdumm");
        org.assertj.db.api.Assertions.assertThat(request).column("arUser").value()
                .isEqualTo("dummdumm");
    }

}
