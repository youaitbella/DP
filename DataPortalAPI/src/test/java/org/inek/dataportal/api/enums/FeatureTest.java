package org.inek.dataportal.api.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 *
 * @author muellermi
 */
public class FeatureTest {
    
    public FeatureTest() {
    }

    @Test
    public void featureFromIdReturnsFeatureForValidId() {
        Feature result = Feature.getFeatureFromId(1);
        assertThat(result).isEqualTo(Feature.ADMIN);
    }
    
    @Test
    public void featureFromIdThrowsExceptionForInvalidId() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> Feature.getFeatureFromId(-1));
        assertThat(exception.getMessage()).isEqualTo("Failed to obtain feature. Unknown id -1");
    }
    
}
