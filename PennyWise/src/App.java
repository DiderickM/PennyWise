import java.util.Scanner;

/**
 * Created on Mon Feb 09 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

/**
 * SAMPLE RUN:
 * =========================================
 * Welcome to PennyWise Financial Manager!
 * =========================================
 * 1. Regular User Mode
 * 2. Admin Mode
 * 3. Exit
 * 
 * Please select an option (1-3): 1
 * 
 * --- Regular User Mode ---
 * 1. Register New Account
 * 2. Login
 * Select option (1-2): 1
 * 
 * --- Registration ---
 * Enter User ID: user001
 * Enter Username: john_doe
 * Enter Password: secure123
 * Enter Email: john@example.com
 * 
 * Select Account Type:
 * 1. Savings Account (3% interest)
 * 2. Checking Account (with overdraft)
 * 
 * Select account type (1-2): 1
 * 
 * ... (Menu continues with more options: another account, deposit, withdraw, view transactions, etc.)
 * =========================================
 */

/**
 * SAMPLE RUN: Admin Mode (NOTE: Admin login credentials are hardcoded for demonstration, and no users are pre-populated in this sample run)
 * ========================================
 * Welcome to PennyWise Financial Manager!
 * ========================================    
 * 1. Regular User Mode
 * 2. Admin Mode  
 * 3. Exit
 * 
 * Please select an option (1-3): 2
 * 
 * --- Admin Mode ---
 * Enter Admin Username: admin
 * Enter Admin Password: admin123
 * 
 * ========== Admin Dashboard ==========
 *  Welcome, Admin admin!
 *  Admin Level: SUPER
 *  Options: View all users, Modify accounts, Generate reports
 * =====================================
 *
 * --- Admin Menu ---
 * ... (Menu continues with more options: View all users, Modify accounts, Generate reports, etc.)
*/

public class App {
    
    // ARRAYS: Store users and accounts (encapsulated - use accessors)
    private static final User[] users = new User[100];
    private static int userCount = 0;

    /**
     * VALUE RETURNING METHOD: Main method - entry point of the application.
     */
    public static void main(String[] args) {
        // Load saved data on startup
        if (DataStorage.dataExists()) {
            System.out.println("Loading saved data...");
            }
        }
        
