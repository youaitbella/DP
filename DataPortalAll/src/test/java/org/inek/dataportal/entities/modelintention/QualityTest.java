package org.inek.dataportal.entities.modelintention;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author muellermi
 */
public class QualityTest {
    
    @Test
    public void testObjectsWithSameNonNullIdShallBeTreatedAsEqual() {
        System.out.println("testObjectsWithSameNonNullIdShallBeTreatedAsEqual");
        Quality instance = new Quality();
        instance.setId(4711);
        instance.setIndicator("instance");
        Quality other = new Quality();
        other.setId(4711);
        other.setIndicator("other");
        assertTrue(instance.equals(other));
    }

    @Test
    public void testObjectsWithSameNonNullIdShallHaveSameHash() {
        System.out.println("testObjectsWithSameNonNullIdShallHaveSameHash");
        Quality instance = new Quality();
        instance.setId(4711);
        instance.setIndicator("instance");
        Quality other = new Quality();
        other.setId(4711);
        other.setIndicator("other");
        assertEquals(instance.hashCode(), other.hashCode());
    }

    @Test
    public void testObjectsWithSameNullIdAndSameTypeAndIndicatorShallBeTreatedAsEqual() {
        System.out.println("testObjectsWithSameNullIdAndSameTypeAndIndicatorShallBeTreatedAsEqual");
        Quality instance = new Quality();
        instance.setTypeId(1);
        instance.setIndicator("indicator");
        Quality other = new Quality();
        other.setTypeId(1);
        other.setIndicator("indicator");
        assertTrue(instance.equals(other));
    }

    @Test
    public void testObjectsWithSameNullIdAndSameTypeAndIndicatorShallHaveSameHash() {
        System.out.println("testObjectsWithSameNullIdAndSameTypeAndIndicatorShallHaveSameHash");
        Quality instance = new Quality();
        instance.setTypeId(1);
        instance.setIndicator("indicator");
        Quality other = new Quality();
        other.setTypeId(1);
        other.setIndicator("indicator");
        assertEquals(instance.hashCode(), other.hashCode());
    }

    @Test
    public void testObjectsWithSameNullIdAndDifferentTypeShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithSameNullIdAndDifferentTypeShallBeTreatedAsDifferent");
        Quality instance = new Quality();
        instance.setTypeId(1);
        instance.setIndicator("indicator");
        Quality other = new Quality();
        other.setTypeId(2);
        other.setIndicator("indicator");
        assertFalse(instance.equals(other));
    }

    @Test
    public void testObjectsWithSameNullIdAndDifferentTypeShallHaveDifferentHash() {
        System.out.println("testObjectsWithSameNullIdAndDifferentTypeShallHaveDifferentHash");
        Quality instance = new Quality();
        instance.setTypeId(1);
        instance.setIndicator("indicator");
        Quality other = new Quality();
        other.setTypeId(2);
        other.setIndicator("indicator");
        assertFalse(instance.hashCode() == other.hashCode());
    }

    @Test
    public void testObjectsWithSameNullIdAndDifferentIndicatorShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithSameNullIdAndDifferentIndicatorShallBeTreatedAsDifferent");
        Quality instance = new Quality();
        instance.setTypeId(1);
        instance.setIndicator("indicator");
        Quality other = new Quality();
        other.setTypeId(1);
        other.setIndicator("other");
        assertFalse(instance.equals(other));
    }

    @Test
    public void testObjectsWithSameNullIdAndDifferentIndicatorShallHaveDifferentHash() {
        System.out.println("testObjectsWithSameNullIdAndDifferentIndicatorShallHaveDifferentHash");
        Quality instance = new Quality();
        instance.setTypeId(1);
        instance.setIndicator("indicator");
        Quality other = new Quality();
        other.setTypeId(1);
        other.setIndicator("other");
        assertFalse(instance.hashCode() == other.hashCode());
    }

    @Test
    public void testObjectsWithDifferentIdsShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithDifferentIdsShallBeTreatedAsDifferent");
        Quality instance = new Quality();
        instance.setId(4711);
        instance.setTypeId(1);
        instance.setIndicator("indicator");
        Quality other = new Quality();
        other.setId(1);
        other.setTypeId(1);
        other.setIndicator("other");
        assertFalse(instance.equals(other));
    }

    @Test
    public void testObjectsWithDifferentIdsShallHaveDifferentHash() {
        System.out.println("testObjectsWithDifferentIdsShallBeTreatedAsDifferent");
        Quality instance = new Quality();
        instance.setId(4711);
        instance.setTypeId(1);
        instance.setIndicator("indicator");
        Quality other = new Quality();
        other.setId(1);
        other.setTypeId(1);
        other.setIndicator("other");
        assertFalse(instance.hashCode() == other.hashCode());
    }

    @Test
    public void testObjectsWithNullIdAndOtherIdShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithNullIdAndOtherIdShallBeTreatedAsDifferent");
        Quality instance = new Quality();
        instance.setId(null);
        instance.setTypeId(1);
        instance.setIndicator("indicator");
        Quality other = new Quality();
        other.setId(1);
        other.setTypeId(1);
        other.setIndicator("other");
        assertFalse(instance.equals(other));
    }

    @Test
    public void testObjectsWithNullIdAndOtherIdShallHaveDifferentHash() {
        System.out.println("testObjectsWithNullIdAndOtherIdShallHaveDifferentHash");
        Quality instance = new Quality();
        instance.setId(null);
        instance.setTypeId(1);
        instance.setIndicator("indicator");
        Quality other = new Quality();
        other.setId(1);
        other.setTypeId(1);
        other.setIndicator("other");
        assertFalse(instance.hashCode() == other.hashCode());
    }

    
}
