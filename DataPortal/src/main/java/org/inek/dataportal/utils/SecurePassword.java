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
        char lastChar = 0;
        int malus = 0;
        CharacterType currentType = CharacterType.Undef;
        CharacterType lastType = CharacterType.Undef;
        
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            malus += Math.abs(c-lastChar) < 2 ? 1 : 0;
            lastChar = c;
            if (Character.isUpperCase(c)) {
                upCount++;
            }
            if (Character.isLowerCase(c)) {
                loCount++;
            }
            if (Character.isLetter(c)){
               currentType = CharacterType.Letter;
            }
            if (Character.isDigit(c)) {
                digit++;
               currentType = CharacterType.Digit;
            }
            if (Character.isWhitespace(c)) {
                whitespace++;
               currentType = CharacterType.Whitespace;
            }
            if (c >= 33 && c <= 47|| c == 64 || c >= 91 && c <= 96  || c >= 123 && c <= 127 ) {
                special++;
               currentType = CharacterType.Special;
            }
            if (c > 127){
                nonAscii++;
               currentType = CharacterType.NonAscii;
            }
            malus += lastType == currentType ? 1 :0;
            lastType = currentType;
        }

        double score = Math.sqrt(upCount)
                + Math.sqrt(loCount)
                + Math.sqrt(digit)
                + Math.sqrt(whitespace)
                + Math.sqrt(special)
                + Math.sqrt(nonAscii)
                + (password.length() - 6);

/*        
        todo: check this alternative
                double score = Math.sqrt(upCount)
                + 2 * Math.sqrt(loCount)
                + 2 * Math.sqrt(digit)
                + 2 * Math.sqrt(whitespace)
                + 2 * Math.sqrt(special)
                + 2 * Math.sqrt(nonAscii)
                + (password.length() - 6)
                - malus;
*/
        if (score > 9){return Quality.Strong;}
        if (score > 6){return Quality.Good;}
        if (score > 4){return Quality.Medium;}
        return Quality.Poor;
    }
    
    @Inject AccountPwdFacade _pwdFacade;
    Quality _quality = Quality.Poor;
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
