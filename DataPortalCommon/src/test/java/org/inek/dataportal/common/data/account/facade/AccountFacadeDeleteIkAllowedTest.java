/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.account.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.assertj.core.api.Assertions;
import org.assertj.db.type.Source;
import org.inek.dataportal.common.data.account.entities.Account;
import org.junit.Test;
import org.junit.BeforeClass;

/**
 *
 * @author lautenti
 */
public class AccountFacadeDeleteIkAllowedTest {

    private static EntityManager em;
    private static EntityTransaction transaction;
    private static Source source;

    @BeforeClass
    public static void setUpClass() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DataPortalLocalScriptPU");
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

    public AccountFacadeDeleteIkAllowedTest() {
    }

    @Test
    public void TestAllTables() {
        AccountFacade accountFacade = new AccountFacade(em);

        final List<Pair<Integer, Integer>> values = new ArrayList<>();
        values.add(new Pair<>(222222221, 2));
        values.add(new Pair<>(222222222, 1));

        for (Pair<Integer, Integer> value : values) {
            transaction.begin();
            Query q1 = em.createNativeQuery("insert into calc.kglbaseinformation values(1," + value.getKey() + "," + value.getValue() + ")");
            q1.executeUpdate();
            Query q2 = em.createNativeQuery("insert into calc.kgpbaseinformation values(1," + value.getKey() + "," + value.getValue() + ")");
            q2.executeUpdate();
            Query q3 = em.createNativeQuery("insert into calc.StatementOfParticipance values(1," + value.getKey() + "," + value.getValue() + ")");
            q3.executeUpdate();
            Query q4 = em.createNativeQuery("insert into calc.DistributionModelMaster values(1," + value.getKey() + "," + value.getValue() + ")");
            q4.executeUpdate();
            Query q5 = em.createNativeQuery("insert into vr.ValuationRatio values(1," + value.getKey() + "," + value.getValue() + ")");
            q5.executeUpdate();
            Query q6 = em.createNativeQuery("insert into spf.RequestMaster values(1," + value.getKey() + "," + value.getValue() + ")");
            q6.executeUpdate();
            Query q7 = em.createNativeQuery("insert into spf.AgreementMaster values(1," + value.getKey() + "," + value.getValue() + ")");
            q7.executeUpdate();
            Query q8 = em.createNativeQuery("insert into dbo.NubProposal values(1," + value.getKey() + "," + value.getValue() + ")");
            q8.executeUpdate();
            Query q9 = em.createNativeQuery("insert into psy.StaffProofMaster values(1," + value.getKey() + "," + value.getValue() + ")");
            q9.executeUpdate();
            transaction.commit();
        }

        Query result1 = em.createNativeQuery("SELECT * FROM calc.kglbaseinformation");
        Query result2 = em.createNativeQuery("SELECT * FROM calc.kgpbaseinformation");
        Query result3 = em.createNativeQuery("SELECT * FROM calc.StatementOfParticipance");
        Query result4 = em.createNativeQuery("SELECT * FROM calc.DistributionModelMaster");
        Query result5 = em.createNativeQuery("SELECT * FROM vr.ValuationRatio");
        Query result6 = em.createNativeQuery("SELECT * FROM spf.RequestMaster");
        Query result7 = em.createNativeQuery("SELECT * FROM spf.AgreementMaster");
        Query result8 = em.createNativeQuery("SELECT * FROM dbo.NubProposal");
        Query result9 = em.createNativeQuery("SELECT * FROM psy.StaffProofMaster");

        Assertions.assertThat(result1.getResultList().size() > 0).isTrue();
        Assertions.assertThat(result2.getResultList().size() > 0).isTrue();
        Assertions.assertThat(result3.getResultList().size() > 0).isTrue();
        Assertions.assertThat(result4.getResultList().size() > 0).isTrue();
        Assertions.assertThat(result5.getResultList().size() > 0).isTrue();
        Assertions.assertThat(result6.getResultList().size() > 0).isTrue();
        Assertions.assertThat(result7.getResultList().size() > 0).isTrue();
        Assertions.assertThat(result8.getResultList().size() > 0).isTrue();
        Assertions.assertThat(result9.getResultList().size() > 0).isTrue();

        Account acc = new Account();
        acc.setId(1);

        Assertions.assertThat(accountFacade.deleteIkAllowed(222222221, acc)).isTrue();
        Assertions.assertThat(accountFacade.deleteIkAllowed(222222222, acc)).isFalse();
    }

