package org.inek.dataportal.care.proof;

import org.inek.dataportal.common.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProofFacadeTest {

    @Test
    public void checkIkYearQuarter() {
        Set<Integer> iks = new HashSet<>();
        iks.add(111111111);
        iks.add(222222222);

        ProofFacade facade = mock(ProofFacade.class);
        when(facade.retrievePossibleIkYearQuarters(iks)).thenCallRealMethod();
        when(facade.retrieveExistingInfo(iks)).thenReturn(Collections.emptyList());
        List<IkYearQuarter> ikYearQuarters = facade.retrievePossibleIkYearQuarters(iks);

        int countQuarters = 4 + (DateUtils.currentMonth() + 2) / 3; // 4 of prev year

        assertThat(ikYearQuarters).isNotNull().isNotEmpty().hasSize(countQuarters * iks.size());
    }
}
