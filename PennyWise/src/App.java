import java.util.Scanner;

/**
 * Created on Mon Feb 09 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

/**
 * App class - DRIVER CLASS for PennyWise Financial Management System.
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

 *  Options: View all users, Modify accounts, Generate reports
 * =====================================
 *
 * --- Admin Menu ---
 * ... (Menu continues with more options: View all users, Modify accounts, Generate reports, etc.)
*/

public class App {

    /**
     * VALUE RETURNING METHOD: Main method - entry point of the application.
     */
    public static void main(String[] args) {
        // Load saved data on startup
        if (DataStorage.dataExists()) {
            System.out.println("Loading saved data...");
            if (DataLoader.loadAllData()) {
                System.out.println("Data loaded successfully! (" + UserManager.getUserCount() + " users)");
            } else {
                System.out.println("Warning: Failed to load some data.");
            }
        }
        
        try (Scanner scanner = new Scanner(System.in)) {
            // Create UserInterface instance
            UserInterface ui = new UserInterface(scanner);
            
            // Display welcome message
            ui.displayWelcome();
            
            // LOOPS: while loop for main menu system
            boolean running = true;
            while (running) {
                ui.displayMainMenu();
                System.out.print("Please select an option (1-3): ");
                
                // SELECTION: switch for menu options
                int choice = InputValidator.getIntInput(scanner);
                
                switch (choice) {
                    case 1 -> ui.regularUserMode(); // Delegate to UserInterface
                    case 2 -> Admin.launchAdmin(scanner); // Admin mode
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
            }
        }
    }
}
