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

    private static final Map<String, String> _char2Word = new HashMap<>();

    static {
        _char2Word.put("a", "Anton");
        _char2Word.put("b", "Berta");
        _char2Word.put("c", "Cäsar");
        _char2Word.put("d", "Dora");
        _char2Word.put("e", "Emil");
        _char2Word.put("f", "Friedrich");
        _char2Word.put("g", "Gustav");
        _char2Word.put("h", "Heinrich");
        _char2Word.put("i", "Ida");
        _char2Word.put("j", "Julius");
        _char2Word.put("k", "Kaufmann");
        _char2Word.put("l", "Ludwig");
        _char2Word.put("m", "Martha");
        _char2Word.put("n", "Nordpol");
        _char2Word.put("o", "Otto");
        _char2Word.put("ö", "Paula");
        _char2Word.put("p", "Gustav");
        _char2Word.put("q", "Quelle");
        _char2Word.put("r", "Richard");
        _char2Word.put("s", "Samuel");
        _char2Word.put("ß", "Eszett");
        _char2Word.put("t", "Theodor");
        _char2Word.put("u", "Ulrich");
        _char2Word.put("ü", "Übermut");
        _char2Word.put("v", "Viktor");
        _char2Word.put("w", "Wilhelm");
        _char2Word.put("x", "Xanthippe");
        _char2Word.put("y", "Ypsilon");
        _char2Word.put("z", "Zeppelin");
        _char2Word.put("1", "Eins");
        _char2Word.put("2", "Zwei");
        _char2Word.put("3", "Drei");
        _char2Word.put("4", "Vier");
        _char2Word.put("5", "Fünf");
        _char2Word.put("6", "Sechs");
        _char2Word.put("7", "Sieben");
        _char2Word.put("8", "Acht");
        _char2Word.put("9", "Neun");
        _char2Word.put("0", "Null");
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
        if (_char2Word.containsKey(c.toLowerCase())) {
            return c + " = " + _char2Word.get(c.toLowerCase()) + (c.toUpperCase().equals(c) ? "" : "(klein)");
        } else {
            return "";
        }
    }

    public Map<String, String> getMap() {
        return _char2Word;

    }

}
