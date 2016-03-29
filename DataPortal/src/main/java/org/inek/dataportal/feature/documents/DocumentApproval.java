package org.inek.dataportal.feature.documents;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountDocument;
import org.inek.dataportal.entities.account.WaitingDocument;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.account.WaitingDocumentFacade;
import org.inek.dataportal.helper.StreamHelper;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.helper.structures.DocInfo;
import org.inek.dataportal.helper.tree.AccountTreeNode;
import org.inek.dataportal.helper.tree.DocumentInfoTreeNode;
import org.inek.dataportal.mail.Mailer;
import org.inek.portallib.structures.KeyValue;
import org.inek.portallib.tree.RootNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "DocumentUpload")
public class DocumentApproval implements TreeNodeObserver {
    private static final Logger _logger = Logger.getLogger("DocumentApproval");

    @Inject SessionController _sessionController;
    @Inject WaitingDocumentFacade _waitingDocFacade;
    @Inject AccountDocumentFacade _accountDocFacade;
    @Inject AccountFacade _accountFacade;

    private final RootNode _rootNode = RootNode.create(0, this);

    @PostConstruct
    private void init() {
        _rootNode.setExpanded(true);
    }

    public RootNode getRootNode() {
        return _rootNode;
    }

    @Override
    public void obtainChildren(TreeNode treeNode, Collection<TreeNode> children) {
        if (treeNode instanceof RootNode) {
            obtainRootNodeChildren((RootNode) treeNode, children);
        }
        if (treeNode instanceof AccountTreeNode) {
            obtainAccountNodeChildren((AccountTreeNode) treeNode, children);
        }
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode, Collection<TreeNode> children) {
        return children;
    }

    private void obtainRootNodeChildren(RootNode node, Collection<TreeNode> children) {
        List<Account> accounts = _waitingDocFacade.getAgents();
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
        int agentId = accountTreeNode.getId();
        List<DocInfo> infos = _waitingDocFacade.getDocInfos(agentId);
        List<Integer> checked = new ArrayList<>();
        for (TreeNode child : accountTreeNode.copyChildren()) {
            if (child.isChecked()) {
                checked.add(child.getId());
            }
        }
        accountTreeNode.getChildren().clear();
        for (DocInfo info : infos) {
            DocumentInfoTreeNode node = DocumentInfoTreeNode.create(accountTreeNode, info, this);
            if (checked.contains(node.getId())) {
                node.setChecked(true);
            }
            accountTreeNode.getChildren().add(node);
        }
    }

    public void approveSelected() {
        Map<Integer, Set<String>> accountMailInfos = new HashMap<>();
        //Set<Integer> accountIds = new HashSet<>();
        for (TreeNode node : _rootNode.getSelectedNodes()) {
            KeyValue<Integer, String> mailInfo = approveAndReturnMailInfo(node.getId());
            int accountId = mailInfo.getKey();
            Set<String> mailInfos = accountMailInfos.get(accountId);
            if (mailInfos == null) {
                mailInfos = new HashSet<>();
            }
            mailInfos.add(mailInfo.getValue());
            accountMailInfos.put(accountId, mailInfos);
        }
        for (Entry<Integer, Set<String>> entry : accountMailInfos.entrySet()) {
            for (String jsonMail : entry.getValue()) {
                notify(entry.getKey(), jsonMail);
            }
        }
        _rootNode.refresh();
    }

    public void approve(int docId) {
        KeyValue<Integer, String> mailInfo = approveAndReturnMailInfo(docId);
        notify(mailInfo.getKey(), mailInfo.getValue());
        _rootNode.refresh();
    }

    public KeyValue<Integer, String> approveAndReturnMailInfo(int docId) {
        WaitingDocument waitingDoc = _waitingDocFacade.find(docId);
        AccountDocument accountDoc = new AccountDocument(waitingDoc.getName());
        accountDoc.setAccountId(waitingDoc.getAccountId());
        accountDoc.setContent(waitingDoc.getContent());
        accountDoc.setDomain(waitingDoc.getDomain());
        accountDoc.setAgentAccountId(_sessionController.getAccountId());
        accountDoc.setValidity(waitingDoc.getValidity());
        String jsonMail = waitingDoc.getJsonMail();
        _accountDocFacade.save(accountDoc);
        _waitingDocFacade.remove(waitingDoc);
        return new KeyValue<>(accountDoc.getAccountId(), jsonMail);
    }

    @Inject private Mailer _mailer;

    private void notify(int accountId, String jsonMail) {
        if (jsonMail.isEmpty()) {
            return;
        }

        Account account = _accountFacade.find(accountId);
        Charset charset = Charset.forName("UTF-8");
        JsonReader reader = Json.createReader(new ByteArrayInputStream(jsonMail.getBytes(charset)));
        JsonObject mailInfo = reader.readObject();
        String from = mailInfo.getString("from", "datenportal@inek.org");
        String bcc = mailInfo.getString("bcc");
        String subject = mailInfo.getString("subject");
        String body = mailInfo.getString("body");
        _mailer.sendMailFrom(from, account.getEmail(), bcc, subject, body);

    }
    
    public String downloadDocument(int docId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        WaitingDocument doc = _waitingDocFacade.find(docId);
        if (_sessionController.getAccountId() != doc.getAccountId() && !_sessionController.isInekUser(Feature.DOCUMENTS)) {
            return "";
        }
        try {
            byte[] buffer = doc.getContent();
            externalContext.setResponseHeader("Content-Type", "text/plain");
            externalContext.setResponseHeader("Content-Length", "" + buffer.length);
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + doc.getName() + "\"");
            ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            new StreamHelper().copyStream(is, externalContext.getResponseOutputStream());

        } catch (IOException ex) {
            _logger.log(Level.SEVERE, null, ex);
            return Pages.Error.URL();
        }
        facesContext.responseComplete();
        return "";
    }
    
}
