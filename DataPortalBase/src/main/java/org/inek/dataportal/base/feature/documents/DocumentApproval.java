package org.inek.dataportal.base.feature.documents;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.base.facades.account.DocumentFacade;
import org.inek.dataportal.base.facades.account.WaitingDocumentFacade;
import org.inek.dataportal.base.helper.tree.DocumentInfoTreeNode;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.WaitingDocument;
import org.inek.dataportal.common.data.common.CommonDocument;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.helper.structures.DocInfo;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.tree.RootNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.entityTree.AccountTreeNode;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "DocumentUpload")
public class DocumentApproval implements TreeNodeObserver, Serializable {

    private static final Logger LOGGER = Logger.getLogger("DocumentApproval");

    @Inject
    private SessionController _sessionController;
    @Inject
    private WaitingDocumentFacade _waitingDocFacade;
    @Inject
    private DocumentFacade _documentFacade;

    private final RootNode _rootNode = RootNode.create(0, this);

    @PostConstruct
    private void init() {
        _rootNode.setExpanded(true);
    }

    // <editor-fold defaultstate="collapsed" desc="Property Filter">
    private String _filter = "";

    public String getFilter() {
        return _filter;
    }

    public void setFilter(String filter) {
        _filter = filter;
    }

    public String reload() {
        return "";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SortCriteria + state">
    private String _sortCriteria = "";
    private boolean _isDescending = false;

    public boolean isDescending() {
        return _isDescending;
    }

    public void setDescending(boolean isDescending) {
        _isDescending = isDescending;
    }

    public void setSortCriteria(String sortCriteria) {
        if (_sortCriteria.equals(sortCriteria)) {
            _isDescending = !_isDescending;
        } else {
            _isDescending = false;
        }
        _sortCriteria = sortCriteria == null ? "" : sortCriteria;
    }

    public String getSortCriteria() {
        return _sortCriteria;
    }
    // </editor-fold>

    public RootNode getRootNode() {
        return _rootNode;
    }

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        if (treeNode instanceof RootNode) {
            return obtainRootNodeChildren((RootNode) treeNode);
        }
        if (treeNode instanceof AccountTreeNode) {
            return obtainAccountNodeChildren((AccountTreeNode) treeNode);
        }
        return new ArrayList<>();
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        if (treeNode instanceof AccountTreeNode) {
            return sortAccountNodeChildren((AccountTreeNode) treeNode);
        }
        return treeNode.getChildren();
    }

    public Collection<TreeNode> sortAccountNodeChildren(AccountTreeNode treeNode) {
        Stream<DocumentInfoTreeNode> docInfoStream = treeNode.getChildren().stream().map(n -> (DocumentInfoTreeNode) n);
        if (!_filter.isEmpty()) {
            docInfoStream = docInfoStream
                    .filter(d -> d.getDocInfo().getName().toLowerCase().contains(_filter)
                    || d.getDocInfo().getDomain().toLowerCase().contains(_filter)
                    || d.getDocInfo().getTag().toLowerCase().contains(_filter));
        }
        int direction = _isDescending ? -1 : 1;
        switch (_sortCriteria.toLowerCase()) {
            case "domain":
                docInfoStream = docInfoStream.sorted((n1, n2) -> direction * n1.getDocInfo().getDomain().compareTo(n2.getDocInfo().getDomain()));
                break;
            case "document":
                docInfoStream = docInfoStream.sorted((n1, n2) -> direction * n1.getDocInfo().getName().compareTo(n2.getDocInfo().getName()));
                break;
            case "rceipient":
                docInfoStream = docInfoStream.sorted((n1, n2) -> direction * n1.getDocInfo().getTag().compareTo(n2.getDocInfo().getTag()));
                break;
            case "date":
                docInfoStream = docInfoStream.sorted((n1, n2) -> direction * n1.getDocInfo().getCreated().compareTo(n2.getDocInfo().getCreated()));
                break;
            default:
        }
        return docInfoStream.collect(Collectors.toList());
    }

    private Collection<TreeNode> obtainRootNodeChildren(RootNode treeNode) {
        List<Account> accounts = _waitingDocFacade.getAgents();
        Account currentUser = _sessionController.getAccount();
        if (accounts.contains(currentUser)) {
            // ensure current user is first, if in list
            accounts.remove(currentUser);
            accounts.add(0, currentUser);
        }
        List<? extends TreeNode> oldChildren = new ArrayList<>(treeNode.getChildren());
        Collection<TreeNode> children = new ArrayList<>();
        for (Account account : accounts) {
            Integer id = account.getId();
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == id).findFirst();
            AccountTreeNode childNode = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.create(treeNode, account, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (account == currentUser) {
                // auto expand user's own data
                childNode.expand();
            }
        }
        return children;
    }

