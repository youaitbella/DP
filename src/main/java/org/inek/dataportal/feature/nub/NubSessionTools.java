package org.inek.dataportal.feature.nub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.CooperationTools;
import static org.inek.dataportal.common.CooperationTools.canReadCompleted;
import static org.inek.dataportal.common.CooperationTools.canReadSealed;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.entities.cooperation.CooperationRight;
import org.inek.dataportal.entities.nub.NubRequest;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.NubRequestFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;
import org.inek.dataportal.helper.ObjectUtils;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.MessageContainer;
import org.inek.dataportal.helper.structures.ProposalInfo;
import org.inek.dataportal.helper.tree.AccountTreeNode;
import org.inek.dataportal.helper.tree.ProposalInfoTreeNode;
import org.inek.dataportal.helper.tree.RootNode;
import org.inek.dataportal.helper.tree.TreeNode;
import org.inek.dataportal.helper.tree.TreeNodeObserver;
import org.inek.dataportal.helper.tree.YearTreeNode;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.utils.KeyValueLevel;

@Named @SessionScoped
public class NubSessionTools implements Serializable, TreeNodeObserver {

    private static final Logger _logger = Logger.getLogger("NubSessionTools");
    private static final long serialVersionUID = 1L;

    @Inject private CooperationRightFacade _cooperationRightFacade;
    @Inject private NubRequestFacade _nubRequestFacade;
    @Inject private SessionController _sessionController;

    private String _nubFilter = "";

    public String getNubFilter() {
        return _nubFilter;
    }

    public void setNubFilter(String nubFilter) {
        _nubFilter = nubFilter == null ? "" : nubFilter;
        refreshNodes();
    }

    // Seal own NUB is a marker, whether a NUB may be sealed by the owner (true)
    // or by a supervisor only (false)
    // It is used in coopearative environment
    private Map<Integer, Boolean> _sealOwnNub;

    public Map<Integer, Boolean> getSealOwnNub() {
        ensureSealOwnNub();
        return _sealOwnNub;
    }

    /**
     * clears cache of sealOwnNub e.g. to ensure update after changing rights.
     */
    public void clearCache() {
        _sealOwnNub = null;
    }

    private void ensureSealOwnNub() {
        if (_sealOwnNub != null) {
            return;
        }
        _sealOwnNub = new HashMap<>();
        Account account = _sessionController.getAccount();
        for (int ik : account.getFullIkList()) {
            // allowed for own NUB if supervisor herself or no supervisor exists
            _sealOwnNub.put(ik, _cooperationRightFacade.isIkSupervisor(Feature.NUB, ik, account.getId()) || !_cooperationRightFacade.hasSupervisor(Feature.NUB, ik));
        }
        List<CooperationRight> rights = _cooperationRightFacade
                .getGrantedCooperationRights(account.getId(), Feature.NUB);
        for (CooperationRight right : rights) {
            if (right.getCooperativeRight().equals(CooperativeRight.ReadCompletedSealSupervisor)
                    || right.getCooperativeRight().equals(CooperativeRight.ReadWriteCompletedSealSupervisor)
                    || right.getCooperativeRight().equals(CooperativeRight.ReadWriteSealSupervisor)) {
                _sealOwnNub.put(right.getIk(), Boolean.FALSE);
            }
        }
    }

    public CooperativeRight getCooperativeRight(NubRequest nubRequest) {
        return _cooperationRightFacade.getCooperativeRight(
                nubRequest.getAccountId(),
                _sessionController.getAccountId(),
                Feature.NUB,
                nubRequest.getIk());
    }

    public CooperativeRight getSupervisorRight(NubRequest nub) {
        return _cooperationRightFacade.getIkSupervisorRight(Feature.NUB, nub.getIk(), _sessionController.getAccountId());
    }

    private final RootNode _rootNode = RootNode.create(0, this);
    private AccountTreeNode _accountNode;
    @Inject private CooperationTools _cooperationTools;

    @PostConstruct
    private void init() {
        _rootNode.setExpanded(true);
    }

    public RootNode getEditNode() {
        return getRootNode(1);
    }

