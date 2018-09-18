/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.psy.entities.modelintention;

import org.inek.dataportal.psy.modelintention.entities.Remuneration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author schwarzst
 */
public class RemunerationTest {
    
    public RemunerationTest() {
    }
    
    
    @Test
    public void testObjectsWithSameNonNullIdShallBeTreatedAsEqual() {
        Remuneration instance = new Remuneration();
        instance.setId(new Long("123456"));
        instance.setText("Hallo_1");
        Remuneration other = new Remuneration();
        other.setId(new Long("123456"));
        other.setText("Hallo_2");
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithSameNonNullIdShallHaveSameHash() {
        Remuneration instance = new Remuneration();
        instance.setId(new Long("123456"));
        instance.setText("Hallo_1");
        Remuneration other = new Remuneration();
        other.setId(new Long("123456"));
        other.setText("Hallo_2");
        assertEquals(instance.hashCode(), other.hashCode());
    }
    
    @Test
    public void testObjectsWithSameNullIdAndSameTypeAndIndicatorShallBeTreatedAsEqual() {
        Remuneration instance = new Remuneration();
        instance.setCode("A888999A");
        instance.setText("Equal");
        Remuneration other = new Remuneration();
        other.setCode("A888999A");
        other.setText("Equal");
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithSameNullIdAndSameTypeAndIndicatorShallHaveSameHash() {
        Remuneration instance = new Remuneration();
        instance.setCode("A888999A");
        instance.setText("Equal");
        Remuneration other = new Remuneration();
        other.setCode("A888999A");
        other.setText("Equal");
        assertEquals(instance.hashCode(), other.hashCode());
    }
    
    @Test
    public void testObjectsWithSameNullIdAndDifferentTypeShallBeTreatedAsDifferent() {
        Remuneration instance = new Remuneration();
        instance.setCode("A888999A");
        instance.setText("Equal");
        Remuneration other = new Remuneration();
        other.setCode("A888999B");
        other.setText("Equal");
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithSameNullIdAndDifferentTypeShallHaveDifferentHash() {
        Remuneration instance = new Remuneration();
        instance.setCode("A888999A");
        instance.setText("Equal");
        Remuneration other = new Remuneration();
        other.setCode("A888999B");
        other.setText("Equal");
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithSameNullIdAndDifferentIndicatorShallBeTreatedAsDifferent() {
        Remuneration instance = new Remuneration();
        instance.setCode("A888999A");
        instance.setText("Different_1");
        Remuneration other = new Remuneration();
        other.setCode("A888999A");
        other.setText("Different_2");
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithSameNullIdAndDifferentIndicatorShallHaveDifferentHash() {
        Remuneration instance = new Remuneration();
        instance.setCode("A888999A");
        instance.setText("Different_1");
        Remuneration other = new Remuneration();
        other.setCode("A888999A");
        other.setText("Different_2");
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithDifferentIdsShallBeTreatedAsDifferent() {
        // abgeändert
        Remuneration instance = new Remuneration();
        instance.setId(new Long("123456"));
        instance.setCode("A888999A");
        instance.setText("Equal");
        Remuneration other = new Remuneration();
        instance.setId(new Long("234567"));
        other.setCode("A888999A");
        other.setText("Equal");
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithDifferentIdsShallHaveDifferentHash() {
        Remuneration instance = new Remuneration();
        instance.setId(new Long("123456"));
        instance.setCode("A888999A");
        instance.setText("Equal");
        Remuneration other = new Remuneration();
        instance.setId(new Long("234567"));
        other.setCode("A888999A");
        other.setText("Equal");
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithNullIdAndOtherIdShallBeTreatedAsDifferent() {
        Remuneration instance = new Remuneration();
        instance.setId(null);
        instance.setCode("A888999A");
        instance.setText("Equal");
        Remuneration other = new Remuneration();
        instance.setId(new Long("234567"));
        other.setCode("A888999A");
        other.setText("Equal");
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithNullIdAndOtherIdShallHaveDifferentHash() {
        Remuneration instance = new Remuneration();
        instance.setId(null);
        instance.setCode("A888999A");
        instance.setText("Equal");
        Remuneration other = new Remuneration();
        instance.setId(new Long("234567"));
        other.setCode("A888999A");
        other.setText("Equal");
        assertFalse(instance.hashCode() == other.hashCode());
    }
}