    private Collection<TreeNode> obtainAccountNodeChildren(AccountTreeNode treeNode) {
        int agentId = treeNode.getId();
        List<DocInfo> infos = _waitingDocFacade.getDocInfos(agentId);
        List<Integer> checked = new ArrayList<>();
        for (TreeNode child : treeNode.getChildren()) {
            if (child.isChecked()) {
                checked.add(child.getId());
            }
        }
        Collection<TreeNode> children = new ArrayList<>();
        for (DocInfo info : infos) {
            DocumentInfoTreeNode node = DocumentInfoTreeNode.create(treeNode, info, this);
            if (checked.contains(node.getId())) {
                node.setChecked(true);
            }
            children.add(node);
        }
        return children;
    }

    public void approveSelected() {
        Map<String, Set<Account>> mailInfos = new HashMap<>();

        try {
            _rootNode.getSelectedNodes().stream().map((node) -> approveAndReturnMailInfo(node.getId())).forEach((mailInfo) -> {
                // collect mail info to reduce redundant mails
                String key = mailInfo.getJsonMail();
                Set<Account> accounts = mailInfos.get(key);
                if (accounts == null) {
                    accounts = new HashSet<>();
                }
                accounts.addAll(mailInfo.getAccounts());
                mailInfos.put(key, accounts);
            });
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
        }
        for (Entry<String, Set<Account>> entry : mailInfos.entrySet()) {
            notify(entry.getKey(), entry.getValue());
        }
        _rootNode.refresh();
    }

    public void delete(int docId) {
        WaitingDocument waitingDoc = _waitingDocFacade.find(docId);
        _waitingDocFacade.remove(waitingDoc);
        _rootNode.refresh();
    }

    public void approve(int docId) {
        try {
            MailInfo mailInfo = approveAndReturnMailInfo(docId);
            notify(mailInfo.getJsonMail(), mailInfo.getAccounts());
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
        }
        _rootNode.refresh();
    }

    private MailInfo approveAndReturnMailInfo(int docId) {
        WaitingDocument waitingDoc = _waitingDocFacade.find(docId);
        if (waitingDoc == null) {
            throw new IllegalArgumentException("WaitingDocument not found: " + docId);
        }
        CommonDocument commonDocument = createCommonDocument(waitingDoc);
        List<Account> accounts = waitingDoc.getAccounts();
        for (Account account : accounts) {
            createAccountDocument(account, commonDocument, waitingDoc.getValidity());
        }
        String jsonMail = waitingDoc.getJsonMail();
        _waitingDocFacade.remove(waitingDoc);
        return new MailInfo(jsonMail, accounts);
    }

    private CommonDocument createCommonDocument(WaitingDocument waitingDoc) {
        CommonDocument commonDocument = new CommonDocument(waitingDoc.getName());
        commonDocument.setContent(waitingDoc.getContent());
        commonDocument.setDomain(waitingDoc.getDomain());
        commonDocument.setAccountId(waitingDoc.getAgentAccountId());
        _documentFacade.saveCommonDocument(commonDocument);
        return commonDocument;
    }

    private void createAccountDocument(Account account, CommonDocument commonDocument, int validity) {
        _documentFacade.createAccountDocument(account, commonDocument, validity);
        if (account == _sessionController.getAccount()) {
            _sessionController.refreshAccount(account.getId());
        }
    }

    @Inject
    private Mailer _mailer;

    private void notify(String jsonMail, Collection<Account> accounts) {
        if (jsonMail.isEmpty()) {
            return;
        }

        Charset charset = Charset.forName("UTF-8");
        JsonReader reader = Json.createReader(new ByteArrayInputStream(jsonMail.getBytes(charset)));
        JsonObject mailInfo = reader.readObject();
        String from = mailInfo.getString("from", "Anfragen@datenstelle.de");
        String bcc = mailInfo.getString("bcc");
        String subject = mailInfo.getString("subject");
        for (Account account : accounts) {
            String salutation = _mailer.getFormalSalutation(account);
            String body = mailInfo.getString("body").replace("{formalSalutation}", salutation);
            _mailer.sendMailFrom(from, account.getEmail(), bcc, subject, body);
        }
    }

    public String downloadDocument(int docId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        WaitingDocument doc = _waitingDocFacade.find(docId);
        if (!_sessionController.isInekUser(Feature.DOCUMENTS)) {
            return "";
        }
        if (doc == null || doc.getContent() == null) {
            LOGGER.log(Level.SEVERE, "Doocument or content missing: {0}", docId);
            return Pages.Error.URL();
        }
        return Utils.downloadDocument(doc);
    }

    private static final class MailInfo {

        private final String _jsonMail;
        private final List<Account> _accounts;

        private MailInfo(String jsonMail, List<Account> accounts) {
            _jsonMail = jsonMail;
            _accounts = accounts;
        }

        public String getJsonMail() {
            return _jsonMail;
        }

        public List<Account> getAccounts() {
            return _accounts;
        }

    }
}
