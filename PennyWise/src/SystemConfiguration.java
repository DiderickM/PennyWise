/**
 * Created on Sun Feb 16 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

/**
 * SystemConfiguration class manages system-wide default settings for accounts.
 * Demonstrates ENCAPSULATION and SINGLETON pattern (static instance).
 * 
 * This class stores and manages:
 * - Default interest rate for savings accounts
 * - Default overdraft limit for checking accounts
 * - Default overdraft fee for checking accounts
 * - Default maximum withdrawals per month for savings accounts
 */
public class SystemConfiguration {
    // SINGLETON: Static instance with default values
    private static SystemConfiguration instance;
    
    // ENCAPSULATION: Private fields for default settings
    private double defaultSavingsInterestRate;
    private double defaultCheckingOverdraftLimit;
    private double defaultCheckingOverdraftFee;
    private int defaultSavingsMaxWithdrawals;
    
    /**
     * Private constructor for Singleton pattern.
     * Initializes with default values from AppConstants.
     */
    private SystemConfiguration() {
        this.defaultSavingsInterestRate = AppConstants.DEFAULT_SAVINGS_INTEREST_RATE;
        this.defaultCheckingOverdraftLimit = AppConstants.DEFAULT_CHECKING_OVERDRAFT_LIMIT;
        this.defaultCheckingOverdraftFee = AppConstants.DEFAULT_CHECKING_OVERDRAFT_FEE;
        this.defaultSavingsMaxWithdrawals = AppConstants.DEFAULT_SAVINGS_MAX_WITHDRAWALS;
    }
    
    /**
     * VALUE RETURNING METHOD: Get the singleton instance.
     * Creates instance if it doesn't exist (lazy initialization).
     * 
     * @return Singleton instance of SystemConfiguration
     */
    public static SystemConfiguration getInstance() {
        if (instance == null) {
            instance = new SystemConfiguration();
        }
        return instance;
    }
    
    // ENCAPSULATION: Getters and Setters for all configuration values
    
    public double getDefaultSavingsInterestRate() {
        return defaultSavingsInterestRate;
    }
    
    public void setDefaultSavingsInterestRate(double rate) {
        if (rate >= 0) {
            this.defaultSavingsInterestRate = rate;
        }
    }
    
    public double getDefaultCheckingOverdraftLimit() {
        return defaultCheckingOverdraftLimit;
    }
    
    public void setDefaultCheckingOverdraftLimit(double limit) {
        if (limit >= 0) {
            this.defaultCheckingOverdraftLimit = limit;
        }
    }
    
    public double getDefaultCheckingOverdraftFee() {
        return defaultCheckingOverdraftFee;
    }
    
    public void setDefaultCheckingOverdraftFee(double fee) {
        if (fee >= 0) {
            this.defaultCheckingOverdraftFee = fee;
        }
    }
    
    public int getDefaultSavingsMaxWithdrawals() {
        return defaultSavingsMaxWithdrawals;
    }
    
    public void setDefaultSavingsMaxWithdrawals(int max) {
        if (max > 0) {
            this.defaultSavingsMaxWithdrawals = max;
        }
    }
    
    /**
     * VOID METHOD: Display all current configuration settings.
     */
    public void displaySettings() {
        System.out.println("\n========== System Configuration ==========");
        System.out.println("Default Savings Interest Rate: " + String.format("%.2f", defaultSavingsInterestRate * 100) + "%");
        System.out.println("Default Checking Overdraft Limit: $" + InputValidator.formatMoney(defaultCheckingOverdraftLimit));
        System.out.println("Default Checking Overdraft Fee: $" + InputValidator.formatMoney(defaultCheckingOverdraftFee));
        System.out.println("Default Savings Max Withdrawals/Month: " + defaultSavingsMaxWithdrawals);
        System.out.println("=========================================");
    }
}
