package org.inek.dataportal.common.data.KhComparison.entities;

import org.assertj.core.api.Assertions;
import org.eclipse.persistence.internal.jpa.EntityManagerFactoryProvider;
import org.inek.dataportal.common.data.account.entities.Account;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class InekComparisonJobTest {

    private static EntityManager entityManager;

    @BeforeAll
    public static void obtainEntityManager() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DataPortalDevLocalPU");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeEntityManager() {
        entityManager.close();
    }

    @Test
    void createInekComparisonJob() {
        Account account = entityManager.find(Account.class, 6006);
        Assertions.assertThat(account.getLastName()).isEqualToIgnoringCase("Kunkel");
        InekComparisonJob inekComparisonJob = new InekComparisonJob();
        inekComparisonJob.setAccount(account);
        // make inekComparisonJob managed by persistence manager
        entityManager.persist(inekComparisonJob);
        inekComparisonJob.setDataYear(2018);
        inekComparisonJob.setAebUpTo("2020-01-17");
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.flush();
        transaction.commit();
        Assertions.assertThat(inekComparisonJob.getId()).isNotEqualTo(0);

    }

}
