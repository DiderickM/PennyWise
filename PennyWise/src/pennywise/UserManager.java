/**
 * Created on Sun Feb 16 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

package pennywise;

/**
 * UserManager class handles all user-related operations.
 * Demonstrates SINGLE RESPONSIBILITY PRINCIPLE and ENCAPSULATION.
 * 
 * This class manages:
 * - User storage and retrieval
 * - User authentication
 * - User CRUD operations
 */
public class UserManager {
    
    // ENCAPSULATION: Private user storage
    private static final User[] users = new User[AppConstants.MAX_USERS];
    private static int userCount = 0;
    
    /**
     * VALUE RETURNING METHOD: Gets the total number of users.
     * 
     * @return number of registered users
     */
    public static int getUserCount() {
        return userCount;
    }
    
    /**
     * VALUE RETURNING METHOD: Gets a user by index.
     * 
     * @param index Index of the user
     * @return User at the specified index, or null if not found
     */
    public static User getUser(int index) {
        if (index >= 0 && index < userCount) {
            return users[index];
        }
        return null;
    }
    
    /**
     * VALUE RETURNING METHOD: Adds a regular user to the system.
     * 
     * @param user RegularUser to add
     * @return true if added successfully, false if array is full
     */
    public static boolean addRegularUser(RegularUser user) {
        if (user == null) {
            return false;
        }
        
        if (userCount < users.length) {
            users[userCount] = user;
            userCount++;
            return true;
        }
        return false;
    }
    
    /**
     * VALUE RETURNING METHOD: Finds a regular user by username.
     * 
     * @param username Username to search for
     * @return RegularUser if found, null otherwise
     */
    public static RegularUser getRegularUserByUsername(String username) {
        if (username == null) {
            return null;
        }
        
        for (int i = 0; i < userCount; i++) {
            if (users[i] instanceof RegularUser user) {
                if (user.getUsername().equals(username)) {
                    return user;
                }
            }
        }
        return null;
    }
    
    /**
     * VALUE RETURNING METHOD: Authenticates a regular user.
     * Uses password hashing for secure authentication.
     * 
     * @param username Username to authenticate
     * @param password Password to verify
     * @return RegularUser if authentication successful, null otherwise
     */
    public static RegularUser authenticateRegularUser(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        
        for (int i = 0; i < userCount; i++) {
            if (users[i] instanceof RegularUser user) {
                if (user.getUsername().equals(username)) {
                    // Use password verification with hashing
                    if (PasswordUtil.verifyPassword(password, user.getPassword())) {
                        return user;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * VALUE RETURNING METHOD: Checks if a username already exists.
     * 
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    public static boolean usernameExists(String username) {
        return getRegularUserByUsername(username) != null;
    }
    
    /**
     * VALUE RETURNING METHOD: Removes a regular user by username.
     * 
     * @param username Username of user to remove
     * @return true if removed successfully, false if not found
     */
    public static boolean removeRegularUser(String username) {
        if (username == null) {
            return false;
        }
        
        int userIndex = -1;
        for (int i = 0; i < userCount; i++) {
            if (users[i] instanceof RegularUser user) {
                if (user.getUsername().equals(username)) {
                    userIndex = i;
                    break;
                }
            }
        }
        
        if (userIndex == -1) {
            return false;
        }
        
        // Shift array elements
        for (int i = userIndex; i < userCount - 1; i++) {
            users[i] = users[i + 1];
        }
        users[userCount - 1] = null;
        userCount--;
        
        return true;
    }
    
    /**
     * VALUE RETURNING METHOD: Finds a user by account number.
     * Searches through all users' accounts.
     * 
     * @param accountNumber Account number to search for
     * @param excludeUser User to exclude from search (e.g., current user)
     * @return RegularUser owning the account, or null if not found
     */
    public static RegularUser findUserByAccountNumber(String accountNumber, RegularUser excludeUser) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            return null;
        }
        
        for (int i = 0; i < userCount; i++) {
            if (users[i] instanceof RegularUser user && user != excludeUser) {
                Account[] accounts = user.getAccounts();
                if (accounts != null) {
                    for (Account account : accounts) {
                        if (account != null && account.getAccountNumber().equals(accountNumber)) {
                            return user;
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * VALUE RETURNING METHOD: Finds an account by account number.
     * Searches through all users' accounts.
     * 
     * @param accountNumber Account number to search for
     * @return Account if found, null otherwise
     */
    public static Account findAccountByNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            return null;
        }
        
        for (int i = 0; i < userCount; i++) {
            if (users[i] instanceof RegularUser user) {
                Account[] accounts = user.getAccounts();
                if (accounts != null) {
                    for (Account account : accounts) {
                        if (account != null && account.getAccountNumber().equals(accountNumber)) {
                            return account;
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * VALUE RETURNING METHOD: Gets all regular users.
     * 
     * @return Array of RegularUser objects (may contain nulls)
     */
    public static RegularUser[] getAllRegularUsers() {
        RegularUser[] regularUsers = new RegularUser[userCount];
        int count = 0;
        
        for (int i = 0; i < userCount; i++) {
            if (users[i] instanceof RegularUser regularUser) {
                regularUsers[count++] = regularUser;
            }
        }
        
        return regularUsers;
    }
    
    /**
     * VOID METHOD: Displays all users in the system.
     * Used by administrators.
     */
    public static void displayAllUsers() {
        System.out.println("\n========== All Users ==========");
        if (userCount == 0) {
            System.out.println("No users registered.");
        } else {
            for (int i = 0; i < userCount; i++) {
                User u = users[i];
                if (u != null) {
                    System.out.println((i + 1) + ". " + u.getUsername() + " (ID: " + u.getUserId() + ")");
                }
            }
        }
        System.out.println("==============================");
    }
    
    // Private constructor to prevent instantiation
    private UserManager() {
        throw new AssertionError("Manager class should not be instantiated");
    }
}
