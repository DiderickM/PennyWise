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
                System.out.println("8. Manage Administrators (Simulated)");
                System.out.println("9. Manage System Configuration");
                System.out.println("10. [DANGER] Delete All Stored Data");
                System.out.println("11. Logout");
            } else {
                System.out.println("5. Logout");
            }
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
                    if (hasSuperPrivileges()) manageSystemConfiguration(scanner);
                    else System.out.println("You don't have permission for this action.");
                }
                case "10" -> {
                    if (hasSuperPrivileges()) deleteAllStoredData(scanner);
                    else {
                        inAdmin = false;
                        System.out.println("Admin logged out.");
                    }
                }
                case "11" -> {
                    if (hasSuperPrivileges()) {
                        inAdmin = false;
                        System.out.println("Admin logged out.");
                    } else {
                        System.out.println("Invalid option.");
                    }
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
                    DataStorage.saveAllData(); // Save after user modification
                }
                case "2" -> {
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.nextLine();
                    user.setEmail(newEmail);
                    System.out.println("Email updated successfully!");
                    DataStorage.saveAllData(); // Save after user modification
                }
                case "3" -> {
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    user.setPassword(newPassword);
                    System.out.println("Password updated successfully!");
                    DataStorage.saveAllData(); // Save after user modification
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

        if (user != null) {
            Account[] accounts = user.getAccounts();
            if (App.hasNoAccounts(accounts)) {
                System.out.println("User has no accounts.");
                return;
            }
            
            // If multiple accounts, let admin choose
            Account account;
            if (accounts.length > 1) {
                System.out.println("\n--- Select Account ---");
                for (int i = 0; i < accounts.length; i++) {
                    if (accounts[i] != null) {
                        System.out.println((i+1) + ". " + accounts[i].getAccountType() + 
                                         " - Balance: $" + App.formatMoney(accounts[i].getBalance()));
                    }
                }
                System.out.print("Select account (1-" + accounts.length + "): ");
                int accountChoice = (int) App.getDoubleInput(scanner) - 1;
                if (accountChoice >= 0 && accountChoice < accounts.length && accounts[accountChoice] != null) {
                    account = accounts[accountChoice];
                } else {
                    System.out.println("Invalid account selection.");
                    return;
                }
            } else {
                account = accounts[0];
            }
            
            System.out.println("\n--- Adjust Account Balance ---");
            System.out.println("Current Balance: $" + App.formatMoney(account.getBalance()));
            System.out.println("1. Add Amount");
            System.out.println("2. Subtract Amount");
            System.out.println("3. Set Exact Balance");
            System.out.println("4. Cancel");
            System.out.print("Select option (1-4): ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    System.out.print("Enter amount to add: $");
                    double amount = App.getDoubleInput(scanner);
                    if (amount > 0) {
                        account.setBalance(account.getBalance() + amount);
                        System.out.println("Amount added. New Balance: $" + App.formatMoney(account.getBalance()));
                        DataStorage.saveAllData(); // Save after balance adjustment
                    } else {
                        System.out.println("Invalid amount.");
                    }
                }
                case "2" -> {
                    System.out.print("Enter amount to subtract: $");
                    double amount = App.getDoubleInput(scanner);
                    if (amount > 0) {
                        account.setBalance(account.getBalance() - amount);
                        System.out.println("Amount subtracted. New Balance: $" + App.formatMoney(account.getBalance()));
                        DataStorage.saveAllData(); // Save after balance adjustment
                    } else {
                        System.out.println("Invalid amount.");
                    }
                }
                case "3" -> {
                    System.out.print("Enter exact balance amount: $");
                    double amount = App.getDoubleInput(scanner);
                    if (amount >= 0) {
                        account.setBalance(amount);
                        System.out.println("Balance set to: $" + App.formatMoney(account.getBalance()));
                        DataStorage.saveAllData(); // Save after balance adjustment
                    } else {
                        System.out.println("Balance must be non-negative.");
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
                    Account[] accounts = user.getAccounts();
                    if (!App.hasNoAccounts(accounts)) {
                        int accountIndex = 1;
                        for (Account account : accounts) {
                            if (account != null) {
                                System.out.println((i + 1) + "." + accountIndex + " " + user.getUsername() + " - Account: " + account.getAccountType());
                                System.out.println("   Balance: $" + App.formatMoney(account.getBalance()));
                                System.out.println("   Transactions: " + account.getTransactionCount());
                                totalBalance += account.getBalance();
                                totalTransactions += account.getTransactionCount();
                                if (account instanceof SavingsAccount) savingsAccountCount++;
                                else if (account instanceof CheckingAccount) checkingAccountCount++;
                                accountIndex++;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("\n--- Financial Summary ---");
        System.out.println("Total Users: " + App.getUserCount());
        System.out.println("Savings Accounts: " + savingsAccountCount);
        System.out.println("Checking Accounts: " + checkingAccountCount);
        System.out.println("Total System Balance: $" + App.formatMoney(totalBalance));
        System.out.println("Total Transactions: " + totalTransactions);
        System.out.println("Average Balance per User: $" + App.formatMoney(App.getUserCount() > 0 ? totalBalance / App.getUserCount() : 0));
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
                Account[] accounts = user.getAccounts();
                if (!App.hasNoAccounts(accounts)) {
                    for (Account account : accounts) {
                        // SELECTION: Check if account is a SavingsAccount
                        if (account instanceof SavingsAccount savingsAccount) {
                            double balanceBefore = account.getBalance();
                            // Apply account-specific features (interest in this case)
                            savingsAccount.applyAccountFeatures();
                            double balanceAfter = account.getBalance();
                            double interestEarned = balanceAfter - balanceBefore;
                            
                            System.out.println("User: " + user.getUsername());
                            System.out.println("  Account: " + account.getAccountNumber());
                            System.out.println("  Balance Before: $" + App.formatMoney(balanceBefore));
                            System.out.println("  Interest Applied: $" + App.formatMoney(interestEarned));
                            System.out.println("  Balance After: $" + App.formatMoney(balanceAfter));
                            
                            savingsAccountCount++;
                            totalInterestApplied += interestEarned;
                        }
                    }
                }
            }
        }

        // Display summary
        System.out.println("\n--- Summary ---");
        System.out.println("Total Savings Accounts Processed: " + savingsAccountCount);
        System.out.println("Total Interest Applied System-wide: $" + App.formatMoney(totalInterestApplied));
        System.out.println("====================================================================");
        
        // Save data after applying interest
        DataStorage.saveAllData();
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
                Account[] accounts = user.getAccounts();
                if (!App.hasNoAccounts(accounts)) {
                    for (Account account : accounts) {
                        // SELECTION: Check if account is a CheckingAccount
                        if (account instanceof CheckingAccount checkingAccount) {
                            double balanceBefore = account.getBalance();
                            // Apply account-specific features (overdraft fees in this case)
                            checkingAccount.applyAccountFeatures();
                            double balanceAfter = account.getBalance();
                            double feesApplied = balanceBefore - balanceAfter;
                            
                            System.out.println("User: " + user.getUsername());
                            System.out.println("  Account: " + account.getAccountNumber());
                            System.out.println("  Balance Before: $" + App.formatMoney(balanceBefore));
                            System.out.println("  Overdraft Status: " + (checkingAccount.isInOverdraft() ? "IN OVERDRAFT" : "NORMAL"));
                            System.out.println("  Fees Applied: $" + App.formatMoney(feesApplied));
                            System.out.println("  Balance After: $" + App.formatMoney(balanceAfter));
                            
                            checkingAccountCount++;
                            totalOverdraftFeesApplied += feesApplied;
                        }
                    }
                }
            }
        }

        // Display summary
        System.out.println("\n--- Summary ---");
        System.out.println("Total Checking Accounts Processed: " + checkingAccountCount);
        System.out.println("Total Overdraft Fees Applied System-wide: $" + App.formatMoney(totalOverdraftFeesApplied));
        System.out.println("====================================================================");
        
        // Save data after applying fees
        DataStorage.saveAllData();
    }
    
    /**
     * VOID METHOD: Deletes all stored data files.
     * SUPER ADMIN ONLY - This is a dangerous operation that clears all persisted data.
     * Requires double confirmation before proceeding.
     */
    private void deleteAllStoredData(Scanner scanner) {
        System.out.println("\n========================================");
        System.out.println("WARNING: DELETE ALL STORED DATA");
        System.out.println("========================================");
        System.out.println("This action will permanently delete:");
        System.out.println("- All saved user accounts");
        System.out.println("- All account balances");
        System.out.println("- All transaction history");
        System.out.println("\nCurrent session data will remain until you exit.");
        System.out.println("However, this data will NOT be saved on next exit.");
        System.out.println("========================================\n");
        
        System.out.print("Are you ABSOLUTELY SURE you want to delete all data? (type 'DELETE' to confirm): ");
        String confirmation1 = scanner.nextLine();
        
        if (!confirmation1.equals("DELETE")) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        System.out.print("\nFinal confirmation - Type 'YES' to proceed: ");
        String confirmation2 = scanner.nextLine();
        
        if (!confirmation2.equalsIgnoreCase("YES")) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        // Proceed with deletion
        System.out.println("\nDeleting all stored data...");
        if (DataStorage.deleteAllData()) {
            System.out.println("All stored data has been permanently deleted!");
            System.out.println("The system will start fresh on next launch.");
            System.out.println("Current session data remains active until you exit.");
        } else {
            System.out.println("Error: Failed to delete data files.");
        }
    }
    
    /**
     * VOID METHOD: Manage system-wide configuration settings.
     * SUPER ADMIN ONLY - Allows configuration of default account parameters.
     */
    private void manageSystemConfiguration(Scanner scanner) {
        boolean inConfigMenu = true;
        while (inConfigMenu) {
            SystemConfiguration config = SystemConfiguration.getInstance();
            config.displaySettings();
            
            System.out.println("\n--- System Configuration Menu ---");
            System.out.println("1. Set Default Savings Interest Rate");
            System.out.println("2. Set Specific Savings Account Interest Rate");
            System.out.println("3. Set Default Checking Overdraft Limit");
            System.out.println("4. Set Specific Checking Account Overdraft Limit");
            System.out.println("5. Set Default Checking Overdraft Fee");
            System.out.println("6. Set Default Max Withdrawals per Month (Savings)");
            System.out.println("7. Set Specific Savings Account Max Withdrawals");
            System.out.println("8. View Configuration");
            System.out.println("9. Save and Exit Configuration Menu");
            System.out.print("Select option: ");
            
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> setDefaultInterestRate(scanner);
                case "2" -> setSavingsAccountInterestRate(scanner);
                case "3" -> setDefaultOverdraftLimit(scanner);
                case "4" -> setCheckingAccountOverdraftLimit(scanner);
                case "5" -> setDefaultOverdraftFee(scanner);
                case "6" -> setDefaultMaxWithdrawals(scanner);
                case "7" -> setSavingsAccountMaxWithdrawals(scanner);
                case "8" -> config.displaySettings();
                case "9" -> {
                    inConfigMenu = false;
                    DataStorage.saveAllData();
                    System.out.println("Configuration saved successfully!");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
    
    /**
     * VOID METHOD: Set the default interest rate for all new savings accounts.
     */
    private void setDefaultInterestRate(Scanner scanner) {
        System.out.print("Enter new default interest rate (as percentage, e.g., 3.5 for 3.5%): ");
        try {
            double rate = App.getDoubleInput(scanner) / 100.0; // Convert percentage to decimal
            if (rate >= 0) {
                SystemConfiguration.getInstance().setDefaultSavingsInterestRate(rate);
                System.out.println("Default savings interest rate set to: " + String.format("%.2f", rate * 100) + "%");
            } else {
                System.out.println("Interest rate must be non-negative.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
    
    /**
     * VOID METHOD: Set the interest rate for a specific savings account.
     */
    private void setSavingsAccountInterestRate(Scanner scanner) {
        System.out.print("Enter username of account holder: ");
        String username = scanner.nextLine();
        RegularUser user = findRegularUserByUsername(username);
        
        if (user != null) {
            Account[] accounts = user.getAccounts();
            if (App.hasNoAccounts(accounts)) {
                System.out.println("User has no accounts.");
                return;
            }
            
            // Find savings accounts
            int savingsIndex = -1;
            for (int i = 0; i < accounts.length; i++) {
                if (accounts[i] instanceof SavingsAccount) {
                    if (savingsIndex == -1) savingsIndex = i;
                    System.out.println((savingsIndex + 1) + ". Savings Account: " + accounts[i].getAccountNumber() + 
                                     " (Rate: " + String.format("%.2f", ((SavingsAccount) accounts[i]).getInterestRate() * 100) + "%)");
                }
            }
            
            if (savingsIndex == -1) {
                System.out.println("User has no savings accounts.");
                return;
            }
            
            System.out.print("Select account: ");
            try {
                int choice = (int) App.getDoubleInput(scanner) - 1;
                if (choice >= 0 && choice < accounts.length && accounts[choice] instanceof SavingsAccount) {
                    SavingsAccount savingsAccount = (SavingsAccount) accounts[choice];
                    System.out.print("Enter new interest rate (as percentage, e.g., 5 for 5%): ");
                    double rate = App.getDoubleInput(scanner) / 100.0;
                    if (rate >= 0) {
                        savingsAccount.setInterestRate(rate);
                        System.out.println("Interest rate updated to: " + String.format("%.2f", rate * 100) + "%");
                    } else {
                        System.out.println("Interest rate must be non-negative.");
                    }
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        } else {
            System.out.println("User not found.");
        }
    }
    
    /**
     * VOID METHOD: Set the default overdraft limit for all new checking accounts.
     */
    private void setDefaultOverdraftLimit(Scanner scanner) {
        System.out.print("Enter new default overdraft limit ($): ");
        try {
            double limit = App.getDoubleInput(scanner);
            if (limit >= 0) {
                SystemConfiguration.getInstance().setDefaultCheckingOverdraftLimit(limit);
                System.out.println("Default checking overdraft limit set to: $" + App.formatMoney(limit));
            } else {
                System.out.println("Overdraft limit must be non-negative.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
    
    /**
     * VOID METHOD: Set the overdraft limit for a specific checking account.
     */
    private void setCheckingAccountOverdraftLimit(Scanner scanner) {
        System.out.print("Enter username of account holder: ");
        String username = scanner.nextLine();
        RegularUser user = findRegularUserByUsername(username);
        
        if (user != null) {
            Account[] accounts = user.getAccounts();
            if (App.hasNoAccounts(accounts)) {
                System.out.println("User has no accounts.");
                return;
            }
            
            // Find checking accounts
            int checkingCount = 0;
            System.out.println("\n--- Checking Accounts ---");
            for (int i = 0; i < accounts.length; i++) {
                if (accounts[i] instanceof CheckingAccount) {
                    CheckingAccount checkingAccount = (CheckingAccount) accounts[i];
                    System.out.println((checkingCount + 1) + ". " + accounts[i].getAccountNumber() + 
                                     " (Overdraft Limit: $" + App.formatMoney(checkingAccount.getOverdraftLimit()) + ")");
                    checkingCount++;
                }
            }
            
            if (checkingCount == 0) {
                System.out.println("User has no checking accounts.");
                return;
            }
            
            System.out.print("Select account: ");
            try {
                int choice = (int) App.getDoubleInput(scanner) - 1;
                int accountIndex = 0;
                for (int i = 0; i < accounts.length; i++) {
                    if (accounts[i] instanceof CheckingAccount) {
                        if (accountIndex == choice) {
                            CheckingAccount checkingAccount = (CheckingAccount) accounts[i];
                            System.out.print("Enter new overdraft limit ($): ");
                            double limit = App.getDoubleInput(scanner);
                            if (limit >= 0) {
                                checkingAccount.setOverdraftLimit(limit);
                                System.out.println("Overdraft limit updated to: $" + App.formatMoney(limit));
                            } else {
                                System.out.println("Overdraft limit must be non-negative.");
                            }
                            return;
                        }
                        accountIndex++;
                    }
                }
                System.out.println("Invalid selection.");
            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        } else {
            System.out.println("User not found.");
        }
    }
    
    /**
     * VOID METHOD: Set the default overdraft fee for all new checking accounts.
     */
    private void setDefaultOverdraftFee(Scanner scanner) {
        System.out.print("Enter new default overdraft fee ($): ");
        try {
            double fee = App.getDoubleInput(scanner);
            if (fee >= 0) {
                SystemConfiguration.getInstance().setDefaultCheckingOverdraftFee(fee);
                System.out.println("Default overdraft fee set to: $" + App.formatMoney(fee));
            } else {
                System.out.println("Overdraft fee must be non-negative.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
    
    /**
     * VOID METHOD: Set the default maximum withdrawals per month for savings accounts.
     */
    private void setDefaultMaxWithdrawals(Scanner scanner) {
        System.out.print("Enter new default maximum withdrawals per month: ");
        try {
            int max = (int) App.getDoubleInput(scanner);
            if (max > 0) {
                SystemConfiguration.getInstance().setDefaultSavingsMaxWithdrawals(max);
                System.out.println("Default maximum withdrawals per month set to: " + max);
            } else {
                System.out.println("Maximum withdrawals must be greater than 0.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
    
    /**
     * VOID METHOD: Set the maximum withdrawals per month for a specific savings account.
     */
    private void setSavingsAccountMaxWithdrawals(Scanner scanner) {
        System.out.print("Enter username of account holder: ");
        String username = scanner.nextLine();
        RegularUser user = findRegularUserByUsername(username);
        
        if (user != null) {
            Account[] accounts = user.getAccounts();
            if (App.hasNoAccounts(accounts)) {
                System.out.println("User has no accounts.");
                return;
            }
            
            // Find savings accounts
            int savingsCount = 0;
            System.out.println("\n--- Savings Accounts ---");
            for (int i = 0; i < accounts.length; i++) {
                if (accounts[i] instanceof SavingsAccount) {
                    SavingsAccount savingsAccount = (SavingsAccount) accounts[i];
                    System.out.println((savingsCount + 1) + ". " + accounts[i].getAccountNumber() + 
                                     " (Max Withdrawals: " + savingsAccount.getMaxWithdrawalsPerMonth() + ")");
                    savingsCount++;
                }
            }
            
            if (savingsCount == 0) {
                System.out.println("User has no savings accounts.");
                return;
            }
            
            System.out.print("Select account: ");
            try {
                int choice = (int) App.getDoubleInput(scanner) - 1;
                int accountIndex = 0;
                for (Account account : accounts) {
                    if (account instanceof SavingsAccount) {
                        if (accountIndex == choice) {
                            SavingsAccount savingsAccount = (SavingsAccount) account;
                            System.out.print("Enter new maximum withdrawals per month: ");
                            int max = (int) App.getDoubleInput(scanner);
                            if (max > 0) {
                                savingsAccount.setMaxWithdrawalsPerMonth(max);
                                System.out.println("Maximum withdrawals updated to: " + max);
                            } else {
                                System.out.println("Maximum withdrawals must be greater than 0.");
                            }
                            return;
                        }
                        accountIndex++;
                    }
                }
                System.out.println("Invalid selection.");
            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        } else {
            System.out.println("User not found.");
        }
    }
}
