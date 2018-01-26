package org.inek.dataportal.utils;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author muellermi
 */
public class DocumentationUtilTest2 {

    //@Test
    public void testTranslation0() {
        System.out.println("testTranslation1");
        TranslateEntity entity = new TranslateEntity();
        entity._toy = 0;
        List<KeyValueLevel<?, ?>> expResult = new ArrayList<>();
        expResult.add(new KeyValueLevel<>("toy", "", 0));
        List<KeyValueLevel> result = DocumentationUtil.getDocumentation(entity);
        System.out.println(result.size());
        assertEquals(expResult, result);
    }

    //@Test
    public void testTranslation1() {
        System.out.println("testTranslation1");
        TranslateEntity entity = new TranslateEntity();
        entity._toy = 0;
        List<KeyValueLevel<?, ?>> expResult = new ArrayList<>();
        expResult.add(new KeyValueLevel<>("toy", "puppet", 0));
        List<KeyValueLevel> result = DocumentationUtil.getDocumentation(entity);
        System.out.println(result.size());
        assertEquals(expResult, result);
    }

    //@Test
    public void testTranslation2() {
        System.out.println("testTranslation1");
        TranslateEntity entity = new TranslateEntity();
        entity._toy = 0;
        List<KeyValueLevel<?, ?>> expResult = new ArrayList<>();
        expResult.add(new KeyValueLevel<>("toy", "playstation", 0));
        List<KeyValueLevel> result = DocumentationUtil.getDocumentation(entity);
        System.out.println(result.size());
        assertEquals(expResult, result);
    }

    //@Test
    public void testTranslation3() {
        System.out.println("testTranslation1");
        TranslateEntity entity = new TranslateEntity();
        entity._toy = 0;
        List<KeyValueLevel<?, ?>> expResult = new ArrayList<>();
        expResult.add(new KeyValueLevel<>("toy", "", 0));
        List<KeyValueLevel> result = DocumentationUtil.getDocumentation(entity);
        System.out.println(result.size());
        assertEquals(expResult, result);
    }

}

class TranslateEntity {

    @Documentation(name = "toy", translateValue = "translateToy()")
    public int _toy;

    private String translateToy(int id) {
        switch (id) {
            case 1: return "puppet";
            case 2: return "playstation";
            default: return "";
        }
    }
}
