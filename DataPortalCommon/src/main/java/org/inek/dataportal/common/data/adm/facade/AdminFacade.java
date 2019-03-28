/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.adm.facade;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.adm.ReportTemplate;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

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
        return findReportTemplateByName("", name);
    }

    public Optional<ReportTemplate> findReportTemplateByName(String group, String name) {
        String jpql = "select rt from ReportTemplate rt where rt._group = :group and rt._name = :name and rt._address <> ''";
        TypedQuery<ReportTemplate> query = getEntityManager().createQuery(jpql, ReportTemplate.class);
        query.setParameter("group", group);
        query.setParameter("name", name);
        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "No such ReportTemplate: {0}", name);
            return Optional.empty();
        }
    }

    public List<ReportTemplate> getReportTemplatesByGroup(String group) {
        String jpql = "select rt from ReportTemplate rt where rt._group = :group";
        TypedQuery<ReportTemplate> query = getEntityManager().createQuery(jpql, ReportTemplate.class);
        query.setParameter("group", group);
        return query.getResultList();
    }
}
