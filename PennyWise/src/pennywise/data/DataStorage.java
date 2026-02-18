/**
 * Created on Sun Feb 16 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

package pennywise.data;

import java.io.*;
import pennywise.Account;
import pennywise.Admin;
import pennywise.CheckingAccount;
import pennywise.RegularUser;
import pennywise.SavingsAccount;
import pennywise.SuperAdmin;
import pennywise.Transaction;
import pennywise.User;
import pennywise.UserManager;
import pennywise.config.DataConfiguration;

/**
 * DataStorage class handles saving all application data to files.
 * Demonstrates FILE I/O operations, data persistence, and POLYMORPHISM.
 * Extends DataPersistence and implements abstract methods for saving operations.
 * 
 * This class saves users, accounts, and transactions to text files
 * so that data persists between program runs.
 */
public class DataStorage extends DataPersistence {
    
    /**
     * POLYMORPHIC IMPLEMENTATION: Validates that there is data to save.
     * 
     * @return true if there are users to save, false otherwise
     */
    @Override
    protected boolean validatePreconditions() {
        // We can save even with 0 users (to clear files)
        return true;
    }
    
    /**
     * POLYMORPHIC IMPLEMENTATION: Performs the save operation.
     * Saves users, accounts, transactions, and configuration to files.
     * 
     * @return true if all saves successful, false otherwise
     * @throws IOException if file operations fail
     */
    @Override
    protected boolean performOperation() throws IOException {
        saveUsers();
        saveAccounts();
        saveTransactions();
        DataConfiguration.saveConfig(); // Also save configuration
        return true;
    }
    
    /**
     * POLYMORPHIC IMPLEMENTATION: Returns the operation name.
     * 
     * @return "Data Save" operation name
     */
    @Override
    protected String getOperationName() {
        return "Data Save";
    }
    
    /**
     * Saves all user data to files.
     * This method saves users, their accounts, and all transactions.
     * Uses the template method pattern from parent class.
     */
    public static boolean saveAllData() {
        DataStorage storage = new DataStorage();
        return storage.execute();
    }
    
    /**
     * Saves all users to the users file.
     * Format: REGULAR|userId|username|password|email
     *         ADMIN|userId|username|password|email
     *         SUPERADMIN|userId|username|password|email
     */
    private static void saveUsers() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            int userCount = UserManager.getUserCount();
            
            for (int i = 0; i < userCount; i++) {
                User user = UserManager.getUser(i);
                if (user != null) {
                    if (user instanceof RegularUser) {
                        // Format: REGULAR|userId|username|password|email
                        writer.println("REGULAR|" + user.getUserId() + "|" + 
                                     user.getUsername() + "|" + user.getPassword() + "|" + 
                                     user.getEmail());
                    } else if (user instanceof SuperAdmin) {
                        // Format: SUPERADMIN|userId|username|password|email
                        writer.println("SUPERADMIN|" + user.getUserId() + "|" + 
                                     user.getUsername() + "|" + user.getPassword() + "|" + 
                                     user.getEmail());
                    } else if (user instanceof Admin) {
                        // Format: ADMIN|userId|username|password|email
                        writer.println("ADMIN|" + user.getUserId() + "|" + 
                                     user.getUsername() + "|" + user.getPassword() + "|" + 
                                     user.getEmail());
                    }
                }
            }
        }
    }
    
    /**
     * Saves all accounts for all users.
     * Format: userId|accountType|accountNumber|balance|specificFields
     */
    private static void saveAccounts() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            int userCount = UserManager.getUserCount();
            
            for (int i = 0; i < userCount; i++) {
                User user = UserManager.getUser(i);
                if (user != null && user instanceof RegularUser) {
                    Account[] accounts = user.getAccounts();
                    if (accounts != null) {
                        for (Account account : accounts) {
                            if (account != null) {
                                if(account instanceof SavingsAccount sa) {
                                    // Format: userId|SAVINGS|accountNumber|balance|interestRate|maxWithdrawals
                                    writer.println(user.getUserId() + "|SAVINGS|" +
                                            account.getAccountNumber() + "|" +
                                            account.getBalance() + "|" +
                                            sa.getInterestRate() + "|" +
                                            sa.getMaxWithdrawalsPerMonth());
                                } else if(account instanceof CheckingAccount ca) {
                                    // Format: userId|CHECKING|accountNumber|balance|overdraftLimit|overdraftFee
                                    writer.println(user.getUserId() + "|CHECKING|" +
                                            account.getAccountNumber() + "|" +
                                            account.getBalance() + "|" +
                                            ca.getOverdraftLimit() + "|" +
                                            ca.getOverdraftFee());
                                } else {
                                    // For any other account types, we can add more cases here
                                    // throwException();
                                    System.out.println("Unknown account type for user " + user.getUsername() + ": " + account.getClass().getSimpleName());
                                }

                                // Switch statement not supported in java 17, using if-else instead
                                // switch (account) {
                                //     case SavingsAccount sa -> // Format: userId|SAVINGS|accountNumber|balance|interestRate|maxWithdrawals
                                //         writer.println(user.getUserId() + "|SAVINGS|" +
                                //                 account.getAccountNumber() + "|" +
                                //                 account.getBalance() + "|" +
                                //                 sa.getInterestRate() + "|" +
                                //                 sa.getMaxWithdrawalsPerMonth());
                                //     case CheckingAccount ca -> // Format: userId|CHECKING|accountNumber|balance|overdraftLimit|overdraftFee
                                //         writer.println(user.getUserId() + "|CHECKING|" +
                                //                 account.getAccountNumber() + "|" +
                                //                 account.getBalance() + "|" +
                                //                 ca.getOverdraftLimit() + "|" +
                                //                 ca.getOverdraftFee());
                                //     default -> {
                                //     }
                                // }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Saves all transactions for all accounts.
     * Format: accountNumber|amount|type|date
     */
    private static void saveTransactions() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
            int userCount = UserManager.getUserCount();
            
            for (int i = 0; i < userCount; i++) {
                User user = UserManager.getUser(i);
                if (user != null && user instanceof RegularUser) {
                    Account[] accounts = user.getAccounts();
                    if (accounts != null) {
                        for (Account account : accounts) {
                            if (account != null) {
                                int transCount = account.getTransactionCount();
                                for (int j = 0; j < transCount; j++) {
                                    Transaction trans = account.getTransaction(j);
                                    if (trans != null) {
                                        // Format: accountNumber|amount|type|date
                                        writer.println(account.getAccountNumber() + "|" + 
                                                     trans.getAmount() + "|" + 
                                                     trans.getType() + "|" + 
                                                     trans.getDate());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Deletes all stored data files.
     * This is used by the admin to reset the system.
     */
    public static boolean deleteAllData() {
        try {
            File usersFile = new File(USERS_FILE);
            File accountsFile = new File(ACCOUNTS_FILE);
            File transFile = new File(TRANSACTIONS_FILE);
            
            boolean success = true;
            if (usersFile.exists()) {
                success &= usersFile.delete();
            }
            if (accountsFile.exists()) {
                success &= accountsFile.delete();
            }
            if (transFile.exists()) {
                success &= transFile.delete();
            }
            
            return success;
        } catch (Exception e) {
            System.out.println("Error deleting data files: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Checks if saved data exists.
     * Uses inherited dataFilesExist() method from DataPersistence.
     */
    public static boolean dataExists() {
        return dataFilesExist();
    }
}
