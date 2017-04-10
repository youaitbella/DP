package org.inek.dataportal.components;

/**
 * An ExpandingTable uses a unified data format.
 * All attributes which are mandatory are defined here.
 * 
 * @author muellermi
 */
public interface ExpandingTableLine {
    
    Integer getId();
    void setId(Integer id);
    
    String getCode();
    void setCode(String code);
    
    String getDescription();
    void setDescription(String description);
    
}
