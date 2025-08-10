package org.example;

import org.example.model.PasswordEntry;
import sun.security.util.Password;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.HashMap;

/**
 * Password Manager
 *
 */
public class App 
{
    // constants
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final HashMap<String, PasswordEntry> passwordList = new HashMap<>();

    private static String masterPassword;

    // Generate secret key from master password
    public static SecretKey getSecretKeyFromPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKey secretKey = factory.generateSecret(spec);
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

    // Encrypt data
    public static String encrypt(String plaintext, String password, byte[] salt, byte[] iv)
            throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        SecretKey secretKey = getSecretKeyFromPassword(password, salt);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Decrypt data
    public static String decrypt(String encryptedText, String password, byte[] salt, byte[] iv)
            throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        SecretKey secretKey = getSecretKeyFromPassword(password, salt);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decrypted);
    }

    // Utility: Generate secure random bytes
    public static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

    public static void addPassword(BufferedReader br) throws
            InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException,
            InvalidKeySpecException, BadPaddingException,
            InvalidKeyException, IOException {

        System.out.println("Enter master password:");
        String inputMasterPassword = br.readLine();
        if(!masterPassword.equals(inputMasterPassword)) {
            System.out.println("Incorrect master password");
            return;
        }
        System.out.println("Enter platform:");
        String platform = br.readLine();
        System.out.println("Enter password:");
        String plainText = br.readLine();

        // Generate a fresh salt and IV for this entry
        byte[] entrySalt = generateRandomBytes(16); // 16 bytes is fine for salt
        byte[] entryIv = generateRandomBytes(16);   // 16 bytes for AES-CBC IV

        String encrypted = encrypt(plainText, inputMasterPassword, entrySalt, entryIv);
        passwordList.put(platform.toLowerCase(), new PasswordEntry(encrypted, entrySalt, entryIv));
        System.out.println("Added password for " + platform);
    }

    public static void getPassword(BufferedReader br) throws
            InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException,
            InvalidKeySpecException, BadPaddingException,
            InvalidKeyException, IOException {

        System.out.println("Enter master password:");
        String inputMasterPassword = br.readLine();
        if(!masterPassword.equals(inputMasterPassword)) {
            System.out.println("Incorrect master password");
            return;
        }
        System.out.println("Enter platform:");
        String platform = br.readLine();
        PasswordEntry passwordEntry = passwordList.get(platform.toLowerCase());
        if(passwordEntry == null) {
            System.out.println("Password for platform " + platform + " not yet created");
            return;
        }
        String decrypted = decrypt(passwordEntry.getCipherText(), masterPassword, passwordEntry.getSalt(), passwordEntry.getIv());
        System.out.println("Decrypted password: " + decrypted);
    }
    public static void main( String[] args ) throws IOException {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String continueOps = "y";
            System.out.println("Set master password:");
            masterPassword = br.readLine();
            do {
                System.out.println("-------------- MENU --------------");
                System.out.println("1. Add password");
                System.out.println("2. Get password");
                System.out.println("Enter your choice: ");
                int choice = Integer.parseInt(br.readLine());
                switch(choice) {
                    case 1:
                        addPassword(br);
                        break;
                    case 2:
                        getPassword(br);
                        break;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }
                System.out.println("Want to continue?(y/n)");
                continueOps = br.readLine();
            } while(continueOps.equals("y"));




        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }



    }
}
