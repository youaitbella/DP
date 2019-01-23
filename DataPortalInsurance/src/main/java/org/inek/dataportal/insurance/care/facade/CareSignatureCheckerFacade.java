package org.inek.dataportal.insurance.care.facade;

import java.util.List;
import org.inek.dataportal.common.data.AbstractDataAccess;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.inek.dataportal.insurance.care.backingbean.SignatureEntry;

@RequestScoped
@Transactional
public class CareSignatureCheckerFacade extends AbstractDataAccess {

    public SignatureEntry retrieveInformationForSignature(String signature) {
        String sql = "select bi.prbiSignature, bi.prbiIk, cu.cuName, bi.prbiYear, bi.prbiQuarter\n"
                + "from care.ProofRegulationBaseInformation bi\n"
                + "join CallCenterDB.dbo.ccCustomer cu on cu.cuIK = bi.prbiIk\n"
                + "where prbiStatusId = 10\n"
                + "and bi.prbiSignature = ?";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter(1, signature);
        List resultList = query.getResultList();
        Object[] object = (Object[]) resultList.get(0);
        return new SignatureEntry((String) object[0], (int) object[1], (String) object[2], (int) object[3], (int) object[4]);
    }
}
