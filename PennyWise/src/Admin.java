/**
 * Created on Mon Feb 09 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

/**
 * Admin class extends User with system administration capabilities.
 * Demonstrates INHERITANCE and POLYMORPHISM.
 * 
 * Administrators have elevated permissions to view and modify all user accounts.
 */
import java.util.Scanner;

public class Admin extends User {
    // ENCAPSULATION: Private field for admin privileges
    private String adminLevel; // "BASIC" or "SUPER"

    /**
     * Constructor to initialize an Admin.
     * 
     * @param userId Unique user identifier
     * @param username User's login username
     * @param password User's login password
     * @param email User's email address
     * @param adminLevel Admin privilege level
     */
    public Admin(String userId, String username, String password, String email, String adminLevel) {
        super(userId, username, password, email);
        this.adminLevel = adminLevel;
    }

    // ENCAPSULATION: Getter and Setter for admin level
    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    /**
     * POLYMORPHISM IMPLEMENTATION: Admin displays the admin dashboard.
     * This overrides the abstract displayDashboard() method from User.
     */
    @Override
    public void displayDashboard() {
        System.out.println("\n========== Admin Dashboard ==========");
        System.out.println("Welcome, Admin " + getUsername() + "!");
        System.out.println("Admin Level: " + adminLevel);
        System.out.println("Options: View all users, Modify accounts, Generate reports");
        System.out.println("=====================================");
    }

    /**
     * VALUE RETURNING METHOD: Returns whether this admin has super privileges.
     */
    public boolean hasSuperPrivileges() {
        return adminLevel.equals("SUPER");
    }

    /**
     * VOID METHOD: Displays admin capabilities and notable actions.
     */
    public void displayAdminCapabilities() {
        System.out.println("\n--- Admin Capabilities ---");
        System.out.println("1. View all user accounts");
        System.out.println("2. Modify user information");
        System.out.println("3. Adjust account balances (for corrections)");
        System.out.println("4. Delete user accounts");
        if (hasSuperPrivileges()) {
            System.out.println("5. [SUPER] Generate system-wide reports");
            System.out.println("6. [SUPER] Manage other administrators");
        }
        System.out.println("-------------------------");
    }

