package org.inek.dataportal.common.helper.nub;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NubValueFormatterTest {

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
            Assertions.assertThat(NubValueFormatter.formatValuesForDatabase(entry.getKey())).as(entry.getKey()).isEqualTo(entry.getValue());
        }

    }

}