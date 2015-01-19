package org.inek.dataportal.enums;

import org.hamcrest.core.Is;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertThat(CooperativeRight.values().length, Is.is(12));
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
        assertTrue(CooperativeRight.ReadWriteSeal.canReadAlways());
        assertFalse(CooperativeRight.ReadCompletedSealSupervisor.canReadAlways());
        assertFalse(CooperativeRight.ReadWriteCompletedSealSupervisor.canReadAlways());
        assertTrue(CooperativeRight.ReadSealSupervisor.canReadAlways());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canReadAlways());
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
        assertTrue(CooperativeRight.ReadWriteSeal.canReadCompleted());
        assertTrue(CooperativeRight.ReadCompletedSealSupervisor.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.canReadCompleted());
        assertTrue(CooperativeRight.ReadSealSupervisor.canReadCompleted());
        assertTrue(CooperativeRight.ReadAllWriteCompletedSealSupervisor.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canReadCompleted());
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
        assertTrue(CooperativeRight.ReadWriteSeal.canReadSealed());
        assertTrue(CooperativeRight.ReadCompletedSealSupervisor.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.canReadSealed());
        assertTrue(CooperativeRight.ReadSealSupervisor.canReadSealed());
        assertTrue(CooperativeRight.ReadAllWriteCompletedSealSupervisor.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canReadSealed());
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
        assertTrue(CooperativeRight.ReadWriteSeal.canWriteAlways());
        assertFalse(CooperativeRight.ReadCompletedSealSupervisor.canWriteAlways());
        assertFalse(CooperativeRight.ReadWriteCompletedSealSupervisor.canWriteAlways());
        assertFalse(CooperativeRight.ReadSealSupervisor.canWriteAlways());
        assertFalse(CooperativeRight.ReadAllWriteCompletedSealSupervisor.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canWriteAlways());
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
        assertTrue(CooperativeRight.ReadWriteSeal.canWriteAlways());
        assertFalse(CooperativeRight.ReadCompletedSealSupervisor.canWriteCompleted());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.canWriteCompleted());
        assertFalse(CooperativeRight.ReadSealSupervisor.canWriteCompleted());
        assertTrue(CooperativeRight.ReadAllWriteCompletedSealSupervisor.canWriteCompleted());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canWriteCompleted());
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
        assertTrue(CooperativeRight.ReadWriteSeal.canSeal());
        assertTrue(CooperativeRight.ReadCompletedSealSupervisor.canSeal());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.canSeal());
        assertTrue(CooperativeRight.ReadSealSupervisor.canSeal());
        assertTrue(CooperativeRight.ReadAllWriteCompletedSealSupervisor.canSeal());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canSeal());
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
        assertFalse(CooperativeRight.ReadWriteSeal.isSupervisor());
        assertTrue(CooperativeRight.ReadCompletedSealSupervisor.isSupervisor());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.isSupervisor());
        assertTrue(CooperativeRight.ReadSealSupervisor.isSupervisor());
        assertTrue(CooperativeRight.ReadAllWriteCompletedSealSupervisor.isSupervisor());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.isSupervisor());
    }

    @Test
    public void testMerge(){
        System.out.println("testMerge");
        assertThat(CooperativeRight.ReadWriteCompleted.mergeRights(CooperativeRight.ReadSealSupervisor), Is.is(CooperativeRight.ReadAllWriteCompletedSealSupervisor));
        assertThat(CooperativeRight.ReadCompleted.mergeRights(CooperativeRight.ReadSealSupervisor), Is.is(CooperativeRight.ReadSealSupervisor));
        assertThat(CooperativeRight.ReadSealed.mergeRightFromStrings("220"), Is.is(CooperativeRight.ReadWriteCompleted));
    }

    
}
