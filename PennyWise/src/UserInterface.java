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
            // Check if user has any checking accounts for external transfers
            boolean hasCheckingAccount = hasCheckingAccount(user);
            
            System.out.println("\n--- Account Menu ---");
            System.out.println("1. View Dashboard");
            System.out.println("2. Create New Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer (Between Your Accounts)");
            if (hasCheckingAccount) {
                System.out.println("6. Transfer (To Another User)");
            }
            System.out.println((hasCheckingAccount ? "7" : "6") + ". View Transaction History");
            System.out.println((hasCheckingAccount ? "8" : "7") + ". Edit Profile");
            System.out.println((hasCheckingAccount ? "9" : "8") + ". Close Account");
            System.out.println((hasCheckingAccount ? "10" : "9") + ". Logout");
            System.out.print("Select option (1-" + (hasCheckingAccount ? "10" : "9") + "): ");
            
            int choice = InputValidator.getIntInput(scanner);
            
            // Adjust choice if no checking account (shift down by 1 for options 6+)
            if (!hasCheckingAccount && choice >= 6) {
                choice++; // Map 6->7, 7->8, 8->9, 9->10
            }
            
            switch (choice) {
                case 1 -> user.displayDashboard();
                case 2 -> createAdditionalAccount(user);
                case 3 -> handleDeposit(user);
                case 4 -> handleWithdraw(user);
                case 5 -> handleInternalTransfer(user);
                case 6 -> {
                    if (hasCheckingAccount) {
                        handleExternalTransfer(user);
                    } else {
                        System.out.println("Invalid option. Please try again.");
                    }
                }
                case 7 -> viewTransactionHistory(user);
                case 8 -> editProfile(user);
                case 9 -> handleCloseAccount(user);
                case 10 -> {
                    userLoggedIn = false;
                    System.out.println("Logged out successfully.");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    /**
     * Checks if the user has at least one checking account.
     * @param user The RegularUser to check
     * @return true if user has at least one checking account, false otherwise
     */
    private boolean hasCheckingAccount(RegularUser user) {
        Account[] accounts = user.getAccounts();
        if (accounts == null) {
            return false;
        }
        
        for (Account account : accounts) {
            if (account instanceof CheckingAccount) {
                return true;
            }
        }
        return false;
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
     * Only checking accounts can transfer to external accounts.
     * @param user The RegularUser making the transfer
     */
    private void handleExternalTransfer(RegularUser user) {
        Account[] accounts = user.getAccounts();
        
        if (AccountManager.hasNoAccounts(accounts)) {
            System.out.println("No accounts available.");
            return;
        }
        
        // Filter to get only checking accounts
        Account[] checkingAccounts = AccountManager.getCheckingAccounts(accounts);
        
        if (checkingAccounts.length == 0) {
            System.out.println("You need a checking account to transfer to external accounts.");
            System.out.println("Savings accounts are restricted to internal transfers only.");
            return;
        }
        
        System.out.println("\n--- External Transfer ---");
        System.out.println("Select your checking account:");
        Account fromAccount = AccountManager.selectCheckingAccount(scanner, checkingAccounts, "Your account:");
        
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
        
        // Check if destination account belongs to current user
        Account[] userAccounts = user.getAccounts();
        for (Account account : userAccounts) {
            if (account != null && account.getAccountNumber().equals(destAccountNumber)) {
                System.out.println("Cannot transfer to your own account. Use internal transfer instead.");
                return;
            }
        }
        
        // Only allow transfers to checking accounts
        if (!(toAccount instanceof CheckingAccount)) {
            System.out.println("Error: Can only transfer to checking accounts.");
            System.out.println("Savings accounts cannot receive external transfers.");
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
     * Handles closing/deleting an account.
     * Transfers remaining balance to another account if needed.
     * @param user The RegularUser closing the account
     */
    private void handleCloseAccount(RegularUser user) {
        Account[] accounts = user.getAccounts();
        
        if (AccountManager.hasNoAccounts(accounts)) {
            System.out.println("No accounts available.");
            return;
        }
        
        if (accounts.length < 2) {
            System.out.println("Error: You must maintain at least one account.");
            System.out.println("Create another account before closing this one.");
            return;
        }
        
        System.out.println("\n--- Close Account ---");
        System.out.println("Select the account to close:");
        Account accountToClose = AccountManager.selectAccount(scanner, accounts, "Account to close:");
        
        if (accountToClose == null) {
            return;
        }
        
        // Check if balance is negative
        if (accountToClose.getBalance() < 0) {
            System.out.println("Error: Cannot close account with a negative balance of $" + 
                             InputValidator.formatMoney(accountToClose.getBalance()));
            System.out.println("Please deposit funds to cover the negative balance first.");
            return;
        }
        
        // If there's a positive balance, ask where to transfer it
        if (accountToClose.getBalance() > 0) {
            System.out.println("\nThis account has a balance of $" + InputValidator.formatMoney(accountToClose.getBalance()));
            System.out.println("Select the account to transfer this balance to:");
            
            // Find index of account to close
            int closeIndex = -1;
            for (int i = 0; i < accounts.length; i++) {
                if (accounts[i] == accountToClose) {
                    closeIndex = i;
                    break;
                }
            }
            
            // Select destination account (excluding the one being closed)
            int destIndex = AccountManager.selectAccountExcluding(scanner, accounts, closeIndex, 
                                                                  "Destination account:");
            
            if (destIndex == -1) {
                System.out.println("Account closure cancelled.");
                return;
            }
            
            Account destAccount = accounts[destIndex];
            double balanceToTransfer = accountToClose.getBalance();
            
            // Transfer with special message
            accountToClose.recordTransaction(balanceToTransfer, "ACCOUNT CLOSURE TRANSFER", 
                                            java.time.LocalDate.now().toString());
            destAccount.recordTransaction(balanceToTransfer, "ACCOUNT CLOSURE TRANSFER" + " (from " + accountToClose.getAccountNumber() + ")", 
                                         java.time.LocalDate.now().toString());
            destAccount.setBalance(destAccount.getBalance() + balanceToTransfer);
            accountToClose.setBalance(0);
            
            System.out.println("\nTransfer from account closure completed!");
            System.out.println("From: " + accountToClose.getAccountNumber() + " (Amount: $" + 
                             InputValidator.formatMoney(balanceToTransfer) + ")");
            System.out.println("To: " + destAccount.getAccountNumber() + " (New balance: $" + 
                             InputValidator.formatMoney(destAccount.getBalance()) + ")");
        }
        
        // Remove the account from user's accounts
        Account[] newAccounts = new Account[accounts.length - 1];
        int index = 0;
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] != accountToClose) {
                newAccounts[index++] = accounts[i];
            }
        }
        user.setAccounts(newAccounts);
        
        System.out.println("\nAccount " + accountToClose.getAccountNumber() + " has been closed successfully.");
        
        // Save data after closing account
        DataStorage.saveAllData();
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
