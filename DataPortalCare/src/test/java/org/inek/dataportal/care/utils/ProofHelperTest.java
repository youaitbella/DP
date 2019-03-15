package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ProofHelperTest {

    @Test
    void proofIsAllowedForSendQuarder1Test() {
        ProofRegulationBaseInformation baseInfo = new ProofRegulationBaseInformation();
        baseInfo.setQuarter(1);
        baseInfo.setYear(2018);

        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,2,2018))).isFalse();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(31,3,2018))).isFalse();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,4,2018))).isTrue();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(15,4,2018))).isTrue();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,3,2019))).isTrue();
    }

    @Test
    void proofIsAllowedForSendQuarder2Test() {
        ProofRegulationBaseInformation baseInfo = new ProofRegulationBaseInformation();
        baseInfo.setQuarter(2);
        baseInfo.setYear(2018);

        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,2,2018))).isFalse();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,5,2018))).isFalse();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(30,6,2018))).isFalse();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,7,2018))).isTrue();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(15,7,2018))).isTrue();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,5,2019))).isTrue();
    }

    @Test
    void proofIsAllowedForSendQuarder3Test() {
        ProofRegulationBaseInformation baseInfo = new ProofRegulationBaseInformation();
        baseInfo.setQuarter(3);
        baseInfo.setYear(2018);

        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,2,2018))).isFalse();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,8,2018))).isFalse();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(30,9,2018))).isFalse();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,10,2018))).isTrue();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(15,10,2018))).isTrue();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,8,2019))).isTrue();
    }

    @Test
    void proofIsAllowedForSendQuarder4Test() {
        ProofRegulationBaseInformation baseInfo = new ProofRegulationBaseInformation();
        baseInfo.setQuarter(4);
        baseInfo.setYear(2018);

        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,2,2018))).isFalse();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,11,2018))).isFalse();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(31,12,2018))).isFalse();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,1,2019))).isTrue();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(15,1,2019))).isTrue();
        Assertions.assertThat(ProofHelper.proofIsAllowedForSend(baseInfo, createDate(1,12,2020))).isTrue();
    }

    private Date createDate(int day, int month, int year) {
        LocalDate date = LocalDate.of(year, Month.of(month), day);

        return java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}