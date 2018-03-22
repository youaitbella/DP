package org.inek.dataportal.psy.entities.modelintention;

import org.inek.dataportal.psy.modelintention.entities.ModelLife;
import org.eclipse.persistence.internal.helper.Helper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author schlapapjo
 */
public class ModelLifeTest {
    
    @Test
    public void testObjectsWithSameNonNullIdShallBeTreatedAsEqual() {
        System.out.println("testOöbjectsWithSameNonNullIdShallBeTreatedAsEqual");
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        ModelLife other = new ModelLife();
        other.setId(4711);
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithDifferentNonNullIdShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithDifferentNonNullIdShallBeTreatedAsDifferent");
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        ModelLife other = new ModelLife();
        other.setId(4712);
        assertFalse(instance.equals(other));
    }
    
    
    @Test
    public void testObjectsWithSameNonNullSameDateShallBeTreatedAsEqual() {
        System.out.println("testObjectsWithSameNonNullSameDateShallBeTreatedAsEqual");
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        instance.setStartDate(Helper.dateFromString("2014-01-01"));
        ModelLife other = new ModelLife();
        other.setId(4711);
        other.setStartDate(Helper.dateFromString("2014-01-01"));
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithNonNullIdDifferentDateShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithNonNullIdDifferentDateShallBeTreatedAsDifferent");
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        instance.setStartDate(Helper.dateFromString("2014-01-01"));
        ModelLife other = new ModelLife();
        other.setId(4712);
        other.setStartDate(Helper.dateFromString("2014-02-01"));
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithSameNonNullIdSameDateSameDurationShallBeTreatedAsEqual() {
        System.out.println("testObjectsWithSameNonNullIdSameDateSameDurationShallBeTreatedAsEqual");
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        instance.setStartDate(Helper.dateFromString("2014-01-01"));
        instance.setMonthDuration(5);
        ModelLife other = new ModelLife();
        other.setId(4711);
        other.setStartDate(Helper.dateFromString("2014-01-01"));
        other.setMonthDuration(5);
        assertTrue(instance.equals(other));
    }
       
    @Test
    public void testObjectsWithSameNonNullIdDifferentDateSameDurationShallBeTreatedAsEqual() {
        System.out.println("testObjectsWithSameNonNullIdDifferentDateSameDurationShallBeTreatedAsEqual");
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        instance.setStartDate(Helper.dateFromString("2014-01-01"));
        instance.setMonthDuration(5);
        ModelLife other = new ModelLife();
        other.setId(4711);
        other.setStartDate(Helper.dateFromString("2014-02-01"));
        other.setMonthDuration(5);
        assertTrue(instance.equals(other));
    }
       
    
    @Test
    public void testObjectsWithNullIdDifferentDateSameDurationShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithNullIdDifferentDateSameDurationShallBeTreatedAsDifferent");
        ModelLife instance = new ModelLife();
        instance.setStartDate(Helper.dateFromString("2014-01-01"));
        instance.setMonthDuration(5);
        ModelLife other = new ModelLife();
        other.setStartDate(Helper.dateFromString("2014-02-01"));
        other.setMonthDuration(5);
        assertFalse(instance.equals(other));
    }
    
    
    
    @Test
    public void testObjectsWithSameNonNullIdShallHaveSameHash() {
        System.out.println("testObjectsWithSameNonNullIdShallHaveSameHash");
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        ModelLife other = new ModelLife();
        other.setId(4711);
        assertEquals(instance.hashCode(), other.hashCode());
    }
 
    @Test
    public void testObjectsWithSameNonNullIdAndSameDateShallHaveSameHash() {
        System.out.println("testObjectsWithSameNonNullIdAndSameDurationShallHaveSameHash");
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        instance.setStartDate(Helper.dateFromString("2014-01-01"));
        ModelLife other = new ModelLife();
        other.setId(4711);
        other.setStartDate(Helper.dateFromString("2014-01-01"));
        assertEquals(instance.hashCode(), other.hashCode());
    }
    
    @Test
    public void testObjectsWithDifferentNonNullIdShallHaveDifferentHash() {
        System.out.println("testObjectsWithDifferentNonNullIdShallHaveDifferentHash");
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        ModelLife other = new ModelLife();
        other.setId(4712);
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithNonNullIdAndDifferentDateShallHaveDifferentHash() {
        System.out.println("testObjectsWithNonNullIdAndDifferentDateShallHaveDifferentHash");
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        instance.setStartDate(Helper.dateFromString("2014-01-01"));
        ModelLife other = new ModelLife();
        other.setId(4712);
        other.setStartDate(Helper.dateFromString("2014-02-01"));
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithSameNonNullIdAndSameDateSameDurationShallHaveSameHash() {
        System.out.println("testObjectsWithSameNonNullIdAndSameDateSameDurationShallHaveSameHash");
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        instance.setStartDate(Helper.dateFromString("2014-01-01"));
        instance.setMonthDuration(5);
        ModelLife other = new ModelLife();
        other.setId(4711);
        other.setStartDate(Helper.dateFromString("2014-01-01"));
        other.setMonthDuration(5);
        assertEquals(instance.hashCode(), other.hashCode());
    }    
    
    @Test
    public void testObjectsWithNonNullIdAndDifferentDateSameDurationShallHaveDifferentHash() {
        System.out.println("testObjectsWithNonNullIdAndDifferentDateSameDurationShallHaveDifferentHash");
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        instance.setStartDate(Helper.dateFromString("2014-01-01"));
        instance.setMonthDuration(5);
        ModelLife other = new ModelLife();
        other.setId(4712);
        other.setStartDate(Helper.dateFromString("2014-02-01"));
        other.setMonthDuration(5);
        assertFalse(instance.hashCode() == other.hashCode());
    }  
 
    @Test
    public void testObjectsWithNullIdAndDifferentDateSameDurationShallHaveDifferentHash() {
        System.out.println("testObjectsWithNullIdAndDifferentDateSameDurationShallHaveDifferentHash");
        ModelLife instance = new ModelLife();
        instance.setStartDate(Helper.dateFromString("2014-01-01"));
        instance.setMonthDuration(5);
        ModelLife other = new ModelLife();
        other.setStartDate(Helper.dateFromString("2014-02-01"));
        other.setMonthDuration(5);
        assertFalse(instance.hashCode() == other.hashCode());
    }    
    
    //todo: Insert tests for null ids. 
    
}
