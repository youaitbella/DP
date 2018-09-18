package org.inek.dataportal.psy.entities.modelintention;

import org.inek.dataportal.psy.modelintention.entities.ModelLife;
import org.eclipse.persistence.internal.helper.Helper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author schlapapjo
 */
public class ModelLifeTest {
    
    @Test
    public void testObjectsWithSameNonNullIdShallBeTreatedAsEqual() {
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        ModelLife other = new ModelLife();
        other.setId(4711);
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithDifferentNonNullIdShallBeTreatedAsDifferent() {
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        ModelLife other = new ModelLife();
        other.setId(4712);
        assertFalse(instance.equals(other));
    }
    
    
    @Test
    public void testObjectsWithSameNonNullSameDateShallBeTreatedAsEqual() {
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
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        ModelLife other = new ModelLife();
        other.setId(4711);
        assertEquals(instance.hashCode(), other.hashCode());
    }
 
    @Test
    public void testObjectsWithSameNonNullIdAndSameDateShallHaveSameHash() {
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
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        ModelLife other = new ModelLife();
        other.setId(4712);
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithNonNullIdAndDifferentDateShallHaveDifferentHash() {
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
