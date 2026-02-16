/**
 * Created on Mon Feb 09 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

/**
 * CheckingAccount class extends Account.
 * Demonstrates INHERITANCE and POLYMORPHISM with specific checking account features.
 * 
 * Checking accounts are designed for frequent transactions with overdraft protection.
 */
public class CheckingAccount extends Account {
    // ENCAPSULATION: Private fields for overdraft features
    private double overdraftLimit; // Maximum negative balance allowed
    private double overdraftFee;   // Fee charged when overdraft is used

    /**
     * Constructor to initialize a CheckingAccount.
     * 
     * @param accountNumber Unique account identifier
     * @param initialBalance Initial account balance
     * @param overdraftLimit Maximum allowed overdraft amount
     * @param overdraftFee Fee charged for overdraft usage
     */
    public CheckingAccount(String accountNumber, double initialBalance, double overdraftLimit, double overdraftFee) {
        super(accountNumber, initialBalance, AppConstants.ACCOUNT_TYPE_CHECKING);
        this.overdraftLimit = overdraftLimit;
        this.overdraftFee = overdraftFee;
    }

    // ENCAPSULATION: Getter and Setter methods
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftFee() {
        return overdraftFee;
    }

    public void setOverdraftFee(double overdraftFee) {
        this.overdraftFee = overdraftFee;
    }

    /**
     * POLYMORPHISM IMPLEMENTATION: CheckingAccount applies overdraft features.
     * Overrides abstract method from Account.
     */
    @Override
    public void applyAccountFeatures() {
        // SELECTION: Check if account is in overdraft
        if (getBalance() < 0) {
            System.out.println("Overdraft fee of $" + overdraftFee + " applied.");
            super.withdraw(overdraftFee, "OVERDRAFT FEE of $" + overdraftFee);
        }
    }

    /**
     * POLYMORPHISM IMPLEMENTATION: Override withdraw with overdraft protection.
     * Checking accounts allow withdrawals up to the overdraft limit.
     */
    @Override
    public boolean withdraw(double amount) {
        // SELECTION: Check if withdrawal exceeds overdraft limit
        double totalAvailable = getBalance() + overdraftLimit;
        System.out.println("Attempting withdrawal of $" + InputValidator.formatMoney(amount) + " - Available funds (including overdraft): $" + InputValidator.formatMoney(totalAvailable));
        if (amount > 0 && amount <= totalAvailable) {
            setBalance(getBalance() - amount);
            // Record transaction using parent method
            recordTransaction(amount, AppConstants.TRANSACTION_WITHDRAWAL, getCurrentDate());
            // SELECTION: Check if overdraft is now in use
            if (getBalance() < 0) {
                System.out.println("WARNING: Account is in overdraft!");
            }
            return true;
        }
        return false;
    }

    /**
     * VALUE RETURNING METHOD: Checks if account is in overdraft.
     */
    public boolean isInOverdraft() {
        return getBalance() < 0;
    }

    /**
     * VALUE RETURNING METHOD: Returns available funds including overdraft limit.
     */
    public double getAvailableFunds() {
        return getBalance() + overdraftLimit;
    }

    /**
     * VOID METHOD: Displays checking account specific information.
     */
    public void displayCheckingInfo() {
        super.displayAccountInfo();
        System.out.println("Overdraft Limit: $" + InputValidator.formatMoney(overdraftLimit));
        System.out.println("Overdraft Fee: $" + InputValidator.formatMoney(overdraftFee));
        System.out.println("Available Funds (including overdraft): $" + InputValidator.formatMoney(getAvailableFunds()));
        
        // SELECTION: Display overdraft status
        if (isInOverdraft()) {
            System.out.println("STATUS: Account is in OVERDRAFT");
        } else {
            System.out.println("STATUS: Account is NORMAL");
        }
    }

    /**
     * VOID METHOD: Displays overdraft summary using LOOPS.
     */
    public void displayOverdraftHistory() {
        System.out.println("\n========== Overdraft Usage History ==========");
        
        int overdraftCount = 0;
        // LOOPS: for loop to count overdraft occurrences
        for (int i = 0; i < getTransactionCount(); i++) {
            Transaction t = getTransaction(i);
            // This is a simplified check - in a real system, we'd track balance after each transaction
            if (t != null && t.getType().equals("WITHDRAWAL")) {
                overdraftCount++;
            }
        }
        
        System.out.println("Total withdrawal transactions: " + overdraftCount);
        System.out.println("==========================================");
    }
}
