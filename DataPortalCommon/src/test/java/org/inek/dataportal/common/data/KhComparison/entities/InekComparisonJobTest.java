package org.inek.dataportal.common.data.KhComparison.entities;

import org.assertj.core.api.Assertions;
import org.assertj.db.type.Request;
import org.assertj.db.type.Source;
import org.eclipse.persistence.internal.jpa.EntityManagerFactoryProvider;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
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

        AEBFacade aebFacade = new AEBFacade(entityManager);
        transaction.begin();
        aebFacade.generateInekComparisonHospitals(inekComparisonJob);
        transaction.commit();
        Request request = new Request(
                new Source("jdbc:sqlserver://vdatabase01:1433;databaseName=DataPortalDev;integratedSecurity=true","", ""),
                "select count(1) as anz from psy.InekComparisonHospital where ichInekComparisonJobId = " + inekComparisonJob.getId());
        org.assertj.db.api.Assertions.assertThat(request).row(0).column(0).value().isGreaterThan(100);
    }
}
