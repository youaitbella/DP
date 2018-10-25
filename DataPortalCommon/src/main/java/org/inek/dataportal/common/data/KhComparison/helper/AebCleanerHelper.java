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
public class AebCleanerHelper {

    public static void removeEmptyEntries(AEBBaseInformation info) {
        info.getAebPageE1_1().removeIf(c -> c.getPepp().length() == 0);
        info.getAebPageE1_2().removeIf(c -> c.getEt().length() == 0);
        info.getAebPageE2().removeIf(c -> c.getZe().length() == 0);
        info.getAebPageE3_1().removeIf(c -> c.getRenumeration().length() == 0);
        info.getAebPageE3_2().removeIf(c -> c.getZe().length() == 0);
        info.getAebPageE3_3().removeIf(c -> c.getRenumeration().length() == 0);
    }
}
