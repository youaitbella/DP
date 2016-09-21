package org.inek.dataportal.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author muellermi
 */
public class Crypt {

    public static String getPasswordHash(String password, int accountId) {
        String saltedKey = "" + accountId;
        saltedKey = "000000".substring(0, 6 - saltedKey.length()) + saltedKey;
        saltedKey += password;
        return getHash("SHA", saltedKey);
    }

    public static String getHash(String algorithm, String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            return byte2hex(md.digest(input.getBytes("utf-8")));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private static String byte2hex(byte[] source) {
        StringBuilder sb = new StringBuilder();
        for (byte b : source) {
            String hex = Integer.toHexString(b + 128);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String hashPassword(String password, String salt) {
        try {
            byte[] saltBytes = salt.getBytes("UTF-8");
            SecretKeyFactory skf = SecretKeyFactory.
                    getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes,
                    100285, 1024);
            SecretKey key = skf.generateSecret(spec);
            return byte2hex(key.getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | UnsupportedEncodingException ex) {
            Logger.getLogger("HashUtils").log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}
