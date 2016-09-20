/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.utils;

import java.util.Scanner;

/**
 *
 * @author vohldo
 */
public class SecurePassword {
    
    public static boolean checkSecurePassword(String password) {
        // Settings
        int min = 8;
        int max = 18;
        int minSpecial = 1;
        int minDigit = 1;
        int minLowChar = 1;
        int minUpChar = 1;
        // -----------
        
        // Memo
        int digit = 0;
        int special = 0;
        int upCount = 0;
        int loCount = 0;
        // -----------
        if(password.equals(""))
            return false;
        Scanner scan = new Scanner(password);
        password = scan.nextLine();
        if (password.length() >= min && password.length() <= max) {
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
                if (c >= 33 && c <= 46 || c == 64) {
                    special++;
                }
            }
            if(special < minSpecial) {
                System.out.println("Password "+password+" needs at least "+minSpecial+" special characters.");
            }
            if(upCount < minUpChar) {
                System.out.println("Password "+password+" needs at least "+minUpChar+" upper characters.");
            }
            if(loCount < minLowChar) {
                System.out.println("Password "+password+" needs at least "+minLowChar+" lower characters.");
            }
            if(digit < minDigit) {
                System.out.println("Password "+password+" needs at least "+minDigit+" digits.");
            }
                
            // Password is good
            if (special >= minSpecial && loCount >= minLowChar && upCount >= minUpChar && digit >= minDigit) {
                System.out.println(" Password "+password+" is good");
                return true;
            }
        } else if(password.length() < min){
            System.out.println(" Password "+password+" is to short.");
        } else {
            System.out.println("Password "+password+" is to long.");
        }
        return false;
    }
}
