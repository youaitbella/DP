package org.inek.dataportal.insurance.specificfunction.entity;

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
        AgreedCenter center = agreement.addAgreedCenter();
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

//    public void deleteAgreedCenter(AgreedCenter center) {
//        _agreedCenters.remove(center);
//    }
//
//    public void removeEmptyAgreedCenters() {
//        _agreedCenters.removeIf(c -> c.isEmpty());
//    }
//    
}