    private RootNode getRootNode(int id) {
        Optional<TreeNode> optionalRoot = _rootNode.getChildren().stream().filter(n -> n.getId() == id).findAny();
        if (optionalRoot.isPresent()) {
            return (RootNode) optionalRoot.get();
        }
        RootNode node = RootNode.create(id, this);
        node.expand();
        _rootNode.getChildren().add(node);
        return node;
    }

    public RootNode getViewNode() {
        return getRootNode(2);
    }

    public AccountTreeNode getAccountNode() {
        if (_accountNode == null) {
            _accountNode = AccountTreeNode.create(null, _sessionController.getAccount(), this);
            _accountNode.expand();
        }
        return _accountNode;
    }

    public void refreshNodes() {
        _rootNode.refresh();
    }

    @Override
    public void obtainChildren(TreeNode treeNode, Collection<TreeNode> children) {
        if (treeNode instanceof RootNode && treeNode.getId() == 1) {
            obtainNubEditNodeChildren((RootNode) treeNode, children);
        }
        if (treeNode instanceof RootNode && treeNode.getId() == 2) {
            obtainNubViewNodeChildren((RootNode) treeNode, children);
        }
        if (treeNode instanceof YearTreeNode) {
            obtainYearNodeChildren((YearTreeNode) treeNode, children);
        }
        if (treeNode instanceof AccountTreeNode) {
            obtainAccountNodeChildren((AccountTreeNode) treeNode, children);
        }
    }

    private void obtainNubEditNodeChildren(RootNode node, Collection<TreeNode> children) {
        Set<Integer> accountIds = _cooperationTools.determineAccountIds(Feature.NUB, canReadCompleted());
        accountIds = _nubRequestFacade.checkAccountsForNubOfYear(accountIds, -1, WorkflowStatus.New, WorkflowStatus.ApprovalRequested);
        List<Account> accounts = _accountFacade.getAccountsForIds(accountIds);
        Account currentUser = _sessionController.getAccount();
        if (accounts.contains(currentUser)) {
            // ensure current user is first, if in list
            accounts.remove(currentUser);
            accounts.add(0, currentUser);
        }
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        children.clear();
        for (Account account : accounts) {
            Integer id = account.getId();
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == id).findFirst();
            AccountTreeNode childNode = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.create(node, account, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            childNode.expand();  // auto-expand all edit nodes by default
        }
    }

    private void obtainNubViewNodeChildren(RootNode node, Collection<TreeNode> children) {
        Set<Integer> accountIds = _cooperationTools.determineAccountIds(Feature.NUB, canReadSealed());
        List<Integer> years = _nubRequestFacade.getNubYears(accountIds);
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        int targetYear = Utils.getTargetYear(Feature.NUB);
        children.clear();
        for (Integer year : years) {
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == year).findFirst();
            YearTreeNode childNode = existing.isPresent() ? (YearTreeNode) existing.get() : YearTreeNode.create(node, year, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (year == targetYear) {
                childNode.expand();
            }
        }
    }

    @Inject
    private AccountFacade _accountFacade;

    private void obtainYearNodeChildren(YearTreeNode node, Collection<TreeNode> children) {
        Set<Integer> accountIds = _cooperationTools.determineAccountIds(Feature.NUB, canReadSealed());
        accountIds = _nubRequestFacade.checkAccountsForNubOfYear(accountIds, node.getId(), WorkflowStatus.Provided, WorkflowStatus.Retired);
        List<Account> accounts = _accountFacade.getAccountsForIds(accountIds);
        Account currentUser = _sessionController.getAccount();
        if (accounts.contains(currentUser)) {
            // ensure current user is first, if in list
            accounts.remove(currentUser);
            accounts.add(0, currentUser);
        }
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        children.clear();
        for (Account account : accounts) {
            Integer id = account.getId();
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == id).findFirst();
            AccountTreeNode childNode = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.create(node, account, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (account == currentUser) {
                // auto expand user's own data
                childNode.expand();
            }
        }
    }

