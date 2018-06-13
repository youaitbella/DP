package org.inek.dataportal.common.specificfunction.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;



/**
 *
 * @author muellermi
 */
public class RequestAgreedCenterTest {
    
    public RequestAgreedCenterTest() {
    }

    @Test
    public void checkNewObjectIsEmpty() {
        RequestAgreedCenter agreedCenter = new RequestAgreedCenter();
        assertTrue(agreedCenter.isEmpty());
    }

    @Test
    public void checkObjectWithAmountIsNotEmpty() {
        RequestAgreedCenter agreedCenter = new RequestAgreedCenter();
        agreedCenter.setAmount(1);
        assertFalse(agreedCenter.isEmpty());
    }
    
    @Test
    public void checkObjectWithRemunerationKeyIsNotEmpty() {
        RequestAgreedCenter agreedCenter = new RequestAgreedCenter();
        agreedCenter.setRemunerationKey("xxx");
        assertFalse(agreedCenter.isEmpty());
    }
    
}
