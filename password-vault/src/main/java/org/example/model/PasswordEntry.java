package org.example.model;

public class PasswordEntry {
    private final String cipherText;
    private final byte[] salt;
    private final byte[] iv;

    public String getCipherText() {
        return cipherText;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getIv() {
        return iv;
    }

    public PasswordEntry(String cipherText, byte[] salt, byte[] iv) {
        this.cipherText = cipherText;
        this.salt = salt;
        this.iv = iv;
    }
}
