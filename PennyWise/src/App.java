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
 * ========== Regular User Dashboard ==========
 * Welcome, john_doe!
 * Account Type: SAVINGS
 * Current Balance: $1000.00
 * ===========================================
 * 
 * ... (Menu continues with deposit, withdraw, view transactions, etc.)
 * =========================================
 */

/**
 * App class - DRIVER CLASS for PennyWise Financial Management System.
 * 
 * This class demonstrates:
 * - SELECTIONS (if-else, switch statements)
 * - LOOPS (while loop for menu system)
 * - METHODS (value returning and void methods)
 * - USER INTERACTION (Scanner for System.in)
 * - INHERITANCE & POLYMORPHISM (User subclasses, Account subclasses)
 * - ENCAPSULATION (private fields with getters/setters)
 * - ARRAYS (storing users and accounts)
 */
public class App {
    
    // ARRAYS: Store users and accounts (encapsulated - use accessors)
    private static final User[] users = new User[100];
    private static int userCount = 0;

    /**
     * VALUE RETURNING METHOD: Main method - entry point of the application.
     */
    public static void main(String[] args) {
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

        // Select account type
        System.out.println("\nSelect Account Type:");
        System.out.println("1. Savings Account (3% interest)");
        System.out.println("2. Checking Account (with overdraft)");
        System.out.print("Select account type (1-2): ");
        String accountChoice = scanner.nextLine();
        
        // SELECTION: switch expression for account type selection
        Account account = switch (accountChoice) {
            case "1" -> {
                System.out.println("Savings Account created.");
                yield new SavingsAccount("SA-" + userId, 1000.0, 0.03);
            }
            case "2" -> {
                System.out.println("Checking Account created.");
                yield new CheckingAccount("CA-" + userId, 1000.0, 500.0, 35.0);
            }
            default -> {
                System.out.println("Invalid option. Savings Account created by default.");
                yield new SavingsAccount("SA-" + userId, 1000.0, 0.03);
            }
        };

        // ARRAYS: Add user via encapsulated accessor
        RegularUser newUser = new RegularUser(userId, username, password, email, account);
        if (addRegularUser(newUser)) {
            System.out.println("Registration successful! Welcome, " + username + "!");
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
     */
    private static void userAccountMenu(Scanner scanner, RegularUser user) {
        boolean inAccount = true;
        
        // LOOPS: while loop for account menu
        while (inAccount) {
            Account account = user.getAccount();
            
            System.out.println("\n--- Account Menu ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. View Transaction History");
            System.out.println("5. View Profile");
            // Display account-specific options based on account type
            if (account instanceof CheckingAccount) {
                System.out.println("6. View Checking Account Details");
                System.out.println("7. View Overdraft History");
                System.out.println("8. Logout");
            } else {
                System.out.println("6. Logout");
            }
            System.out.print("Select option (1-" + (account instanceof CheckingAccount ? "8" : "6") + "): ");

            String choice = scanner.nextLine();
            
            // SELECTION: switch expression for account menu options
            switch (choice) {
                case "1" -> System.out.println("Current Balance: $" + String.format("%.2f", user.getBalance()));
                case "2" -> {
                    System.out.print("Enter deposit amount: $");
                    try {
                        double amount = Double.parseDouble(scanner.nextLine());
                        if (account.deposit(amount)) {
                            System.out.println("Deposit successful! New balance: $" + String.format("%.2f", account.getBalance()));
                        } else {
                            System.out.println("Invalid deposit amount.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
                case "3" -> {
                    System.out.print("Enter withdrawal amount: $");
                    try {
                        double amount = Double.parseDouble(scanner.nextLine());
                        if (account.withdraw(amount)) {
                            System.out.println("Withdrawal successful! New balance: $" + String.format("%.2f", account.getBalance()));
                        } else {
                            System.out.println("Insufficient funds, invalid amount or other invalid input.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
                case "4" -> user.viewTransactionHistory();
                case "5" -> user.displayProfile();
                case "6" -> {
                    if (account instanceof CheckingAccount checkingAccount) {
                        checkingAccount.displayCheckingInfo();
                    } else {
                        // Default logout for non-checking accounts
                        inAccount = false;
                        System.out.println("Logged out successfully.");
                    }
                }
                case "7" -> {
                    if (account instanceof CheckingAccount checkingAccount) {
                        checkingAccount.displayOverdraftHistory();
                    } else {
                        System.out.println("Invalid option.");
                    }
                }
                case "8" -> {
                    if (account instanceof CheckingAccount) {
                        inAccount = false;
                        System.out.println("Logged out successfully.");
                    } else {
                        System.out.println("Invalid option.");
                    }
                }
                default -> System.out.println("Invalid option.");
            }
        }
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

    // Admin methods moved into Admin.java; App no longer contains admin helper methods.
}