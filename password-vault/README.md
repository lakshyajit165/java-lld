
# Dynamic Salt and IV in Java Encryption

This document explains the key functions and logic used to make **Salt** and **IV** dynamic in a Java-based encryption/decryption system.  
Dynamic Salt and IV improve security by ensuring that each encryption operation produces unique ciphertext, even for identical plaintext.

---

## 1. `generateSalt()`
**Signature:**
```java
private static byte[] generateSalt() throws NoSuchAlgorithmException
```
**Description:**  
Generates a **random 16-byte salt** using Java's `SecureRandom`. This salt is unique per encryption operation and is stored alongside the ciphertext (usually in Base64 format) so it can be reused for decryption.

---

## 2. `generateIV()`
**Signature:**
```java
private static IvParameterSpec generateIV()
```
**Description:**  
Creates a random 16-byte **Initialization Vector (IV)** using `SecureRandom`. The IV is required for CBC mode encryption and must be stored with the encrypted output.  
The IV **does not** need to be kept secret but must be unique for every encryption operation.

---

## 3. `getKeyFromPassword(String password, byte[] salt)`
**Signature:**
```java
private static SecretKey getKeyFromPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException
```
**Description:**  
Uses **PBKDF2WithHmacSHA256** to derive a secure AES key from the provided password and the dynamically generated salt.  
The number of iterations (e.g., 65536) and key length (e.g., 256 bits) determine the computational difficulty for brute force attacks.

---

## 4. `encrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv)`
**Signature:**
```java
private static String encrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv) throws GeneralSecurityException
```
**Description:**  
Encrypts the input text using the provided AES key and IV.  
Uses CBC mode with PKCS5 padding. Returns **Base64-encoded ciphertext**.

---

## 5. `decrypt(String algorithm, String cipherText, SecretKey key, IvParameterSpec iv)`
**Signature:**
```java
private static String decrypt(String algorithm, String cipherText, SecretKey key, IvParameterSpec iv) throws GeneralSecurityException
```
**Description:**  
Decrypts the Base64-encoded ciphertext using the provided AES key and IV, restoring the original plaintext.

---

## 6. Storage Format
When encrypting, you must store:
- Base64-encoded **Salt**
- Base64-encoded **IV**
- Base64-encoded **Ciphertext**

A common approach is to store them as JSON:
```json
{
  "salt": "<base64_salt>",
  "iv": "<base64_iv>",
  "data": "<base64_ciphertext>"
}
```
This ensures all necessary components for decryption are packaged together.

---

## 7. Example Flow
1. User enters a password (Master Password).
2. System generates a **random salt** and **random IV**.
3. Salt is used to derive a key from the password.
4. IV is used for the AES encryption process.
5. Store salt, IV, and ciphertext together (in DB, JSON file, etc.).
6. For decryption, retrieve all three and repeat the key derivation with the stored salt.

---

**Security Benefits:**
- Prevents identical ciphertext for identical passwords.
- Makes brute force attacks significantly harder.
- Avoids reuse of the same IV and salt, which can weaken encryption.

