import java.util.Scanner;

/**
 * UserInterface class - Handles all UI/presentation logic for PennyWise.
 * Separates presentation layer from business logic.
 * 
 * Implements clean architecture principles:
 * - Single Responsibility: Only handles user interaction and display
 * - Delegation: Delegates business logic to UserManager and AccountManager
 * - Input Validation: Uses InputValidator for all user inputs
 * 
 * @author PennyWise Team
 * @version 2.0
 */
public class UserInterface {
    private final Scanner scanner;
    
    /**
     * Constructor initializes the UserInterface with a Scanner.
     * @param scanner Scanner instance for user input
     */
    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
    }
    
    /**
     * Displays welcome message for PennyWise application.
     */
    public void displayWelcome() {
        System.out.println("=========================================");
        System.out.println("Welcome to PennyWise Financial Manager!");
        System.out.println("=========================================");
    }
    
    /**
     * Displays the main menu options.
     */
    public void displayMainMenu() {
        System.out.println("\n1. Regular User Mode");
        System.out.println("2. Admin Mode");
        System.out.println("3. Exit");
    }
    
    /**
     * Handles the regular user mode flow.
     * Provides options for registration and login.
     */
    public void regularUserMode() {
        System.out.println("\n--- Regular User Mode ---");
        System.out.println("1. Register New Account");
        System.out.println("2. Login");
        System.out.print("Select option (1-2): ");
        
        int choice = InputValidator.getIntInput(scanner);
        
        switch (choice) {
            case 1 -> registerRegularUser();
            case 2 -> loginRegularUser();
            default -> System.out.println("Invalid option.");
        }
    }
    
    /**
     * Handles new user registration process.
     * Creates user account with validation and password security.
     */
    public void registerRegularUser() {
        System.out.println("\n--- Registration ---");
        
        // Get and validate user ID
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine().trim();
        if (userId.isEmpty()) {
            System.out.println("User ID cannot be empty.");
            return;
        }
        
        // Get and validate username (with duplicate check)
        String username = InputValidator.getValidatedUsername(scanner, "Enter Username: ");
        if (username == null) {
            return; // Validation failed or cancelled
        }
        
        // Check for duplicate username
        if (UserManager.usernameExists(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }
        
        // Get and validate password
        String password = InputValidator.getValidatedPassword(scanner, "Enter Password: ");
        if (password == null) {
            return; // Validation failed or cancelled
        }
        
        // Hash the password before storing
        String hashedPassword = PasswordUtil.hashPassword(password);
        
        // Get and validate email
        String email = InputValidator.getValidatedEmail(scanner, "Enter Email: ");
        if (email == null) {
            return; // Validation failed or cancelled
        }
        
        // Create new RegularUser with hashed password
        RegularUser newUser = new RegularUser(userId, username, hashedPassword, email);
        
        // Create first account for the user
        System.out.println("\n--- Create Your First Account ---");
        AccountManager.displayAccountTypeMenu();
        System.out.print("Select account type (1-2): ");
        
        String accountChoice = scanner.nextLine().trim();
        Account firstAccount = AccountManager.createAccount(userId, accountChoice, 0);
        
        if (firstAccount != null) {
            newUser.addAccount(firstAccount);
            
            // Add user to system
            if (UserManager.addRegularUser(newUser)) {
                System.out.println("\nRegistration successful! Welcome, " + username + "!");
                System.out.println("Your account number is: " + firstAccount.getAccountNumber());
                
                // Save data after registration
                DataStorage.saveAllData();
                
                // Proceed to user menu
                userAccountMenu(newUser);
            } else {
                System.out.println("Registration failed. Maximum user limit reached.");
            }
        } else {
            System.out.println("Failed to create account. Registration cancelled.");
        }
    }
    
    /**
     * Handles user login process with secure password verification.
     */
    public void loginRegularUser() {
        System.out.println("\n--- Login ---");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();
        
        // Authenticate user with secure password verification
        RegularUser user = UserManager.authenticateRegularUser(username, password);
        
        if (user != null) {
            System.out.println("\nLogin successful! Welcome back, " + username + "!");
            userAccountMenu(user);
        } else {
            System.out.println("Invalid username or password.");
        }
    }
    
    /**
     * Displays and handles the user account menu.
     * Provides options for account management and transactions.
     * @param user The logged-in RegularUser
     */
    public void userAccountMenu(RegularUser user) {
        boolean userLoggedIn = true;
        
        while (userLoggedIn) {
            System.out.println("\n--- Account Menu ---");
            System.out.println("1. View Dashboard");
            System.out.println("2. Create New Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer (Between Your Accounts)");
            System.out.println("6. Transfer (To Another User)");
            System.out.println("7. View Transaction History");
            System.out.println("8. Edit Profile");
            System.out.println("9. Logout");
            System.out.print("Select option (1-9): ");
            
            int choice = InputValidator.getIntInput(scanner);
            
            switch (choice) {
                case 1 -> user.displayDashboard();
                case 2 -> createAdditionalAccount(user);
                case 3 -> handleDeposit(user);
                case 4 -> handleWithdraw(user);
                case 5 -> handleInternalTransfer(user);
                case 6 -> handleExternalTransfer(user);
                case 7 -> viewTransactionHistory(user);
                case 8 -> editProfile(user);
                case 9 -> {
                    userLoggedIn = false;
                    System.out.println("Logged out successfully.");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    /**
     * Creates an additional account for an existing user.
     * @param user The RegularUser creating the account
     */
    private void createAdditionalAccount(RegularUser user) {
        Account[] accounts = user.getAccounts();
        int accountCount = (accounts == null) ? 0 : accounts.length;
        
        if (accountCount >= AppConstants.MAX_ACCOUNTS_PER_USER) {
            System.out.println("Maximum account limit reached.");
            return;
        }
        
        System.out.println("\n--- Create Additional Account ---");
        AccountManager.displayAccountTypeMenu();
        System.out.print("Select account type (1-2): ");
        
        String accountChoice = scanner.nextLine().trim();
        Account newAccount = AccountManager.createAccount(user.getUserId(), accountChoice, accountCount);
        
        if (newAccount != null) {
            user.addAccount(newAccount);
            System.out.println("Account created successfully!");
            System.out.println("Account Number: " + newAccount.getAccountNumber());
            
            // Save data after creating account
            DataStorage.saveAllData();
        } else {
            System.out.println("Failed to create account.");
        }
    }
    
    /**
     * Handles deposit operation.
     * @param user The RegularUser making the deposit
     */
    private void handleDeposit(RegularUser user) {
        if (AccountManager.hasNoAccounts(user.getAccounts())) {
            System.out.println("No accounts available.");
            return;
        }
        
        System.out.println("\n--- Deposit ---");
        Account[] accounts = user.getAccounts();
        Account account = AccountManager.selectAccount(scanner, accounts, "Select account for deposit:");
        
        if (account != null) {
            System.out.print("Enter deposit amount: $");
            double amount = InputValidator.getDoubleInput(scanner);
            
            if (amount > 0) {
                account.deposit(amount);
                System.out.println("Deposit successful!");
                System.out.println("New balance: " + InputValidator.formatMoney(account.getBalance()));
                
                // Save data after transaction
                DataStorage.saveAllData();
            } else {
                System.out.println("Invalid amount. Must be positive.");
            }
        }
    }
    
    /**
     * Handles withdrawal operation.
     * @param user The RegularUser making the withdrawal
     */
    private void handleWithdraw(RegularUser user) {
        if (AccountManager.hasNoAccounts(user.getAccounts())) {
            System.out.println("No accounts available.");
            return;
        }
        
        System.out.println("\n--- Withdraw ---");
        Account[] accounts = user.getAccounts();
        Account account = AccountManager.selectAccount(scanner, accounts, "Select account for withdrawal:");
        
        if (account != null) {
            System.out.print("Enter withdrawal amount: $");
            double amount = InputValidator.getDoubleInput(scanner);
            
            if (amount > 0) {
                if (account.withdraw(amount)) {
                    System.out.println("Withdrawal successful!");
                    System.out.println("New balance: " + InputValidator.formatMoney(account.getBalance()));
                    
                    // Save data after transaction
                    DataStorage.saveAllData();
                } else {
                    System.out.println("Insufficient funds or invalid amount.");
                }
            } else {
                System.out.println("Invalid amount. Must be positive.");
            }
        }
    }
    
    /**
     * Handles internal transfer between user's own accounts.
     * @param user The RegularUser making the transfer
     */
    private void handleInternalTransfer(RegularUser user) {
        Account[] accounts = user.getAccounts();
        int accountCount = (accounts == null) ? 0 : accounts.length;
        
        if (accountCount < 2) {
            System.out.println("You need at least 2 accounts to transfer between them.");
            return;
        }
        
        System.out.println("\n--- Internal Transfer ---");
        System.out.println("Select source account:");
        Account fromAccount = AccountManager.selectAccount(scanner, accounts, "Source account:");
        
        if (fromAccount == null) {
            return;
        }
        
        // Find the index of fromAccount
        int fromIndex = -1;
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i] == fromAccount) {
                fromIndex = i;
                break;
            }
        }
        
        System.out.println("\nSelect destination account:");
        int toIndex = AccountManager.selectAccountExcluding(scanner, accounts, 
                                                            fromIndex, "Destination account:");
        
        if (toIndex == -1) {
            return;
        }
        
        Account toAccount = accounts[toIndex];
        
        System.out.print("Enter transfer amount: $");
        double amount = InputValidator.getDoubleInput(scanner);
        
        if (amount > 0) {
            if (fromAccount.withdraw(amount)) {
                toAccount.deposit(amount);
                
                // Transfer already recorded in deposit/withdraw
                // No need to record again as Account class handles it
                
                System.out.println("\nTransfer successful!");
                System.out.println("From: " + fromAccount.getAccountNumber() + 
                                 " (New balance: " + InputValidator.formatMoney(fromAccount.getBalance()) + ")");
                System.out.println("To: " + toAccount.getAccountNumber() + 
                                 " (New balance: " + InputValidator.formatMoney(toAccount.getBalance()) + ")");
                
                // Save data after transaction
                DataStorage.saveAllData();
            } else {
                System.out.println("Insufficient funds in source account.");
            }
        } else {
            System.out.println("Invalid amount. Must be positive.");
        }
    }
    
    /**
     * Handles external transfer to another user's account.
     * @param user The RegularUser making the transfer
     */
    private void handleExternalTransfer(RegularUser user) {
        if (AccountManager.hasNoAccounts(user.getAccounts())) {
            System.out.println("No accounts available.");
            return;
        }
        
        System.out.println("\n--- External Transfer ---");
        System.out.println("Select your account:");
        Account[] accounts = user.getAccounts();
        Account fromAccount = AccountManager.selectAccount(scanner, accounts, "Your account:");
        
        if (fromAccount == null) {
            return;
        }
        
        System.out.print("Enter destination account number: ");
        String destAccountNumber = scanner.nextLine().trim();
        
        // Find the destination account
        Account toAccount = UserManager.findAccountByNumber(destAccountNumber);
        
        if (toAccount == null) {
            System.out.println("Destination account not found.");
            return;
        }
        
        // Prevent transfer to own account
        if (toAccount.getAccountNumber().equals(fromAccount.getAccountNumber())) {
            System.out.println("Cannot transfer to the same account. Use internal transfer instead.");
            return;
        }
        
        System.out.print("Enter transfer amount: $");
        double amount = InputValidator.getDoubleInput(scanner);
        
        if (amount > 0) {
            if (fromAccount.withdraw(amount)) {
                toAccount.deposit(amount);
                
                // Transfer already recorded in deposit/withdraw
                // No need to record again as Account class handles it
                
                System.out.println("\nExternal transfer successful!");
                System.out.println("From: " + fromAccount.getAccountNumber() + 
                                 " (New balance: " + InputValidator.formatMoney(fromAccount.getBalance()) + ")");
                System.out.println("To: " + destAccountNumber + " (Amount: " + 
                                 InputValidator.formatMoney(amount) + ")");
                
                // Save data after transaction
                DataStorage.saveAllData();
            } else {
                System.out.println("Insufficient funds.");
            }
        } else {
            System.out.println("Invalid amount. Must be positive.");
        }
    }
    
    /**
     * Displays transaction history for selected account.
     * @param user The RegularUser viewing transaction history
     */
    private void viewTransactionHistory(RegularUser user) {
        if (AccountManager.hasNoAccounts(user.getAccounts())) {
            System.out.println("No accounts available.");
            return;
        }
        
        System.out.println("\n--- Transaction History ---");
        Account[] accounts = user.getAccounts();
        Account account = AccountManager.selectAccount(scanner, accounts, "Select account:");
        
        if (account != null) {
            account.displayTransactionHistory();
        }
    }
    
    /**
     * Allows user to edit their profile information.
     * @param user The RegularUser editing their profile
     */
    private void editProfile(RegularUser user) {
        System.out.println("\n--- Edit Profile ---");
        System.out.println("1. Change Username");
        System.out.println("2. Change Password");
        System.out.println("3. Change Email");
        System.out.println("4. View Profile");
        System.out.println("5. Back");
        System.out.print("Select option (1-5): ");
        
        int choice = InputValidator.getIntInput(scanner);
        
        switch (choice) {
            case 1 -> changeUsername(user);
            case 2 -> changePassword(user);
            case 3 -> changeEmail(user);
            case 4 -> user.displayProfile();
            case 5 -> System.out.println("Returning to account menu...");
            default -> System.out.println("Invalid option.");
        }
    }
    
    /**
     * Changes user's username with validation.
     * @param user The RegularUser changing username
     */
    private void changeUsername(RegularUser user) {
        System.out.println("\n--- Change Username ---");
        String newUsername = InputValidator.getValidatedUsername(scanner, "Enter new username: ");
        
        if (newUsername != null) {
            // Check for duplicate username
            if (UserManager.usernameExists(newUsername)) {
                System.out.println("Username already exists. Please choose a different username.");
                return;
            }
            
            user.setUsername(newUsername);
            System.out.println("Username updated successfully!");
            
            // Save data after profile update
            DataStorage.saveAllData();
        }
    }
    
    /**
     * Changes user's password with secure hashing.
     * @param user The RegularUser changing password
     */
    private void changePassword(RegularUser user) {
        System.out.println("\n--- Change Password ---");
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine().trim();
        
        // Verify current password
        if (!PasswordUtil.verifyPassword(currentPassword, user.getPassword())) {
            System.out.println("Current password is incorrect.");
            return;
        }
        
        String newPassword = InputValidator.getValidatedPassword(scanner, "Enter new password: ");
        
        if (newPassword != null) {
            // Hash the new password
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            user.setPassword(hashedPassword);
            System.out.println("Password updated successfully!");
            
            // Save data after profile update
            DataStorage.saveAllData();
        }
    }
    
    /**
     * Changes user's email with validation.
     * @param user The RegularUser changing email
     */
    private void changeEmail(RegularUser user) {
        System.out.println("\n--- Change Email ---");
        String newEmail = InputValidator.getValidatedEmail(scanner, "Enter new email: ");
        
        if (newEmail != null) {
            user.setEmail(newEmail);
            System.out.println("Email updated successfully!");
            
            // Save data after profile update
            DataStorage.saveAllData();
        }
    }
}
