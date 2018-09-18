/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.utils;

import org.inek.dataportal.common.utils.StringUtil;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author muellermi
 */
public class StringUtilTest {
    
    public StringUtilTest() {
    }

    @Test
    public void testIsNullOrEmpty01() {
        assertEquals(true, StringUtil.isNullOrEmpty(null));
    }

    @Test
    public void testIsNullOrEmpty02() {
        assertEquals(true, StringUtil.isNullOrEmpty(""));
    }

    @Test
    public void testIsNullOrEmpty03() {
        assertEquals(false, StringUtil.isNullOrEmpty("x"));
    }

    @Test
    public void testSplitAtUnquotedSemicolon01() {
        String line = "";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {""};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon02() {
        String line = "1;2;3";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"1", "2", "3"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon03() {
        String line = "\"1\";2;3";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"1", "2", "3"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon04() {
        String line = "\"1;2\";3";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"1;2", "3"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon05() {
        String line = "\"1;2;3";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"\"1;2;3"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon06() {
        String line = "\"1;2;\"3";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"\"1;2;\"3"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon07() {
        String line = "\"Hi; this is me\";2";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"Hi; this is me", "2"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon08() {
        String line = "\"Hi; this is \"me;2";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"\"Hi; this is \"me;2"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon09() {
        String line = "Hi; this is \"me;2";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"Hi", " this is \"me", "2"};
        assertArrayEquals(expResult, result);
    }
    
    @Test
    public void testParseLocalizedDoubleAsInt() {
    }

    @Test
    public void testParseLocalizedDouble() {
    }

    @Test
    public void testGetSqlFilter01() {
        assertEquals("'123456789'", StringUtil.getSqlFilter("123456789"));
    }
    
    @Test
    public void testGetSqlFilter02() {
        assertEquals("'%abc%'", StringUtil.getSqlFilter("abc"));
    }
    
    @Test
    public void testGetSqlFilter03() {
        assertEquals("'abc%'", StringUtil.getSqlFilter("abc%"));
    }
    
    @Test
    public void testGetSqlFilter04() {
        assertEquals("'ab%c'", StringUtil.getSqlFilter("'ab'%c"));
    }
    
}
