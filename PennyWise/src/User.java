/**
 * Abstract User class represents the base user in PennyWise system.
 * Demonstrates INHERITANCE, POLYMORPHISM, and ENCAPSULATION.
 * 
 * This is the parent class for RegularUser and Admin subclasses.
 * Each subclass must implement the abstract displayDashboard() method differently.
 */
public abstract class User {
    // ENCAPSULATION: Private fields with getters and setters
    private String userId;
    private String username;
    private String password;
    private String email;
    private Account[] accounts = null; 
    
    /**
     * Constructor to initialize User attributes.
     * 
     * @param userId Unique user identifier
     * @param username User's login username
     * @param password User's login password
     * @param email User's email address
     */
    public User(String userId, String username, String password, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // ENCAPSULATION: Getter and Setter methods
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Account[] getAccounts() {
        return accounts;
    }

    public void setAccounts(Account[] accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        if (accounts == null) {
            accounts = new Account[1];
            accounts[0] = account;
        } else {
            // Create a new array with one additional slot
            Account[] newAccounts = new Account[accounts.length + 1];
            System.arraycopy(accounts, 0, newAccounts, 0, accounts.length);
            newAccounts[accounts.length] = account; // Add new account to the end
            accounts = newAccounts; // Replace old array with new array
        }
    }

    /**
     * POLYMORPHISM: Abstract method that will be implemented differently
     * by RegularUser and Admin subclasses.
     */
    public abstract void displayDashboard();

    /**
     * VOID METHOD: Displays user profile information.
     */
    public void displayProfile() {
        System.out.println("\n========== User Profile ==========");
        System.out.println("User ID: " + userId);
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("\nAccounts:");
        for (Account account : accounts) {
            if (account != null) {
                System.out.println("  [" + account.getAccountNumber() + "] " + 
                                 account.getAccountType() + " - Balance: $" + App.formatMoney(account.getBalance()));
            }
        }
        System.out.println("==================================");
    }
}
