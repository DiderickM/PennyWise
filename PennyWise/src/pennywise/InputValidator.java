/**
 * Created on Sun Feb 16 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

package pennywise;

import java.util.Scanner;

/**
 * InputValidator class provides centralized validation for all user inputs.
 * Demonstrates ENCAPSULATION and Single Responsibility Principle.
 * 
 * This class handles:
 * - String input validation (username, email, etc.)
 * - Numeric input parsing and validation
 * - Menu choice validation
 */
public class InputValidator {
    
    /**
     * VALUE RETURNING METHOD: Safely parses a double from user input.
     * 
     * @param scanner Scanner to read input from
     * @return parsed double value, or -1 if invalid
     */
    public static double getDoubleInput(Scanner scanner) {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * VALUE RETURNING METHOD: Safely parses an integer from user input.
     * 
     * @param scanner Scanner to read input from
     * @return parsed integer value, or -1 if invalid
     */
    public static int getIntInput(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * VALUE RETURNING METHOD: Gets a validated string input.
     * 
     * @param scanner Scanner to read input from
     * @return trimmed non-empty string, or null if empty
     */
    public static String getStringInput(Scanner scanner) {
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? null : input;
    }
    
    /**
     * VALUE RETURNING METHOD: Validates username format and length.
     * 
     * @param username Username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        int length = username.length();
        if (length < AppConstants.MIN_USERNAME_LENGTH || length > AppConstants.MAX_USERNAME_LENGTH) {
            return false;
        }
        
        // Username should contain only alphanumeric characters and underscores
        return username.matches("^[a-zA-Z0-9_]+$");
    }
    
    /**
     * VALUE RETURNING METHOD: Validates password format and length.
     * 
     * @param password Password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        
        int length = password.length();
        return length >= AppConstants.MIN_PASSWORD_LENGTH && length <= AppConstants.MAX_PASSWORD_LENGTH;
    }
    
    /**
     * VALUE RETURNING METHOD: Validates email format.
     * Basic validation - checks for @ and . in reasonable positions.
     * 
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Basic email validation
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    /**
     * VALUE RETURNING METHOD: Validates amount is positive.
     * 
     * @param amount Amount to validate
     * @return true if positive, false otherwise
     */
    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }
    
    /**
     * VALUE RETURNING METHOD: Validates menu choice is within range.
     * 
     * @param choice User's choice
     * @param minOption Minimum valid option
     * @param maxOption Maximum valid option
     * @return true if valid, false otherwise
     */
    public static boolean isValidMenuChoice(int choice, int minOption, int maxOption) {
        return choice >= minOption && choice <= maxOption;
    }
    
    /**
     * VALUE RETURNING METHOD: Gets validated username from user.
     * Prompts until valid input received.
     * 
     * @param scanner Scanner to read input from
     * @param prompt Prompt message to display
     * @return valid username
     */
    public static String getValidatedUsername(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String username = getStringInput(scanner);
            
            if (isValidUsername(username)) {
                return username;
            }
            
            System.out.println("Invalid username. Must be " + AppConstants.MIN_USERNAME_LENGTH + 
                             "-" + AppConstants.MAX_USERNAME_LENGTH + 
                             " characters, alphanumeric and underscores only.");
        }
    }
    
    /**
     * VALUE RETURNING METHOD: Gets validated password from user.
     * Prompts until valid input received.
     * 
     * @param scanner Scanner to read input from
     * @param prompt Prompt message to display
     * @return valid password
     */
    public static String getValidatedPassword(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String password = scanner.nextLine(); // Don't trim - allow spaces in passwords
            
            if (isValidPassword(password)) {
                return password;
            }
            
            System.out.println("Invalid password. Must be " + AppConstants.MIN_PASSWORD_LENGTH + 
                             "-" + AppConstants.MAX_PASSWORD_LENGTH + " characters.");
        }
    }
    
    /**
     * VALUE RETURNING METHOD: Gets validated email from user.
     * Prompts until valid input received.
     * 
     * @param scanner Scanner to read input from
     * @param prompt Prompt message to display
     * @return valid email
     */
    public static String getValidatedEmail(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String email = getStringInput(scanner);
            
            if (isValidEmail(email)) {
                return email;
            }
            
            System.out.println("Invalid email format. Please enter a valid email address.");
        }
    }
    
    /**
     * VALUE RETURNING METHOD: Gets validated positive amount from user.
     * Prompts until valid input received.
     * 
     * @param scanner Scanner to read input from
     * @param prompt Prompt message to display
     * @return valid positive amount
     */
    public static double getValidatedAmount(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            double amount = getDoubleInput(scanner);
            
            if (isValidAmount(amount)) {
                return amount;
            }
            
            System.out.println("Invalid amount. Please enter a positive number.");
        }
    }
    
    /**
     * VALUE RETURNING METHOD: Gets a validated integer within a range from user.
     * Prompts until valid input received.
     * 
     * @param scanner Scanner to read input from
     * @param prompt Prompt message to display
     * @param min Minimum valid value (inclusive)
     * @param max Maximum valid value (inclusive)
     * @return valid integer within range
     */
    public static int getValidatedInteger(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            int value = getIntInput(scanner);
            
            if (value != -1 && isValidMenuChoice(value, min, max)) {
                return value;
            }
            
            System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
        }
    }
    
    /**
     * VALUE RETURNING METHOD: Gets a validated double from user.
     * Prompts until valid input received.
     * 
     * @param scanner Scanner to read input from
     * @param errorMessage Error message to display on invalid input
     * @return valid double value
     */
    public static double getValidatedDouble(Scanner scanner, String errorMessage) {
        while (true) {
            double value = getDoubleInput(scanner);
            
            if (value != -1) {
                return value;
            }
            
            System.out.println(errorMessage);
        }
    }
    
    /**
     * VALUE RETURNING METHOD: Gets yes/no confirmation from user.
     * 
     * @param scanner Scanner to read input from
     * @param prompt Prompt message to display
     * @return true if user confirms (yes/y), false otherwise
     */
    public static boolean getConfirmation(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }
    
    /**
     * VALUE RETURNING METHOD: Formats money for display.
     * 
     * @param amount Amount to format
     * @return formatted money string
     */
    public static String formatMoney(double amount) {
        return String.format(AppConstants.MONEY_FORMAT, amount);
    }
    
    /**
     * VALUE RETURNING METHOD: Formats percentage for display.
     * 
     * @param rate Rate as decimal (e.g., 0.03 for 3%)
     * @return formatted percentage string
     */
    public static String formatPercentage(double rate) {
        return String.format(AppConstants.PERCENTAGE_FORMAT, rate * 100) + "%";
    }
    
    // Private constructor to prevent instantiation
    private InputValidator() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
