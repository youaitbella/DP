/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.components;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.inject.Inject;
import org.inek.dataportal.enums.NubListType;
import org.inek.dataportal.feature.nub.NubProposalList;
import org.inek.dataportal.helper.structures.Triple;

/**
 *
 * @author muellermi
 */
@FacesComponent(value = "org.inek.dataportal.components.NubList", createTag = false)
public class NubList extends UIComponentBase implements NamingContainer {

    private static final Logger _logger = Logger.getLogger("NubList");
    @Inject NubProposalList _nubProposalList;

    private NubListType _nubListType;
    public NubListType getNubType() {
        return _nubListType;
    }

    public void setNubType(NubListType nubListType) {
        _nubListType = nubListType;
    }

    public List<Triple> getNubProposals() {
        if (_nubListType == NubListType.PrivateDisplay) {
            return _nubProposalList.getSealedNubProposals();
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getFamily() {
        return UINamingContainer.COMPONENT_FAMILY;
    }

}
