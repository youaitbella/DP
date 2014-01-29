package org.inek.dataportal.facades;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.inek.dataportal.entities.AccountDocument;
import org.inek.dataportal.entities.NubProposal;
import org.inek.dataportal.helper.structures.Triple;

@Stateless
public class AccountDocumentFacade extends AbstractFacade<AccountDocument> {

    public AccountDocumentFacade() {
        super(AccountDocument.class);
    }
    
    public List<Triple> getDocInfos(int accountId) {
        List<AccountDocument> docs = findAll(accountId);
        List<Triple> docInfos = new ArrayList<>(); 
        for (AccountDocument doc : docs){
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
            Date d = doc.getTimestamp();
            docInfos.add(new Triple(doc.getId(), doc.getName(), df.format(d)));
        }
        return docInfos;
    }
    
    public List<AccountDocument> findAll(int accountId) {
        String sql = "SELECT p FROM AccountDocument p WHERE p._accountId = :accountId ORDER BY p._adId DESC";
        Query query = getEntityManager().createQuery(sql, AccountDocument.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }
    
    public boolean readDoc(int docId) {
        AccountDocument doc = find(docId);
        if(doc.isRead()) {
            return true;
        }
        return false;
    }
}
