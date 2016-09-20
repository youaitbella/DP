/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.utils;

import org.inek.dataportal.enums.Quality;

/**
 *
 * @author vohldo
 */
public class SecurePassword {

    public static Quality determinePasswordQuality(String password) {

        int digit = 0;
        int special = 0;
        int upCount = 0;
        int loCount = 0;
        int whitespace = 0;
        int nonAscii = 0;
        // -----------
        if (password.equals("")) {
            return Quality.Poor;
        }
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                upCount++;
            }
            if (Character.isLowerCase(c)) {
                loCount++;
            }
            if (Character.isDigit(c)) {
                digit++;
            }
            if (Character.isWhitespace(c)) {
                whitespace++;
            }
            if (c >= 33 && c <= 46 || c == 64) {
                special++;
            }
            if (c > 127){
                nonAscii++;
            }
        }

        double score = Math.sqrt(upCount)
                + Math.sqrt(loCount)
                + Math.sqrt(digit)
                + Math.sqrt(whitespace)
                + Math.sqrt(special)
                + Math.sqrt(nonAscii)
                + password.length() - 6;
        
        if (score > 9){return Quality.Strong;}
        if (score > 6){return Quality.Good;}
        if (score > 4){return Quality.Medium;}
        return Quality.Poor;
    }
}
