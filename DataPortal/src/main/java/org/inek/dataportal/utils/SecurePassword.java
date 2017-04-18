/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.utils;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIInput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.enums.Quality;
import org.inek.dataportal.facades.account.AccountPwdFacade;

/**
 *
 * @author vohldo
 */
@Named
@RequestScoped
public class SecurePassword {

    public Quality determinePasswordQuality(String password) {

        boolean digit = false;
        boolean special = false;
        boolean upCount = false;
        boolean loCount = false;
        boolean whitespace = false;
        boolean nonAscii = false;
        // -----------
        if (password.equals("")) {
            return Quality.Poor;
        }
        char lastChar = 0;
        int malus = 0;
        CharacterType currentType = CharacterType.Undef;
        CharacterType lastType = CharacterType.Undef;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            malus += c == lastChar ? 3 : Math.abs(c - lastChar) <= 1 ? 2 : 0;
            lastChar = c;
            if (Character.isUpperCase(c)) {
                upCount = true;
            }
            if (Character.isLowerCase(c)) {
                loCount = true;
            }
            if (Character.isLetter(c)) {
                currentType = CharacterType.Letter;
            }
            if (Character.isDigit(c)) {
                digit = true;
                currentType = CharacterType.Digit;
            }
            if (Character.isWhitespace(c)) {
                whitespace = true;
                currentType = CharacterType.Whitespace;
            }
            if (c >= 33 && c <= 47 || c == 64 || c >= 91 && c <= 96 || c >= 123 && c <= 127) {
                special = true;
                currentType = CharacterType.Special;
            }
            if (c > 127) {
                nonAscii = true;
                currentType = CharacterType.NonAscii;
            }
            malus += lastType == currentType ? 1 : 0;
            lastType = currentType;
        }

        int differentTypes = (loCount ? 1 : 0) + (upCount ? 1 : 0) + (digit ? 1 : 0) + (whitespace ? 1 : 0) + (special ? 1 : 0) +  + (nonAscii ? 1 : 0);
        
        int score = (password.length() > 8 ? 8 * (password.length() - 7) : 4 * (password.length() - 5))
                + 5 * (differentTypes - 1) 
                - malus;
        if (score > 26) {
            return Quality.Strong;
        }
        if (score > 16) {
            return Quality.Good;
        }
        if (score > 6) {
            return Quality.Medium;
        }
        return Quality.Poor;
    }

    @Inject private AccountPwdFacade _pwdFacade;
    private Quality _quality = Quality.Poor;

    public void checkPasswordQuality(AjaxBehaviorEvent event) {
        UIInput pw = (UIInput) event.getComponent();
        String test = pw.getValue().toString();
        _quality = _pwdFacade.isWeakPassword(test) ? Quality.Poor : determinePasswordQuality(test);
    }

    public Quality getQuality() {
        return _quality;
    }

    enum CharacterType {
        Undef, Letter, Digit, Whitespace, Special, NonAscii;
    }
}
