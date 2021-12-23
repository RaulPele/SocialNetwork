package com.pelr.socialnetwork_extins.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class PasswordEncryptor {

    public String generatePasswordHash(String password) {
        int iterations = 1000;
        char[] passwordChars = password.toCharArray();

        try {
            byte[] salt = getSalt();
            PBEKeySpec keySpec = new PBEKeySpec(passwordChars, salt, iterations,64 * 8);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = secretKeyFactory.generateSecret(keySpec).getEncoded();

            return iterations + ":" + toHex(salt) + ":" + toHex(hash);
        }catch (NoSuchAlgorithmException ex) {
            System.out.println("Hashing algorithm error!");
            ex.printStackTrace();
        }catch (InvalidKeySpecException ex) {
            System.out.println("Hashing invalid keyspec error!");
            ex.printStackTrace();
        }

        return null;
    }

    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);

        return salt;
    }

    private String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();

        if(paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
}
