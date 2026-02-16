/**
 * Created on Sun Feb 16 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

package pennywise;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * PasswordUtil class provides secure password hashing and verification.
 * Demonstrates SECURITY best practices - never store plain text passwords.
 * 
 * Uses SHA-256 hashing with salt for secure password storage.
 * This prevents:
 * - Rainbow table attacks
 * - Dictionary attacks
 * - Direct password exposure in case of data breach
 */
public class PasswordUtil {
    
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16; // bytes
    
    /**
     * VALUE RETURNING METHOD: Generates a random salt for password hashing.
     * 
     * @return Base64-encoded salt string
     */
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * VALUE RETURNING METHOD: Hashes a password with salt.
     * 
     * @param password Plain text password
     * @param salt Salt to use for hashing
     * @return Base64-encoded hash
     */
    private static String hashWithSalt(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            
            // Combine password and salt
            String combined = password + salt;
            byte[] hashBytes = digest.digest(combined.getBytes());
            
            // Return Base64-encoded hash
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            // Fallback to simple encoding if SHA-256 is not available
            // This should never happen with modern Java
            System.err.println("Warning: SHA-256 not available, using fallback encoding");
            return Base64.getEncoder().encodeToString(password.getBytes());
        }
    }
    
    /**
     * VALUE RETURNING METHOD: Hashes a password for storage.
     * Returns format: "salt:hash"
     * 
     * @param password Plain text password to hash
     * @return Hashed password string in format "salt:hash"
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        String salt = generateSalt();
        String hash = hashWithSalt(password, salt);
        
        // Store salt and hash together separated by :
        return salt + ":" + hash;
    }
    
    /**
     * VALUE RETURNING METHOD: Verifies a password against stored hash.
     * 
     * @param password Plain text password to verify
     * @param storedHash Stored hash in format "salt:hash"
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        if (password == null || storedHash == null) {
            return false;
        }
        
        try {
            // Split stored hash into salt and hash components
            String[] parts = storedHash.split(":", 2);
            if (parts.length != 2) {
                return false;
            }
            
            String salt = parts[0];
            String hash = parts[1];
            
            // Hash the provided password with the stored salt
            String providedHash = hashWithSalt(password, salt);
            
            // Compare hashes
            return hash.equals(providedHash);
        } catch (Exception e) {
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * VALUE RETURNING METHOD: Checks if a stored password is hashed.
     * 
     * @param storedPassword Stored password string
     * @return true if password is hashed (contains :), false if plain text
     */
    public static boolean isPasswordHashed(String storedPassword) {
        return storedPassword != null && storedPassword.contains(":");
    }
    
    /**
     * VALUE RETURNING METHOD: Migrates a plain text password to hashed format.
     * Used for upgrading existing plain text passwords.
     * 
     * @param plainPassword Plain text password
     * @return Hashed password
     */
    public static String migrateToHashed(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            return plainPassword;
        }
        
        // If already hashed, return as is
        if (isPasswordHashed(plainPassword)) {
            return plainPassword;
        }
        
        // Hash the plain text password
        return hashPassword(plainPassword);
    }
    
    // Private constructor to prevent instantiation
    private PasswordUtil() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
