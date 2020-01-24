package org.inek.dataportal.drg.valuationratio;

public class ValuationRatioHelper {


    public static final double MIN_PERCENT = 0.95;
    public static final double MAX_PERCENT = 1.05;

    public static boolean userInputIsAllowed(int userInput, int median, int inekCount) {
        int minCount = (int) (inekCount * MIN_PERCENT);
        int maxCount = (int) (inekCount * MAX_PERCENT);

        boolean userInputOk = minCount <= userInput && userInput <= maxCount;

        return !(inekCount > median
                && !userInputOk
                && userInput < median);
    }
}
