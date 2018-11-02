package org.inek.dataportal.cert.Helper;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class LetterConverterTest {

    public LetterConverterTest() {
    }

    @Test
    public void describeLettersReturnsString() {
        List<String> result = LetterConverter.describeLetters("abcdefghijklmnopqrstuvwxyzäößüABCDEFGHIJKLMNOPQRSTUVWXYZÄÖßÜ01234567890!?.,#$&-_/");
        assertThat(result).isNotNull().isNotEmpty().containsOnly("a = Anton(klein)", "b = Berta(klein)", "c = Cäsar(klein)", "d = Dora(klein)", "e = Emil(klein)", "f = Friedrich(klein)", "g = Gustav(klein)", "h = Heinrich(klein)", "i = Ida(klein)", "j = Julius(klein)", "k = Kaufmann(klein)", "l = Ludwig(klein)", "m = Martha(klein)", "n = Nordpol(klein)", "o = Otto(klein)", "p = Paula(klein)", "q = Quelle(klein)", "r = Richard(klein)", "s = Samuel(klein)", "t = Theodor(klein)", "u = Ulrich(klein)", "v = Viktor(klein)", "w = Wilhelm(klein)", "x = Xanthippe(klein)", "y = Ypsilon(klein)", "z = Zeppelin(klein)", "ä = Ärger(klein)", "ö = Ökonom(klein)", "ß = Eszett(klein)", "ü = Übermut(klein)", "A = Anton", "B = Berta", "C = Cäsar", "D = Dora", "E = Emil", "F = Friedrich", "G = Gustav", "H = Heinrich", "I = Ida", "J = Julius", "K = Kaufmann", "L = Ludwig", "M = Martha", "N = Nordpol", "O = Otto", "P = Paula", "Q = Quelle", "R = Richard", "S = Samuel", "T = Theodor", "U = Ulrich", "V = Viktor", "W = Wilhelm", "X = Xanthippe", "Y = Ypsilon", "Z = Zeppelin", "Ä = Ärger", "Ö = Ökonom", "ß = Eszett(klein)", "Ü = Übermut", "0 = Null", "1 = Eins", "2 = Zwei", "3 = Drei", "4 = Vier", "5 = Fünf", "6 = Sechs", "7 = Sieben", "8 = Acht", "9 = Neun", "! = Ausrufezeichen", "? = Fragezeichen", ". = Punkt", ", = Komma", "# = Hashtag", "$ = Dollar", "& = Kaufmannsund", "- = Bindestrich", "_ = Unterstrich", "/ = Schrägstrich");
    }

}
