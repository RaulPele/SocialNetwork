package com.pelr.socialnetwork_extins.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class PasswordVerifier {

    public boolean verifyPassword(String originalPassword, String storedHashedPassword) {
        String[] parts = storedHashedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);

        try {
            byte[] salt = fromHex(parts[1]);
            byte[] hash = fromHex(parts[2]);

            PBEKeySpec keySpec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] testHash = secretKeyFactory.generateSecret(keySpec).getEncoded();

            int diff = hash.length ^ testHash.length;

            for(int i = 0; i < hash.length && i < testHash.length; i++) {
                diff |= hash[i] ^ testHash[i];
            }

            return diff == 0;
        } catch (NoSuchAlgorithmException ex){
            System.out.println("Password verifier no such algorithm exception!");
            ex.printStackTrace();
        }catch (InvalidKeySpecException ex) {
            System.out.println("Password verifier invalid keyspec exception!");
            ex.printStackTrace();
        }

        return false;
    }

    private byte[] fromHex(String hex) throws NoSuchAlgorithmException{
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0 ;i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }

        return bytes;
    }
}
