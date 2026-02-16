/**
 * Created on Sun Feb 16 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

import java.util.Scanner;

/**
 * AccountManager class handles all account-related operations.
 * Demonstrates SINGLE RESPONSIBILITY PRINCIPLE and ENCAPSULATION.
 * 
 * This class manages:
 * - Account creation
 * - Account selection (common pattern extraction)
 * - Account type validation
 * - Account number generation
 */
public class AccountManager {
    
    /**
     * VALUE RETURNING METHOD: Creates a new account based on user selection.
     * Eliminates duplicate account creation logic.
     * 
     * @param userId User ID for account number generation
     * @param accountChoice User's account type choice (1=Savings, 2=Checking)
     * @param accountCount Current number of accounts (for unique numbering)
     * @return Newly created Account object
     */
    public static Account createAccount(String userId, String accountChoice, int accountCount) {
        return switch (accountChoice) {
            case "1" -> {
                System.out.println("Savings Account created.");
                yield new SavingsAccount(
                    AppConstants.SAVINGS_ACCOUNT_PREFIX + userId + "-" + (accountCount + 1),
                    AppConstants.DEFAULT_INITIAL_BALANCE,
                    SystemConfiguration.getInstance().getDefaultSavingsInterestRate()
                );
            }
            case "2" -> {
                System.out.println("Checking Account created.");
                yield new CheckingAccount(
                    AppConstants.CHECKING_ACCOUNT_PREFIX + userId + "-" + (accountCount + 1),
                    AppConstants.DEFAULT_INITIAL_BALANCE,
                    SystemConfiguration.getInstance().getDefaultCheckingOverdraftLimit(),
                    SystemConfiguration.getInstance().getDefaultCheckingOverdraftFee()
                );
            }
            default -> {
                System.out.println("Invalid option. Savings Account created by default.");
                yield new SavingsAccount(
                    AppConstants.SAVINGS_ACCOUNT_PREFIX + userId + "-" + (accountCount + 1),
                    AppConstants.DEFAULT_INITIAL_BALANCE,
                    SystemConfiguration.getInstance().getDefaultSavingsInterestRate()
                );
            }
        };
    }
    