    /**
     * STATIC ENTRY: Launch an admin session (handles login and session control).
     */
    public static void launchAdmin(Scanner scanner) {
        System.out.println("\n--- Admin Mode ---");
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();

        // For demo, create a default admin
        Admin admin = new Admin("admin001", "admin", "admin123", "admin@pennywise.com", "SUPER");

        if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
            admin.displayDashboard();
            admin.runAdminSession(scanner);
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    /**
     * INSTANCE METHOD: Run the interactive admin session. All admin operations
     * (view users, modify, adjust balances, delete, reports, manage admins)
     * are implemented here and operate via `App` accessor methods.
     */
    public void runAdminSession(Scanner scanner) {
        boolean inAdmin = true;
        while (inAdmin) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View All Users");
            System.out.println("2. Modify User Information");
            System.out.println("3. Adjust Account Balance");
            System.out.println("4. Delete User Account");
            if (hasSuperPrivileges()) {
                System.out.println("5. Generate System-wide Report");
                System.out.println("6. Apply Account Features (Interest) to All Savings Accounts");
                System.out.println("7. Apply Account Features (Overdraft Fees) to All Checking Accounts");
                System.out.println("8. Manage Administrators");
            }
            System.out.println("9. Logout");
            System.out.print("Select option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> displayAllUsers();
                case "2" -> modifyUserInformation(scanner);
                case "3" -> adjustAccountBalance(scanner);
                case "4" -> deleteUserAccount(scanner);
                case "5" -> {
                    if (hasSuperPrivileges()) generateSystemReport();
                    else System.out.println("You don't have permission for this action.");
                }
                case "6" -> {
                    if (hasSuperPrivileges()) applyAccountFeaturesToAllSavings();
                    else System.out.println("You don't have permission for this action.");
                }
                case "7" -> {
                    if (hasSuperPrivileges()) applyAccountFeaturesToAllChecking();
                    else System.out.println("You don't have permission for this action.");
                }
                case "8" -> {
                    if (hasSuperPrivileges()) manageAdmins(scanner);
                    else System.out.println("You don't have permission for this action.");
                }
                case "9" -> {
                    inAdmin = false;
                    System.out.println("Admin logged out.");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ---------------- Admin helper methods (moved from App) -----------------

    private void displayAllUsers() {
        System.out.println("\n========== All Users ==========");
        if (App.getUserCount() == 0) {
            System.out.println("No users registered.");
        } else {
            for (int i = 0; i < App.getUserCount(); i++) {
                User u = App.getUser(i);
                if (u != null) {
                    System.out.println((i + 1) + ". " + u.getUsername() + " (ID: " + u.getUserId() + ")");
                }
            }
        }
        System.out.println("==============================");
    }

    private RegularUser findRegularUserByUsername(String username) {
        // Prefer App-provided accessor for RegularUser lookup
        return App.getRegularUserByUsername(username);
    }

    private void modifyUserInformation(Scanner scanner) {
        System.out.print("Enter username of user to modify: ");
        String username = scanner.nextLine();
        RegularUser user = findRegularUserByUsername(username);

        if (user != null) {
            System.out.println("\n--- Modify User Information ---");
            System.out.println("1. Change Username");
            System.out.println("2. Change Email");
            System.out.println("3. Change Password");
            System.out.println("4. Cancel");
            System.out.print("Select option (1-4): ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    System.out.print("Enter new username: ");
                    String newUsername = scanner.nextLine();
                    user.setUsername(newUsername);
                    System.out.println("Username updated successfully!");
                }
                case "2" -> {
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.nextLine();
                    user.setEmail(newEmail);
                    System.out.println("Email updated successfully!");
                }
                case "3" -> {
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    user.setPassword(newPassword);
                    System.out.println("Password updated successfully!");
                }
                case "4" -> System.out.println("Modification cancelled.");
                default -> System.out.println("Invalid option.");
            }
        } else {
            System.out.println("User not found.");
        }
    }

    private void adjustAccountBalance(Scanner scanner) {
        System.out.print("Enter username of user to adjust balance: ");
        String username = scanner.nextLine();
        RegularUser user = findRegularUserByUsername(username);

        if (user != null && user.getAccount() != null) {
            Account account = user.getAccount();
            System.out.println("\n--- Adjust Account Balance ---");
            System.out.println("Current Balance: $" + String.format("%.2f", account.getBalance()));
            System.out.println("1. Add Amount");
            System.out.println("2. Subtract Amount");
            System.out.println("3. Set Exact Balance");
            System.out.println("4. Cancel");
            System.out.print("Select option (1-4): ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    try {
                        System.out.print("Enter amount to add: $");
                        double addAmount = Double.parseDouble(scanner.nextLine());
                        if (addAmount > 0) {
                            account.setBalance(account.getBalance() + addAmount);
                            System.out.println("Amount added. New Balance: $" + String.format("%.2f", account.getBalance()));
                        } else {
                            System.out.println("Invalid amount.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
                case "2" -> {
                    try {
                        System.out.print("Enter amount to subtract: $");
                        double subtractAmount = Double.parseDouble(scanner.nextLine());
                        if (subtractAmount > 0) {
                            account.setBalance(account.getBalance() - subtractAmount);
                            System.out.println("Amount subtracted. New Balance: $" + String.format("%.2f", account.getBalance()));
                        } else {
                            System.out.println("Invalid amount.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
                case "3" -> {
                    try {
                        System.out.print("Enter exact balance amount: $");
                        double exactAmount = Double.parseDouble(scanner.nextLine());
                        if (exactAmount >= 0) {
                            account.setBalance(exactAmount);
                            System.out.println("Balance set to: $" + String.format("%.2f", account.getBalance()));
                        } else {
                            System.out.println("Balance must be non-negative.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
                case "4" -> System.out.println("Adjustment cancelled.");
                default -> System.out.println("Invalid option.");
            }
        } else {
            System.out.println("User or account not found.");
        }
    }

    private void deleteUserAccount(Scanner scanner) {
        System.out.print("Enter username of user to delete: ");
        String username = scanner.nextLine();

        int userIndex = -1;
        for (int i = 0; i < App.getUserCount(); i++) {
            User u = App.getUser(i);
            if (u instanceof RegularUser user) {
                if (user.getUsername().equals(username)) {
                    userIndex = i;
                    break;
                }
            }
        }

        if (userIndex != -1) {
            System.out.print("Are you sure you want to delete user '" + username + "'? (yes/no): ");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("yes")) {
                boolean removed = App.removeRegularUserByUsername(username);
                if (removed) System.out.println("User '" + username + "' has been deleted successfully.");
                else System.out.println("Failed to delete user.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            System.out.println("User not found.");
        }
    }

    private void generateSystemReport() {
        System.out.println("\n========== SYSTEM-WIDE FINANCIAL REPORT ==========");
        System.out.println("Generated at: " + new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date()));
        double totalBalance = 0;
        int totalTransactions = 0;
        int savingsAccountCount = 0;
        int checkingAccountCount = 0;

        System.out.println("\n--- User Account Summary ---");
        if (App.getUserCount() == 0) {
            System.out.println("No users in system.");
        } else {
            for (int i = 0; i < App.getUserCount(); i++) {
                User u = App.getUser(i);
                if (u instanceof RegularUser user) {
                    Account account = user.getAccount();
                    if (account != null) {
                        System.out.println((i + 1) + ". " + user.getUsername() + " - Account: " + account.getAccountType());
                        System.out.println("   Balance: $" + String.format("%.2f", account.getBalance()));
                        System.out.println("   Transactions: " + account.getTransactionCount());
                        totalBalance += account.getBalance();
                        totalTransactions += account.getTransactionCount();
                        if (account instanceof SavingsAccount) savingsAccountCount++;
                        else if (account instanceof CheckingAccount) checkingAccountCount++;
                    }
                }
            }
        }

        System.out.println("\n--- Financial Summary ---");
        System.out.println("Total Users: " + App.getUserCount());
        System.out.println("Savings Accounts: " + savingsAccountCount);
        System.out.println("Checking Accounts: " + checkingAccountCount);
        System.out.println("Total System Balance: $" + String.format("%.2f", totalBalance));
        System.out.println("Total Transactions: " + totalTransactions);
        System.out.println("Average Balance per User: $" + String.format("%.2f", App.getUserCount() > 0 ? totalBalance / App.getUserCount() : 0));
        System.out.println("==================================================");
    }

    private void manageAdmins(Scanner scanner) {
        System.out.println("\n--- Administrator Management ---");
        System.out.println("1. View Admins (Simulated - Only one admin in demo)");
        System.out.println("2. Create New Admin");
        System.out.println("3. Modify Admin");
        System.out.println("4. Cancel");
        System.out.print("Select option (1-4): ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> {
                System.out.println("\n--- Current Admins ---");
                System.out.println("1. admin (SUPER) - admin@pennywise.com");
                System.out.println("Note: In full implementation, multiple admins would be stored in an array.");
            }
            case "2" -> {
                System.out.println("\n--- Create New Admin ---");
                System.out.print("Enter new admin username: ");
                String newAdminUsername = scanner.nextLine();
                System.out.print("Enter admin privilege level (BASIC/SUPER): ");
                String newAdminLevel = scanner.nextLine();
                if (newAdminLevel.equalsIgnoreCase("BASIC") || newAdminLevel.equalsIgnoreCase("SUPER")) {
                    System.out.println("New admin '" + newAdminUsername + "' created with " + newAdminLevel + " privileges.");
                    System.out.println("Note: In full implementation, this would be stored in a database.");
                } else {
                    System.out.println("Invalid privilege level. Must be BASIC or SUPER.");
                }
            }
            case "3" -> {
                System.out.println("\n--- Modify Admin ---");
                System.out.print("Enter admin username to modify: ");
                String adminUsername = scanner.nextLine();
                if (adminUsername.equals("admin")) {
                    System.out.println("1. Change Email");
                    System.out.println("2. Change Privilege Level");
                    System.out.print("Select option (1-2): ");
                    String modifyChoice = scanner.nextLine();
                    switch (modifyChoice) {
                        case "1" -> {
                            System.out.print("Enter new email: ");
                            String newEmail = scanner.nextLine();
                            System.out.println("Admin email updated to: " + newEmail);
                        }
                        case "2" -> {
                            System.out.print("Enter new privilege level (BASIC/SUPER): ");
                            String newLevel = scanner.nextLine();
                            if (newLevel.equalsIgnoreCase("BASIC") || newLevel.equalsIgnoreCase("SUPER")) {
                                System.out.println("Admin privilege level changed to: " + newLevel);
                            } else {
                                System.out.println("Invalid privilege level.");
                            }
                        }
                        default -> System.out.println("Invalid option.");
                    }
                } else {
                    System.out.println("Admin not found.");
                }
            }
            case "4" -> System.out.println("Admin management cancelled.");
            default -> System.out.println("Invalid option.");
        }
    }

    /**
     * VOID METHOD: Applies account features (like interest) to all savings accounts.
     * Super admin only - processes interest calculations for all savings accounts.
     */
    private void applyAccountFeaturesToAllSavings() {
        System.out.println("\n========== Applying Account Features to All Savings Accounts ==========");
        int savingsAccountCount = 0;
        double totalInterestApplied = 0;

        // LOOPS: for loop to iterate through all users
        for (int i = 0; i < App.getUserCount(); i++) {
            User u = App.getUser(i);
            if (u instanceof RegularUser user) {
                Account account = user.getAccount();
                // SELECTION: Check if account is a SavingsAccount
                if (account instanceof SavingsAccount savingsAccount) {
                    double balanceBefore = account.getBalance();
                    // Apply account-specific features (interest in this case)
                    savingsAccount.applyAccountFeatures();
                    double balanceAfter = account.getBalance();
                    double interestEarned = balanceAfter - balanceBefore;
                    
                    System.out.println("User: " + user.getUsername());
                    System.out.println("  Account: " + account.getAccountNumber());
                    System.out.println("  Balance Before: $" + String.format("%.2f", balanceBefore));
                    System.out.println("  Interest Applied: $" + String.format("%.2f", interestEarned));
                    System.out.println("  Balance After: $" + String.format("%.2f", balanceAfter));
                    
                    savingsAccountCount++;
                    totalInterestApplied += interestEarned;
                }
            }
        }

        // Display summary
        System.out.println("\n--- Summary ---");
        System.out.println("Total Savings Accounts Processed: " + savingsAccountCount);
        System.out.println("Total Interest Applied System-wide: $" + String.format("%.2f", totalInterestApplied));
        System.out.println("====================================================================");
    }

    /**
     * VOID METHOD: Applies account features (overdraft fees) to all checking accounts.
     * Super admin only - processes overdraft fee calculations for all checking accounts.
     */
    private void applyAccountFeaturesToAllChecking() {
        System.out.println("\n========== Applying Account Features to All Checking Accounts ==========");
        int checkingAccountCount = 0;
        double totalOverdraftFeesApplied = 0;

        // LOOPS: for loop to iterate through all users
        for (int i = 0; i < App.getUserCount(); i++) {
            User u = App.getUser(i);
            if (u instanceof RegularUser user) {
                Account account = user.getAccount();
                // SELECTION: Check if account is a CheckingAccount
                if (account instanceof CheckingAccount checkingAccount) {
                    double balanceBefore = account.getBalance();
                    // Apply account-specific features (overdraft fees in this case)
                    checkingAccount.applyAccountFeatures();
                    double balanceAfter = account.getBalance();
                    double feesApplied = balanceBefore - balanceAfter;
                    
                    System.out.println("User: " + user.getUsername());
                    System.out.println("  Account: " + account.getAccountNumber());
                    System.out.println("  Balance Before: $" + String.format("%.2f", balanceBefore));
                    System.out.println("  Overdraft Status: " + (checkingAccount.isInOverdraft() ? "IN OVERDRAFT" : "NORMAL"));
                    System.out.println("  Fees Applied: $" + String.format("%.2f", feesApplied));
                    System.out.println("  Balance After: $" + String.format("%.2f", balanceAfter));
                    
                    checkingAccountCount++;
                    totalOverdraftFeesApplied += feesApplied;
                }
            }
        }

        // Display summary
        System.out.println("\n--- Summary ---");
        System.out.println("Total Checking Accounts Processed: " + checkingAccountCount);
        System.out.println("Total Overdraft Fees Applied System-wide: $" + String.format("%.2f", totalOverdraftFeesApplied));
        System.out.println("====================================================================");
    }
}