        try (Scanner scanner = new Scanner(System.in)) {
            // Display welcome message
            displayWelcome();
            
            // LOOPS: while loop for main menu system
            boolean running = true;
            while (running) {
                displayMainMenu();
                System.out.print("Please select an option (1-3): ");
                
                // SELECTION: if-else for menu options
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    
                    switch (choice) {
                        case 1 -> regularUserMode(scanner);
                        case 2 -> Admin.launchAdmin(scanner);// All admin logic moved to Admin class
                        case 3 -> {
                            // Save data before exiting
                            System.out.println("Saving data...");
                            if (DataStorage.saveAllData()) {
                                System.out.println("Data saved successfully!");
                            } else {
                                System.out.println("Warning: Failed to save some data.");
                            }
                            running = false;
                            System.out.println("\nThank you for using PennyWise. Goodbye!");
                        }
                        default -> System.out.println("Invalid option. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
        }
    }

    /**
     * VOID METHOD: Displays the welcome message.
     */
    private static void displayWelcome() {
        System.out.println("\n=========================================");
        System.out.println("  Welcome to PennyWise Financial Manager!");
        System.out.println("=========================================");
    }

    /**
     * VOID METHOD: Displays the main menu.
     */
    private static void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Regular User Mode");
        System.out.println("2. Admin Mode");
        System.out.println("3. Exit");
    }

    /**
     * VOID METHOD: Handles regular user operations.
     * Demonstrates POLYMORPHISM - RegularUser displayDashboard() method.
     */
    private static void regularUserMode(Scanner scanner) {
        System.out.println("\n--- Regular User Mode ---");
        System.out.println("1. Register New Account");
        System.out.println("2. Login");
        System.out.print("Select option (1-2): ");
        
        String choice = scanner.nextLine();
        
        // SELECTION: switch for user mode options
        switch (choice) {
            case "1" -> registerRegularUser(scanner);
            case "2" -> loginRegularUser(scanner);
            default -> System.out.println("Invalid option.");
        }
    }

    /**
     * VOID METHOD: Registers a new regular user.
     * Demonstrates ENCAPSULATION and ARRAYS usage.
     */
    private static void registerRegularUser(Scanner scanner) {
        System.out.println("\n--- Registration ---");
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        // Create user first (without accounts)
        RegularUser newUser = new RegularUser(userId, username, password, email);
        
        // Allow adding one or more accounts
        boolean addingAccounts = true;
        int accountCount = 0;
        while (addingAccounts) {
            // Select account type
            System.out.println();
            displayAccountTypeMenu();
            System.out.print("Select account type (1-2): ");
            String accountChoice = scanner.nextLine();
            
            // Create account using helper method
            Account account = createAccount(userId, accountChoice, accountCount);
            newUser.addAccount(account);
            accountCount++;
            
            // Ask if user wants to add another account
            System.out.print("Add another account? (yes/no): ");
            String response = scanner.nextLine().toLowerCase();
            if (!response.equals("yes") && !response.equals("y")) {
                addingAccounts = false;
            }
        }
        
        // ARRAYS: Add user via encapsulated accessor
        if (addRegularUser(newUser)) {
            System.out.println("Registration successful! Welcome, " + username + "!");
            System.out.println("You have " + accountCount + " account(s).");
            // Save data after registration
            DataStorage.saveAllData();
        } else {
            System.out.println("Maximum user limit reached.");
        }
    }

    /**
     * VOID METHOD: Handles regular user login and operations.
     * Demonstrates POLYMORPHISM through user dashboard.
     */
    private static void loginRegularUser(Scanner scanner) {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        // Find user in array
        RegularUser user = findRegularUser(username, password);

        if (user != null) {
            // POLYMORPHISM: Call displayDashboard() - overridden in RegularUser
            user.displayDashboard();
            userAccountMenu(scanner, user);
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    /**
     * VOID METHOD: User account menu for transactions and operations.
     * Demonstrates LOOPS (while loop) for menu navigation.
     * Supports multiple accounts per user.
     */
    private static void userAccountMenu(Scanner scanner, RegularUser user) {
        boolean inAccount = true;
        
        // LOOPS: while loop for account menu
        while (inAccount) {
            // Display all accounts and select one
            Account[] accounts = user.getAccounts();
            
            if (hasNoAccounts(accounts)) {
                System.out.println("\nYou have no accounts. Please add an account first.");
                break;
            }
            
            // If only one account, use it directly. Otherwise, let user choose.
            Account selectedAccount;
            int selectedAccountIndex = 0;
            
            if (accounts.length > 1) {
                System.out.println("\n--- Select Account ---");
                for (int i = 0; i < accounts.length; i++) {
                    if (accounts[i] != null) {
                        System.out.println((i+1) + ". [" + accounts[i].getAccountNumber() + "] " + 
                                         accounts[i].getAccountType() + 
                                         " - Balance: $" + formatMoney(accounts[i].getBalance()));
                    }
                }
                System.out.println((accounts.length+1) + ". Add New Account");
                System.out.println((accounts.length+2) + ". View Profile");
                System.out.println((accounts.length+3) + ". Logout");
                System.out.print("Select account (1-" + (accounts.length+3) + "): ");
                
                try {
                    int accountChoice = Integer.parseInt(scanner.nextLine());
                    if (accountChoice > 0 && accountChoice <= accounts.length) {
                        selectedAccountIndex = accountChoice - 1;
                        selectedAccount = accounts[selectedAccountIndex];
                    } else if (accountChoice == accounts.length + 1) {
                        // Add new account
                        addNewAccountToUser(scanner, user);
                        continue;
                    } else if (accountChoice == accounts.length + 2) {
                        // View profile
                        user.displayProfile();
                        continue;
                    } else if (accountChoice == accounts.length + 3) {
                        // Logout
                        System.out.println("Logged out successfully.");
                        break;
                    } else {
                        System.out.println("Invalid choice.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                    continue;
                }
            } else {
                selectedAccount = accounts[0];
            }
            
            if (selectedAccount == null) {
                System.out.println("Invalid account selection.");
                continue;
            }
            
            // Account operations menu
            System.out.println("\n--- Account Menu (" + selectedAccount.getAccountType() + ") ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. View Transaction History");
            System.out.println("5. Transfer Money");
            // Display account-specific options based on account type
            if (selectedAccount instanceof CheckingAccount) {
                System.out.println("6. View Checking Account Details");
                System.out.println("7. View Overdraft History");
                System.out.println("8. " + (accounts.length > 1 ? "Back to Account Selection" : "Logout"));
            } else if (selectedAccount instanceof SavingsAccount) {
                System.out.println("6. View Savings Account Details");
                System.out.println("7. " + (accounts.length > 1 ? "Back to Account Selection" : "Logout"));
            } else {
                System.out.println("6. " + (accounts.length > 1 ? "Back to Account Selection" : "Logout"));
            }
            int maxOption = (selectedAccount instanceof CheckingAccount) ? 9 : ((selectedAccount instanceof SavingsAccount) ? 8 : 7);
            System.out.print("Select option (1-" + maxOption + "): ");

            String choice = scanner.nextLine();
            
            // SELECTION: switch expression for account menu options
            switch (choice) {
                case "1" -> System.out.println("Current Balance: $" + formatMoney(selectedAccount.getBalance()));
                case "2" -> {
                    System.out.print("Enter deposit amount: $");
                    double amount = getDoubleInput(scanner);
                    if (amount > 0 && selectedAccount.deposit(amount)) {
                        System.out.println("Deposit successful! New balance: $" + formatMoney(selectedAccount.getBalance()));
                        DataStorage.saveAllData(); // Save after transaction
                    } else if (amount > 0) {
                        System.out.println("Invalid deposit amount.");
                    } else {
                        System.out.println("Invalid input.");
                    }
                }
                case "3" -> {
                    System.out.print("Enter withdrawal amount: $");
                    double amount = getDoubleInput(scanner);
                    if (amount > 0 && selectedAccount.withdraw(amount)) {
                        System.out.println("Withdrawal successful! New balance: $" + formatMoney(selectedAccount.getBalance()));
                        DataStorage.saveAllData(); // Save after transaction
                    } else if (amount > 0) {
                        System.out.println("Insufficient funds, invalid amount or other invalid input.");
                    } else {
                        System.out.println("Invalid input.");
                    }
                }
                case "4" -> user.viewTransactionHistory(selectedAccountIndex);
                case "5" -> {
                    System.out.println("\n--- Transfer Money ---");
                    if (selectedAccount instanceof CheckingAccount) {
                        System.out.println("1. Transfer between your accounts");
                        System.out.println("2. Transfer to another user");
                        System.out.print("Select option (1-2): ");
                    } else {
                        System.out.println("1. Transfer between your accounts");
                        System.out.print("Select option (1): ");
                    }
                    String transferChoice = scanner.nextLine();

                    switch (transferChoice) {
                        case "1" -> {
                            if (accounts.length < 2) {
                                System.out.println("You need at least 2 accounts to transfer money.");
                                break;
                            }

                            System.out.println("Transferring FROM: " + selectedAccount.getAccountType() +
                                             " (Account: " + selectedAccount.getAccountNumber() +
                                             ", Balance: $" + formatMoney(selectedAccount.getBalance()) + ")");

                            System.out.println("\nAvailable accounts to transfer TO:");
                            for (int i = 0; i < accounts.length; i++) {
                                if (i != selectedAccountIndex && accounts[i] != null) {
                                    System.out.println((i + 1) + ". " + accounts[i].getAccountType() +
                                                     " (Account: " + accounts[i].getAccountNumber() + ")" +
                                                     " - Balance: $" + formatMoney(accounts[i].getBalance()));
                                }
                            }

                            System.out.print("Select target account (1-" + accounts.length + "): ");
                            int targetIndex = (int) getDoubleInput(scanner) - 1;
                            if (targetIndex >= 0 && targetIndex < accounts.length &&
                                targetIndex != selectedAccountIndex && accounts[targetIndex] != null) {
                                System.out.print("Enter transfer amount: $");
                                double amount = getDoubleInput(scanner);

                                if (amount > 0 && user.transferBetweenAccounts(selectedAccountIndex, targetIndex, amount)) {
                                    System.out.println("Transfer successful!");
                                    System.out.println("Source account balance: $" + formatMoney(selectedAccount.getBalance()));
                                    System.out.println("Target account balance: $" + formatMoney(accounts[targetIndex].getBalance()));
                                    DataStorage.saveAllData(); // Save after transfer
                                } else if (amount <= 0) {
                                    System.out.println("Invalid input.");
                                }
                            } else {
                                System.out.println("Invalid target account.");
                            }
                        }
                        case "2" -> {
                            if (!(selectedAccount instanceof CheckingAccount)) {
                                System.out.println("Transfers to other users are only allowed from a checking account.");
                                break;
                            }

                            RegularUser targetUser = selectUserByAccountNumber(scanner, user);
                            if (targetUser == null) break;

                            int targetIndex = selectTargetAccountIndex(scanner, targetUser);
                            if (targetIndex < 0) break;

                            System.out.print("Enter transfer amount: $");
                            double amount = getDoubleInput(scanner);

                            if (amount > 0 && user.transferToOtherUser(selectedAccountIndex, targetUser, targetIndex, amount)) {
                                System.out.println("Transfer successful!");
                                System.out.println("Source account balance: $" + formatMoney(selectedAccount.getBalance()));
                                Account targetAccount = targetUser.getAccountByIndex(targetIndex);
                                if (targetAccount != null) {
                                    System.out.println("Target account balance: $" + formatMoney(targetAccount.getBalance()));
                                }
                                DataStorage.saveAllData(); // Save after transfer
                            } else if (amount <= 0) {
                                System.out.println("Invalid input.");
                            }
                        }
                        default -> System.out.println("Invalid option.");
                    }
                }
                case "6" -> {
                    switch (selectedAccount) {
                        case CheckingAccount checkingAccount -> checkingAccount.displayCheckingInfo();
                        case SavingsAccount savingsAccount -> savingsAccount.displaySavingsInfo();
                        default -> {
                            // Back or logout
                            if (accounts.length == 1) {
                                inAccount = false;
                                System.out.println("Logged out successfully.");
                            }
                        }
                    }
                }
                case "7" -> {
                    if (selectedAccount instanceof CheckingAccount checkingAccount) {
                        checkingAccount.displayOverdraftHistory();
                    } else if (selectedAccount instanceof SavingsAccount) {
                        // Back or logout for Savings Account
                        if (accounts.length == 1) {
                            inAccount = false;
                            System.out.println("Logged out successfully.");
                        }
                    } else {
                        System.out.println("Invalid option.");
                    }
                }
                case "8" -> {
                    if (selectedAccount instanceof CheckingAccount) {
                        if (accounts.length == 1) {
                            inAccount = false;
                            System.out.println("Logged out successfully.");
                        }
                    } else {
                        System.out.println("Invalid option.");
                    }
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    /**
     * VOID METHOD: Adds a new account to an existing user.
     */
    private static void addNewAccountToUser(Scanner scanner, RegularUser user) {
        System.out.println("\n--- Create New Account ---");
        displayAccountTypeMenu();
        System.out.print("Select account type (1-2): ");
        
        String accountChoice = scanner.nextLine();
        String userId = user.getUserId();
        int accountCount = user.getAccounts() != null ? user.getAccounts().length : 0;
        
        Account newAccount = createAccount(userId, accountChoice, accountCount);
        user.addAccount(newAccount);
        System.out.println("Account added successfully!");
        DataStorage.saveAllData(); // Save after adding account
    }

    /**
     * VALUE RETURNING METHOD: Finds a regular user by username and password.
     * Used for regular user login.
     */
    private static RegularUser findRegularUser(String username, String password) {
        for (int i = 0; i < userCount; i++) {
            if (users[i] instanceof RegularUser user) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return user;
                }
            }
        }
        return null;
    }

    // ---------------- Controlled accessors for user storage ----------------
    public static int getUserCount() {
        return userCount;
    }

    public static User getUser(int index) {
        if (index >= 0 && index < userCount) return users[index];
        return null;
    }
    /**
     * Adds a RegularUser to the system. Kept specific to preserve encapsulation.
     */
    public static boolean addRegularUser(RegularUser u) {
        if (u == null) return false;
        if (userCount < users.length) {
            users[userCount] = u;
            userCount++;
            return true;
        }
        return false;
    }

    /**
     * Removes a regular user by username. Returns true if removed.
     */
    public static boolean removeRegularUserByUsername(String username) {
        if (username == null) return false;
        int userIndex = -1;
        for (int i = 0; i < userCount; i++) {
            if (users[i] instanceof RegularUser user) {
                if (user.getUsername().equals(username)) {
                    userIndex = i;
                    break;
                }
            }
        }
        if (userIndex == -1) return false;
        for (int i = userIndex; i < userCount - 1; i++) {
            users[i] = users[i + 1];
        }
        users[userCount - 1] = null;
        userCount--;
        DataStorage.saveAllData(); // Save after removing user
        return true;
    }

    /**
     * Returns a RegularUser by username or null if not found.
     */
    public static RegularUser getRegularUserByUsername(String username) {
        if (username == null) return null;
        for (int i = 0; i < userCount; i++) {
            if (users[i] instanceof RegularUser user) {
                if (user.getUsername().equals(username)) return user;
            }
        }
        return null;
    }

    // ============== UTILITY HELPER METHODS ==============

    /**
     * Formats a currency value to 2 decimal places.
     * Replaces redundant String.format("%.2f", value) calls throughout codebase.
     */
    public static String formatMoney(double amount) {
        return String.format("%.2f", amount);
    }

    /**
     * Safely parses a double from user input with error handling.
     * Returns -1 if input is invalid.
     */
    public static double getDoubleInput(Scanner scanner) {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Creates a new account based on user selection.
     * Eliminates duplicate account creation logic.
     */
    public static Account createAccount(String userId, String accountChoice, int accountCount) {
        return switch (accountChoice) {
            case "1" -> {
                System.out.println("Savings Account created.");
                yield new SavingsAccount("SA-" + userId + "-" + (accountCount + 1), 1000.0, 0.03);
            }
            case "2" -> {
                System.out.println("Checking Account created.");
                yield new CheckingAccount("CA-" + userId + "-" + (accountCount + 1), 1000.0, 500.0, 35.0);
            }
            default -> {
                System.out.println("Invalid option. Savings Account created by default.");
                yield new SavingsAccount("SA-" + userId + "-" + (accountCount + 1), 1000.0, 0.03);
            }
        };
    }

    /**
     * Displays the account type selection menu.
     */
    public static void displayAccountTypeMenu() {
        System.out.println("Select Account Type:");
        System.out.println("1. Savings Account (3% interest)");
        System.out.println("2. Checking Account (with overdraft)");
    }

    /**
     * Checks if accounts array is null or empty.
     */
    public static boolean hasNoAccounts(Account[] accounts) {
        return accounts == null || accounts.length == 0;
    }

    private static RegularUser selectUserByAccountNumber(Scanner scanner, RegularUser currentUser) {
        System.out.println("\n--- Transfer to Another User ---");
        System.out.print("Enter recipient account number: ");
        String accountNumber = scanner.nextLine().trim();

        if (accountNumber.isEmpty()) {
            System.out.println("Account number cannot be empty.");
            return null;
        }

        // Search for the account number across all users
        for (int i = 0; i < userCount; i++) {
            if (users[i] instanceof RegularUser user && user != currentUser) {
                Account[] accounts = user.getAccounts();
                if (!hasNoAccounts(accounts)) {
                    for (Account account : accounts) {
                        if (account != null && account.getAccountNumber().equals(accountNumber)) {
                            System.out.println("Recipient found: " + user.getUsername() + " (" + account.getAccountType() + ")");
                            return user;
                        }
                    }
                }
            }
        }

        System.out.println("Account number not found or belongs to current user.");
        return null;
    }

    private static int selectTargetAccountIndex(Scanner scanner, RegularUser targetUser) {
        Account[] targetAccounts = targetUser.getAccounts();
        if (hasNoAccounts(targetAccounts)) {
            System.out.println("Target user has no accounts.");
            return -1;
        }

        if (targetAccounts.length > 1) {
            System.out.println("\n--- Select Target Account ---");
            for (int i = 0; i < targetAccounts.length; i++) {
                Account account = targetAccounts[i];
                System.out.println((i + 1) + ". [" + account.getAccountNumber() + "] " + 
                                 account.getAccountType() +
                                 " - Balance: $" + formatMoney(account.getBalance()));
            }
            System.out.print("Select target account (1-" + targetAccounts.length + "): ");
            int index = (int) getDoubleInput(scanner) - 1;
            if (index >= 0 && index < targetAccounts.length) {
                return index;
            }
            System.out.println("Invalid target account.");
            return -1;
        }

        return 0;
    }
}