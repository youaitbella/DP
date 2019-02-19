package org.inek.dataportal.common.helper;

import org.inek.dataportal.common.helper.structures.FieldValues;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectComparerTest {
    public static final int NUMBER = 4711;
    public static final String NAME = "foo";
    public static final String NAME2 = "bar";

    @Test
    void getDifferences() {
        DummyData original = new DummyData();
        original.setNumber(NUMBER);
        original.setName(NAME);

        DummyData copy = new DummyData();
        copy.setNumber(NUMBER + 1);
        copy.setName(NAME2);

        Map<String, FieldValues> differences = ObjectComparer.getDifferences(original, copy);

        assertThat(differences).isNotNull().containsKeys("number", "name");

        FieldValues fieldNumber = differences.get("number");
        assertThat(fieldNumber.getValue1()).isEqualTo(NUMBER);
        assertThat(fieldNumber.getValue2()).isEqualTo(NUMBER + 1);

        FieldValues fieldName = differences.get("name");
        assertThat(fieldName.getValue1()).isEqualTo(NAME);
        assertThat(fieldName.getValue2()).isEqualTo(NAME2);
    }

    @Test
    void checkCircularReference() {
        DummyData original = new DummyData();
        original.setNumber(NUMBER);
        original.setName(NAME);
        original.getData().put(NUMBER, original);

        DummyData copy = new DummyData();
        copy.setNumber(NUMBER + 1);
        copy.setName(NAME2);
        copy.getData().put(NUMBER, copy);

        Map<String, FieldValues> differences = ObjectComparer.getDifferences(original, copy);

        assertThat(differences).isNotNull().containsKeys("number", "name");

        FieldValues fieldNumber = differences.get("number");
        assertThat(fieldNumber.getValue1()).isEqualTo(NUMBER);
        assertThat(fieldNumber.getValue2()).isEqualTo(NUMBER + 1);

        FieldValues fieldName = differences.get("name");
        assertThat(fieldName.getValue1()).isEqualTo(NAME);
        assertThat(fieldName.getValue2()).isEqualTo(NAME2);
    }

    private class DummyData {
        private int number;
        private String name;
        private Map<Integer, DummyData> data = new HashMap<>();

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<Integer, DummyData> getData() {
            return data;
        }

        public void setData(Map<Integer, DummyData> data) {
            this.data = data;
        }
    }

}