package org.inek.dataportal.calc.backingbean;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.calc.BeanValidator;
import org.inek.dataportal.calc.entities.psy.KGPListCostCenter;
import org.junit.jupiter.api.Test;

class DataImporterTest {

    @Test
    public void checkValidatorOfEntity() {
        KGPListCostCenter kgpListCostCenter = new KGPListCostCenter();
        kgpListCostCenter.setCostCenterText("EinLangerStringUeber50ZeichenUmEinBeanValidationConstraintAuszulösen");
        kgpListCostCenter.setCostCenterNumber("was für ein Name für einer Zahl");
        String msg = BeanValidator.validateData(kgpListCostCenter);
        Assertions.assertThat(msg).isNotEmpty().contains("zwischen 0 und");
    }

}