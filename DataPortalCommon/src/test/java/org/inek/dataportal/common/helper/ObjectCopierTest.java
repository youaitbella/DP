package org.inek.dataportal.common.helper;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectCopierTest {

    public static final int NUMBER = 4711;
    public static final String NAME = "Name";

    @Test
    void copyRetursAnObjectWithSameFieldsAndValues() {
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
    void copyWithCircularReferencePerformsWithoutStackOverflow() {
        DummyData original = new DummyData();
        original.setNumber(NUMBER);
        original.setName(NAME);
        original.getData().put(NUMBER, original); // this caused and endless recursion / test ended with overflow
        DummyData copy = ObjectCopier.copy(original);
        assertThat(copy.getNumber()).isNotNull().isEqualTo(original.getNumber());
        assertThat(copy.getName()).isNotNull().isEqualTo(original.getName());
    }


    private class DummyData {
        private int number;
        private String name;
        private Map<Integer, Object> data = new HashMap<>();

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

        public Map<Integer, Object> getData() {
            return data;
        }

        public void setData(Map<Integer, Object> data) {
            this.data = data;
        }
    }
}