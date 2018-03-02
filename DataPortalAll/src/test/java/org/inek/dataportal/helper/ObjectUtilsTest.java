/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper;

import org.inek.dataportal.common.helper.ObjectUtils;
import java.util.Map;
import org.inek.dataportal.entities.nub.NubRequest;
import org.inek.dataportal.common.helper.structures.FieldValues;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author muellermi
 */
public class ObjectUtilsTest {
    
    public ObjectUtilsTest() {
    }

    @Test
    public void newRequestsHaveNoDifferences() {
        NubRequest r1 = new NubRequest();
        NubRequest r2 = new NubRequest();
        Map<String, FieldValues> differences = ObjectUtils.getDifferences(r1, r2);
        assertTrue(differences.isEmpty());
    }
    
    @Test
    public void RequestsWithSameContentHaveNoDifferences() {
        NubRequest r1 = new NubRequest();
        r1.setName("test");
        NubRequest r2 = new NubRequest();
        r2.setName("test");
        Map<String, FieldValues> differences = ObjectUtils.getDifferences(r1, r2);
        assertTrue(differences.isEmpty());
    }
    
    @Test
    public void RequestsWithDifferntContentHaveDifferences() {
        NubRequest r1 = new NubRequest();
        r1.setName("test1");
        NubRequest r2 = new NubRequest();
        r2.setName("test2");
        Map<String, FieldValues> differences = ObjectUtils.getDifferences(r1, r2);
        assertEquals(1, differences.size());
        assertTrue(differences.keySet().contains("_name"));
        assertEquals("test1", differences.get("_name").getValue1());
        assertEquals("test2", differences.get("_name").getValue2());
    }
    
}
