package org.inek.dataportal.psy.nub.helper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

class PsyNubRequestValueCheckerTest {

    @ParameterizedTest
    @ValueSource(strings = {"01/02", "12/12", "05/68", "01/99", "01/00", ""})
    void isValidStringForDateValueWithValidValues(String value) {
        Assertions.assertThat(PsyNubRequestValueChecker.isValidStringForDateValue(value)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"01-02", "12/2005", "5/68", "0199", "01.01.2005", "Gesterb", "31/12", "13/01", "00/00", "00/01"})
    void isValidStringForDateValueWithNotValidValues(String value) {
        Assertions.assertThat(PsyNubRequestValueChecker.isValidStringForDateValue(value)).isFalse();
    }
}