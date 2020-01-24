package org.inek.dataportal.base.feature.ikadmin.backingbean;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.IkReference;
import org.inek.dataportal.api.enums.IkUsage;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.data.ikadmin.entity.AccountResponsibility;
import org.inek.dataportal.common.data.ikadmin.facade.IkAdminFacade;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.Right;
import org.inek.dataportal.common.helper.AccessRightHelper;
import org.inek.dataportal.common.helper.Utils;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ResponsibleForEditor implements Serializable {
    private static final Logger LOGGER = Logger.getLogger("ResponsibleForEditor");

    @Inject
    private IkAdminFacade _ikAdminFacade;
    @Inject
    private SessionController _sessionController;

    private final Map<String, List<AccountResponsibility>> _responsibleForIks = new HashMap<>();
    private int _ik;
    private List<AccessRight> _accessRights = new ArrayList<>();

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String ikParam = "" + params.get("ik");
        try {
            int ik = Integer.parseInt(ikParam);
            if (_sessionController.getAccount().getAdminIks().stream().anyMatch(a -> a.getIk() == ik)) {
                _ik = ik;
                List<Feature> features = obtainManageableFeatures(ik);
                _accessRights = _ikAdminFacade.findAccessRights(_ik, features);
                return;
            }
        } catch (NumberFormatException ex) {
            // ignore here and handle after catch
        }
        Utils.navigate(Pages.NotAllowed.RedirectURL());
    }

    private List<Feature> obtainManageableFeatures(int ik) {
        List<Feature> features = _sessionController
                .getAccount()
                .getAdminIks()
                .stream()
                .filter(a -> a.getIk() == ik)
                .findAny()
                .get()
                .getIkAdminFeatures()
                .stream()
                .map(af -> af.getFeature())
                .filter(f -> f.getIkReference() != IkReference.None)
                .collect(Collectors.toList());
        return features;
    }


    public List<AccountResponsibility> obtainIkList(int accountId, Feature feature) {
        String key = buildKey(accountId, feature, _ik);
        if (!_responsibleForIks.containsKey(key)) {
            List<AccountResponsibility> responsibleForIks = _ikAdminFacade.
                    obtainAccountResponsibilities(accountId, feature, _ik);
            _responsibleForIks.put(key, responsibleForIks);
        }
        return _responsibleForIks.get(key);
    }

    public void deleteIk(int accountId, Feature feature, AccountResponsibility responsibility) {
        String key = buildKey(accountId, feature, _ik);
        _responsibleForIks.get(key).remove(responsibility);
    }

    public void addIk(int accountId, Feature feature) {
        String key = buildKey(accountId, feature, _ik);
        _responsibleForIks.get(key).add(new AccountResponsibility(accountId, feature, _ik, 0));
    }

    private String buildKey(int accountId, Feature feature, int ik) {
        return accountId + "|" + feature.name() + "|" + ik;
    }


    public Boolean getContainsResponsibility() {
        return _accessRights.stream().
                anyMatch(r -> r.getFeature().getIkUsage() == IkUsage.ByResponsibilityAndCorrelation);
    }


    public List<AccessRight> getResponsibilities() {
        return _accessRights
                .stream()
                .filter(r -> r.getRight() != Right.Deny)
                .filter(r -> r.getFeature().getIkUsage() == IkUsage.ByResponsibilityAndCorrelation)
                .collect(Collectors.toList());
    }

    public String saveResponsibilities() {

        StringBuilder errorMessages = new StringBuilder();

        if (AccessRightHelper.responsibilitiesHasNotToMuchUsers(_responsibleForIks, errorMessages)) {
            _ikAdminFacade.saveResponsibilities(_responsibleForIks);
            DialogController.showSaveDialog();
            return "";
        } else {
            DialogController.showWarningDialog("Fehler beim Speichern", errorMessages.toString());
            return "";
        }
    }


}
