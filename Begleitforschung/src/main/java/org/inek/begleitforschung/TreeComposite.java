package org.inek.begleitforschung;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import org.inek.portallib.tree.MenuTreeNode;

@FacesComponent("treeComposite")
public class TreeComposite extends UINamingContainer {

    private MenuTreeNode _node;

    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        if ("parentNode".equals(name)) {
            setParentNode((MenuTreeNode) binding.getValue(getFacesContext().getELContext()));
        }
        else {
            super.setValueExpression(name, binding);
        }
    }

    public MenuTreeNode getParentNode() {
        return _node;
    }

    public void setParentNode(MenuTreeNode node) {
        this._node = node;
    }

}
