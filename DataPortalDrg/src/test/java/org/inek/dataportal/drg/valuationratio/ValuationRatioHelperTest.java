package org.inek.dataportal.drg.valuationratio;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValuationRatioHelperTest {

    @ParameterizedTest
    @CsvSource({"110,100,200"
            , "99,100,101"
            , "88,100,90"
            ,"92,100,90",
            "60,100,90"})
    void userInputIsAllowedCorrectValuesTest(int userInput, int median, int inekCount) {
        Assertions.assertThat(ValuationRatioHelper.userInputIsAllowed(userInput, median, inekCount)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"90,100,200"
            , "70,100,102"})
    void userInputIsAllowedIncorrectValuesTest(int userInput, int median, int inekCount) {
        Assertions.assertThat(ValuationRatioHelper.userInputIsAllowed(userInput, median, inekCount)).isFalse();
    }
}