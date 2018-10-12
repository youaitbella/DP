package org.inek.dataportal.cert.Helper;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class LetterConverterTest {

    public LetterConverterTest() {
    }

    @Test
    public void describeLettersReturnsString() {
        List<String> result = LetterConverter.describeLetters("T3t!");
        assertThat(result).isNotNull().isNotEmpty().containsOnly("T = Theodor", "3 = Drei", "t = Theodor(klein)", "! = Ausrufezeichen");
    }

}
