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

    /**
     * Constructor to initialize an Admin.
     * 
     * @param userId Unique user identifier
     * @param username User's login username
     * @param password User's login password
     * @param email User's email address
     */
    public Admin(String userId, String username, String password, String email) {
        super(userId, username, password, email);
    }

    /**
     * POLYMORPHISM IMPLEMENTATION: Admin displays the admin dashboard.
     * This overrides the abstract displayDashboard() method from User.
     */
    @Override
    public void displayDashboard() {
        System.out.println("\n========== Admin Dashboard ==========");
        System.out.println("Welcome, Admin " + getUsername() + "!");
        System.out.println("Options: View all users, Modify accounts");
        System.out.println("=====================================");
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

        // For demo, create a default super admin
        Admin admin = new SuperAdmin("admin001", "admin", "admin123", "admin@pennywise.com");

        if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
            admin.displayDashboard();
            admin.runAdminSession(scanner);
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    /**
     * INSTANCE METHOD: Run the interactive admin session (basic admin).
     */
    public void runAdminSession(Scanner scanner) {
        boolean inAdmin = true;
        while (inAdmin) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View All Users");
            System.out.println("2. Modify User Information");
            System.out.println("3. Adjust Account Balance");
            System.out.println("4. Delete User Account");
            System.out.println("5. Logout");
            System.out.print("Select option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> displayAllUsers();
                case "2" -> modifyUserInformation(scanner);
                case "3" -> adjustAccountBalance(scanner);
                case "4" -> deleteUserAccount(scanner);
                case "5" -> {
                    inAdmin = false;
                    System.out.println("Admin logged out.");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ---------------- Admin helper methods (moved from App) -----------------

    protected void displayAllUsers() {
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

    protected RegularUser findRegularUserByUsername(String username) {
        // Prefer App-provided accessor for RegularUser lookup
        return App.getRegularUserByUsername(username);
    }

    protected void modifyUserInformation(Scanner scanner) {
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

    protected void adjustAccountBalance(Scanner scanner) {
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

    protected void deleteUserAccount(Scanner scanner) {
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

}
