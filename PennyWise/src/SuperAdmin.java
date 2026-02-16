/**
 * Created on Sun Feb 16 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

import java.util.Scanner;

/**
 * SuperAdmin extends Admin with elevated permissions.
 * Demonstrates INHERITANCE and POLYMORPHISM via overridden menu and dashboard.
 */
public class SuperAdmin extends Admin {
    /**
     * Constructor to initialize a SuperAdmin.
     *
     * @param userId Unique user identifier
     * @param username User's login username
     * @param password User's login password
     * @param email User's email address
     */
    public SuperAdmin(String userId, String username, String password, String email) {
        super(userId, username, password, email);
    }

    /**
     * POLYMORPHISM IMPLEMENTATION: SuperAdmin displays the super admin dashboard.
     */
    @Override
    public void displayDashboard() {
        System.out.println("\n====== Super Admin Dashboard ======");
        System.out.println("Welcome, Super Admin " + getUsername() + "!");
        System.out.println("Options: System reports, configuration, and admin tools");
        System.out.println("===================================");
    }

    /**
     * VOID METHOD: Displays super admin capabilities and notable actions.
     */
    @Override
    public void displayAdminCapabilities() {
        System.out.println("\n--- Super Admin Capabilities ---");
        System.out.println("1. View all user accounts");
        System.out.println("2. Modify user information");
        System.out.println("3. Adjust account balances (for corrections)");
        System.out.println("4. Delete user accounts");
        System.out.println("5. Generate system-wide reports");
        System.out.println("6. Apply features to all savings accounts");
        System.out.println("7. Apply features to all checking accounts");
        System.out.println("8. Manage other administrators");
        System.out.println("9. Manage system configuration");
        System.out.println("10. Delete all stored data");
        System.out.println("-------------------------------");
    }