    private void obtainAccountNodeChildren(AccountTreeNode accountTreeNode, Collection<TreeNode> children) {
        int partnerId = accountTreeNode.getId();
        List<ProposalInfo> infos;
        if (accountTreeNode.getParent() instanceof YearTreeNode) {
            int year = accountTreeNode.getParent().getId();
            infos = obtainNubInfosForRead(partnerId, year);
        } else {
            infos = obtainNubInfosForEdit(partnerId);
        }
        List<Integer> checked = new ArrayList<>();
        for (TreeNode child : accountTreeNode.copyChildren()) {
            if (child.isChecked()) {
                checked.add(child.getId());
            }
        }
        accountTreeNode.getChildren().clear();
        for (ProposalInfo info : infos) {
            ProposalInfoTreeNode node = ProposalInfoTreeNode.create(accountTreeNode, info, this);
            if (checked.contains(node.getId())) {
                node.setChecked(true);
            }
            accountTreeNode.getChildren().add(node);
        }
    }

    private List<ProposalInfo> obtainNubInfosForRead(int partnerId, int year) {
        List<ProposalInfo> infos = new ArrayList<>();
        if (partnerId == _sessionController.getAccountId()) {
            infos = _nubRequestFacade.getNubRequestInfos(_sessionController.getAccountId(), -1, year, DataSet.AllSealed, getFilter());
        } else {
            Set<Integer> iks = _cooperationTools.getPartnerIks(Feature.NUB, partnerId);
            for (int ik : iks) {
                CooperativeRight achievedRight = _cooperationTools.getAchievedRight(Feature.NUB, partnerId, ik);
                DataSet dataSet = achievedRight.canReadSealed() ? DataSet.AllSealed : DataSet.None;
                List<ProposalInfo> infosForIk = _nubRequestFacade.getNubRequestInfos(partnerId, ik, year, dataSet, getFilter());
                infos.addAll(infosForIk);
            }
        }
        return infos;
    }

    private List<ProposalInfo> obtainNubInfosForEdit(int partnerId) {
        List<ProposalInfo> infos = new ArrayList<>();
        if (partnerId == _sessionController.getAccountId()) {
            infos = _nubRequestFacade.getNubRequestInfos(_sessionController.getAccountId(), -1, -1, DataSet.AllOpen, getFilter());
        } else {
            Set<Integer> iks = _cooperationTools.getPartnerIks(Feature.NUB, partnerId);
            for (int ik : iks) {
                CooperativeRight achievedRight = _cooperationTools.getAchievedRight(Feature.NUB, partnerId, ik);
                DataSet dataSet = achievedRight.canReadAlways() ? DataSet.AllOpen
                        : achievedRight.canReadCompleted() ? DataSet.ApprovalRequested : DataSet.None;
                List<ProposalInfo> infosForIk = _nubRequestFacade.getNubRequestInfos(partnerId, ik, -1, dataSet, getFilter());
                infos.addAll(infosForIk);
            }
        }
        return infos;
    }

    public List<ProposalInfo> getNubRequests(AccountTreeNode accountNode) {
        return accountNode.getChildren().stream().map(a -> ((ProposalInfoTreeNode) a).getProposalInfo()).collect(Collectors.toList());
    }

    private String getFilter() {
        String filter = getNubFilter();
        if (!filter.isEmpty() && !filter.contains("%")) {
            filter = "%" + filter + "%";
        }
        return filter;
    }

    public String refreshAndGotoNubSummary() {
        _rootNode.refresh();
        return Pages.NubSummary.URL();
    }

    private String printRequests(List<NubRequest> nubRequests) {
        if (nubRequests.isEmpty()) {
            return "";
        }
        Map<String, List<KeyValueLevel>> documents = new TreeMap<>();
        int count = 1;
        for (NubRequest nubRequest : nubRequests) {
            String key = nubRequest.getExternalId();
            if (key.isEmpty()) {
                key = "<nicht gesendet> " + count++;
            }
            documents.put(key, DocumentationUtil.getDocumentation(nubRequest));
        }
        List<String> keys = new ArrayList<>(documents.keySet());
        Utils.getFlash().put("headLine", Utils.getMessage("nameNUB"));
        Utils.getFlash().put("targetPage", Pages.NubSummary.URL());
        Utils.getFlash().put("printContentKeys", keys);
        Utils.getFlash().put("printContent", documents);
        return Pages.PrintMultipleView.URL();
    }

    public void selectAll(RootNode root) {
        root.selectAll(ProposalInfoTreeNode.class, true);
    }

