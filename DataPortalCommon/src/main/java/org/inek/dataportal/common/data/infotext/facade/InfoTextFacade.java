/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.infotext.facade;

import java.util.List;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.infotext.entity.InfoText;

/**
 *
 * @author lautenti
 */
@Named
@Transactional
public class InfoTextFacade extends AbstractDataAccess {

    public InfoText getInfoText(String key, String language) {
        String jpql = "select it from InfoText it where it._key = :key and it._language = :lang";
        TypedQuery<InfoText> query = getEntityManager().createQuery(jpql, InfoText.class);
        query.setParameter("key", key);
        query.setParameter("lang", language);
        return query.getSingleResult();
    }

    public List<InfoText> getAllInfoTexts(String language) {
        String jpql = "select it from InfoText it where it._language = :lang";
        TypedQuery<InfoText> query = getEntityManager().createQuery(jpql, InfoText.class);
        query.setParameter("lang", language);
        return query.getResultList();
    }

}
