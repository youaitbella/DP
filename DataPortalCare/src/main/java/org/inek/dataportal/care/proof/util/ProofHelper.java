package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.SensitiveDomain;
import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.facades.BaseDataFacade;
import org.inek.dataportal.care.proof.ProofFacade;
import org.inek.dataportal.care.proof.ProofWardInfo;
import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.care.proof.entity.ProofWard;
import org.inek.dataportal.common.utils.DateUtils;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

public class ProofHelper {

    private static final int LAST_DAY_Q1 = 31;
    private static final int LAST_MONTH_Q1 = 3;

    private static final int LAST_DAY_Q2 = 30;
    private static final int LAST_MONTH_Q2 = 6;

    private static final int LAST_DAY_Q3 = 30;
    private static final int LAST_MONTH_Q3 = 9;

    private static final int LAST_DAY_Q4 = 31;
    private static final int LAST_MONTH_Q4 = 12;

    public static boolean proofIsAllowedForSend(ProofRegulationBaseInformation baseInfo) {
        return proofIsAllowedForSend(baseInfo, new Date());
    }

    public static boolean proofIsAllowedForSend(ProofRegulationBaseInformation baseInfo, Date currentDate) {
        Date allowedSince;
        switch (baseInfo.getQuarter()) {
            case 1:
                allowedSince = createDate(LAST_DAY_Q1, LAST_MONTH_Q1, baseInfo.getYear());
                break;
            case 2:
                allowedSince = createDate(LAST_DAY_Q2, LAST_MONTH_Q2, baseInfo.getYear());
                break;
            case 3:
                allowedSince = createDate(LAST_DAY_Q3, LAST_MONTH_Q3, baseInfo.getYear());
                break;
            case 4:
                allowedSince = createDate(LAST_DAY_Q4, LAST_MONTH_Q4, baseInfo.getYear());
                break;
                default:
                    allowedSince = createDate(1,1,2000);
                    break;
        }
        return currentDate.after(allowedSince);
    }

    private static Date createDate(int day, int month, int year) {
        LocalDate date = LocalDate.of(year, Month.of(month), day);

        return java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static Proof fillProof(Proof proof, ProofWardInfo proofWardInfo, int month, Shift shift,
                                  ProofFacade proofFacade, BaseDataFacade baseDataFacade) {
        proof.setMonth(Months.getById(month));
        proof.setShift(shift);
        proof.setBeds(proofWardInfo.getBeds());
        int duration = DateUtils.duration(proofWardInfo.getFrom(), proofWardInfo.getTo());
        int ik = proof.getBaseInformation().getIk();
        ProofWard proofWard = proofFacade.retrieveOrCreateProofWard(ik, proofWardInfo.getLocationNumber(),
                proofWardInfo.getLocationP21(), proofWardInfo.getWardName());
        proof.setProofWard(proofWard);
        proof.setValidFrom(proofWardInfo.getFrom());
        proof.setValidTo(proofWardInfo.getTo());
        proof.setCountShift(duration);
        proof.setDeptNumbers(proofWardInfo.depts());
        proof.setDeptNames(proofWardInfo.deptNames());
        proof.setSensitiveDomains(proofWardInfo.sensitiveDomains());
        int year = proof.getBaseInformation().getYear();
        SensitiveDomain sensitiveDomain = baseDataFacade.determineSignificantDomain(year, proofWardInfo.sensitiveDomainSet());
        proof.setSignificantSensitiveDomain(sensitiveDomain);
        return proof;
    }

}
