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
        _menuRoot = MenuTreeNode.createRoot(null);
    }
    
    @Test
    public void levelOfFirstEntryIsOne() {
        MenuTreeNode menuNode= MenuTreeNode.create(_menuRoot, "item1", "item1.xhtml");
        assertThat (menuNode.getLevel(), is(1));
    }
    
    @Test
    public void levelOfSubEntryIsTwo() {
        MenuTreeNode menuNode= MenuTreeNode.create(_menuRoot, "item1", "item1.xhtml");
        MenuTreeNode menuSubNode= MenuTreeNode.create(menuNode, "item1.1", "item1-1.xhtml");
        assertThat (menuSubNode.getLevel(), is(2));
    }

    @Test
    public void levelOfSecondEntryIsOne() {
        MenuTreeNode menuNode1 = MenuTreeNode.create(_menuRoot, "item1", "item1.xhtml");
        MenuTreeNode menuNode2 = MenuTreeNode.create(_menuRoot, "item2", "item2.xhtml");
        assertThat (menuNode2.getLevel(), is(1));
    }
    
    @Test
    public void countOfTwoChildrenIsTwo() {
        MenuTreeNode menuNode1 = MenuTreeNode.create(_menuRoot, "item1", "item1.xhtml");
        MenuTreeNode menuNode2 = MenuTreeNode.create(_menuRoot, "item2", "item2.xhtml");
        assertThat (_menuRoot.getChildren().size(), is(2));
    }
    @Test
    public void countOfTwoChildrenPlusSubNodesIsTwo() {
        MenuTreeNode menuNode1 = MenuTreeNode.create(_menuRoot, "item1", "item1.xhtml");
        MenuTreeNode.create(menuNode1, "item1.1", "item1-1.xhtml");
        MenuTreeNode menuNode2 = MenuTreeNode.create(_menuRoot, "item2", "item2.xhtml");
        assertThat (_menuRoot.getChildren().size(), is(2));
    }
    
}
