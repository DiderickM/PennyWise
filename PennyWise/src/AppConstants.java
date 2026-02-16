/**
 * Created on Sun Feb 16 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

/**
 * AppConstants class centralizes all magic numbers and configuration values.
 * Demonstrates best practice of using named constants instead of magic numbers.
 * 
 * This makes the code more maintainable and easier to understand.
 */
public class AppConstants {
    
    // Array size limits
    public static final int MAX_USERS = 100;
    public static final int MAX_TRANSACTIONS_PER_ACCOUNT = 100;
    public static final int MAX_ACCOUNTS_PER_USER = 10;
    
    // Default account values
    public static final double DEFAULT_INITIAL_BALANCE = 1000.0;
    public static final double DEFAULT_SAVINGS_INTEREST_RATE = 0.03; // 3%
    public static final double DEFAULT_CHECKING_OVERDRAFT_LIMIT = 500.0;
    public static final double DEFAULT_CHECKING_OVERDRAFT_FEE = 35.0;
    public static final int DEFAULT_SAVINGS_MAX_WITHDRAWALS = 3;
    
    // Input validation limits
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MAX_PASSWORD_LENGTH = 100;
    
    // Account number prefixes
    public static final String SAVINGS_ACCOUNT_PREFIX = "SA-";
    public static final String CHECKING_ACCOUNT_PREFIX = "CA-";
    
    // Transaction types
    public static final String TRANSACTION_DEPOSIT = "DEPOSIT";
    public static final String TRANSACTION_WITHDRAWAL = "WITHDRAWAL";
    public static final String TRANSACTION_TRANSFER_IN = "TRANSFER IN";
    public static final String TRANSACTION_TRANSFER_OUT = "TRANSFER OUT";
    public static final String TRANSACTION_INITIAL_DEPOSIT = "INITIAL DEPOSIT";
    
    // Account types
    public static final String ACCOUNT_TYPE_SAVINGS = "SAVINGS";
    public static final String ACCOUNT_TYPE_CHECKING = "CHECKING";
    
    // Date format
    public static final String DATE_FORMAT = "MM/dd/yyyy";
    
    // Decimal formatting
    public static final String MONEY_FORMAT = "%.2f";
    public static final String PERCENTAGE_FORMAT = "%.2f";
    
    // Private constructor to prevent instantiation
    private AppConstants() {
        throw new AssertionError("Constants class should not be instantiated");
    }
}
