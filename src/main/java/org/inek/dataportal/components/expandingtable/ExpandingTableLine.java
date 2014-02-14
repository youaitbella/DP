package org.inek.dataportal.components.expandingtable;

/**
 * An ExpandingTable uses a unified data format.
 * All attributes which are mandatory are defined here.
 * 
 * @author muellermi
 */
public interface ExpandingTableLine {
    
    public Integer getId();
    public void setId(Integer id);
    
    public String getCode();
    public void setCode(String code);
    
    public String getDescription();
    public void setDescription(String description);
    
}