    @Test
    public void TestKgl() {
        AccountFacade accountFacade = new AccountFacade(em);

        final List<Pair<Integer, Integer>> values = new ArrayList<>();
        values.add(new Pair<>(222222221, 2));
        values.add(new Pair<>(222222222, 1));

        for (Pair<Integer, Integer> value : values) {
            transaction.begin();
            Query q1 = em.createNativeQuery("insert into calc.kglbaseinformation values(1," + value.getKey() + "," + value.getValue() + ")");
            q1.executeUpdate();
            transaction.commit();
        }

        Query result1 = em.createNativeQuery("SELECT * FROM calc.kglbaseinformation");

        Assertions.assertThat(result1.getResultList().size() > 0).isTrue();

        Account acc = new Account();
        acc.setId(1);

        Assertions.assertThat(accountFacade.deleteIkAllowed(222222221, acc)).isTrue();
        Assertions.assertThat(accountFacade.deleteIkAllowed(222222222, acc)).isFalse();
    }

    @Test
    public void TestKgp() {
        AccountFacade accountFacade = new AccountFacade(em);

        final List<Pair<Integer, Integer>> values = new ArrayList<>();
        values.add(new Pair<>(222222221, 2));
        values.add(new Pair<>(222222222, 1));

        for (Pair<Integer, Integer> value : values) {
            transaction.begin();
            Query q1 = em.createNativeQuery("insert into calc.kgpbaseinformation values(1," + value.getKey() + "," + value.getValue() + ")");
            q1.executeUpdate();
            transaction.commit();
        }

        Query result1 = em.createNativeQuery("SELECT * FROM calc.kgpbaseinformation");

        Assertions.assertThat(result1.getResultList().size() > 0).isTrue();

        Account acc = new Account();
        acc.setId(1);

        Assertions.assertThat(accountFacade.deleteIkAllowed(222222221, acc)).isTrue();
        Assertions.assertThat(accountFacade.deleteIkAllowed(222222222, acc)).isFalse();
    }

    @Test
    public void TestSop() {
        AccountFacade accountFacade = new AccountFacade(em);

        final List<Pair<Integer, Integer>> values = new ArrayList<>();
        values.add(new Pair<>(222222221, 2));
        values.add(new Pair<>(222222222, 1));

        for (Pair<Integer, Integer> value : values) {
            transaction.begin();
            Query q1 = em.createNativeQuery("insert into calc.StatementOfParticipance values(1," + value.getKey() + "," + value.getValue() + ")");
            q1.executeUpdate();
            transaction.commit();
        }

        Query result1 = em.createNativeQuery("SELECT * FROM calc.StatementOfParticipance");

        Assertions.assertThat(result1.getResultList().size() > 0).isTrue();

        Account acc = new Account();
        acc.setId(1);

        Assertions.assertThat(accountFacade.deleteIkAllowed(222222221, acc)).isTrue();
        Assertions.assertThat(accountFacade.deleteIkAllowed(222222222, acc)).isFalse();
    }

    @Test
    public void TestDmm() {
        AccountFacade accountFacade = new AccountFacade(em);

        final List<Pair<Integer, Integer>> values = new ArrayList<>();
        values.add(new Pair<>(222222221, 2));
        values.add(new Pair<>(222222222, 1));

        for (Pair<Integer, Integer> value : values) {
            transaction.begin();
            Query q1 = em.createNativeQuery("insert into calc.DistributionModelMaster values(1," + value.getKey() + "," + value.getValue() + ")");
            q1.executeUpdate();
            transaction.commit();
        }

        Query result1 = em.createNativeQuery("SELECT * FROM calc.DistributionModelMaster");

        Assertions.assertThat(result1.getResultList().size() > 0).isTrue();

        Account acc = new Account();
        acc.setId(1);

        Assertions.assertThat(accountFacade.deleteIkAllowed(222222221, acc)).isTrue();
        Assertions.assertThat(accountFacade.deleteIkAllowed(222222222, acc)).isFalse();
    }

    @Test
    public void TestValuationRatio() {
        AccountFacade accountFacade = new AccountFacade(em);

        final List<Pair<Integer, Integer>> values = new ArrayList<>();
        values.add(new Pair<>(222222221, 2));
        values.add(new Pair<>(222222222, 1));

        for (Pair<Integer, Integer> value : values) {
            transaction.begin();
            Query q1 = em.createNativeQuery("insert into vr.ValuationRatio values(1," + value.getKey() + "," + value.getValue() + ")");
            q1.executeUpdate();
            transaction.commit();
        }

        Query result1 = em.createNativeQuery("SELECT * FROM vr.ValuationRatio");

        Assertions.assertThat(result1.getResultList().size() > 0).isTrue();

        Account acc = new Account();
        acc.setId(1);

        Assertions.assertThat(accountFacade.deleteIkAllowed(222222221, acc)).isTrue();
        Assertions.assertThat(accountFacade.deleteIkAllowed(222222222, acc)).isFalse();
    }

