package org.inek.dataportal.feature.calculationhospital.tree.inek.distmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.facades.calc.DistributionModelFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.CalcHospitalTreeNode;
import org.inek.dataportal.common.tree.RootNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;

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

    private int _year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        _year = year;
        refreshNodes();
    }

}
