package org.inek.dataportal.drg.additionalcost;

import static org.assertj.core.api.Assertions.assertThat;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.drg.additionalcost.entity.AdditionalCost;
import org.inek.dataportal.drg.additionalcost.facade.AdditionalCostFacade;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CI_AdditionalCostListTest {

    @Test
    public void confirmMessageWithLowStatusReturnsDeleteMessage() {
        int status = 9;
        String text = Utils.getMessage("msgConfirmDelete");
        performTest(status, text);
    }

    @Test
    public void confirmMessageWithHighStatusReturnsRetireMessage() {
        int status = 10;
        String text = Utils.getMessage("msgConfirmRetire");
        performTest(status, text);
    }

    public void performTest(int status, String text) {
        int ik = 222222222;
        AdditionalCost addCost = mock(AdditionalCost.class);
        when(addCost.getIk()).thenReturn(ik);
        when(addCost.getStatusId()).thenReturn(status);
        AdditionalCostFacade acFacade = mock(AdditionalCostFacade.class);
        when(acFacade.findAdditionalCost(4711)).thenReturn(addCost);
        AdditionalCostList acList = new AdditionalCostList(acFacade, null);

        String result = acList.getConfirmMessage(4711);

        String expectet = "return confirm ('Meldung für " + ik + "\\r\\n" + text + "');";
        assertThat(result).isEqualTo(expectet);
    }

}
