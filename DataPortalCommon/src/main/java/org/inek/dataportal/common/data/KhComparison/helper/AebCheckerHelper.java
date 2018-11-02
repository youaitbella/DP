package org.inek.dataportal.common.data.KhComparison.helper;

import org.inek.dataportal.common.data.KhComparison.entities.AEBBaseInformation;

/**
 * @author lautenti
 */
public class AebCheckerHelper {

    public static Boolean baseInfoisComplete(AEBBaseInformation info) {
        if (info.getIk() != 0 && info.getYear() != 0) {
            return true;
        }
        return false;
    }
}
