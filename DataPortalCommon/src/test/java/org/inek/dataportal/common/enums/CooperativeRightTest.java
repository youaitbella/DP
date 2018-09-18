package org.inek.dataportal.common.enums;

import org.inek.dataportal.common.enums.CooperativeRight;
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
        Assertions.assertThat(CooperativeRight.values().length).isEqualTo(15);
    }

    @Test
    public void testCanReadAlways() {
        assertFalse(CooperativeRight.None.canReadAlways());
        assertTrue(CooperativeRight.ReadOnly.canReadAlways());
        assertFalse(CooperativeRight.ReadSealed.canReadAlways());
        assertFalse(CooperativeRight.ReadCompleted.canReadAlways());
        assertFalse(CooperativeRight.ReadWriteCompleted.canReadAlways());
        assertTrue(CooperativeRight.ReadWrite.canReadAlways());
        assertTrue(CooperativeRight.ReadWriteTake.canReadAlways());
        assertTrue(CooperativeRight.ReadWriteSeal.canReadAlways());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canReadAlways());
        assertFalse(CooperativeRight.ReadCompletedSealSupervisor.canReadAlways());
        assertFalse(CooperativeRight.ReadWriteCompletedSealSupervisor.canReadAlways());
        assertTrue(CooperativeRight.ReadSealSupervisor.canReadAlways());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canReadAlways());
        assertTrue(CooperativeRight.ReadWriteTakeSealSupervisor.canReadAlways());
    }

    @Test
    public void testCanReadCompleted() {
        assertFalse(CooperativeRight.None.canReadCompleted());
        assertTrue(CooperativeRight.ReadOnly.canReadCompleted());
        assertFalse(CooperativeRight.ReadSealed.canReadCompleted());
        assertTrue(CooperativeRight.ReadCompleted.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteCompleted.canReadCompleted());
        assertTrue(CooperativeRight.ReadWrite.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteTake.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteSeal.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canReadCompleted());
        assertTrue(CooperativeRight.ReadCompletedSealSupervisor.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.canReadCompleted());
        assertTrue(CooperativeRight.ReadSealSupervisor.canReadCompleted());
        assertTrue(CooperativeRight.ReadAllWriteCompletedSealSupervisor.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteTakeSealSupervisor.canReadCompleted());
    }

    @Test
    public void testCanReadSealed() {
        assertFalse(CooperativeRight.None.canReadSealed());
        assertTrue(CooperativeRight.ReadOnly.canReadSealed());
        assertTrue(CooperativeRight.ReadSealed.canReadSealed());
        assertTrue(CooperativeRight.ReadCompleted.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteCompleted.canReadSealed());
        assertTrue(CooperativeRight.ReadWrite.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteTake.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteSeal.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canReadSealed());
        assertTrue(CooperativeRight.ReadCompletedSealSupervisor.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.canReadSealed());
        assertTrue(CooperativeRight.ReadSealSupervisor.canReadSealed());
        assertTrue(CooperativeRight.ReadAllWriteCompletedSealSupervisor.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteTakeSealSupervisor.canReadSealed());
    }

    @Test
    public void testCanWriteAlways() {
        assertFalse(CooperativeRight.None.canWriteAlways());
        assertFalse(CooperativeRight.ReadOnly.canWriteAlways());
        assertFalse(CooperativeRight.ReadSealed.canWriteAlways());
        assertFalse(CooperativeRight.ReadCompleted.canWriteAlways());
        assertFalse(CooperativeRight.ReadWriteCompleted.canWriteAlways());
        assertTrue(CooperativeRight.ReadWrite.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteTake.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteSeal.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canWriteAlways());
        assertFalse(CooperativeRight.ReadCompletedSealSupervisor.canWriteAlways());
        assertFalse(CooperativeRight.ReadWriteCompletedSealSupervisor.canWriteAlways());
        assertFalse(CooperativeRight.ReadSealSupervisor.canWriteAlways());
        assertFalse(CooperativeRight.ReadAllWriteCompletedSealSupervisor.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteTakeSealSupervisor.canWriteAlways());
    }

    @Test
    public void testCanWriteCompleted() {
        assertFalse(CooperativeRight.None.canWriteCompleted());
        assertFalse(CooperativeRight.ReadOnly.canWriteCompleted());
        assertFalse(CooperativeRight.ReadSealed.canWriteCompleted());
        assertFalse(CooperativeRight.ReadCompleted.canWriteAlways());
        assertFalse(CooperativeRight.ReadWriteCompleted.canWriteAlways());
        assertTrue(CooperativeRight.ReadWrite.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteTake.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteSeal.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canWriteAlways());
        assertFalse(CooperativeRight.ReadCompletedSealSupervisor.canWriteCompleted());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.canWriteCompleted());
        assertFalse(CooperativeRight.ReadSealSupervisor.canWriteCompleted());
        assertTrue(CooperativeRight.ReadAllWriteCompletedSealSupervisor.canWriteCompleted());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canWriteCompleted());
        assertTrue(CooperativeRight.ReadWriteTakeSealSupervisor.canWriteCompleted());
    }

    @Test
    public void testCanSeal() {
        assertFalse(CooperativeRight.None.canSeal());
        assertFalse(CooperativeRight.ReadOnly.canSeal());
        assertFalse(CooperativeRight.ReadSealed.canSeal());
        assertFalse(CooperativeRight.ReadCompleted.canSeal());
        assertFalse(CooperativeRight.ReadWriteCompleted.canSeal());
        assertFalse(CooperativeRight.ReadWrite.canSeal());
        assertFalse(CooperativeRight.ReadWriteTake.canSeal());
        assertTrue(CooperativeRight.ReadWriteSeal.canSeal());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canSeal());
        assertTrue(CooperativeRight.ReadCompletedSealSupervisor.canSeal());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.canSeal());
        assertTrue(CooperativeRight.ReadSealSupervisor.canSeal());
        assertTrue(CooperativeRight.ReadAllWriteCompletedSealSupervisor.canSeal());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canSeal());
        assertTrue(CooperativeRight.ReadWriteTakeSealSupervisor.canSeal());
    }

    @Test
    public void testIsSupervisor() {
        assertFalse(CooperativeRight.None.isSupervisor());
        assertFalse(CooperativeRight.ReadOnly.isSupervisor());
        assertFalse(CooperativeRight.ReadSealed.isSupervisor());
        assertFalse(CooperativeRight.ReadCompleted.isSupervisor());
        assertFalse(CooperativeRight.ReadWriteCompleted.isSupervisor());
        assertFalse(CooperativeRight.ReadWrite.isSupervisor());
        assertFalse(CooperativeRight.ReadWriteTake.isSupervisor());
        assertFalse(CooperativeRight.ReadWriteSeal.isSupervisor());
        assertFalse(CooperativeRight.ReadWriteTakeSeal.isSupervisor());
        assertTrue(CooperativeRight.ReadCompletedSealSupervisor.isSupervisor());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.isSupervisor());
        assertTrue(CooperativeRight.ReadSealSupervisor.isSupervisor());
        assertTrue(CooperativeRight.ReadAllWriteCompletedSealSupervisor.isSupervisor());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.isSupervisor());
        assertTrue(CooperativeRight.ReadWriteTakeSealSupervisor.isSupervisor());
    }

    @Test
    public void testCanTake() {
        assertFalse(CooperativeRight.None.canTake());
        assertFalse(CooperativeRight.ReadOnly.canTake());
        assertFalse(CooperativeRight.ReadSealed.canTake());
        assertFalse(CooperativeRight.ReadCompleted.canTake());
        assertFalse(CooperativeRight.ReadWriteCompleted.canTake());
        assertFalse(CooperativeRight.ReadWrite.canTake());
        assertTrue(CooperativeRight.ReadWriteTake.canTake());
        assertFalse(CooperativeRight.ReadWriteSeal.canTake());
        assertTrue(CooperativeRight.ReadWriteTakeSeal.canTake());
        assertFalse(CooperativeRight.ReadCompletedSealSupervisor.canTake());
        assertFalse(CooperativeRight.ReadWriteCompletedSealSupervisor.canTake());
        assertFalse(CooperativeRight.ReadSealSupervisor.canTake());
        assertFalse(CooperativeRight.ReadAllWriteCompletedSealSupervisor.canTake());
        assertFalse(CooperativeRight.ReadWriteSealSupervisor.canTake());
        assertTrue(CooperativeRight.ReadWriteTakeSealSupervisor.canTake());
    }

    @Test
    public void testMerge() {
        Assertions.assertThat(CooperativeRight.ReadWriteCompleted.mergeRights(CooperativeRight.ReadSealSupervisor))
                .isEqualTo(CooperativeRight.ReadAllWriteCompletedSealSupervisor);
        Assertions.assertThat(CooperativeRight.ReadCompleted.mergeRights(CooperativeRight.ReadSealSupervisor))
                .isEqualTo(CooperativeRight.ReadSealSupervisor);
        Assertions.assertThat(CooperativeRight.ReadSealed.mergeRightFromStrings("2200"))
                .isEqualTo(CooperativeRight.ReadWriteCompleted);
    }

}