    /**
     * VALUE RETURNING METHOD: Selects an account from a user's account list.
     * Extracts common account selection pattern.
     * 
     * @param scanner Scanner for user input
     * @param accounts Array of accounts to select from
     * @param prompt Custom prompt message
     * @return Selected account, or null if invalid selection
     */
    public static Account selectAccount(Scanner scanner, Account[] accounts, String prompt) {
        if (hasNoAccounts(accounts)) {
            System.out.println("No accounts available.");
            return null;
        }
        
        if (accounts.length == 1) {
            return accounts[0]; // Auto-select if only one account
        }
        
        // Display accounts
        System.out.println("\n--- Select Account ---");
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] != null) {
                System.out.println((i + 1) + ". [" + accounts[i].getAccountNumber() + "] " +
                                 accounts[i].getAccountType() +
                                 " - Balance: $" + InputValidator.formatMoney(accounts[i].getBalance()));
            }
        }
        
        System.out.print(prompt);
        int choice = InputValidator.getIntInput(scanner);
        
        if (InputValidator.isValidMenuChoice(choice, 1, accounts.length)) {
            return accounts[choice - 1];
        }
        
        System.out.println("Invalid selection.");
        return null;
    }

    
    /**
     * VALUE RETURNING METHOD: Selects a target account excluding a specific one.
     * Used for transfer operations where source and target must differ.
     * 
     * @param scanner Scanner for user input
     * @param accounts Array of accounts to select from
     * @param excludeIndex Index of account to exclude
     * @param prompt Custom prompt message
     * @return Selected account index (0-based), or -1 if invalid selection
     */
    public static int selectAccountExcluding(Scanner scanner, Account[] accounts, int excludeIndex, String prompt) {
        if (hasNoAccounts(accounts) || accounts.length < 2) {
            System.out.println("Need at least 2 accounts for this operation.");
            return -1;
        }
        
        // Display available accounts (excluding specified index)
        System.out.println("\n--- Available Accounts ---");
        for (int i = 0; i < accounts.length; i++) {
            if (i != excludeIndex && accounts[i] != null) {
                System.out.println((i + 1) + ". [" + accounts[i].getAccountNumber() + "] " +
                                 accounts[i].getAccountType() +
                                 " - Balance: $" + InputValidator.formatMoney(accounts[i].getBalance()));
            }
        }
        
        System.out.print(prompt);
        int choice = InputValidator.getIntInput(scanner) - 1;
        
        if (choice >= 0 && choice < accounts.length && choice != excludeIndex && accounts[choice] != null) {
            return choice;
        }
        
        System.out.println("Invalid selection.");
        return -1;
    }
    
    /**
     * VALUE RETURNING METHOD: Checks if accounts array is null or empty.
     * 
     * @param accounts Array to check
     * @return true if no accounts, false otherwise
     */
    public static boolean hasNoAccounts(Account[] accounts) {
        return accounts == null || accounts.length == 0;
    }
    
    /**
     * VALUE RETURNING METHOD: Finds account by account number in user's accounts.
     * 
     * @param user User to search accounts for
     * @param accountNumber Account number to find
     * @return Account if found, null otherwise
     */
    public static Account findAccountByNumber(RegularUser user, String accountNumber) {
        if (user == null || accountNumber == null) {
            return null;
        }
        
        Account[] accounts = user.getAccounts();
        if (hasNoAccounts(accounts)) {
            return null;
        }
        
        for (Account account : accounts) {
            if (account != null && account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        
        return null;
    }
    
    /**
     * VALUE RETURNING METHOD: Gets account index by account number.
     * 
     * @param user User to search accounts for
     * @param accountNumber Account number to find
     * @return Account index (0-based), or -1 if not found
     */
    public static int getAccountIndexByNumber(RegularUser user, String accountNumber) {
        if (user == null || accountNumber == null) {
            return -1;
        }
        
        Account[] accounts = user.getAccounts();
        if (hasNoAccounts(accounts)) {
            return -1;
        }
        
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] != null && accounts[i].getAccountNumber().equals(accountNumber)) {
                return i;
            }
        }
        
        return -1;
    }
    
    /**
     * VOID METHOD: Displays the account type selection menu.
     */
    public static void displayAccountTypeMenu() {
        System.out.println("Select Account Type:");
        System.out.println("1. Savings Account (" + InputValidator.formatPercentage(SystemConfiguration.getInstance().getDefaultSavingsInterestRate()) + " interest and " + InputValidator.formatMoney(SystemConfiguration.getInstance().getDefaultSavingsMaxWithdrawals()) + " max monthly withdrawals)");
        System.out.println("2. Checking Account (with overdraft fee of: $" + InputValidator.formatMoney(SystemConfiguration.getInstance().getDefaultCheckingOverdraftFee()) + "/" + InputValidator.formatMoney(SystemConfiguration.getInstance().getDefaultCheckingOverdraftLimit()) + " overdraft limit)");
    }
    
    /**
     * VALUE RETURNING METHOD: Counts accounts of a specific type for a user.
     * 
     * @param user User to count accounts for
     * @param accountType Type of account (SAVINGS or CHECKING)
     * @return Number of accounts of specified type
     */
    public static int countAccountsByType(RegularUser user, String accountType) {
        if (user == null || accountType == null) {
            return 0;
        }
        
        Account[] accounts = user.getAccounts();
        if (hasNoAccounts(accounts)) {
            return 0;
        }
        
        int count = 0;
        for (Account account : accounts) {
            if (account != null && account.getAccountType().equals(accountType)) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * VALUE RETURNING METHOD: Filters accounts to return only checking accounts.
     * 
     * @param accounts Array of all accounts
     * @return Array containing only checking accounts
     */
    public static Account[] getCheckingAccounts(Account[] accounts) {
        if (accounts == null) {
            return new Account[0];
        }
        
        // Count checking accounts
        int count = 0;
        for (Account account : accounts) {
            if (account instanceof CheckingAccount) {
                count++;
            }
        }
        
        // Create array with checking accounts only
        Account[] checkingAccounts = new Account[count];
        int index = 0;
        for (Account account : accounts) {
            if (account instanceof CheckingAccount) {
                checkingAccounts[index++] = account;
            }
        }
        
        return checkingAccounts;
    }
    
    /**
     * VALUE RETURNING METHOD: Selects a checking account from the filtered list.
     * Only displays and allows selection of checking accounts.
     * 
     * @param scanner Scanner for user input
     * @param checkingAccounts Array of checking accounts
     * @param prompt Prompt message
     * @return Selected checking account or null
     */
    public static Account selectCheckingAccount(Scanner scanner, Account[] checkingAccounts, String prompt) {
        if (checkingAccounts == null || checkingAccounts.length == 0) {
            return null;
        }
        
        if (checkingAccounts.length == 1) {
            System.out.println("Only one checking account available. Auto-selecting it.");
            return checkingAccounts[0]; // Auto-select if only one checking account
        }
        
        // Display checking accounts only
        System.out.println("\n--- Select Checking Account ---");
        for (int i = 0; i < checkingAccounts.length; i++) {
            if (checkingAccounts[i] != null) {
                System.out.println((i + 1) + ". [" + checkingAccounts[i].getAccountNumber() + "] " +
                                 checkingAccounts[i].getAccountType() +
                                 " - Balance: $" + InputValidator.formatMoney(checkingAccounts[i].getBalance()));
            }
        }
        
        System.out.print(prompt + " (1-" + checkingAccounts.length + "): ");
        int choice = InputValidator.getIntInput(scanner);
        
        if (InputValidator.isValidMenuChoice(choice, 1, checkingAccounts.length)) {
            return checkingAccounts[choice - 1];
        }
        
        System.out.println("Invalid selection.");
        return null;
    }
    
    // Private constructor to prevent instantiation
    private AccountManager() {
        throw new AssertionError("Manager class should not be instantiated");
    }
}
