package org.inek.dataportal.drg.valuationratio;

import org.inek.dataportal.drg.valuationratio.entities.ValuationRatio;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ValuationRatioTest {

    public ValuationRatioTest(){}

    @Test
    public void drgOnListEmpty(){
        ValuationRatio valuationRatio = new ValuationRatio();
        assertThat(valuationRatio.DrgOnList(true)).isEqualTo("<keine>");
    }

    @Test
    public void drgOnListOverMedianI68E(){
        ValuationRatio valuationRatio = new ValuationRatio();
        valuationRatio.setI68eList(true);
        assertThat(valuationRatio.DrgOnList(true)).isEqualTo("I68E");
    }

    @Test
    public void drgOnListOverMedianI68D(){
        ValuationRatio valuationRatio = new ValuationRatio();
        valuationRatio.setI68eList(false);
        valuationRatio.setI68dList(true);
        assertThat(valuationRatio.DrgOnList(true)).isEqualTo("I68D");
    }

    @Test
    public void drgOnListOverMedianI68E_I68D(){
        ValuationRatio valuationRatio = new ValuationRatio();
        valuationRatio.setI68eList(true);
        valuationRatio.setI68dList(true);
        assertThat(valuationRatio.DrgOnList(true)).isEqualTo("I68D, I68E");
    }

    @Test
    public void drgOnListUnderMedianI68E_I68D(){
        ValuationRatio valuationRatio = new ValuationRatio();
        valuationRatio.setI68eList(false);
        valuationRatio.setI68dList(false);
        assertThat(valuationRatio.DrgOnList(false)).isEqualTo("I68D, I68E");
    }

    @Test
    public void drgOnListUnderMedianI68E(){
        ValuationRatio valuationRatio = new ValuationRatio();
        valuationRatio.setI68eList(false);
        valuationRatio.setI68dList(true);
        assertThat(valuationRatio.DrgOnList(false)).isEqualTo("I68E");
    }

    @Test
    public void drgOnListUnderMedianI68D(){
        ValuationRatio valuationRatio = new ValuationRatio();
        valuationRatio.setI68eList(true);
        valuationRatio.setI68dList(false);
        assertThat(valuationRatio.DrgOnList(false)).isEqualTo("I68D");
    }

}
