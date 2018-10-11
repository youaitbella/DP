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

    private static final Map<String, String> Char2Word = new HashMap<>();

    static {
        Char2Word.put("a", "Anton");
        Char2Word.put("b", "Berta");
        Char2Word.put("c", "Cäsar");
        Char2Word.put("d", "Dora");
        Char2Word.put("e", "Emil");
        Char2Word.put("f", "Friedrich");
        Char2Word.put("g", "Gustav");
        Char2Word.put("h", "Heinrich");
        Char2Word.put("i", "Ida");
        Char2Word.put("j", "Julius");
        Char2Word.put("k", "Kaufmann");
        Char2Word.put("l", "Ludwig");
        Char2Word.put("m", "Martha");
        Char2Word.put("n", "Nordpol");
        Char2Word.put("o", "Otto");
        Char2Word.put("ö", "Paula");
        Char2Word.put("p", "Gustav");
        Char2Word.put("q", "Quelle");
        Char2Word.put("r", "Richard");
        Char2Word.put("s", "Samuel");
        Char2Word.put("ß", "Eszett");
        Char2Word.put("t", "Theodor");
        Char2Word.put("u", "Ulrich");
        Char2Word.put("ü", "Übermut");
        Char2Word.put("v", "Viktor");
        Char2Word.put("w", "Wilhelm");
        Char2Word.put("x", "Xanthippe");
        Char2Word.put("y", "Ypsilon");
        Char2Word.put("z", "Zeppelin");
        Char2Word.put("1", "Eins");
        Char2Word.put("2", "Zwei");
        Char2Word.put("3", "Drei");
        Char2Word.put("4", "Vier");
        Char2Word.put("5", "Fünf");
        Char2Word.put("6", "Sechs");
        Char2Word.put("7", "Sieben");
        Char2Word.put("8", "Acht");
        Char2Word.put("9", "Neun");
        Char2Word.put("0", "Null");
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
        if (Char2Word.containsKey(c.toLowerCase())) {
            return c + " = " + Char2Word.get(c.toLowerCase()) + (c.toUpperCase().equals(c) ? "" : "(klein)");
        } else {
            return "";
        }
    }

    public Map<String, String> getMap() {
        return Char2Word;

    }

}
