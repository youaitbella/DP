package org.inek.dataportal.common.helper;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectCopierTest {

    public static final int NUMBER = 4711;
    public static final String NAME = "Name";

    @Test
    void copy() {
        DummyData original = new DummyData();
        original.setNumber(NUMBER);
        original.setName(NAME);
        original.getData().put(NUMBER, NAME);
        DummyData copy = ObjectCopier.copy(original);
        assertThat(copy.getNumber()).isNotNull().isEqualTo(original.getNumber());
        assertThat(copy.getName()).isNotNull().isEqualTo(original.getName());
        assertThat(copy.getData()).isNotNull().isNotEmpty().isEqualTo(original.getData());
    }

    @Test
    void copyFieldValue() {
        DummyData original = new DummyData();
        original.setNumber(NUMBER);
        original.setName(NAME);
        original.getData().put(NUMBER, NAME);
        DummyData target = new DummyData();
        // todo: ObjectCopier.copyFieldValue();
    }

    private class DummyData {
        private int number;
        private String name;
        private Map<Integer, String> data = new HashMap<>();

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

        public Map<Integer, String> getData() {
            return data;
        }

        public void setData(Map<Integer, String> data) {
            this.data = data;
        }
    }
}