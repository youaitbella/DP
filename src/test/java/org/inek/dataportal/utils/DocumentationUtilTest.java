package org.inek.dataportal.utils;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author muellermi
 */
public class DocumentationUtilTest {
    
    @Test
    public void testGetDocumentation() {
        System.out.println("getDocumentation");
        Object o = new TestEntity();
        List<KeyValueLevel> expResult = new ArrayList<>();
        expResult.add(new KeyValueLevel("Name", "myName", 0));
        expResult.add(new KeyValueLevel("Anzahl", "100", 0));
        List<KeyValueLevel> result = DocumentationUtil.getDocumentation(o);
        assertEquals(expResult, result);
    }
    
}

class TestEntity{
    @Documentation
    private String _name = "myName";
    
    @Documentation(name = "Anzahl")
    private int count=100;
}
