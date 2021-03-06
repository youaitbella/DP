package org.inek.dataportal.calc.tree.inek.distmodel;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.tree.RootNode;

/**
 *
 * @author muellermi
 */
@Named @SessionScoped
public class DistributionModellTreeHandler implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("DistributionModellTreeHandler");
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

}
