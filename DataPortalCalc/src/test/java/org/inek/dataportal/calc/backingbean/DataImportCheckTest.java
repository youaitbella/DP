package org.inek.dataportal.calc.backingbean;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DataImportCheckTest {

    @Test
    void tryImportIntegerWith5Returns5() {
        ErrorCounter counter = new ErrorCounter();
        TestEntity entity = new TestEntity();
        DataImportCheck.tryImportInteger(entity, "5", TestEntity::setValue, "", counter);
        assertThat(entity.getValue()).isEqualTo(5);
    }

    @Test
    void tryImportIntegerWithEmptyReturns0() {
        ErrorCounter counter = new ErrorCounter();
        TestEntity entity = new TestEntity();
        DataImportCheck.tryImportInteger(entity, "", TestEntity::setValue, "Empty", counter);
        assertThat(entity.getValue()).isEqualTo(0);
        assertThat(counter.containsError()).isTrue();
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