package org.inek.dataportal.common.data.icmt.facade;

import javax.faces.validator.ValidatorException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class CI_CustomerFacadeTest {

    private CustomerFacade createCustomerFacade() {
        CustomerFacade facade = spy(CustomerFacade.class);
        doReturn(false).when(facade).checkIK(anyInt());
        doReturn(true).when(facade).checkIK(eq(261101015));
        doReturn(true).when(facade).checkIK(eq(260100125));
        return facade;
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "XXX", "111111111", "", "1234567890", "261101010"})
    public void isFormalCorrectIkReturnsFalseForIncorretIk(String ik) {
        CustomerFacade facade = createCustomerFacade();
        Boolean result = facade.isFormalCorrectIk(ik);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"261101015", "260100125", "333333337"})
    public void isFormalCorrectIkReturnsTrueForCorretIk(String ik) {
        CustomerFacade facade = createCustomerFacade();
        Boolean result = facade.isFormalCorrectIk(ik);
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"261101015", "260100125"})
    public void isValidIkReturnsTrueForValidIk(String ik) {
        CustomerFacade facade = createCustomerFacade();
        Boolean result = facade.isValidIK(ik);
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "XXX", "111111111", "", "1234567890", "261101010", "333333337"})
    public void isValidIkReturnsFalseForIncorretOrInvalidIk(String ik) {
        CustomerFacade facade = createCustomerFacade();
        Boolean result = facade.isValidIK(ik);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"261101015", "260100125"})
    public void isIkValidThrowsNoExceptionForValidIk(String ik) {
        CustomerFacade facade = createCustomerFacade();
        facade.isIKValid(null, null, ik);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "XXX", "111111111", "", "1234567890", "261101010"})
    public void isIkValidThrowsExceptionForIncorrectIk(String ik) {
        CustomerFacade facade = createCustomerFacade();

        Throwable exception = assertThrows(ValidatorException.class, () -> {
            facade.isIKValid(null, null, ik);
        });
        
        assertThat(exception.getMessage()).isEqualTo("Ungültiges IK");
    }

    @ParameterizedTest
    @ValueSource(strings = {"333333337"})
    public void isIkValidThrowsExceptionForInvalidIk(String ik) {
        CustomerFacade facade = createCustomerFacade();

        Throwable exception = assertThrows(ValidatorException.class, () -> {
            facade.isIKValid(null, null, ik);
        });
        
        assertThat(exception.getMessage()).isEqualTo("Unbekanntes IK");
    }

}
