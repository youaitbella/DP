/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.helper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import org.inek.dataportal.common.data.infotext.entity.InfoText;
import org.inek.dataportal.common.data.infotext.facade.InfoTextFacade;

/**
 *
 * @author lautenti
 */
@Named
@ApplicationScoped
public class InfoTextHelper {

    @Inject
    private InfoTextFacade _infoTextFacade;

    private String getInfoText(String key, String language) {
        try {
            InfoText it = _infoTextFacade.getInfoText(key.toUpperCase(), language.toUpperCase());
            return it.getText();
        } catch (NoResultException ex) {
            return "No text found for " + key;
        }
    }

    public String getInfoText(String key) {
        return getInfoText(key, "DE");
    }
}
