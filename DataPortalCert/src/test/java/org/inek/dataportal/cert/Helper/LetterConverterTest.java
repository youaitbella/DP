package org.inek.dataportal.cert.Helper;

import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LetterConverterTest {

    public LetterConverterTest() {
    }

    private static final Map<String, String> CHAR_2_WORD = new HashMap<>();
    
    static {
        CHAR_2_WORD.put("a", "Anton");
        CHAR_2_WORD.put("ä", "Ärger");
        CHAR_2_WORD.put("b", "Berta");
        CHAR_2_WORD.put("c", "Cäsar");
        CHAR_2_WORD.put("d", "Dora");
        CHAR_2_WORD.put("e", "Emil");
        CHAR_2_WORD.put("f", "Friedrich");
        CHAR_2_WORD.put("g", "Gustav");
        CHAR_2_WORD.put("h", "Heinrich");
        CHAR_2_WORD.put("i", "Ida");
        CHAR_2_WORD.put("j", "Julius");
        CHAR_2_WORD.put("k", "Kaufmann");
        CHAR_2_WORD.put("l", "Ludwig");
        CHAR_2_WORD.put("m", "Martha");
        CHAR_2_WORD.put("n", "Nordpol");
        CHAR_2_WORD.put("o", "Otto");
        CHAR_2_WORD.put("ö", "Ökonom");
        CHAR_2_WORD.put("p", "Paula");
        CHAR_2_WORD.put("q", "Quelle");
        CHAR_2_WORD.put("r", "Richard");
        CHAR_2_WORD.put("s", "Samuel");
        CHAR_2_WORD.put("ß", "Eszett");
        CHAR_2_WORD.put("t", "Theodor");
        CHAR_2_WORD.put("u", "Ulrich");
        CHAR_2_WORD.put("ü", "Übermut");
        CHAR_2_WORD.put("v", "Viktor");
        CHAR_2_WORD.put("w", "Wilhelm");
        CHAR_2_WORD.put("x", "Xanthippe");
        CHAR_2_WORD.put("y", "Ypsilon");
        CHAR_2_WORD.put("z", "Zeppelin");
        CHAR_2_WORD.put("1", "Eins");
        CHAR_2_WORD.put("2", "Zwei");
        CHAR_2_WORD.put("3", "Drei");
        CHAR_2_WORD.put("4", "Vier");
        CHAR_2_WORD.put("5", "Fünf");
        CHAR_2_WORD.put("6", "Sechs");
        CHAR_2_WORD.put("7", "Sieben");
        CHAR_2_WORD.put("8", "Acht");
        CHAR_2_WORD.put("9", "Neun");
        CHAR_2_WORD.put("0", "Null");
        CHAR_2_WORD.put("!", "Ausrufezeichen");
        CHAR_2_WORD.put("?", "Fragezeichen");
        CHAR_2_WORD.put(".", "Punkt");
        CHAR_2_WORD.put(",", "Komma");
        CHAR_2_WORD.put("#", "Hashtag");
        CHAR_2_WORD.put("$", "Dollar");
        CHAR_2_WORD.put("&", "Kaufmannsund");
        CHAR_2_WORD.put("-", "Bindestrich");
        CHAR_2_WORD.put("_", "Unterstrich");
        CHAR_2_WORD.put("/", "Schrägstrich");
    }
    
    
    @Test
    public void describeLettersWithLowerCaseCharsTest() {
        for (String key : CHAR_2_WORD.keySet()) {
            List<String> result = new ArrayList<String>();
            result = LetterConverter.describeLetters(key);

            if (!key.matches("[^a-z ä-ü ]")) {
                    assertThat(result.get(0)).isEqualTo(key + " = " + CHAR_2_WORD.get(key)+"(klein)");
            }else{
                    assertThat(result.get(0)).isEqualTo(key + " = " + CHAR_2_WORD.get(key)); 
            } 
        }
    }
    @Test
    public void describeLettersWithUpperCaseCharsTest() {
        for (String key : CHAR_2_WORD.keySet()) {
            List<String> result = new ArrayList<String>();
            if(key.equals("ß")){
                result = LetterConverter.describeLetters(key);
                assertThat(result.get(0)).isEqualTo(key + " = " + CHAR_2_WORD.get(key));
            }else{
                result = LetterConverter.describeLetters(key.toUpperCase());
                assertThat(result.get(0)).isEqualTo(key.toUpperCase() + " = " + CHAR_2_WORD.get(key));
            }
        }
    }
    
    @Test
    public void describeLettersWithScharfesSTest() {
        String inputChar = "ß";
        String outputString = CHAR_2_WORD.get(inputChar);
        
        List<String> result = LetterConverter.describeLetters(inputChar);
        assertThat(result.get(0)).isEqualTo(inputChar + " = " + outputString);
    }
    
    @Test
    public void describeLettersWithUpperCharTest() {
        String inputChar = "A";
        String outputString = CHAR_2_WORD.get(inputChar.toLowerCase());
        
        List<String> result = LetterConverter.describeLetters(inputChar);
        assertThat(result.get(0)).isEqualTo(inputChar + " = " + outputString);
    }
    
    @Test
    public void describeLettersWithLowerCharTest() {
        String inputChar = "a";
        String outputString = CHAR_2_WORD.get(inputChar) + "(klein)";
        
        List<String> result = LetterConverter.describeLetters(inputChar);
        assertThat(result.get(0)).isEqualTo(inputChar + " = " + outputString);
    }
    
    @Test
    public void describeLettersWithNumberTest() {
        String inputChar = "0";
        String outputString = CHAR_2_WORD.get(inputChar);
        
        List<String> result = LetterConverter.describeLetters(inputChar);
        assertThat(result.get(0)).isEqualTo(inputChar + " = " + outputString);
    }

}
