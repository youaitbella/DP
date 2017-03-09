/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author muellermi
 */
public class StringUtil {
    public static boolean isNullOrEmpty(String test){
        return test == null || test.isEmpty();
    }
    
    public static String[] splitAtUnquotedSemicolon(String line) {
        if (line.endsWith(";")) {
            line = line + " ";
        }

        int start = 0;
        List<String> toks = new ArrayList<>();
        boolean withinQuote = false;
        for (int end = 0; end < line.length(); end++) {
            char c = line.charAt(end);
            switch (c) {
                case ';':
                    if (!withinQuote) {
                        toks.add(line.substring(start, end));
                        start = end + 1;
                    }
                    break;
                case '\"':
                    withinQuote = !withinQuote;
                    break;
            }
        }
        if (start < line.length()) {
            toks.add(line.substring(start));
        }

        for (int i = 0; i < toks.size(); i++) {
            String trimmedTok = toks.get(i).trim();
            if (trimmedTok.startsWith("\"") && trimmedTok.endsWith("\"")) {
                toks.set(i, trimmedTok.substring(1, trimmedTok.length()-1));
            }
        }
        
        String[] result = new String[toks.size()];
        return toks.toArray(result);
    }

}
