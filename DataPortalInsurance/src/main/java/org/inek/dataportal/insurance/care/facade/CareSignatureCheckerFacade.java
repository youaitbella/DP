package org.inek.dataportal.insurance.care.facade;

import org.inek.dataportal.common.data.AbstractDataAccess;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional
public class CareSignatureCheckerFacade extends AbstractDataAccess {

    public List<Object[]> retrieveInformationForSignature(String signature) {
        String sql = "select bi.prbiSignature, bi.prbiIk, cu.cuName, bi.prbiYear, bi.prbiQuarter\n" +
                "from care.ProofRegulationBaseInformation bi\n" +
                "join CallCenterDB.dbo.ccCustomer cu on cu.cuIK = bi.prbiIk\n" +
                "where prbiStatusId = 10\n" +
                "and bi.prbiSignature = '" + signature + "'\n" +
                "COLLATE SQL_Latin1_General_Cp1_CS_AS";
        Query query = getEntityManager().createNativeQuery(sql);

        @SuppressWarnings("unchecked")
        List<Object[]> objects = query.getResultList();
        return objects;
    }
}
