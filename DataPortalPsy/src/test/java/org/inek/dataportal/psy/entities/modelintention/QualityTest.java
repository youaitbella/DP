package org.inek.dataportal.psy.entities.modelintention;

import org.inek.dataportal.psy.modelintention.entities.Quality;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author muellermi
 */
public class QualityTest {
    
    @Test
    public void testObjectsWithSameNonNullIdShallBeTreatedAsEqual() {
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
