package org.inek.dataportal.common.data.KhComparison.helper;

import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.data.KhComparison.checker.AebChecker;
import org.inek.dataportal.common.data.KhComparison.entities.AEBBaseInformation;
import org.inek.dataportal.common.data.KhComparison.facade.AEBListItemFacade;
import org.inek.dataportal.common.data.KhComparison.importer.AebImporter;

import java.io.InputStream;

/**
 * @author lautenti
 */
public class AebUploadHelper {

    public static String handleAebUpload(AEBBaseInformation info, InputStream input, AEBListItemFacade aebListItemFacade) {
        String message = "";

        if (info.getYear() > 0) {
            AebImporter importer = new AebImporter();
            AebChecker checker = new AebChecker(aebListItemFacade, true, false);
            if (importer.startImport(info, input)) {
                checker.checkAeb(info);
                message = "### Anfang Importmeldungen ### \n \n";
                message += importer.getErrorMessages();
                message += "\n### Ende Importmeldungen ###\n";

                message += "\n### Anfang Formatprüfungen ###\n \n";
                message += checker.getMessage() + "\n \n --> " + importer.getCounter() + " Zeilen eingelesen";
                message += "\n### Ende Formatprüfungen ###\n";
                DialogController.showInfoDialog("Upload abgeschlossen", "Ihre Daten wurden erfolgreich hochgeladen");
            }
            else {
                message = importer.getErrorMessages();
                AebCleanerHelper.removeAllEntrys(info);
                DialogController.showInfoDialog("Fehler beim Upload", "Bitte überprüfen Sie die hochgeladenen Datei auf Formatfehler.");
            }
        } else {
            DialogController.showInfoDialog("Fehler beim Upload", "Bitte wählen Sie ein Vereinbahrungsjahr aus um den Import zu benutzen.");
        }
        return message;
    }
}
