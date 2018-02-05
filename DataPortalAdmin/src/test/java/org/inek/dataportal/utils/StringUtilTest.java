/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.utils;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author muellermi
 */
public class StringUtilTest {
    
    public StringUtilTest() {
    }

    @Test
    public void testIsNullOrEmpty01() {
        System.out.println("isNullOrEmpty");
        assertEquals(true, StringUtil.isNullOrEmpty(null));
    }

    @Test
    public void testIsNullOrEmpty02() {
        System.out.println("isNullOrEmpty");
        assertEquals(true, StringUtil.isNullOrEmpty(""));
    }

    @Test
    public void testIsNullOrEmpty03() {
        System.out.println("isNullOrEmpty");
        assertEquals(false, StringUtil.isNullOrEmpty("x"));
    }

    @Test
    public void testSplitAtUnquotedSemicolon01() {
        System.out.println("splitAtUnquotedSemicolon");
        String line = "";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {""};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon02() {
        System.out.println("splitAtUnquotedSemicolon");
        String line = "1;2;3";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"1", "2", "3"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon03() {
        System.out.println("splitAtUnquotedSemicolon");
        String line = "\"1\";2;3";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"1", "2", "3"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon04() {
        System.out.println("splitAtUnquotedSemicolon");
        String line = "\"1;2\";3";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"1;2", "3"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon05() {
        System.out.println("splitAtUnquotedSemicolon");
        String line = "\"1;2;3";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"\"1;2;3"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon06() {
        System.out.println("splitAtUnquotedSemicolon");
        String line = "\"1;2;\"3";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"\"1;2;\"3"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon07() {
        System.out.println("splitAtUnquotedSemicolon");
        String line = "\"Hi; this is me\";2";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"Hi; this is me", "2"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon08() {
        System.out.println("splitAtUnquotedSemicolon");
        String line = "\"Hi; this is \"me;2";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"\"Hi; this is \"me;2"};
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSplitAtUnquotedSemicolon09() {
        System.out.println("splitAtUnquotedSemicolon");
        String line = "Hi; this is \"me;2";
        String[] result = StringUtil.splitAtUnquotedSemicolon(line);
        String[] expResult = {"Hi", " this is \"me", "2"};
        assertArrayEquals(expResult, result);
    }
    
    @Test
    public void testParseLocalizedDoubleAsInt() {
        System.out.println("parseLocalizedDoubleAsInt");
    }

    @Test
    public void testParseLocalizedDouble() {
        System.out.println("parseLocalizedDouble");
    }

    @Test
    public void testGetSqlFilter01() {
        System.out.println("getSqlFilter");
        assertEquals("'123456789'", StringUtil.getSqlFilter("123456789"));
    }
    
    @Test
    public void testGetSqlFilter02() {
        System.out.println("getSqlFilter");
        assertEquals("'%abc%'", StringUtil.getSqlFilter("abc"));
    }
    
    @Test
    public void testGetSqlFilter03() {
        System.out.println("getSqlFilter");
        assertEquals("'abc%'", StringUtil.getSqlFilter("abc%"));
    }
    
    @Test
    public void testGetSqlFilter04() {
        System.out.println("getSqlFilter");
        assertEquals("'ab%c'", StringUtil.getSqlFilter("'ab'%c"));
    }
    
}
