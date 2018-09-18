package org.inek.dataportal.psy.entities.modelintention;

import org.inek.dataportal.psy.modelintention.entities.ModelIntentionContact;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author schlappajo
 */
public class ContactTest {
    @Test
    public void testObjectsWithSameNonNullIdShallBeTreatedAsEqual() {
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4711);
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithDifferentNonNullIdShallBeTreatedAsDifferent() {
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4712);
        assertFalse(instance.equals(other));
    }
    
    
    @Test
    public void testObjectsWithSameNonNullSameDateShallBeTreatedAsEqual() {
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
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        instance.setIk(123456789);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4712);
        other.setIk(123456789);
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithNullIdDifferentIKShallBeTreatedAsDifferent() {
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setIk(123456789);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setIk(123456780);
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithNullIdDifferentTypeShallBeTreatedAsDifferent() {
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setContactTypeId(1);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setContactTypeId(2);
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithNullIdDifferentNameShallBeTreatedAsDifferent() {
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setName("Test1");
        ModelIntentionContact other = new ModelIntentionContact();
        other.setName("Test2");
        assertFalse(instance.equals(other));
    }    
    
    
    
    
    @Test
    public void testObjectsWithSameNonNullIdShallHaveSameHash() {
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4711);
        assertEquals(instance.hashCode(), other.hashCode());
    }
 
    @Test
    public void testObjectsWithSameNonNullIdAndSameTypeShallHaveSameHash() {
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
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4712);
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithNonNullIdAndDifferentTypeShallHaveDifferentHash() {
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
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setId(4711);
        instance.setIk(123456789);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setId(4712);
        other.setIk(123456789);
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    public void testObjectsWithNullIdAndDifferentIKShallHaveDifferentHash() {
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setIk(123456789);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setIk(123456780);
        assertFalse(instance.hashCode() == other.hashCode());
    }
 
    @Test
    public void testObjectsWithNullIdAndDifferentTypeShallHaveDifferentHash() {
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setContactTypeId(1);
        ModelIntentionContact other = new ModelIntentionContact();
        other.setContactTypeId(2);
        assertFalse(instance.hashCode() == other.hashCode());
    }    

    @Test
    public void testObjectsWithNullIdAndDifferentNameShallHaveDifferentHash() {
        ModelIntentionContact instance = new ModelIntentionContact();
        instance.setName("Test1");
        ModelIntentionContact other = new ModelIntentionContact();
        other.setName("Test2");
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
}
