package com.arv.auth.service;

import java.util.Random;


@SuppressWarnings("ALL")
public class CustomByCrypt {

    private static CustomByCrypt instance;


    private CustomByCrypt() {
    }

    public static CustomByCrypt getInstance() {
        if (instance == null) {
            instance = new CustomByCrypt();
        }
        return instance;
    }

    public static String encrypt(String plaintext, String passphrase) {
        StringBuilder ciphertext = new StringBuilder();

        for (int i = 0; i < plaintext.length(); i++) {
            char c = plaintext.charAt(i);
            char shifted = (char) (c + passphrase.length());
            ciphertext.append(shifted);
        }

        return ciphertext.toString();
    }

    public static String decrypt(String ciphertext, String passphrase) {
        StringBuilder plaintext = new StringBuilder();

        for (int i = 0; i < ciphertext.length(); i++) {
            char c = ciphertext.charAt(i);
            char shifted = (char) (c - passphrase.length());
            plaintext.append(shifted);
        }

        return plaintext.toString();
    }

    public String generateSaltPhrase() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+=-{}:;'<>?/`~";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i <= 50; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}

