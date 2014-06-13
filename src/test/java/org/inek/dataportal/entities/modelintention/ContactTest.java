package org.inek.dataportal.entities.modelintention;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author schlappajo
 */
public class ContactTest {
    @Test
    public void testObjectsWithSameNonNullIdShallBeTreatedAsEqual() {
        System.out.println("testObjectsWithSameNonNullIdShallBeTreatedAsEqual");
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4711);
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithDifferentNonNullIdShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithDifferentNonNullIdShallBeTreatedAsDifferent");
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4712);
        assertFalse(instance.equals(other));
    }
    
    
    @Test
    public void testObjectsWithSameNonNullSameDateShallBeTreatedAsEqual() {
        System.out.println("testObjectsWithSameNonNullSameTypeShallBeTreatedAsEqual");
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        instance.setContactTypeId(1);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4711);
        other.setContactTypeId(1);
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithSameNonNullSameIKShallBeTreatedAsEqual() {
        System.out.println("testObjectsWithSameNonNullSameIKShallBeTreatedAsEqual");
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        instance.setIk(123456789);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4711);
        other.setIk(123456789);
        assertTrue(instance.equals(other));
    }
    
    
    @Test
    public void testObjectsWithNonNullIdDifferentDateShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithNonNullIdDifferentTypeShallBeTreatedAsDifferent");
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        instance.setContactTypeId(1);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4712);
        other.setContactTypeId(1);
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithNonNullIdDifferentIDShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithNonNullIdDifferentIDShallBeTreatedAsDifferent");
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        instance.setIk(123456789);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4712);
        other.setIk(123456789);
        assertFalse(instance.equals(other));
    }
    
    
    @Test
    public void testObjectsWithSameNonNullIdShallHaveSameHash() {
        System.out.println("testObjectsWithSameNonNullIdShallHaveSameHash");
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4711);
        assertEquals(instance.hashCode(), other.hashCode());
    }
 
    @Test
    public void testObjectsWithSameNonNullIdAndSameTypeShallHaveSameHash() {
        System.out.println("testObjectsWithSameNonNullIdAndSameTypeShallHaveSameHash");
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        instance.setContactTypeId(1);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4711);
        other.setContactTypeId(1);
        assertEquals(instance.hashCode(), other.hashCode());
    }
    
    @Test
    public void testObjectsWithSameNonNullIdAndSameIKShallHaveSameHash() {
        System.out.println("testObjectsWithSameNonNullIdAndSameTypeShallHaveSameHash");
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        instance.setIk(123456789);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4711);
        other.setIk(123456789);
        assertEquals(instance.hashCode(), other.hashCode());
    }
    
    
    @Test
    public void testObjectsWithDifferentNonNullIdShallHaveDifferentHash() {
        System.out.println("testObjectsWithDifferentNonNullIdShallHaveDifferentHash");
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4712);
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithNonNullIdAndDifferentTypeShallHaveDifferentHash() {
        System.out.println("testObjectsWithNonNullIdAndDifferentTypeShallHaveDifferentHash");
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        instance.setContactTypeId(1);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4712);
        other.setContactTypeId(1);
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithNonNullIdAndDifferentIKShallHaveDifferentHash() {
        System.out.println("testObjectsWithNonNullIdAndDifferentIKShallHaveDifferentHash");
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        instance.setIk(123456789);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4712);
        other.setIk(123456789);
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    //todo: Insert tests for null ids. 
    
}
