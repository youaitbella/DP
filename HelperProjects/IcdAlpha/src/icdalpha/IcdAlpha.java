/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package icdalpha;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

/**
 * This is just a quick and dirty tool to import the alphabetical lists
 *
 * @author muellermi
 */
public class IcdAlpha {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // text in Bezeichnung, Liste alernativer Suchbegriffe
        Map<String, List<String>> translations = new HashMap<>();
        Map<String, String> codes = new HashMap<>();
        readAlphabetIcd(translations, codes);
        readListCode(translations, codes, true);
        translations = new HashMap<>();
        codes = new HashMap<>();
        readAlphabetOps(translations, codes);
        readListCode(translations, codes, false);
    }

    private static void readAlphabetIcd(Map<String, List<String>> translations, Map<String, String> codes) throws IOException {
        //File file = new File("W:/Common/PEPP/DIMDI/ICD/2013/Alphabet/icd10gm2013alpha_edvascii_20120928.txt");
        File file = new File("W:/Medizin/DIMDI/ICD/2014/Alphabet_ICD_2014/icd10gm2014alpha_edvascii_20130930.txt");
        
        String charset = getCharset(file);
        try (FileInputStream is = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset))) {
            String line;
            while (null != (line = reader.readLine())) {
                String[] parts = line.split("\\|");
                if (parts[0].equals("0")) {
                    String words[] = parts[6].replace("s.a. ", "").replace("s. ", "").split(" - ");
                    assert words.length == 2;
                    if (words[1].contains("Art der Krankheit") || words[1].equals("jeweilige Krankheit")) {
                        continue;
                    }
                    String[] synonyms = words[1].split(" oder ");
                    for (String synonym : synonyms) {
                        List<String> wordList;
                        if (translations.containsKey(synonym)) {
                            wordList = translations.get(synonym);
                        } else {
                            wordList = new ArrayList<>();
                            translations.put(synonym, wordList);
                        }
                        if (!wordList.contains(words[0])) {
                            wordList.add(words[0]);
                        }
                    }
                } else {
                    if (!parts[3].isEmpty()) {
                        addWords(codes, parts[3], parts[6]);
                    }
                    if (!parts[4].isEmpty()) {
                        addWords(codes, parts[4], parts[6]);
                    }
                    if (!parts[5].isEmpty()) {
                        addWords(codes, parts[5], parts[6]);
                    }
                }
            }
        }
    }

    private static void readAlphabetOps(Map<String, List<String>> translations, Map<String, String> codes) throws IOException {
        //File file = new File("W:/Common/PEPP/DIMDI/OPS/2013/Alphabet/ops2013alpha_edvascii_20121026.txt");
        File file = new File("W:/Medizin/DIMDI/OPS/2014/Alphabet_OPS_2014/ops2014alpha_edvascii_20131104.txt");
        String charset = getCharset(file);
        try (FileInputStream is = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset))) {
            String line;
            while (null != (line = reader.readLine())) {
                String[] parts = line.split("\\|");
                if (parts[0].equals("0")) {
                    String words[] = parts[4].replace("s.a. ", "").replace("s. ", "").split(" - ");
                    assert words.length == 2;
                    if (words[1].contains("jeweiliger durchgeführter Eingriff") || words[1].contains("jew. durchgeführter Eingriff") || words[0].trim().equals("Zugang")) {
                        continue;
                    }
                    String[] synonyms = words[1].split(" oder ");
                    for (String synonym : synonyms) {
                        List<String> wordList;
                        if (translations.containsKey(synonym)) {
                            wordList = translations.get(synonym);
                        } else {
                            wordList = new ArrayList<>();
                            translations.put(synonym, wordList);
                        }
                        if (!wordList.contains(words[0])) {
                            wordList.add(words[0]);
                        }
                    }
                } else {
                    if (!parts[2].isEmpty()) {
                        addWords(codes, parts[2], parts[4]);
                    }
                    if (!parts[3].isEmpty()) {
                        addWords(codes, parts[3], parts[4]);
                    }
                }
            }
        }
    }

    private static void readListCode(Map<String, List<String>> translations, Map<String, String> codes, boolean isIcd) throws IOException {
        // examples to create list: 
        // select cast(icId as varchar) + '|' + icCode + '|' + icName from listIcd where icSearchWords=''
        // select cast(opId as varchar) + '|' + opCode + '|' + opName from listOps where opSearchWords=''

        File file = new File(isIcd ? "d:/temp/listIcd.txt" : "d:/temp/listOps.txt");
        String charset = getCharset(file);
        try (FileInputStream is = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
                PrintWriter writer = new PrintWriter(new File(isIcd ? "d:/temp/listIcdSearch.txt" : "d:/temp/listOpsSearch.txt"))) {
            String line;
            while (null != (line = reader.readLine())) {
                String[] parts = line.split("\\|");
                String id = parts[0];
                String code = parts[1];
                String text = parts[2];
                addWords(codes, code, text);
                addSubstitutes(codes, code, translations);
                String wordList = makeWordList(codes, code).replace("'", "''");
                String update;
                if (isIcd) {
                    update = "update listIcd set icSearchWords = '" + wordList + "' where icId=" + id;
                } else {
                    update = "update listOps set opSearchWords = '" + wordList + "' where opId=" + id;
                }
                writer.println(update);
            }
        }
    }

    private static void addWords(Map<String, String> codes, String fullCode, String newWords) {
        String code = fullCode.replace("+", "").replace("*", "").replace("!", "").replace("#", "");
        String words = newWords.replace("[", " ").replace("]", " ").replace("(", " ").replace(")", " ").replace(",", " ");
        if (codes.containsKey(code)) {
            words = words + " " + codes.get(code);
        }
        codes.put(code, words);
    }

    private static void addSubstitutes(Map<String, String> codes, String code, Map<String, List<String>> translations) {
        String words = codes.get(code);
        for (Entry<String, List<String>> entrySet : translations.entrySet()) {
            String delimitedWords = " " + words + " ";
            if (delimitedWords.contains(" " + entrySet.getKey().trim() + " ")) {
                // whole word found: add substitutes
                for (String subst : entrySet.getValue()) {
                    words = words + " " + subst;
                }
            }
            if (entrySet.getKey().endsWith("-")) {
                // substitute words beginning with...
                String wordBegin = entrySet.getKey().substring(0, entrySet.getKey().length() - 1);
                String[] wordArray = words.split(" ");
                for (String singleWord : wordArray) {
                    if (singleWord.startsWith(wordBegin)) {
                        for (String subst : entrySet.getValue()) {
                            if (!subst.endsWith("-")){continue;}
                            String newWord = subst.substring(0, subst.length() - 1) + singleWord.substring(wordBegin.length());
                            words = words + " " + newWord;
                        }

                    }
                }
            }
        }
        codes.put(code, words);
    }

    private static String getCharset(File file) throws IOException {
        String chartset = "windows-1252";
        FileInputStream is = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, chartset));
        String line = reader.readLine();
        if (line == null) {
            return chartset;
        }
        byte[] startBytes = line.getBytes(chartset);
        if (startBytes.length >= 2 && startBytes[0] == (byte) 0xFF && startBytes[1] == (byte) 0xFE) {
            chartset = "UTF-16LE";
        } else if (startBytes.length >= 2 && startBytes[0] == (byte) 0xFE && startBytes[1] == (byte) 0xFF) {
            chartset = "UTF-16BE";
        } else if (startBytes.length >= 3 && startBytes[0] == (byte) 0xEF && startBytes[1] == (byte) 0xBB && startBytes[2] == (byte) 0xBF) {
            chartset = "UTF-8";
        } else {
            for (int i = 0; i < startBytes.length - 1; i++) {
                if (startBytes[i] == (byte) 0xC3) {
                    byte t = startBytes[i + 1];
                    // ÄÖÜäöüß
                    if (t == (byte) 0x84 || t == (byte) 0x96 || t == (byte) 0x9C || t == (byte) 0xA4 || t == (byte) 0xB6 || t == (byte) 0xBC || t == (byte) 0x9F) {
                        chartset = "UTF-8";
                        break;
                    }
                }
            }
        }
        reader.close();
        is.close();
        return chartset;

    }

    private static String makeWordList(Map<String, String> codes, String code) {
        String[] words = codes.get(code).split(" ");
        Set<String> wordSet = new TreeSet<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                wordSet.add(word.toLowerCase());
            }
        }
        String wordList = "";
        for (String word : wordSet) {
            if (word.endsWith(":")) {
                word = word.substring(0, word.lastIndexOf(":"));
            }
            if (word.equals("s.a.")) {
                continue;
            }
            if (word.length() < 3) {
                continue;
            }
            boolean isFragment = false;
            for (String word2 : wordSet) {
                if (!word.equals(word2) && word2.contains(word)) {
                    isFragment = true;
                    break;
                }
            }
            if (!isFragment) {
                wordList = wordList + " " + word;
            }
        }

        return wordList.trim();
    }
}
