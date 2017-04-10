package org.inek.dataportal.components;

import java.util.List;

/**
 * An ExpandingTable uses a unified data format.
 * All attributes which are mandatory are defined here.
 * 
 * @author muellermi
 */
public interface ExpandingTableData {
    
    List<ExpandingTableLine> getData();
    
    ExpandingTableLine createElement();
}
