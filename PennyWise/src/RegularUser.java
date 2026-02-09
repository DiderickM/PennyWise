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
 */
public class RegularUser extends User {
    // ENCAPSULATION: Private account field
    private Account account;

    /**
     * Constructor to initialize a RegularUser.
     * 
     * @param userId Unique user identifier
     * @param username User's login username
     * @param password User's login password
     * @param email User's email address
     * @param account User's bank account
     */
    public RegularUser(String userId, String username, String password, String email, Account account) {
        super(userId, username, password, email);
        this.account = account;
    }

    // ENCAPSULATION: Getter and Setter for account
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * POLYMORPHISM IMPLEMENTATION: RegularUser displays their personal dashboard.
     * This overrides the abstract displayDashboard() method from User.
     */
    @Override
    public void displayDashboard() {
        System.out.println("\n========== Regular User Dashboard ==========");
        System.out.println("Welcome, " + getUsername() + "!");
        if (account != null) {
            System.out.println("Account Type: " + account.getAccountType());
            System.out.println("Current Balance: $" + String.format("%.2f", account.getBalance()));
        }
        System.out.println("===========================================");
    }

    /**
     * VALUE RETURNING METHOD: Returns user's current account balance.
     */
    public double getBalance() {
        return (account != null) ? account.getBalance() : 0.0;
    }

    /**
     * VOID METHOD: Displays transaction history from the user's account.
     */
    public void viewTransactionHistory() {
        if (account != null) {
            account.displayTransactionHistory();
        } else {
            System.out.println("No account associated with this user.");
        }
    }
}
