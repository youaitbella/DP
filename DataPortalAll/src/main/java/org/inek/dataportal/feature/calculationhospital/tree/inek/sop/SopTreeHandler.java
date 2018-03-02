package org.inek.dataportal.feature.calculationhospital.tree.inek.sop;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.tree.RootNode;

/**
 *
 * @author muellermi
 */
@Named @SessionScoped
public class SopTreeHandler implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("SopTreeHandler");
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

    private String _filter = "";

    public String getFilter() {
        return _filter;
    }

    public void setFilter(String filter) {
        _filter = filter == null ? "" : filter;
        refreshNodes();
    }

    private int _year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        _year = year;
        refreshNodes();
    }

}
