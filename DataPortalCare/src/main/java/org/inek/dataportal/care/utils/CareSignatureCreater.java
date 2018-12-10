package org.inek.dataportal.care.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class CareSignatureCreater {
    public static String createPvSignature() {
        String generatedString = RandomStringUtils.randomAlphanumeric(18);

        return "PV" + generatedString;
    }
}
