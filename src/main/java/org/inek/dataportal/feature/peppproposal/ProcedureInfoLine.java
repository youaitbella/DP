/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.peppproposal;

import org.inek.dataportal.components.ExpandingTableLine;
import org.inek.dataportal.entities.ProcedureInfo;


/**
 *
 * @author muellermi
 */
public class ProcedureInfoLine extends ProcedureInfo implements ExpandingTableLine{

    @Override
    public String getDescription() {
        return super.getName();
    }

    @Override
    public void setDescription(String description) {
        super.setName(description);
    }
    
}
