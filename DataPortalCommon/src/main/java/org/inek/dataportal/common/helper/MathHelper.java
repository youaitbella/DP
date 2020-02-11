package org.inek.dataportal.common.helper;

import java.math.BigDecimal;

public class MathHelper {

    //    public static double round(double value, int decimalPlaces) {
    //        return Precision.round(value, decimalPlaces);
    //    }

    // here the copy of used methods from apache commons math 3 (Precision.round)
    private static final double POSITIVE_ZERO = 0d;

    public static double round(double x, int scale) {
        return round(x, scale, BigDecimal.ROUND_HALF_UP);
    }

    public static double round(double x, int scale, int roundingMethod) {
        try {
            final double rounded = (new BigDecimal(Double.toString(x))
                    .setScale(scale, roundingMethod))
                    .doubleValue();
            // MATH-1089: negative values rounded to zero should result in negative zero
            return rounded == POSITIVE_ZERO ? POSITIVE_ZERO * x : rounded;
        } catch (NumberFormatException ex) {
            if (Double.isInfinite(x)) {
                return x;
            } else {
                return Double.NaN;
            }
        }
    }

}