    public String deselectAll(RootNode root) {
        root.selectAll(ProposalInfoTreeNode.class, false);
        return "";
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode, Collection<TreeNode> children) {
        if (treeNode instanceof AccountTreeNode) {
            return sortAccountNodeChildren((AccountTreeNode) treeNode, children);
        }
        return children;
    }

    public Collection<TreeNode> sortAccountNodeChildren(AccountTreeNode treeNode, Collection<TreeNode> children) {
        Stream<ProposalInfoTreeNode> stream = children.stream().map(n -> (ProposalInfoTreeNode) n);
        Stream<ProposalInfoTreeNode> sorted;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "id":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n2.getProposalInfo().getId(), n1.getProposalInfo().getId()));
                } else {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n1.getProposalInfo().getId(), n2.getProposalInfo().getId()));
                }
                break;
            case "ik":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n2.getProposalInfo().getIk(), n1.getProposalInfo().getIk()));
                } else {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n1.getProposalInfo().getIk(), n2.getProposalInfo().getIk()));
                }
                break;
            case "name":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> n2.getProposalInfo().getName().compareTo(n1.getProposalInfo().getName()));
                } else {
                    sorted = stream.sorted((n1, n2) -> n1.getProposalInfo().getName().compareTo(n2.getProposalInfo().getName()));
                }
                break;
            case "status":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> getExternalState(n2).compareTo(getExternalState(n1)));
                } else {
                    sorted = stream.sorted((n1, n2) -> getExternalState(n1).compareTo(getExternalState(n2)));
                }
                break;
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }

    private String getExternalState(ProposalInfoTreeNode node) {
        int id = node.getProposalInfo().getId();
        NubRequest nubRequest = _nubRequestFacade.find(id);
        if (nubRequest == null) {
            return "";
        }
        return nubRequest.getExternalState();
    }

    private String sendSelected(List<NubRequest> nubRequests) {
        List<NubRequest> sendNubRequests = new ArrayList<>();
        for (NubRequest nubRequest : nubRequests) {
            if (trySendRequest(nubRequest)) {
                sendNubRequests.add(nubRequest);
            }
        }
        int total = nubRequests.size();
        int send = sendNubRequests.size();
        if (total > send) {
            String msg = (total - send) + " Anfragen von " + total + " konnten nicht gesendet werden.\n"
                    + "Möglicherweise sind diese noch unvollständig oder Sie verfügen nicht über die Sendeberechtigung.\n"
                    + "Bitte senden Sie diese einzeln, um eine ausführliche Meldung zu erhalten.";
            String script = "alert ('" + msg.replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
        }
        return printRequests(sendNubRequests);
    }

    private boolean trySendRequest(NubRequest nubRequest) {
        if (nubRequest.getStatus().getValue() >= WorkflowStatus.Provided.getValue() || nubRequest.getStatus() == WorkflowStatus.Unknown) {
            return false;
        }
        if (!isSealEnabled(nubRequest)) {
            return false;
        }
        if (composeMissingFieldsMessage(nubRequest).containsMessage()) {
            return false;
        }
        nubRequest = prepareSeal(nubRequest);
        try {
            nubRequest = _nubRequestFacade.saveNubRequest(nubRequest);
            if (isValidId(nubRequest.getId())) {
                sendNubConfirmationMail(nubRequest);
                return true;
            }
        } catch (Exception ex) {
            _logger.log(Level.WARNING, ex.getMessage());
        }

        return false;
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public NubRequest prepareSeal(NubRequest nubRequest) {
        nubRequest.setStatus(WorkflowStatus.Accepted);
        nubRequest.setSealedBy(_sessionController.getAccountId());
        nubRequest.setDateSealed(Calendar.getInstance().getTime());

        int targetYear = 1 + Calendar.getInstance().get(Calendar.YEAR);
        if (nubRequest.getTargetYear() < targetYear) {
            // data from last year, not sealed so far
            // we need a new id, thus delete old and create new nub request
            NubRequest copy = ObjectUtils.copy(nubRequest);
            copy.setId(-1);
            copy.setTargetYear(targetYear);
            _nubRequestFacade.remove(nubRequest);
            nubRequest = copy;
        }
        return nubRequest;
    }

    public boolean isSealEnabled(NubRequest nubRequest) {
        if (!_sessionController.isEnabled(ConfigKey.IsNubSendEnabled)) {
            return false;
        }
        return _cooperationTools.isSealedEnabled(Feature.NUB, nubRequest.getStatus(), nubRequest.getAccountId(), nubRequest.getIk());
    }

    public MessageContainer composeMissingFieldsMessage(NubRequest nubRequest) {
        MessageContainer message = new MessageContainer();
        String ik = "";
        if (nubRequest.getIk() != null) {
            ik = nubRequest.getIk().toString();
        }
        checkField(message, ik, "lblIK", "form:ikMulti", EditNubRequest.NubRequestTabs.tabNubAddress);
        checkField(message, nubRequest.getGender(), 1, 2, "lblSalutation", "form:cbxGender", EditNubRequest.NubRequestTabs.tabNubAddress);
        checkField(message, nubRequest.getFirstName(), "lblFirstName", "form:firstname", EditNubRequest.NubRequestTabs.tabNubAddress);
        checkField(message, nubRequest.getLastName(), "lblLastName", "form:lastname", EditNubRequest.NubRequestTabs.tabNubAddress);
        checkField(message, nubRequest.getStreet(), "lblStreet", "form:street", EditNubRequest.NubRequestTabs.tabNubAddress);
        checkField(message, nubRequest.getPostalCode(), "lblPostalCode", "form:zip", EditNubRequest.NubRequestTabs.tabNubAddress);
        checkField(message, nubRequest.getTown(), "lblTown", "form:town", EditNubRequest.NubRequestTabs.tabNubAddress);
        checkField(message, nubRequest.getPhone(), "lblPhone", "form:phone", EditNubRequest.NubRequestTabs.tabNubAddress);
        checkField(message, nubRequest.getEmail(), "lblMail", "form:email", EditNubRequest.NubRequestTabs.tabNubAddress);
        checkField(message, nubRequest.getName(), "lblAppellation", "form:nubName", EditNubRequest.NubRequestTabs.tabNubPage1);
        checkField(message, nubRequest.getDescription(), "lblNubDescription", "form:nubDescription", EditNubRequest.NubRequestTabs.tabNubPage1);
        if (!nubRequest.isHasNoProcs()) {
            checkField(message, nubRequest.getProcs(), "lblNubProcRelated", "form:nubProcedures", EditNubRequest.NubRequestTabs.tabNubPage1);
        }
        checkField(message, nubRequest.getIndication(), "lblIndication", "form:nubIndic", EditNubRequest.NubRequestTabs.tabNubPage2);
        checkField(message, nubRequest.getReplacement(), "lblNubReplacementPrint", "form:nubReplacement", EditNubRequest.NubRequestTabs.tabNubPage2);
        checkField(message, nubRequest.getWhatsNew(), "lblWhatsNew", "form:nubWhatsNew", EditNubRequest.NubRequestTabs.tabNubPage2);
        checkField(message, nubRequest.getInHouseSince(), "lblMethodInHouse", "form:nubInHouse", EditNubRequest.NubRequestTabs.tabNubPage3);
        checkField(message, nubRequest.getPatientsLastYear(), "lblPatientsLastYear", "form:patientsLastYear", EditNubRequest.NubRequestTabs.tabNubPage3);
        checkField(message, nubRequest.getPatientsThisYear(), "lblPatientsThisYear", "form:patientsThisYear", EditNubRequest.NubRequestTabs.tabNubPage3);
        checkField(message, nubRequest.getPatientsFuture(), "lblPatientsFuture", "form:patientsFuture", EditNubRequest.NubRequestTabs.tabNubPage3);
        checkField(message, nubRequest.getAddCosts(), "lblAddCosts", "form:nubAddCost", EditNubRequest.NubRequestTabs.tabNubPage4);
        checkField(message, nubRequest.getWhyNotRepresented(), "lblWhyNotRepresented", "form:nubNotRepresented", EditNubRequest.NubRequestTabs.tabNubPage4);
        if (nubRequest.getRoleId() < 0) {
            message.setMessage(Utils.getMessage("lblContactRole"));
            message.setTopic(EditNubRequest.NubRequestTabs.tabNubAddress.name());
        }
        String proxyErr = checkProxyIKs(nubRequest.getProxyIKs());
        if (!proxyErr.isEmpty()) {
            message.setMessage(Utils.getMessage("lblErrorProxyIKs"));
            message.setTopic(EditNubRequest.NubRequestTabs.tabNubAddress.name());
        }
        return message;
    }

    private void checkField(MessageContainer message, String value, String msgKey, String elementId, EditNubRequest.NubRequestTabs tab) {
        if (Utils.isNullOrEmpty(value)) {
            applyMessageValues(message, msgKey, tab, elementId);
        }
    }

    private void checkField(MessageContainer message, Integer value, Integer minValue, Integer maxValue, String msgKey, String elementId, EditNubRequest.NubRequestTabs tab) {
        if (value == null
                || minValue != null && value < minValue
                || maxValue != null && value > maxValue) {
            applyMessageValues(message, msgKey, tab, elementId);
        }
    }

    private void applyMessageValues(MessageContainer message, String msgKey, EditNubRequest.NubRequestTabs tab, String elementId) {
        message.setMessage(message.getMessage() + "\\r\\n" + Utils.getMessage(msgKey));
        if (message.getTopic().isEmpty()) {
            message.setTopic(tab.name());
            message.setElementId(elementId);
        }
    }

    @Inject private CustomerFacade _customerFacade;

    public String checkProxyIKs(String value) {
        String iks[] = value.split("\\s|,|\r|\n");
        StringBuilder invalidIKs = new StringBuilder();
        for (String ik : iks) {
            if (ik.isEmpty()) {
                continue;
            }
            if (!_customerFacade.isValidIK(ik)) {
                if (invalidIKs.length() > 0) {
                    invalidIKs.append(", ");
                }
                invalidIKs.append(ik);
            }
        }
        if (invalidIKs.length() > 0) {
            if (invalidIKs.indexOf(",") < 0) {
                invalidIKs.insert(0, "Ungültige IK: ");
            } else {
                invalidIKs.insert(0, "Ungültige IKs: ");
            }
        }
        return invalidIKs.toString();
    }

    @Inject Mailer _mailer;

    public boolean sendNubConfirmationMail(NubRequest nubRequest) {
        Account current = _sessionController.getAccount();
        Account owner = _accountFacade.find(nubRequest.getAccountId());
        if (!current.isNubConfirmation() && !owner.isNubConfirmation()) {
            return true;
        }
        if (!current.isNubConfirmation()) {
            current = owner;
        }
        if (!owner.isNubConfirmation()) {
            owner = current;
        }

        MailTemplate template = _mailer.getMailTemplate("NUB confirmation");
        if (template == null) {
            return false;
        }

        String proxy = nubRequest.getProxyIKs().trim();
        if (!proxy.isEmpty()) {
            proxy = "\r\nSie haben diese Anfrage auch stellvertretend für die folgenden IKs gestellt:\r\n" + proxy + "\r\n";
        }

        String salutation = _mailer.getFormalSalutation(current);
        String body = template.getBody()
                .replace("{formalSalutation}", salutation)
                .replace("{id}", "N" + nubRequest.getId())
                .replace("{name}", nubRequest.getName())
                .replace("{ik}", "" + nubRequest.getIk())
                .replace("{proxyIks}", proxy)
                .replace("{targetYear}", "" + nubRequest.getTargetYear());

        String subject = template.getSubject()
                .replace("{id}", "N" + nubRequest.getId())
                .replace("{ik}", "" + nubRequest.getIk());

        return _mailer.sendMailFrom("NUB Datenannahme <nub@inek-drg.de>", current.getEmail(), owner.getEmail(), template.getBcc(), subject, body);
    }

    private String _editAction = "print";

    public String getEditAction() {
        return _editAction;
    }

    public void setEditAction(String action) {
        _editAction = action;
    }

    private String _viewAction = "print";

    public String getViewAction() {
        return _viewAction;
    }

    public void setViewAction(String action) {
        _viewAction = action;
    }

    public List<SelectItem> getEditActions() {
        List<SelectItem> actions = new ArrayList<>();
        actions.add(new SelectItem("print", Utils.getMessage("actionPrint")));
        actions.add(new SelectItem("send", Utils.getMessage("actionSend")));
        actions.add(new SelectItem("take", Utils.getMessage("actionTake")));
        return actions;
    }

    public List<SelectItem> getViewActions() {
        List<SelectItem> actions = new ArrayList<>();
        actions.add(new SelectItem("print", Utils.getMessage("actionPrint")));
        actions.add(new SelectItem("copy", Utils.getMessage("actionCopy")));
        actions.add(new SelectItem("take", Utils.getMessage("actionTake")));
        return actions;
    }

    public String startAction(RootNode root) {
        List<NubRequest> nubRequests = collectRequests(root);
        String action = (root == getEditNode()) ? _editAction : _viewAction;
        _sessionController.logMessage("Batch: " + action + "; count: " + nubRequests.size());

        switch (action) {
            case "print":
                return printRequests(nubRequests);
            case "send":
                return sendSelected(nubRequests);
            case "copy":
                return copySelected(nubRequests);
            case "take":
                return takeSelected(nubRequests);
        }
        return "";
    }

    private List<NubRequest> collectRequests(RootNode root) {
        List<Integer> selectedRequests = root.getSelectedIds(ProposalInfoTreeNode.class);
        List<NubRequest> nubRequests = _nubRequestFacade.find(selectedRequests);
        return nubRequests;
    }

    private String copySelected(List<NubRequest> nubRequests) {
        for (NubRequest nubRequest : nubRequests) {
            copyNubRequest(nubRequest);
        }
        String msg = nubRequests.size() + " Anfragen wurden übernommen.";
        String script = "alert ('" + msg.replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
        _sessionController.setScript(script);
        return "";
    }

    public boolean copyNubRequest(NubRequest nubRequest) {
        int targetYear = 1 + Calendar.getInstance().get(Calendar.YEAR);
        int targetAccountId = _sessionController.getAccountId();
        NubRequest copy = ObjectUtils.copy(nubRequest);
        copy.setId(-1);
        copy.setStatus(WorkflowStatus.New);
        copy.setDateSealed(null);
        copy.setSealedBy(0);
        copy.setLastModified(null);
        copy.setCreationDate(null);
        copy.setDateOfReview(null);
        copy.setExternalState("");
        copy.setByEmail(false);
        copy.setErrorText("");
        copy.setCreatedBy(targetAccountId);
        copy.setLastChangedBy(targetAccountId);
        if (copy.getAccountId() != targetAccountId) {
            // from partner
            copy.setPatientsLastYear("");
            copy.setPatientsThisYear("");
            copy.setPatientsFuture("");
            copy.setHelperId(copy.getAccountId());
            Account partner = _accountFacade.find(copy.getAccountId());
            copy.setFormFillHelper("Kooperationspartner: " + partner.getCompany());
            copy.setAccountId(_sessionController.getAccountId());
            getNubController().populateMasterData(copy, _sessionController.getAccount());
        } else if (copy.getTargetYear() == targetYear - 1) {
            // from previous year
            copy.setPatientsLastYear(nubRequest.getPatientsThisYear());
            copy.setPatientsThisYear(nubRequest.getPatientsFuture());
            copy.setPatientsFuture("");
        } else {
            // elder
            copy.setPatientsLastYear("");
            copy.setPatientsThisYear("");
            copy.setPatientsFuture("");
        }
        copy.setTargetYear(targetYear);
        copy = _nubRequestFacade.saveNubRequest(copy);
        return copy.getId() != -1;
    }

    private NubController getNubController() {
        return (NubController) _sessionController.getFeatureController(Feature.NUB);
    }

    private String takeSelected(List<NubRequest> nubRequests) {
        int count = 0;
        for (NubRequest nubRequest : nubRequests) {
            if (_cooperationTools.isTakeEnabled(Feature.NUB, nubRequest.getStatus(), nubRequest.getAccountId(), nubRequest.getIk())) {
                nubRequest.setAccountId(_sessionController.getAccountId());
                _nubRequestFacade.saveNubRequest(nubRequest);
                count++;
            }
        }
        int total = nubRequests.size();
        String msg = (count) + " von " + total + " Anfragen konnten in Ihren \"Besitz\" übernommen werden.";
        if (total > count) {
            msg += "\nDie übrigen Anfragen befanden sich bereits in Ihrem Besitz oder Sie sind nicht berechtigt, diese zu übernehmen.";
        }
        String script = "alert ('" + msg.replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
        _sessionController.setScript(script);
        return "";
    }

}
