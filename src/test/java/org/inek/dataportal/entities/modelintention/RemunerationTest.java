/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.entities.modelintention;

//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author schwarzst
 */
public class RemunerationTest {
    
    public RemunerationTest() {
    }
    
//    @BeforeClass
//    public static void setUpClass() {
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void testObjectsWithSameNonNullIdShallBeTreatedAsEqual() {
        System.out.println("testObjectsWithSameNonNullIdShallBeTreatedAsEqual");
        Remuneration instance = new Remuneration();
        instance.setId(new Long("123456"));
        instance.setText("Hallo_1");
        Remuneration other = new Remuneration();
        other.setId(new Long("123456"));
        other.setText("Hallo_2");
        assertTrue(instance.equals(other));
       // fail();
    }
    
    @Test
    public void testObjectsWithSameNullIdAndSameTypeAndIndicatorShallBeTreatedAsEqual() {
        System.out.println("testObjectsWithSameNullIdAndSameTypeAndIndicatorShallBeTreatedAsEqual");
        Remuneration instance = new Remuneration();
        instance.setId(new Long("1"));
         instance.setText("Equal");
        Remuneration other = new Remuneration();
        other.setId(new Long("1"));
        other.setText("Equal");
        assertTrue(instance.equals(other));
    }
}
