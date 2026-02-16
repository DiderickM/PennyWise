/**
 * Created on Mon Feb 09 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

/**
 * RegularUser class extends User with account management functionality.
 * Demonstrates INHERITANCE and POLYMORPHISM.
 * 
 * Regular users can view their own accounts and transactions.
 * Supports multiple accounts through the inherited accounts array.
 */
public class RegularUser extends User {

    /**
     * Constructor to initialize a RegularUser without initial accounts.
     * 
     * @param userId Unique user identifier
     * @param username User's login username
     * @param password User's login password
     * @param email User's email address
     */
    public RegularUser(String userId, String username, String password, String email) {
        super(userId, username, password, email);
    }

    /**
     * Constructor to initialize a RegularUser with an initial account.
     * Maintains backward compatibility.
     * 
     * @param userId Unique user identifier
     * @param username User's login username
     * @param password User's login password
     * @param email User's email address
     * @param account Initial bank account
     */
    public RegularUser(String userId, String username, String password, String email, Account account) {
        super(userId, username, password, email);
        if (account != null) {
            this.addAccount(account);
        }
    }

    // ENCAPSULATION: Getter for first account (deprecated - use getAccounts() for multiple accounts)
    public Account getAccount() {
        Account[] accounts = getAccounts();
        if (accounts != null && accounts.length > 0) {
            return accounts[0];
        }
        return null;
    }

    // ENCAPSULATION: Setter for first account (backward compatibility)
    public void setAccount(Account account) {
        if (account != null) {
            Account[] accounts = getAccounts();
            if (accounts == null || accounts.length == 0) {
                addAccount(account);
            } else {
                accounts[0] = account;
            }
        }
    }

    /**
     * VALUE RETURNING METHOD: Returns the account at specified index.
     * 
     * @param index Index of the account
     * @return Account at the specified index, or null if not found
     */
    public Account getAccountByIndex(int index) {
        Account[] accounts = getAccounts();
        if (accounts != null && index >= 0 && index < accounts.length) {
            return accounts[index];
        }
        return null;
    }

    /**
     * POLYMORPHISM IMPLEMENTATION: RegularUser displays their personal dashboard.
     * This overrides the abstract displayDashboard() method from User.
     * Displays all accounts owned by the user.
     */
    @Override
    public void displayDashboard() {
        System.out.println("\n========== Regular User Dashboard ==========");
        System.out.println("Welcome, " + getUsername() + "!");
        Account[] accounts = getAccounts();
        if (accounts != null && accounts.length > 0) {
            System.out.println("Your Accounts ("+accounts.length+"):");
            for (int i = 0; i < accounts.length; i++) {
                if (accounts[i] != null) {
                    System.out.println("  [" + accounts[i].getAccountNumber() + "] " + 
                                     accounts[i].getAccountType() + 
                                     " - Balance: $" + App.formatMoney(accounts[i].getBalance()));
                }
            }
        } else {
            System.out.println("No accounts associated with this user.");
        }
        System.out.println("===========================================");
    }

    /**
     * VALUE RETURNING METHOD: Returns user's current account balance (first account).
     */
    public double getBalance() {
        Account account = getAccount();
        return (account != null) ? account.getBalance() : 0.0;
    }

    /**
     * VOID METHOD: Displays transaction history from the user's first account.
     */
    public void viewTransactionHistory() {
        Account account = getAccount();
        if (account != null) {
            account.displayTransactionHistory();
        } else {
            System.out.println("No account associated with this user.");
        }
    }
    
    /**
     * VOID METHOD: Displays transaction history from a specific account.
     * 
     * @param accountIndex Index of the account
     */
    public void viewTransactionHistory(int accountIndex) {
        Account account = getAccountByIndex(accountIndex);
        if (account != null) {
            account.displayTransactionHistory();
        } else {
            System.out.println("Invalid account index.");
        }
    }

    /**
     * VALUE RETURNING METHOD: Transfers money between two of the user's own accounts.
     * 
     * @param sourceIndex Index of source account
     * @param targetIndex Index of target account
     * @param amount Amount to transfer
     * @return true if transfer successful, false otherwise
     */
    public boolean transferBetweenAccounts(int sourceIndex, int targetIndex, double amount) {
        Account sourceAccount = getAccountByIndex(sourceIndex);
        Account targetAccount = getAccountByIndex(targetIndex);
        
        if (sourceAccount == null) {
            System.out.println("Source account not found.");
            return false;
        }
        
        if (targetAccount == null) {
            System.out.println("Target account not found.");
            return false;
        }
        
        return sourceAccount.transfer(amount, targetAccount);
    }

    /**
     * VALUE RETURNING METHOD: Transfers money from this user to another user.
     * Only allows transfers from a CheckingAccount.
     *
     * @param sourceIndex Index of source account (must be checking)
     * @param targetUser Target user
     * @param targetIndex Index of target user's account
     * @param amount Amount to transfer
     * @return true if transfer successful, false otherwise
     */
    public boolean transferToOtherUser(int sourceIndex, RegularUser targetUser, int targetIndex, double amount) {
        Account sourceAccount = getAccountByIndex(sourceIndex);
        if (sourceAccount == null) {
            System.out.println("Source account not found.");
            return false;
        }

        if (!(sourceAccount instanceof CheckingAccount)) {
            System.out.println("Transfers to other users are only allowed from a checking account.");
            return false;
        }

        if (targetUser == null || targetUser == this) {
            System.out.println("Target user is invalid.");
            return false;
        }

        Account targetAccount = targetUser.getAccountByIndex(targetIndex);
        if (targetAccount == null) {
            System.out.println("Target account not found.");
            return false;
        }

        return sourceAccount.transfer(amount, targetAccount);
    }
}
