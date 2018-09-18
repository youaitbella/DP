/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.psy.entities.modelintention;

import org.inek.dataportal.psy.modelintention.entities.Adjustment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 *
 * @author schwarzst
 */
public class AdjustmentTest {
    
        // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void testObjectsWithSameNonNullIdShallBeTreatedAsEqual() {
        Adjustment instance = new Adjustment();
        instance.setId(1);
        instance.setAdjustmentTypeId(1);
        Adjustment other = new Adjustment();
        other.setId(1);
        other.setAdjustmentTypeId(2);
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithSameNonNullIdShallHaveSameHash() {
        Adjustment instance = new Adjustment();
        instance.setId(1);
        instance.setAdjustmentTypeId(1);
        Adjustment other = new Adjustment();
        other.setId(1);
        other.setAdjustmentTypeId(2);
        assertEquals(instance.hashCode(), other.hashCode());
    }
    
    @Test 
    public void testObjectsWithSameNullIdAndSameTypeAndIndicatorShallBeTreatedAsEqual() {
        Date date = java.util.Calendar.getInstance().getTime();
        Adjustment instance = new Adjustment();
        instance.setAdjustmentTypeId(1);
        instance.setDateFrom(date);
        Adjustment other = new Adjustment();
        other.setAdjustmentTypeId(1);
        other.setDateFrom(date);
        assertTrue(instance.equals(other));
    }
    
    @Test 
    public void testObjectsWithSameNullIdAndSameTypeAndIndicatorShallHaveSameHash() {
        Date date = java.util.Calendar.getInstance().getTime();
        Adjustment instance = new Adjustment();
        instance.setAdjustmentTypeId(1);
        instance.setDateFrom(date);
        Adjustment other = new Adjustment();
        other.setAdjustmentTypeId(1);
        other.setDateFrom(date);
        assertEquals(instance.hashCode(), other.hashCode());
    }
    
    @Test
    public void testObjectsWithSameNullIdAndDifferentTypeShallBeTreatedAsDifferent() {
        Date date = java.util.Calendar.getInstance().getTime();
        Adjustment instance = new Adjustment();
        instance.setAdjustmentTypeId(1);
        instance.setDateFrom(date);
        Adjustment other = new Adjustment();
        other.setAdjustmentTypeId(2);
        other.setDateFrom(date);
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithSameNullIdAndDifferentTypeShallHaveDifferentHash() {
        Date date = java.util.Calendar.getInstance().getTime();
        Adjustment instance = new Adjustment();
        instance.setAdjustmentTypeId(1);
        instance.setDateFrom(date);
        Adjustment other = new Adjustment();
        other.setAdjustmentTypeId(2);
        other.setDateFrom(date);
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithSameNullIdAndDifferentIndicatorShallBeTreatedAsDifferent() {
        Date date1 = java.util.Calendar.getInstance().getTime();
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date()); 
        c.add(java.util.Calendar.DATE, 5);
        Date date2 = c.getTime();
        Adjustment instance = new Adjustment();
        instance.setAdjustmentTypeId(1);
        instance.setDateFrom(date1);
        Adjustment other = new Adjustment();
        other.setAdjustmentTypeId(1);
        other.setDateFrom(date2);
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithSameNullIdAndDifferentIndicatorShallHaveDifferentHash() {
        Date date1 = java.util.Calendar.getInstance().getTime();
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date()); 
        c.add(java.util.Calendar.DATE, 5);
        Date date2 = c.getTime();
        Adjustment instance = new Adjustment();
        instance.setAdjustmentTypeId(1);
        instance.setDateFrom(date1);
        Adjustment other = new Adjustment();
        other.setAdjustmentTypeId(1);
        other.setDateFrom(date2);
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithDifferentIdsShallBeTreatedAsDifferent() {
        Date date = java.util.Calendar.getInstance().getTime();
        Adjustment instance = new Adjustment();
        instance.setId(1);
        instance.setAdjustmentTypeId(1);
        instance.setDateFrom(date);
        Adjustment other = new Adjustment();
        other.setId(2);
        other.setAdjustmentTypeId(1);
        other.setDateFrom(date);
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithDifferentIdsShallHaveDifferentHash() {
        Date date = java.util.Calendar.getInstance().getTime();
        Adjustment instance = new Adjustment();
        instance.setId(1);
        instance.setAdjustmentTypeId(1);
        instance.setDateFrom(date);
        Adjustment other = new Adjustment();
        other.setId(2);
        other.setAdjustmentTypeId(1);
        other.setDateFrom(date);
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithNullIdAndOtherIdShallBeTreatedAsDifferent() {
        Date date = java.util.Calendar.getInstance().getTime();
        Adjustment instance = new Adjustment();
        instance.setId(null);
        instance.setAdjustmentTypeId(1);
        instance.setDateFrom(date);
        instance.setDateTo(date);
        Adjustment other = new Adjustment();
        other.setId(1);
        other.setAdjustmentTypeId(1);
        other.setDateFrom(date);
        other.setDateTo(date);
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithNullIdAndOtherIdShallHaveDifferentHash() {
        Date date = java.util.Calendar.getInstance().getTime();
        Adjustment instance = new Adjustment();
        instance.setId(null);
        instance.setAdjustmentTypeId(1);
        instance.setDateFrom(date);
        instance.setDateTo(date);
        Adjustment other = new Adjustment();
        other.setId(1);
        other.setAdjustmentTypeId(1);
        other.setDateFrom(date);
        other.setDateTo(date);
        assertFalse(instance.hashCode() == other.hashCode());
    }
}
