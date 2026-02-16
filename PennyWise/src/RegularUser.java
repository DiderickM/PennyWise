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
     * Displays all accounts owned by the user with detailed information.
     */
    @Override
    public void displayDashboard() {
        System.out.println("\n========== Regular User Dashboard ==========");
        System.out.println("Welcome, " + getUsername() + "!");
        System.out.println("Email: " + getEmail());
        Account[] accounts = getAccounts();
        if (accounts != null && accounts.length > 0) {
            System.out.println("\nYour Accounts (" + accounts.length + "):");
            double totalBalance = 0;
            
            for (int i = 0; i < accounts.length; i++) {
                if (accounts[i] != null) {
                    System.out.println("\n  Account " + (i + 1) + ":");
                    System.out.println("    Account Number: " + accounts[i].getAccountNumber());
                    System.out.println("    Type: " + accounts[i].getAccountType());
                    System.out.println("    Balance: $" + InputValidator.formatMoney(accounts[i].getBalance()));
                    
                    switch (accounts[i]) {
                        case SavingsAccount savings -> {
                            System.out.println("    Interest Rate: " + InputValidator.formatPercentage(savings.getInterestRate()));
                            System.out.println("    Max Withdrawals/Month: " + savings.getMaxWithdrawalsPerMonth());
                        }
                        case CheckingAccount checking -> {
                            System.out.println("    Overdraft Limit: $" + InputValidator.formatMoney(checking.getOverdraftLimit()));
                            System.out.println("    Overdraft Fee: $" + InputValidator.formatMoney(checking.getOverdraftFee()));
                        }
                        default -> {
                            System.out.println("    No additional details available for this account type.");
                        }
                    }
                    
                    totalBalance += accounts[i].getBalance();
                }
            }
            
            System.out.println("\n  Total Balance: $" + InputValidator.formatMoney(totalBalance));
        } else {
            System.out.println("No accounts associated with this user.");
        }
        System.out.println("===========================================");
    }
}
