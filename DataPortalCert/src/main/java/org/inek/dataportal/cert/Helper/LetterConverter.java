/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.cert.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author palenma
 */
public class LetterConverter {

    private static final Map<String, String> CHAR_2_WORD = new HashMap<>();

    static {
        CHAR_2_WORD.put("a", "Anton");
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
        CHAR_2_WORD.put("ö", "Paula");
        CHAR_2_WORD.put("p", "Gustav");
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
    }

    public static List<String> describeLetters(String letterSequence) {
        char[] c = letterSequence.toCharArray();
        List<String> result = new ArrayList<>();

        for (int letter = 0; letter < letterSequence.length(); letter++) {
            result.add(getWordFromChar(Character.toString(c[letter])));
        }
        return result;

    }

    static String getWordFromChar(String c) {
        if (CHAR_2_WORD.containsKey(c.toLowerCase())) {
            return c + " = " + CHAR_2_WORD.get(c.toLowerCase()) + (c.toUpperCase().equals(c) ? "" : "(klein)");
        } else {
            return "";
        }
    }

    public Map<String, String> getMap() {
        return CHAR_2_WORD;

    }

}
