package org.inek.dataportal.entities.modelintention;

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
        System.out.println("testObjectsWithSameNonNullIdShallBeTreatedAsEqual");
        ModelLife instance = new ModelLife();
        instance.setId(4711);
        
        ModelLife other = new ModelLife();
        other.setId(4711);
        
        assertTrue(instance.equals(other));
    }
    
    
}
