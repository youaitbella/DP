package org.inek.dataportal.insurance.specificfunction.entity;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecificFunctionAgreementTest {

    @Test
    public void addAgreedCenterContains2CenterAfterAdding2Centers() {
        SpecificFunctionAgreement agreement = new SpecificFunctionAgreement();
        agreement.addAgreedCenter();
        agreement.addAgreedCenter();
        assertThat(agreement.getAgreedCenters().size()).isEqualTo(2);
    }

    @Test
    public void deleteAgreedCenterContains1CenterAfterAdding2CentersAndDeleting1OfThem() {
        SpecificFunctionAgreement agreement = new SpecificFunctionAgreement();
        AgreedCenter center = agreement.addAgreedCenter();
        agreement.addAgreedCenter();
        agreement.deleteAgreedCenter(center);
        assertThat(agreement.getAgreedCenters().size()).isEqualTo(1);
    }

    @Test
    public void removeEmptyAgreedCentersReturns0IfTheListContainsEmptyCentersOnly() {
        SpecificFunctionAgreement agreement = new SpecificFunctionAgreement();
        agreement.addAgreedCenter();
        agreement.addAgreedCenter();
        agreement.removeEmptyAgreedCenters();
        assertThat(agreement.getAgreedCenters().size()).isEqualTo(0);
    }

    @Test
    public void removeEmptyAgreedCentersReturns1IfTheListContains1NoneEmptyCenters() {
        SpecificFunctionAgreement agreement = new SpecificFunctionAgreement();
        AgreedCenter center = agreement.addAgreedCenter();
        center.setLocation("Testlocation");
        agreement.addAgreedCenter();
        agreement.removeEmptyAgreedCenters();
        assertThat(agreement.getAgreedCenters().size()).isEqualTo(1);
    }

    @Test
    public void removeEmptyAgreedCentersReturns2IfTheListContains2NoneEmptyCenters() {
        SpecificFunctionAgreement agreement = new SpecificFunctionAgreement();
        AgreedCenter center = agreement.addAgreedCenter();
        center.setLocation("Testlocation1");
        center = agreement.addAgreedCenter();
        center.setLocation("Testlocation2");
        agreement.removeEmptyAgreedCenters();
        assertThat(agreement.getAgreedCenters().size()).isEqualTo(2);
    }
    
    @Test
    public void addAgreedCenterReturnsSequencenumbers_1_2_3_AfterAdding3Centers(){
        SpecificFunctionAgreement agreement = new SpecificFunctionAgreement();
        agreement.addAgreedCenter();
        agreement.addAgreedCenter();
        agreement.addAgreedCenter();
        
        List<Integer> seqNumbers = agreement.getAgreedCenters().stream().map(c -> c.getSequence()).collect(Collectors.toList());
        assertThat(seqNumbers).isNotNull().isNotEmpty().containsOnly(1, 2, 3);
    }

    @Test
    public void addAgreedCenterReturnsSequencenumbers_1_2_4_AfterAdding4Centers(){
        SpecificFunctionAgreement agreement = new SpecificFunctionAgreement();
        agreement.addAgreedCenter();
        agreement.addAgreedCenter();
        AgreedCenter center = agreement.addAgreedCenter();
        agreement.addAgreedCenter();
        
        agreement.deleteAgreedCenter(center);
        List<Integer> seqNumbers = agreement.getAgreedCenters().stream().map(c -> c.getSequence()).collect(Collectors.toList());
        assertThat(seqNumbers).isNotNull().isNotEmpty().containsOnly(1, 2, 4);
    }

}
