package org.inek.dataprtal.common.enums;

import org.inek.dataportal.common.enums.CooperativeRight;
import org.hamcrest.core.Is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author muellermi
 */
public class CooperativeRightTest {

    public CooperativeRightTest() {
    }

    @Test
    public void assertExcpectedEnumCount() {
        System.out.println("assertExcpectedEnumCount");
        assertThat(CooperativeRight.values().length, Is.is(15));
    }

    @Test
    public void testCanReadAlways() {
        System.out.println("canReadAlways");
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
        System.out.println("canReadCompleted");
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
        System.out.println("canReadSealed");
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
        System.out.println("canWriteAlways");
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
        System.out.println("canWriteCompleted");
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
        System.out.println("canSeal");
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
        System.out.println("canSeal");
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
        System.out.println("canTake");
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
        System.out.println("testMerge");
        assertThat(CooperativeRight.ReadWriteCompleted.mergeRights(CooperativeRight.ReadSealSupervisor), Is.is(CooperativeRight.ReadAllWriteCompletedSealSupervisor));
        assertThat(CooperativeRight.ReadCompleted.mergeRights(CooperativeRight.ReadSealSupervisor), Is.is(CooperativeRight.ReadSealSupervisor));
        assertThat(CooperativeRight.ReadSealed.mergeRightFromStrings("2200"), Is.is(CooperativeRight.ReadWriteCompleted));
    }

}
