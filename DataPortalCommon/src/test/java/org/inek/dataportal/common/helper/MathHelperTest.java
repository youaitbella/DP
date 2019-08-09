package org.inek.dataportal.common.helper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MathHelperTest {

    @ParameterizedTest
    @CsvSource({"17.185,17.19"
            , "0.512,0.51"
            , "24.2365874569633,24.24"
            , "0.8333333336,0.830,8333333336" })
    void roundValues2DecimalPlacesTest(double value, double expectedResult) {
        Assertions.assertThat(MathHelper.round(value, 2)).isEqualTo(expectedResult);
    }
}