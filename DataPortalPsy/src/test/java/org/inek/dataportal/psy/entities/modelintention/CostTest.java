/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.psy.entities.modelintention;

import org.inek.dataportal.psy.modelintention.entities.Cost;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author schwarzst
 */
public class CostTest {
    
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void testObjectsWithSameNonNullIdShallBeTreatedAsEqual() {
        System.out.println("testObjectsWithSameNonNullIdShallBeTreatedAsEqual");
        Cost instance = new Cost();
        instance.setId(1);
        instance.setRemunerationCode("Different_1");
        Cost other = new Cost();
        other.setId(1);
        other.setRemunerationCode("Different_2");
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithSameNonNullIdShallHaveSameHash() {
        System.out.println("testObjectsWithSameNonNullIdShallHaveSameHash");
        Cost instance = new Cost();
        instance.setId(1);
        instance.setRemunerationCode("Different_1");
        Cost other = new Cost();
        other.setId(1);
        other.setRemunerationCode("Different_2");
        assertEquals(instance.hashCode(), other.hashCode());
    }
    
    @Test
    public void testObjectsWithSameNullIdAndSameTypeAndIndicatorShallBeTreatedAsEqual() {
        System.out.println("testObjectsWithSameNullIdAndSameTypeAndIndicatorShallBeTreatedAsEqual");
        Cost instance = new Cost();
        instance.setIk(1);
        instance.setRemunerationCode("Equal");
        Cost other = new Cost();
        other.setIk(1);
        other.setRemunerationCode("Equal");
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithSameNullIdAndSameTypeAndIndicatorShallHaveSameHash() {
        System.out.println("testObjectsWithSameNullIdAndSameTypeAndIndicatorShallHaveSameHash");
        Cost instance = new Cost();
        instance.setIk(1);
        instance.setRemunerationCode("Equal");
        Cost other = new Cost();
        other.setIk(1);
        other.setRemunerationCode("Equal");
        assertEquals(instance.hashCode(), other.hashCode());
    }
    
    @Test
    public void testObjectsWithSameNullIdAndDifferentTypeShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithSameNullIdAndDifferentTypeShallBeTreatedAsDifferent");
        Cost instance = new Cost();
        instance.setIk(1);
        instance.setRemunerationCode("Equal");
        Cost other = new Cost();
        other.setIk(2);
        other.setRemunerationCode("Equal");
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithSameNullIdAndDifferentTypeShallHaveDifferentHash() {
        System.out.println("testObjectsWithSameNullIdAndDifferentTypeShallHaveDifferentHash");
        Cost instance = new Cost();
        instance.setIk(1);
        instance.setRemunerationCode("Equal");
        Cost other = new Cost();
        other.setIk(2);
        other.setRemunerationCode("Equal");
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithSameNullIdAndDifferentIndicatorShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithSameNullIdAndDifferentIndicatorShallBeTreatedAsDifferent");
        Cost instance = new Cost();
        instance.setIk(1);
        instance.setRemunerationCode("Different_1");
        Cost other = new Cost();
        other.setIk(2);
        other.setRemunerationCode("Different_2");
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithSameNullIdAndDifferentIndicatorShallHaveDifferentHash() {
        System.out.println("testObjectsWithSameNullIdAndDifferentIndicatorShallHaveDifferentHash");
        Cost instance = new Cost();
        instance.setIk(1);
        instance.setRemunerationCode("Different_1");
        Cost other = new Cost();
        other.setIk(2);
        other.setRemunerationCode("Different_2");
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithDifferentIdsShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithDifferentIdsShallBeTreatedAsDifferent");
        Cost instance = new Cost();
        instance.setId(1);
        instance.setIk(1);
        instance.setRemunerationCode("Equal");
        instance.setCostCenterId(1);
        instance.setCostTypeId(1);
        Cost other = new Cost();
        other.setId(2);
        other.setIk(1);
        other.setRemunerationCode("Equal");
        other.setCostCenterId(1);
        other.setCostTypeId(1);
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithDifferentIdsShallHaveDifferentHash() {
        System.out.println("testObjectsWithDifferentIdsShallBeTreatedAsDifferent");
        Cost instance = new Cost();
        instance.setId(1);
        instance.setIk(1);
        instance.setRemunerationCode("Equal");
        instance.setCostCenterId(1);
        instance.setCostTypeId(1);
        Cost other = new Cost();
        other.setId(2);
        other.setIk(1);
        other.setRemunerationCode("Equal");
        other.setCostCenterId(1);
        other.setCostTypeId(1);
        assertFalse(instance.hashCode() == other.hashCode());
    }
    
    @Test
    public void testObjectsWithNullIdAndOtherIdShallBeTreatedAsDifferent() {
        System.out.println("testObjectsWithNullIdAndOtherIdShallBeTreatedAsDifferent");
        Cost instance = new Cost();
        instance.setId(null);
        instance.setIk(1);
        instance.setRemunerationCode("Equal");
        instance.setCostCenterId(1);
        instance.setCostTypeId(1);
        Cost other = new Cost();
        other.setId(1);
        other.setIk(1);
        other.setRemunerationCode("Equal");
        other.setCostCenterId(1);
        other.setCostTypeId(1);
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testObjectsWithNullIdAndOtherIdShallHaveDifferentHash() {
        System.out.println("testObjectsWithNullIdAndOtherIdShallHaveDifferentHash");
        Cost instance = new Cost();
        instance.setId(null);
        instance.setIk(1);
        instance.setRemunerationCode("Equal");
        instance.setCostCenterId(1);
        instance.setCostTypeId(1);
        Cost other = new Cost();
        other.setId(1);
        other.setIk(1);
        other.setRemunerationCode("Equal");
        other.setCostCenterId(1);
        other.setCostTypeId(1);
        assertFalse(instance.hashCode() == other.hashCode());
    }
}
