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

    private String getInfoText(String key, String language, Boolean shortText) {
        try {
            InfoText it = _infoTextFacade.getInfoText(key.toUpperCase(), language.toUpperCase());
            if (shortText) {
                return it.getShortText();
            } else {
                return it.getDescription();
            }
        } catch (NoResultException ex) {
            addMissingInfoText(key, language);
            return "";
        }
    }

    public String getShortInfoText(String key) {
        return getInfoText(key, "DE", true);
    }

    public String getLongInfoText(String key) {
        return getInfoText(key, "DE", false);
    }

    private void addMissingInfoText(String key, String language) {
        InfoText it = new InfoText(key, language);
        _infoTextFacade.save(it);
    }
}
