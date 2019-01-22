package org.inek.dataportal.common.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.assertj.core.api.Assertions;

/**
 *
 * @author muellermi
 */
public class CooperativeRightTest {

    public CooperativeRightTest() {
    }

    @Test
    public void assertExcpectedEnumCount() {
        Assertions.assertThat(CooperativeRight.values().length).isEqualTo(5);
    }

    @Test
    public void testCanReadAlways() {
        assertFalse(CooperativeRight.None.canReadAlways());
        assertTrue(CooperativeRight.ReadOnly.canReadAlways());
        assertTrue(CooperativeRight.ReadWrite.canReadAlways());
        assertTrue(CooperativeRight.ReadWriteSeal.canReadAlways());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canReadAlways());
    }

    @Test
    public void testCanReadCompleted() {
        assertFalse(CooperativeRight.None.canReadCompleted());
        assertTrue(CooperativeRight.ReadOnly.canReadCompleted());
        assertTrue(CooperativeRight.ReadWrite.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteSeal.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canReadCompleted());
    }

    @Test
    public void testCanReadSealed() {
        assertFalse(CooperativeRight.None.canReadSealed());
        assertTrue(CooperativeRight.ReadOnly.canReadSealed());
        assertTrue(CooperativeRight.ReadWrite.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteSeal.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canReadSealed());
    }

    @Test
    public void testCanWriteAlways() {
        assertFalse(CooperativeRight.None.canWriteAlways());
        assertFalse(CooperativeRight.ReadOnly.canWriteAlways());
        assertTrue(CooperativeRight.ReadWrite.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteSeal.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canWriteAlways());
    }

    @Test
    public void testCanWriteCompleted() {
        assertFalse(CooperativeRight.None.canWriteCompleted());
        assertFalse(CooperativeRight.ReadOnly.canWriteCompleted());
        assertTrue(CooperativeRight.ReadWrite.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteSeal.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canWriteAlways());
    }

    @Test
    public void testCanSeal() {
        assertFalse(CooperativeRight.None.canSeal());
        assertFalse(CooperativeRight.ReadOnly.canSeal());
        assertFalse(CooperativeRight.ReadWrite.canSeal());
        assertTrue(CooperativeRight.ReadWriteSeal.canSeal());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canSeal());
    }

    @Test
    public void testIsSupervisor() {
        assertFalse(CooperativeRight.None.isSupervisor());
        assertFalse(CooperativeRight.ReadOnly.isSupervisor());
        assertFalse(CooperativeRight.ReadWrite.isSupervisor());
        assertFalse(CooperativeRight.ReadWriteSeal.isSupervisor());
        assertFalse(CooperativeRight.ReadWriteTakeSeal.isSupervisor());
    }

    @Test
    public void testCanTake() {
        assertFalse(CooperativeRight.None.canTake());
        assertFalse(CooperativeRight.ReadOnly.canTake());
        assertFalse(CooperativeRight.ReadWrite.canTake());
        assertFalse(CooperativeRight.ReadWriteSeal.canTake());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canTake());
    }

}
