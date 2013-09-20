package org.inek.dataportal.components.expandingtable;

import java.util.List;

/**
 * An ExpandingTable uses a unified data format.
 * All attributes which are mandatory are defined here.
 * 
 * @author muellermi
 */
public interface ExpandingTableData {
    
    public List<ExpandingTableLine> getData();
    
    public ExpandingTableLine createElement();
}
