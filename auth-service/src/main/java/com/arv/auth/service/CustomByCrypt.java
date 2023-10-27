package com.arv.auth.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import javax.crypto.KeyGenerator;
import java.util.Base64;

@SuppressWarnings("ALL")
public class CustomByCrypt {
    private static final int SHIFT_KEY = 5;

    private static CustomByCrypt instance;

    private CustomByCrypt() {
    }

    public static CustomByCrypt getInstance() {
        if (instance == null) {
            instance = new CustomByCrypt();
        }
        return instance;
    }

    private static String encrypt(String originalText, int shift) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < originalText.length(); i++) {
            char c = originalText.charAt(i);
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) (base + (c - base + shift) % 26);
            }
            result.append(c);
        }

        return result.toString();
    }

    // Decryption function
    private static String decrypt(String originalText, int shift) {
        return encrypt(originalText, 26 - shift);
    }

    public static String encrypt(String word, SecretKey secretKey) {
        String encryptedText;
        try {
            encryptedText = encryptAES(word, secretKey);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            encryptedText = encrypt(word, SHIFT_KEY);
        }

        return encryptedText;
    }

    public static String decrypt(String word, SecretKey secretKey) {
        String decryptedText;
        try {
            decryptedText = decryptAES(word, secretKey);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            decryptedText = decrypt(word, SHIFT_KEY);
        }
        return decryptedText;
    }


    public static SecretKey generateAESKey() {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (Exception e) {
            keyGenerator = null;
        }
        assert keyGenerator != null;
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    public static String encryptAES(String plaintext, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptAES(String encryptedText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}

