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

    @Test
    void formatValuesForDatabaseTest() {
        Map<String, String> values = new HashMap<>();
        values.put("222222222,,,,,,,,,,222222224", "222222222, 222222224");
        values.put("222222222,    222222224", "222222222, 222222224");
        values.put("222222222; 222222224", "222222222, 222222224");
        values.put("222222222,    222222224,                222222226", "222222222, 222222224, 222222226");
        values.put("222222222,222222224", "222222222, 222222224");
        values.put("222222222\n222222224", "222222222, 222222224");
        values.put("P003A\nP003B", "P003A, P003B");
        values.put("P003A,              P003B", "P003A, P003B");
        values.put("P003A;P003B", "P003A, P003B");

        for (Map.Entry<String, String> entry : values.entrySet()) {
            Assertions.assertThat(PsyNubRequestValueChecker.formatValuesForDatabase(entry.getKey())).as(entry.getKey()).isEqualTo(entry.getValue());
        }

    }
}