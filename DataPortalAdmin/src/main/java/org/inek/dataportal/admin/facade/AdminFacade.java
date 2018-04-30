/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.admin.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.admin.dao.AccountInfo;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.adm.ReportTemplate;

/**
 * Hides the database accesses fro the admin tasks behind a facade
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class AdminFacade extends AbstractDataAccess {

    private static final String IK = "ik";

    public ReportTemplate findReportTemplate(int id) {
        return getEntityManager().find(ReportTemplate.class, id);
    }

    public Optional<ReportTemplate> findReportTemplateByName(String name) {
        String jpql = "select rt from ReportTemplate rt where rt._name = :name and rt._address != ''";
        TypedQuery<ReportTemplate> query = getEntityManager().createQuery(jpql, ReportTemplate.class);
        query.setParameter("name", name);
        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "No such ReportTemplate: {0}", name);
            return Optional.empty();
        }
    }

    public List<AccountInfo> retrieveNubAccountInfos(int ik) {
        String sql = "SELECT acId, acFirstName, acLastName, acMail, count(0) as acCount \n"
                + "FROM dbo.NubProposal n \n"
                + "JOIN dbo.Account a on n.nubAccountId = a.acId \n"
                + "WHERE n.nubIk = " + ik + "\n"
                + "GROUP BY acId, acFirstName, acLastName, acMail";
        
        Query query = getEntityManager().createNativeQuery(sql, AccountInfo.class);
        @SuppressWarnings("unchecked") List<AccountInfo> infos = query.getResultList();
        return infos;
    }

    public void changeNubOwner(int ik, int oldAccountId, int newAccountId, int agentAccountId) {
        String sql = ("begin transaction;\n"
                + "\n"
                + "insert into adm.Log (logTS, logAccountId, logSession, logMessage )\n"
                + "select getDate(), {agentAccountId}, 'internal', "
                + "'NubOwner changed: nub=' + cast(nubId as varchar) + ', oldOwner={oldAccountId}, newOwner={newAccountId}'\n"
                + "from  dbo.NubProposal where nubAccountId = {oldAccountId} and nubIk = {ik};\n"
                + "\n"
                + "update dbo.NubProposal set nubAccountId = {newAccountId} where nubIk = {ik} and nubAccountId = {oldAccountId};\n"
                + "\n"
                + "commit transaction;")
                .replace("{ik}", "" + ik)
                .replace("{oldAccountId}", "" + oldAccountId)
                .replace("{newAccountId}", "" + newAccountId)
                .replace("{agentAccountId}", "" + agentAccountId);
        Query query = getEntityManager().createNativeQuery(sql);
        query.executeUpdate();
        clearCache();
    }
}
