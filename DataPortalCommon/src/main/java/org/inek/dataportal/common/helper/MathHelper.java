package org.inek.dataportal.common.helper;

import org.apache.commons.math3.util.Precision;

public class MathHelper {

    public static double round(double value, int decimalPlaces) {
        return Precision.round(value, 2);
    }
}
