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
    public void testCanReadSealed() {
        assertFalse(CooperativeRight.None.canRead());
        assertTrue(CooperativeRight.ReadOnly.canRead());
        assertTrue(CooperativeRight.ReadWrite.canRead());
        assertTrue(CooperativeRight.ReadWriteSeal.canRead());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canRead());
    }

    @Test
    public void testCanWriteAlways() {
        assertFalse(CooperativeRight.None.canWrite());
        assertFalse(CooperativeRight.ReadOnly.canWrite());
        assertTrue(CooperativeRight.ReadWrite.canWrite());
        assertTrue(CooperativeRight.ReadWriteSeal.canWrite());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canWrite());
    }

    @Test
    public void testCanWriteCompleted() {
        assertTrue(CooperativeRight.ReadWrite.canWrite());
        assertTrue(CooperativeRight.ReadWriteSeal.canWrite());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canWrite());
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
    public void testCanTake() {
        assertFalse(CooperativeRight.None.canTake());
        assertFalse(CooperativeRight.ReadOnly.canTake());
        assertFalse(CooperativeRight.ReadWrite.canTake());
        assertFalse(CooperativeRight.ReadWriteSeal.canTake());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canTake());
    }

}
