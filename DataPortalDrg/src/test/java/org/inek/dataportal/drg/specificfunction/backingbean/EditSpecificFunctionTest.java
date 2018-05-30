/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.drg.specificfunction.backingbean;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.specificfunction.entity.RequestAgreedCenter;
import org.inek.dataportal.common.specificfunction.entity.SpecificFunctionRequest;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class EditSpecificFunctionTest {

    @Test
    public void clearRequestAgreedCentersForSaveTest() {
        SpecificFunctionRequest request = new SpecificFunctionRequest();
        EditSpecificFunction edit = new EditSpecificFunction();
        edit.clearRequestAgreedCentersForSave(request);
        Assertions.assertThat(request.getRequestAgreedCenters().isEmpty()).isTrue().as("List should be empty");

        request.setHasAgreement(true);
        edit.clearRequestAgreedCentersForSave(request);
        Assertions.assertThat(request.getRequestAgreedCenters().isEmpty()).isTrue().as("List should be empty");

        request.setHasAgreement(true);
        request.getRequestAgreedCenters().add(new RequestAgreedCenter());
        edit.clearRequestAgreedCentersForSave(request);
        Assertions.assertThat(request.getRequestAgreedCenters().isEmpty()).isFalse().as("1 Element shoukd be in List");
        request.getRequestAgreedCenters().add(new RequestAgreedCenter());
        edit.clearRequestAgreedCentersForSave(request);
        Assertions.assertThat(request.getRequestAgreedCenters().size() == 2).isTrue().as("2 Element shoukd be in List");

        request.setHasAgreement(false);
        request.getRequestAgreedCenters().add(new RequestAgreedCenter());
        edit.clearRequestAgreedCentersForSave(request);
        Assertions.assertThat(request.getRequestAgreedCenters().isEmpty()).isTrue().as("List should be empty");
    }

}
