/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.portallib.tree;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author muellermi
 */
public class MenuTreeNodeTest {
    
    private MenuTreeNode _menuRoot;
    
    @Before
    public void setUp() {
        _menuRoot = MenuTreeNode.createRoot("", "");
    }
    
    @Test
    public void levelOfFirstEntryIsOne() {
        MenuTreeNode menuNode= _menuRoot.addChild("item1", "item1.xhtml");
        assertThat (menuNode.getLevel(), is(1));
    }
    
    @Test
    public void levelOfSubEntryIsTwo() {
        MenuTreeNode menuNode= _menuRoot.addChild("item1", "item1.xhtml");
        MenuTreeNode menuSubNode= menuNode.addChild("item1.1", "item1-1.xhtml");
        assertThat (menuSubNode.getLevel(), is(2));
    }

    @Test
    public void levelOfSecondEntryIsOne() {
        _menuRoot.addChild("item1", "item1.xhtml");
        MenuTreeNode menuNode2 = _menuRoot.addChild("item2", "item2.xhtml");
        assertThat (menuNode2.getLevel(), is(1));
    }
    
    @Test
    public void countOfTwoChildrenIsTwo() {
        _menuRoot.addChild("item1", "item1.xhtml");
        _menuRoot.addChild("item2", "item2.xhtml");
        assertThat (_menuRoot.getChildren().size(), is(2));
    }
    @Test
    public void countOfTwoChildrenPlusSubNodesIsTwo() {
        MenuTreeNode menuNode1 = _menuRoot.addChild("item1", "item1.xhtml");
        menuNode1.addChild("item1.1", "item1-1.xhtml");
        _menuRoot.addChild("item2", "item2.xhtml");
        assertThat (_menuRoot.getChildren().size(), is(2));
    }
    
}