    /**
     * INSTANCE METHOD: Run the interactive super admin session.
     */
    @Override
    public void runAdminSession(Scanner scanner) {
        boolean inAdmin = true;
        while (inAdmin) {
            System.out.println("\n--- Super Admin Menu ---");
            System.out.println("1. View All Users");
            System.out.println("2. Modify User Information");
            System.out.println("3. Adjust Account Balance");
            System.out.println("4. Delete User Account");
            System.out.println("5. Generate System-wide Report");
            System.out.println("6. Apply Account Features (Interest) to All Savings Accounts");
            System.out.println("7. Apply Account Features (Overdraft Fees) to All Checking Accounts");
            System.out.println("8. Manage Administrators (Simulated)");
            System.out.println("9. Manage System Configuration");
            System.out.println("10. [DANGER] Delete All Stored Data");
            System.out.println("11. Logout");
            System.out.print("Select option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> displayAllUsers();
                case "2" -> modifyUserInformation(scanner);
                case "3" -> adjustAccountBalance(scanner);
                case "4" -> deleteUserAccount(scanner);
                case "5" -> generateSystemReport();
                case "6" -> applyAccountFeaturesToAllSavings();
                case "7" -> applyAccountFeaturesToAllChecking();
                case "8" -> manageAdmins(scanner);
                case "9" -> manageSystemConfiguration(scanner);
                case "10" -> deleteAllStoredData(scanner);
                case "11" -> {
                    inAdmin = false;
                    System.out.println("Admin logged out.");
                }
                default -> System.out.println("Invalid option.");
            }
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
        if (UserManager.getUserCount() == 0) {
            System.out.println("No users in system.");
        } else {
            for (int i = 0; i < UserManager.getUserCount(); i++) {
                User u = UserManager.getUser(i);
                if (u instanceof RegularUser user) {
                    Account[] accounts = user.getAccounts();
                    if (!AccountManager.hasNoAccounts(accounts)) {
                        int accountIndex = 1;
                        for (Account account : accounts) {
                            if (account != null) {
                                System.out.println((i + 1) + "." + accountIndex + " " + user.getUsername() + " - Account: " + account.getAccountType());
                                System.out.println("   Balance: $" + InputValidator.formatMoney(account.getBalance()));
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
        System.out.println("Total Users: " + UserManager.getUserCount());
        System.out.println("Savings Accounts: " + savingsAccountCount);
        System.out.println("Checking Accounts: " + checkingAccountCount);
        System.out.println("Total System Balance: $" + InputValidator.formatMoney(totalBalance));
        System.out.println("Total Transactions: " + totalTransactions);
        System.out.println("Default savings percentage: " + String.format("%.2f", SystemConfiguration.getInstance().getDefaultSavingsInterestRate() * 100) + "%");
        System.out.println("Average Balance per User: $" + InputValidator.formatMoney(UserManager.getUserCount() > 0 ? totalBalance / UserManager.getUserCount() : 0));
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
                System.out.println("1. admin (SuperAdmin) - admin@pennywise.com");
                System.out.println("Note: In full implementation, multiple admins would be stored in an array.");
            }
            case "2" -> {
                System.out.println("\n--- Create New Admin ---");
                System.out.print("Enter new admin username: ");
                String newAdminUsername = scanner.nextLine();
                System.out.print("Enter admin type (Admin/SuperAdmin): ");
                String newAdminType = scanner.nextLine();
                if (newAdminType.equalsIgnoreCase("Admin") || newAdminType.equalsIgnoreCase("SuperAdmin")) {
                    System.out.println("New " + newAdminType + " '" + newAdminUsername + "' created.");
                    System.out.println("Note: In full implementation, this would be stored in a database.");
                } else {
                    System.out.println("Invalid admin type. Must be Admin or SuperAdmin.");
                }
            }
            case "3" -> {
                System.out.println("\n--- Modify Admin ---");
                System.out.print("Enter admin username to modify: ");
                String adminUsername = scanner.nextLine();
                if (adminUsername.equals("admin")) {
                    System.out.println("1. Change Email");
                    System.out.println("2. Change Admin Type");
                    System.out.print("Select option (1-2): ");
                    String modifyChoice = scanner.nextLine();
                    switch (modifyChoice) {
                        case "1" -> {
                            System.out.print("Enter new email: ");
                            String newEmail = scanner.nextLine();
                            System.out.println("Admin email updated to: " + newEmail);
                        }
                        case "2" -> {
                            System.out.print("Enter new admin type (Admin/SuperAdmin): ");
                            String newType = scanner.nextLine();
                            if (newType.equalsIgnoreCase("Admin") || newType.equalsIgnoreCase("SuperAdmin")) {
                                System.out.println("Admin type changed to: " + newType);
                            } else {
                                System.out.println("Invalid admin type.");
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
        for (int i = 0; i < UserManager.getUserCount(); i++) {
            User u = UserManager.getUser(i);
            if (u instanceof RegularUser user) {
                Account[] accounts = user.getAccounts();
                if (!AccountManager.hasNoAccounts(accounts)) {
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
                            System.out.println("  Balance Before: $" + InputValidator.formatMoney(balanceBefore));
                            System.out.println("  Interest Applied: $" + InputValidator.formatMoney(interestEarned));
                            System.out.println("  Balance After: $" + InputValidator.formatMoney(balanceAfter));

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
        System.out.println("Total Interest Applied System-wide: $" + InputValidator.formatMoney(totalInterestApplied));
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
        for (int i = 0; i < UserManager.getUserCount(); i++) {
            User u = UserManager.getUser(i);
            if (u instanceof RegularUser user) {
                Account[] accounts = user.getAccounts();
                if (!AccountManager.hasNoAccounts(accounts)) {
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
                            System.out.println("  Balance Before: $" + InputValidator.formatMoney(balanceBefore));
                            System.out.println("  Overdraft Status: " + (checkingAccount.isInOverdraft() ? "IN OVERDRAFT" : "NORMAL"));
                            System.out.println("  Fees Applied: $" + InputValidator.formatMoney(feesApplied));
                            System.out.println("  Balance After: $" + InputValidator.formatMoney(balanceAfter));

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
        System.out.println("Total Overdraft Fees Applied System-wide: $" + InputValidator.formatMoney(totalOverdraftFeesApplied));
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
            double rate = InputValidator.getDoubleInput(scanner) / 100.0; // Convert percentage to decimal
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
            if (AccountManager.hasNoAccounts(accounts)) {
                System.out.println("User has no accounts.");
                return;
            }

            // Find savings accounts
            int savingsIndex = -1;
            for (int i = 0; i < accounts.length; i++) {
                if (accounts[i] instanceof SavingsAccount savingsAccount) {
                    if (savingsIndex == -1) savingsIndex = i;
                    System.out.println((savingsIndex + 1) + ". Savings Account: " + accounts[i].getAccountNumber() +
                                     " (Rate: " + String.format("%.2f", savingsAccount.getInterestRate() * 100) + "%)");
                }
            }

            if (savingsIndex == -1) {
                System.out.println("User has no savings accounts.");
                return;
            }

            System.out.print("Select account: ");
            try {
                int choice = (int) InputValidator.getValidatedDouble(scanner, "Invalid account selection. Please enter a valid number.") - 1;
                if (choice >= 0 && choice < accounts.length && accounts[choice] instanceof SavingsAccount) {
                    SavingsAccount savingsAccount = (SavingsAccount) accounts[choice];
                    System.out.print("Enter new interest rate (as percentage, e.g., 5 for 5%): ");
                    double rate = InputValidator.getValidatedDouble(scanner, "Invalid interest rate. Please enter a valid number.") / 100.0;
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
            double limit = InputValidator.getValidatedDouble(scanner, "Invalid overdraft limit. Please enter a valid number.");
            if (limit >= 0) {
                SystemConfiguration.getInstance().setDefaultCheckingOverdraftLimit(limit);
                System.out.println("Default checking overdraft limit set to: $" + InputValidator.formatMoney(limit));
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
            if (AccountManager.hasNoAccounts(accounts)) {
                System.out.println("User has no accounts.");
                return;
            }

            // Find checking accounts
            int checkingCount = 0;
            System.out.println("\n--- Checking Accounts ---");
            for (Account account : accounts) {
                if (account instanceof CheckingAccount checkingAccount) {
                    System.out.println((checkingCount + 1) + ". " + account.getAccountNumber() + " (Overdraft Limit: $" + InputValidator.formatMoney(checkingAccount.getOverdraftLimit()) + ")");
                    checkingCount++;
                }
            }

            if (checkingCount == 0) {
                System.out.println("User has no checking accounts.");
                return;
            }

            System.out.print("Select account: ");
            try {
                int choice = (int) InputValidator.getValidatedDouble(scanner, "Invalid account selection. Please enter a valid number.") - 1;
                int accountIndex = 0;
                for (Account account : accounts) {
                    if (account instanceof CheckingAccount checkingAccount) {
                        if (accountIndex == choice) {
                            System.out.print("Enter new overdraft limit ($): ");
                            double limit = InputValidator.getValidatedDouble(scanner, "Invalid overdraft limit. Please enter a valid number.");
                            if (limit >= 0) {
                                checkingAccount.setOverdraftLimit(limit);
                                System.out.println("Overdraft limit updated to: $" + InputValidator.formatMoney(limit));
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
            double fee = InputValidator.getValidatedDouble(scanner, "Invalid overdraft fee. Please enter a valid number.");
            if (fee >= 0) {
                SystemConfiguration.getInstance().setDefaultCheckingOverdraftFee(fee);
                System.out.println("Default overdraft fee set to: $" + InputValidator.formatMoney(fee));
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
            int max = (int) InputValidator.getValidatedDouble(scanner, "Invalid maximum withdrawals. Please enter a valid number.");
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
            if (AccountManager.hasNoAccounts(accounts)) {
                System.out.println("User has no accounts.");
                return;
            }

            // Find savings accounts
            int savingsCount = 0;
            System.out.println("\n--- Savings Accounts ---");
            for (Account account : accounts) {
                if (account instanceof SavingsAccount savingsAccount) {
                    System.out.println((savingsCount + 1) + ". " + account.getAccountNumber() +
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
                int choice = (int)  InputValidator.getValidatedDouble(scanner, "Invalid account selection. Please enter a valid number.") - 1;
                int accountIndex = 0;
                for (Account account : accounts) {
                    if (account instanceof SavingsAccount savingsAccount) {
                        if (accountIndex == choice) {
                            System.out.print("Enter new maximum withdrawals per month: ");
                            int max = (int) InputValidator.getValidatedDouble(scanner, "Invalid maximum withdrawals. Please enter a valid number.");
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
