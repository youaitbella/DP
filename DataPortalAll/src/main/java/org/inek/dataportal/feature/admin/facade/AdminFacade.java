/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.admin.facade;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.adm.ReportTemplate;

/**
 * Hides the database accesses fro the admin tasks behind a facade
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class AdminFacade extends AbstractDataAccess {

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
    
    public List<ReportTemplate> getReportTemplatesById(int reportType) {
        String jpql = "select rt from ReportTemplate rt where rt._reportTyp = :id";
        TypedQuery<ReportTemplate> query = getEntityManager().createQuery(jpql, ReportTemplate.class);
        return query.setParameter("id", reportType).getResultList();
    }   
}
