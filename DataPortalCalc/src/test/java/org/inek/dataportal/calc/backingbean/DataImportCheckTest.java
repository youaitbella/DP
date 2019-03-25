package org.inek.dataportal.calc.backingbean;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DataImportCheckTest {

    private static final String ERROR_MSG = "Empty";
    private static final int INTEGER = 4711;

    @Test
    void tryImportIntegerWithNumberReturnsNumber() {
        ErrorCounter counter = new ErrorCounter();
        TestEntity entity = new TestEntity();
        String numberString = "" + INTEGER;
        DataImportCheck.tryImportInteger(entity, numberString, TestEntity::setValue, "", counter);
        assertThat(entity.getValue()).isEqualTo(INTEGER);
    }

    @Test
    void tryImportIntegerWithNumberPlusCommaReturnsNumber() {
        ErrorCounter counter = new ErrorCounter();
        TestEntity entity = new TestEntity();
        String numberString = "" + INTEGER;
        String numberWithDot = numberString.substring(0, 1) + "." + numberString.substring(1);
        DataImportCheck.tryImportInteger(entity, numberWithDot, TestEntity::setValue, "", counter);
        assertThat(entity.getValue()).isEqualTo(INTEGER);
    }

    @Test
    void tryImportIntegerWithEmptyReturns0() {
        ErrorCounter counter = new ErrorCounter();
        TestEntity entity = new TestEntity();
        DataImportCheck.tryImportInteger(entity, "", TestEntity::setValue, ERROR_MSG, counter);
        assertThat(entity.getValue()).isEqualTo(0);
        assertThat(counter.containsError()).isTrue();
        assertThat(counter.getMessage()).contains(ERROR_MSG);
    }

    public static class TestEntity {
        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}