    @Test
    public void TestRequestMaster() {
        AccountFacade accountFacade = new AccountFacade(em);

        final List<Pair<Integer, Integer>> values = new ArrayList<>();
        values.add(new Pair<>(222222221, 2));
        values.add(new Pair<>(222222222, 1));

        for (Pair<Integer, Integer> value : values) {
            transaction.begin();
            Query q1 = em.createNativeQuery("insert into spf.RequestMaster values(1," + value.getKey() + "," + value.getValue() + ")");
            q1.executeUpdate();
            transaction.commit();
        }

        Query result1 = em.createNativeQuery("SELECT * FROM spf.RequestMaster");

        Assertions.assertThat(result1.getResultList().size() > 0).isTrue();

        Account acc = new Account();
        acc.setId(1);

        Assertions.assertThat(accountFacade.deleteIkAllowed(222222221, acc)).isTrue();
        Assertions.assertThat(accountFacade.deleteIkAllowed(222222222, acc)).isFalse();
    }

    @Test
    public void TestAgreementMaster() {
        AccountFacade accountFacade = new AccountFacade(em);

        final List<Pair<Integer, Integer>> values = new ArrayList<>();
        values.add(new Pair<>(222222221, 2));
        values.add(new Pair<>(222222222, 1));

        for (Pair<Integer, Integer> value : values) {
            transaction.begin();
            Query q1 = em.createNativeQuery("insert into spf.AgreementMaster values(1," + value.getKey() + "," + value.getValue() + ")");
            q1.executeUpdate();
            transaction.commit();
        }

        Query result1 = em.createNativeQuery("SELECT * FROM spf.AgreementMaster");

        Assertions.assertThat(result1.getResultList().size() > 0).isTrue();

        Account acc = new Account();
        acc.setId(1);

        Assertions.assertThat(accountFacade.deleteIkAllowed(222222221, acc)).isTrue();
        Assertions.assertThat(accountFacade.deleteIkAllowed(222222222, acc)).isFalse();
    }

    @Test
    public void TestNubProposal() {
        AccountFacade accountFacade = new AccountFacade(em);

        final List<Pair<Integer, Integer>> values = new ArrayList<>();
        values.add(new Pair<>(222222221, 2));
        values.add(new Pair<>(222222222, 1));

        for (Pair<Integer, Integer> value : values) {
            transaction.begin();
            Query q1 = em.createNativeQuery("insert into dbo.NubProposal values(1," + value.getKey() + "," + value.getValue() + ")");
            q1.executeUpdate();
            transaction.commit();
        }

        Query result1 = em.createNativeQuery("SELECT * FROM dbo.NubProposal");

        Assertions.assertThat(result1.getResultList().size() > 0).isTrue();

        Account acc = new Account();
        acc.setId(1);

        Assertions.assertThat(accountFacade.deleteIkAllowed(222222221, acc)).isTrue();
        Assertions.assertThat(accountFacade.deleteIkAllowed(222222222, acc)).isFalse();
    }

    @Test
    public void TestStaffProofMaster() {
        AccountFacade accountFacade = new AccountFacade(em);

        final List<Pair<Integer, Integer>> values = new ArrayList<>();
        values.add(new Pair<>(222222221, 2));
        values.add(new Pair<>(222222222, 1));

        for (Pair<Integer, Integer> value : values) {
            transaction.begin();
            Query q1 = em.createNativeQuery("insert into psy.StaffProofMaster values(1," + value.getKey() + "," + value.getValue() + ")");
            q1.executeUpdate();
            transaction.commit();
        }

        Query result1 = em.createNativeQuery("SELECT * FROM psy.StaffProofMaster");

        Assertions.assertThat(result1.getResultList().size() > 0).isTrue();

        Account acc = new Account();
        acc.setId(1);

        Assertions.assertThat(accountFacade.deleteIkAllowed(222222221, acc)).isTrue();
        Assertions.assertThat(accountFacade.deleteIkAllowed(222222222, acc)).isFalse();
    }
}
