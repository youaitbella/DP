package org.inek.dataportal.feature.calculationhospital.tree.inek.calcbasics;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.helper.Utils;
import org.inek.portallib.tree.RootNode;

/**
 *
 * @author muellermi
 */
@Named @SessionScoped
public class CalcBasicsTreeHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject private Instance<RootTreeNodeObserver> _rootTreeNodeObserverProvider;

    private RootNode _rootNode;

    @PostConstruct
    private void init() {
        _rootNode = RootNode.create(0, _rootTreeNodeObserverProvider.get());
    }

    public RootNode getRootNode() {
        if (!_rootNode.isExpanded()) {
            _rootNode.expand();
        }
        return _rootNode;
    }

    public void refreshNodes() {
        _rootNode.refresh();
    }

    //<editor-fold defaultstate="collapsed" desc="Property Filter">
    private String _filter = "";

    public String getFilter() {
        return _filter;
    }

    public void setFilter(String filter) {
        _filter = filter == null ? "" : filter;
        refreshNodes();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Year">
    private int _year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        _year = year;
        refreshNodes();
    }
    //</editor-fold>
}
