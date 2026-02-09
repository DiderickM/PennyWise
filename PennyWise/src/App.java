import java.util.Scanner;

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
    
    // ARRAYS: Store users and accounts
    private static User[] users = new User[100];
    private static int userCount = 0;

    /**
     * VALUE RETURNING METHOD: Main method - entry point of the application.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
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
                    case 1:
                        regularUserMode(scanner);
                        break;
                    case 2:
                        adminMode(scanner);
                        break;
                    case 3:
                        running = false;
                        System.out.println("\nThank you for using PennyWise. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        
        scanner.close();
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
        
        // SELECTION: if-else for user mode options
        if (choice.equals("1")) {
            registerRegularUser(scanner);
        } else if (choice.equals("2")) {
            loginRegularUser(scanner);
        } else {
            System.out.println("Invalid option.");
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

        Account account = null;
        
        // SELECTION: switch statement for account type selection
        switch (accountChoice) {
            case "1":
                // Create Savings Account
                account = new SavingsAccount("SA-" + userId, 1000.0, 0.03);
                System.out.println("Savings Account created.");
                break;
            case "2":
                // Create Checking Account
                account = new CheckingAccount("CA-" + userId, 1000.0, 500.0, 35.0);
                System.out.println("Checking Account created.");
                break;
            default:
                System.out.println("Invalid option. Savings Account created by default.");
                account = new SavingsAccount("SA-" + userId, 1000.0, 0.03);
        }

        // ARRAYS: Add user to the users array
        if (userCount < users.length) {
            users[userCount] = new RegularUser(userId, username, password, email, account);
            userCount++;
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
            System.out.println("\n--- Account Menu ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. View Transaction History");
            System.out.println("5. View Profile");
            System.out.println("6. Logout");
            System.out.print("Select option (1-6): ");

            String choice = scanner.nextLine();
            
            Account account = user.getAccount();
            
            // SELECTION: switch statement for account menu options
            switch (choice) {
                case "1":
                    // VALUE RETURNING METHOD: getBalance()
                    System.out.println("Current Balance: $" + String.format("%.2f", user.getBalance()));
                    break;
                case "2":
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
                    break;
                case "3":
                    System.out.print("Enter withdrawal amount: $");
                    try {
                        double amount = Double.parseDouble(scanner.nextLine());
                        if (account.withdraw(amount)) {
                            System.out.println("Withdrawal successful! New balance: $" + String.format("%.2f", account.getBalance()));
                        } else {
                            System.out.println("Insufficient funds or invalid amount.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                    break;
                case "4":
                    // VOID METHOD: displayTransactionHistory()
                    user.viewTransactionHistory();
                    break;
                case "5":
                    // VOID METHOD: displayProfile()
                    user.displayProfile();
                    break;
                case "6":
                    inAccount = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * VOID METHOD: Handles admin operations.
     * Demonstrates POLYMORPHISM - Admin displayDashboard() method.
     */
    private static void adminMode(Scanner scanner) {
        System.out.println("\n--- Admin Mode ---");
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();

        // For demo, create a default admin
        Admin admin = new Admin("admin001", "admin", "admin123", "admin@pennywise.com", "SUPER");

        // SELECTION: if-else for admin login verification
        if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
            // POLYMORPHISM: Call displayDashboard() - overridden in Admin
            admin.displayDashboard();
            adminMenu(scanner, admin);
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    /**
     * VOID METHOD: Admin menu for system operations.
     * Demonstrates LOOPS (while loop) for admin menu.
     */
    private static void adminMenu(Scanner scanner, Admin admin) {
        boolean inAdmin = true;
        
        // LOOPS: while loop for admin menu
        while (inAdmin) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View All Users");
            System.out.println("2. Modify User Information");
            System.out.println("3. Adjust Account Balance");
            System.out.println("4. Delete User Account");
            if (admin.hasSuperPrivileges()) {
                System.out.println("5. Generate System-wide Report");
                System.out.println("6. Manage Administrators");
            }
            System.out.println("7. Logout");
            System.out.print("Select option: ");

            String choice = scanner.nextLine();
            
            // SELECTION: switch statement for admin menu
            switch (choice) {
                case "1":
                    // VOID METHOD: Display all users (ARRAYS demonstration)
                    displayAllUsers();
                    break;
                case "2":
                    // VOID METHOD: Modify user information
                    modifyUserInformation(scanner);
                    break;
                case "3":
                    // VOID METHOD: Adjust account balance
                    adjustAccountBalance(scanner);
                    break;
                case "4":
                    // VOID METHOD: Delete user account
                    deleteUserAccount(scanner);
                    break;
                case "5":
                    if (admin.hasSuperPrivileges()) {
                        // VOID METHOD: Generate system report
                        generateSystemReport();
                    } else {
                        System.out.println("You don't have permission for this action.");
                    }
                    break;
                case "6":
                    if (admin.hasSuperPrivileges()) {
                        // VOID METHOD: Manage admins
                        manageAdmins(scanner);
                    } else {
                        System.out.println("You don't have permission for this action.");
                    }
                    break;
                case "7":
                    inAdmin = false;
                    System.out.println("Admin logged out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * VOID METHOD: Displays all registered users.
     * Demonstrates ARRAYS and LOOPS.
     */
    private static void displayAllUsers() {
        System.out.println("\n========== All Users ==========");
        
        // LOOPS: for loop through users array
        if (userCount == 0) {
            System.out.println("No users registered.");
        } else {
            for (int i = 0; i < userCount; i++) {
                if (users[i] != null) {
                    System.out.println((i + 1) + ". " + users[i].getUsername() + " (ID: " + users[i].getUserId() + ")");
                }
            }
        }
        System.out.println("==============================");
    }

    /**
     * VOID METHOD: Displays details of a specific user.
     */
    private static void displayUserDetails(String username) {
        RegularUser user = findRegularUserByUsername(username);
        
        if (user != null) {
            user.displayProfile();
            if (user.getAccount() != null) {
                user.getAccount().displayAccountInfo();
            }
        } else {
            System.out.println("User not found.");
        }
    }

    /**
     * VALUE RETURNING METHOD: Finds a regular user by username and password.
     * Demonstrates ARRAYS and LOOPS.
     * 
     * @param username Username to find
     * @param password Password to verify
     * @return RegularUser if found, null otherwise
     */
    private static RegularUser findRegularUser(String username, String password) {
        // LOOPS: for loop through users array
        for (int i = 0; i < userCount; i++) {
            if (users[i] instanceof RegularUser) {
                RegularUser user = (RegularUser) users[i];
                // SELECTION: if to verify credentials
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * VALUE RETURNING METHOD: Finds a user by username only.
     * 
     * @param username Username to find
     * @return RegularUser if found, null otherwise
     */
    private static RegularUser findRegularUserByUsername(String username) {
        // LOOPS: for loop through users array
        for (int i = 0; i < userCount; i++) {
            if (users[i] instanceof RegularUser) {
                RegularUser user = (RegularUser) users[i];
                // SELECTION: if to verify username
                if (user.getUsername().equals(username)) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * VOID METHOD: Modifies user information (username, email, password).
     * Demonstrates ENCAPSULATION through setter methods.
     */
    private static void modifyUserInformation(Scanner scanner) {
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

            // SELECTION: switch statement for modification options
            switch (choice) {
                case "1":
                    System.out.print("Enter new username: ");
                    String newUsername = scanner.nextLine();
                    user.setUsername(newUsername);
                    System.out.println("Username updated successfully!");
                    break;
                case "2":
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.nextLine();
                    user.setEmail(newEmail);
                    System.out.println("Email updated successfully!");
                    break;
                case "3":
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    user.setPassword(newPassword);
                    System.out.println("Password updated successfully!");
                    break;
                case "4":
                    System.out.println("Modification cancelled.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } else {
            System.out.println("User not found.");
        }
    }

    /**
     * VOID METHOD: Adjusts account balances for corrections.
     * Demonstrates ENCAPSULATION through account balance setter.
     */
    private static void adjustAccountBalance(Scanner scanner) {
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

            // SELECTION: switch statement for balance adjustment
            switch (choice) {
                case "1":
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
                    break;
                case "2":
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
                    break;
                case "3":
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
                    break;
                case "4":
                    System.out.println("Adjustment cancelled.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } else {
            System.out.println("User or account not found.");
        }
    }

    /**
     * VOID METHOD: Deletes a user account from the system.
     * Demonstrates ARRAYS manipulation.
     */
    private static void deleteUserAccount(Scanner scanner) {
        System.out.print("Enter username of user to delete: ");
        String username = scanner.nextLine();

        int userIndex = -1;
        // LOOPS: for loop to find user index
        for (int i = 0; i < userCount; i++) {
            if (users[i] instanceof RegularUser) {
                RegularUser user = (RegularUser) users[i];
                if (user.getUsername().equals(username)) {
                    userIndex = i;
                    break;
                }
            }
        }

        if (userIndex != -1) {
            System.out.print("Are you sure you want to delete user '" + username + "'? (yes/no): ");
            String confirmation = scanner.nextLine();

            // SELECTION: if to confirm deletion
            if (confirmation.equalsIgnoreCase("yes")) {
                // Shift array elements to remove user
                for (int i = userIndex; i < userCount - 1; i++) {
                    users[i] = users[i + 1];
                }
                userCount--;
                System.out.println("User '" + username + "' has been deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            System.out.println("User not found.");
        }
    }

    /**
     * VOID METHOD: Generates a system-wide report (SUPER admin only).
     * Demonstrates LOOPS through user data.
     */
    private static void generateSystemReport() {
        System.out.println("\n========== SYSTEM-WIDE FINANCIAL REPORT ==========");
        System.out.println("Generated at: " + new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date()));
        
        double totalBalance = 0;
        int totalTransactions = 0;
        int savingsAccountCount = 0;
        int checkingAccountCount = 0;

        System.out.println("\n--- User Account Summary ---");
        
        // LOOPS: for loop through users array
        if (userCount == 0) {
            System.out.println("No users in system.");
        } else {
            for (int i = 0; i < userCount; i++) {
                if (users[i] instanceof RegularUser) {
                    RegularUser user = (RegularUser) users[i];
                    Account account = user.getAccount();
                    
                    if (account != null) {
                        System.out.println((i + 1) + ". " + user.getUsername() + " - Account: " + account.getAccountType());
                        System.out.println("   Balance: $" + String.format("%.2f", account.getBalance()));
                        System.out.println("   Transactions: " + account.getTransactionCount());
                        
                        totalBalance += account.getBalance();
                        totalTransactions += account.getTransactionCount();
                        
                        // SELECTION: if to count account types
                        if (account instanceof SavingsAccount) {
                            savingsAccountCount++;
                        } else if (account instanceof CheckingAccount) {
                            checkingAccountCount++;
                        }
                    }
                }
            }
        }

        System.out.println("\n--- Financial Summary ---");
        System.out.println("Total Users: " + userCount);
        System.out.println("Savings Accounts: " + savingsAccountCount);
        System.out.println("Checking Accounts: " + checkingAccountCount);
        System.out.println("Total System Balance: $" + String.format("%.2f", totalBalance));
        System.out.println("Total Transactions: " + totalTransactions);
        System.out.println("Average Balance per User: $" + String.format("%.2f", userCount > 0 ? totalBalance / userCount : 0));
        System.out.println("==================================================");
    }

    /**
     * VOID METHOD: Manages administrator accounts (SUPER admin only).
     * Demonstrates user management and ENCAPSULATION.
     */
    private static void manageAdmins(Scanner scanner) {
        System.out.println("\n--- Administrator Management ---");
        System.out.println("1. View Admins (Simulated - Only one admin in demo)");
        System.out.println("2. Create New Admin");
        System.out.println("3. Modify Admin");
        System.out.println("4. Cancel");
        System.out.print("Select option (1-4): ");

        String choice = scanner.nextLine();

        // SELECTION: switch statement for admin management
        switch (choice) {
            case "1":
                System.out.println("\n--- Current Admins ---");
                System.out.println("1. admin (SUPER) - admin@pennywise.com");
                System.out.println("Note: In full implementation, multiple admins would be stored in an array.");
                break;
            case "2":
                System.out.println("\n--- Create New Admin ---");
                System.out.print("Enter new admin username: ");
                String newAdminUsername = scanner.nextLine();
                System.out.print("Enter admin email: ");
                String adminEmail = scanner.nextLine();
                System.out.print("Enter admin privilege level (BASIC/SUPER): ");
                String adminLevel = scanner.nextLine();
                
                // SELECTION: validate privilege level
                if (adminLevel.equalsIgnoreCase("BASIC") || adminLevel.equalsIgnoreCase("SUPER")) {
                    System.out.println("New admin '" + newAdminUsername + "' created with " + adminLevel + " privileges.");
                    System.out.println("Note: In full implementation, this would be stored in a database.");
                } else {
                    System.out.println("Invalid privilege level. Must be BASIC or SUPER.");
                }
                break;
            case "3":
                System.out.println("\n--- Modify Admin ---");
                System.out.print("Enter admin username to modify: ");
                String adminUsername = scanner.nextLine();
                
                if (adminUsername.equals("admin")) {
                    System.out.println("1. Change Email");
                    System.out.println("2. Change Privilege Level");
                    System.out.print("Select option (1-2): ");
                    String modifyChoice = scanner.nextLine();
                    
                    // SELECTION: switch for admin modification
                    switch (modifyChoice) {
                        case "1":
                            System.out.print("Enter new email: ");
                            String newEmail = scanner.nextLine();
                            System.out.println("Admin email updated to: " + newEmail);
                            break;
                        case "2":
                            System.out.print("Enter new privilege level (BASIC/SUPER): ");
                            String newLevel = scanner.nextLine();
                            if (newLevel.equalsIgnoreCase("BASIC") || newLevel.equalsIgnoreCase("SUPER")) {
                                System.out.println("Admin privilege level changed to: " + newLevel);
                            } else {
                                System.out.println("Invalid privilege level.");
                            }
                            break;
                        default:
                            System.out.println("Invalid option.");
                    }
                } else {
                    System.out.println("Admin not found.");
                }
                break;
            case "4":
                System.out.println("Admin management cancelled.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
}