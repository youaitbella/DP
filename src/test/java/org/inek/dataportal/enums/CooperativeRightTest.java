package org.inek.dataportal.enums;

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
    public void testIsReadSealed() {
        System.out.println("isReadSealed");
    }

    @Test
    public void testCanReadAlways() {
        System.out.println("canReadAlways");
        assertFalse(CooperativeRight.None.canReadAlways());
        assertTrue(CooperativeRight.ReadOnly.canReadAlways());
        assertFalse(CooperativeRight.ReadSealed.canReadAlways());
        assertTrue(CooperativeRight.ReadWrite.canReadAlways());
        assertTrue(CooperativeRight.ReadWriteSeal.canReadAlways());
        assertFalse(CooperativeRight.ReadCompletedSealSupervisor.canReadAlways());
        assertFalse(CooperativeRight.ReadWriteCompletedSealSupervisor.canReadAlways());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canReadAlways());
    }

    @Test
    public void testCanReadCompleted() {
        System.out.println("canReadCompleted");
        assertFalse(CooperativeRight.None.canReadCompleted());
        assertTrue(CooperativeRight.ReadOnly.canReadCompleted());
        assertFalse(CooperativeRight.ReadSealed.canReadCompleted());
        assertTrue(CooperativeRight.ReadWrite.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteSeal.canReadCompleted());
        assertTrue(CooperativeRight.ReadCompletedSealSupervisor.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.canReadCompleted());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canReadCompleted());
    }

    @Test
    public void testCanReadSealed() {
        System.out.println("canReadSealed");
        assertFalse(CooperativeRight.None.canReadSealed());
        assertTrue(CooperativeRight.ReadOnly.canReadSealed());
        assertTrue(CooperativeRight.ReadSealed.canReadSealed());
        assertTrue(CooperativeRight.ReadWrite.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteSeal.canReadSealed());
        assertTrue(CooperativeRight.ReadCompletedSealSupervisor.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.canReadSealed());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canReadSealed());
    }

    @Test
    public void testCanWriteAlways() {
        System.out.println("canWriteAlways");
        assertFalse(CooperativeRight.None.canWriteAlways());
        assertFalse(CooperativeRight.ReadOnly.canWriteAlways());
        assertFalse(CooperativeRight.ReadSealed.canWriteAlways());
        assertTrue(CooperativeRight.ReadWrite.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteSeal.canWriteAlways());
        assertFalse(CooperativeRight.ReadCompletedSealSupervisor.canWriteAlways());
        assertFalse(CooperativeRight.ReadWriteCompletedSealSupervisor.canWriteAlways());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canWriteAlways());
    }

    @Test
    public void testCanWriteCompleted() {
        System.out.println("canWriteCompleted");
        assertFalse(CooperativeRight.None.canWriteCompleted());
        assertFalse(CooperativeRight.ReadOnly.canWriteCompleted());
        assertFalse(CooperativeRight.ReadSealed.canWriteCompleted());
        assertTrue(CooperativeRight.ReadWrite.canWriteCompleted());
        assertTrue(CooperativeRight.ReadWriteSeal.canWriteCompleted());
        assertFalse(CooperativeRight.ReadCompletedSealSupervisor.canWriteCompleted());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.canWriteCompleted());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canWriteCompleted());
    }

    @Test
    public void testSealWriteAlways() {
        System.out.println("canSealAlways");
        assertFalse(CooperativeRight.None.canSealAlways());
        assertFalse(CooperativeRight.ReadOnly.canSealAlways());
        assertFalse(CooperativeRight.ReadSealed.canSealAlways());
        assertFalse(CooperativeRight.ReadWrite.canSealAlways());
        assertTrue(CooperativeRight.ReadWriteSeal.canSealAlways());
        assertFalse(CooperativeRight.ReadCompletedSealSupervisor.canSealAlways());
        assertFalse(CooperativeRight.ReadWriteCompletedSealSupervisor.canSealAlways());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canSealAlways());
    }

    @Test
    public void testCanSealCompleted() {
        System.out.println("canSealCompleted");
        assertFalse(CooperativeRight.None.canSealCompleted());
        assertFalse(CooperativeRight.ReadOnly.canSealCompleted());
        assertFalse(CooperativeRight.ReadSealed.canSealCompleted());
        assertFalse(CooperativeRight.ReadWrite.canSealCompleted());
        assertTrue(CooperativeRight.ReadWriteSeal.canSealCompleted());
        assertTrue(CooperativeRight.ReadCompletedSealSupervisor.canSealCompleted());
        assertTrue(CooperativeRight.ReadWriteCompletedSealSupervisor.canSealCompleted());
        assertTrue(CooperativeRight.ReadWriteSealSupervisor.canSealCompleted());
    }

    
}
