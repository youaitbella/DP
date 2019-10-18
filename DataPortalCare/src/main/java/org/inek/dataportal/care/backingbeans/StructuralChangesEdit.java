/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptStation;
import org.inek.dataportal.care.entities.DeptStationsAfterTargetYear;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesBaseInformation;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesWards;
import org.inek.dataportal.care.entities.StructuralChanges.WardsToChange;
import org.inek.dataportal.care.enums.Type;
import org.inek.dataportal.care.facades.DeptFacade;
import org.inek.dataportal.care.facades.StructuralChangesFacade;
import org.inek.dataportal.care.utils.CareExcelExporter;
import org.inek.dataportal.care.utils.CareValidator;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.MailTemplateHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author lautenti
 */
@Named
@ViewScoped
public class StructuralChangesEdit implements Serializable {

    @Inject
    private SessionController _sessionController;
    @Inject
    private StructuralChangesFacade _structuralChangesFacade;
    @PostConstruct
    private void init() {

    }

    public void createNew() {
        /*
        StructuralChangesBaseInformation scbi = new StructuralChangesBaseInformation();
        scbi.setAgendId(99);
        scbi.setIk(999999999);
        scbi.setProcessedAt(Date.from(Instant.now()));
        scbi.setRequestedAt(Date.from(Instant.now()));
        scbi.setStatusId(2);
        scbi.setType(Type.Loeschen);
        scbi.setRequestedAccountId(3577);

        StructuralChangesWards scw = new StructuralChangesWards();
        scw.setMapVersionId(0);

        WardsToChange wtc = new WardsToChange();
        wtc.setDeptId(1);
        wtc.setDeptName("TestAbteilung");
        wtc.setFab("AAAA");
        wtc.setLocationP21(83272);
        wtc.setLocationVz(83271);
        wtc.setValidFrom(Date.from(Instant.now()));
        wtc.setValidTo(Date.from(Instant.now()));
        wtc.setWardName("TestWard");
        wtc.setStructuralChangesWards(scw);

        scw.setWardsToChange(wtc);

        scbi.addStructuralChangesWards(scw);
        */

        StructuralChangesBaseInformation baseEntity = new StructuralChangesBaseInformation();
        baseEntity.setAgendId(99);
        baseEntity.setIk(999999999);
        baseEntity.setProcessedAt(Date.from(Instant.now()));
        baseEntity.setRequestedAt(Date.from(Instant.now()));
        baseEntity.setStatusId(2);
        baseEntity.setType(Type.Loeschen);
        baseEntity.setRequestedAccountId(3577);

        WardsToChange wtc = new WardsToChange();
        wtc.setDeptId(1);
        wtc.setDeptName("TestAbteilung");
        wtc.setFab("AAAA");
        wtc.setLocationP21(83272);
        wtc.setLocationVz(83271);
        wtc.setValidFrom(Date.from(Instant.now()));
        wtc.setValidTo(Date.from(Instant.now()));
        wtc.setWardName("TestWard");
        wtc.setStructuralChangesBaseInformation(baseEntity);

        baseEntity.setWardsToChange(wtc);

        _structuralChangesFacade.save(baseEntity);


    }

}
