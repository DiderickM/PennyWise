/**
 * Created on Mon Feb 09 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

/**
 * SavingsAccount class extends Account.
 * Demonstrates INHERITANCE and POLYMORPHISM with specific savings account features.
 * 
 * Savings accounts earn interest and may have withdrawal restrictions.
 */
public class SavingsAccount extends Account {
    // ENCAPSULATION: Private field for interest rate
    private double interestRate; // e.g., 0.03 for 3%

    /**
     * Constructor to initialize a SavingsAccount.
     * 
     * @param accountNumber Unique account identifier
     * @param initialBalance Initial account balance
     * @param interestRate Annual interest rate (as decimal, e.g., 0.03 for 3%)
     */
    public SavingsAccount(String accountNumber, double initialBalance, double interestRate) {
        super(accountNumber, initialBalance, "SAVINGS");
        this.interestRate = interestRate;
    }

    // ENCAPSULATION: Getter and Setter for interest rate
    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * POLYMORPHISM IMPLEMENTATION: SavingsAccount applies interest benefits.
     * Overrides abstract method from Account.
     */
    @Override
    public void applyAccountFeatures() {
        // Apply monthly interest
        double monthlyRate = interestRate / 12;
        double interest = getBalance() * monthlyRate;
        
        // SELECTION: if-else to validate interest amount
        if (interest > 0) {
            deposit(interest);
            System.out.println("Interest applied: $" + App.formatMoney(interest));
        }
    }

    /**
     * POLYMORPHISM IMPLEMENTATION: Override withdraw with withdrawal limit.
     * Savings accounts limit withdrawals per month.
     */
    @Override
    public boolean withdraw(double amount) {
        // SELECTION: Conditional logic for withdrawal restrictions
        int monthlyWithdrawals = countWithdrawalsThisMonth();
        
        if (monthlyWithdrawals >= 3) {
            System.out.println("Monthly withdrawal limit (3) reached!");
            return false;
        }
        
        return super.withdraw(amount);
    }

    /**
     * VALUE RETURNING METHOD: Counts withdrawals in the current month.
     * Demonstrates LOOPS and ARRAYS.
     */
    private int countWithdrawalsThisMonth() {
        int count = 0;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/yyyy");
        String currentMonth = sdf.format(new java.util.Date());
        
        // LOOPS: for loop to iterate through transactions
        for (int i = 0; i < getTransactionCount(); i++) {
            Transaction t = getTransaction(i);
            if (t != null && t.getType().equals("WITHDRAWAL")) {
                String transMonth = t.getDate().substring(0, 2) + "/" + t.getDate().substring(6);
                if (transMonth.equals(currentMonth)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * VALUE RETURNING METHOD: Calculates projected balance with interest over months.
     * 
     * @param months Number of months to project
     * @return Projected balance
     */
    public double projectFutureBalance(int months) {
        double projectedBalance = getBalance();
        double monthlyRate = interestRate / 12;
        
        // LOOPS: for loop to compound interest
        for (int i = 0; i < months; i++) {
            projectedBalance += projectedBalance * monthlyRate;
        }
        
        return projectedBalance;
    }

    /**
     * VOID METHOD: Displays savings account specific information.
     */
    public void displaySavingsInfo() {
        super.displayAccountInfo();
        System.out.println("Interest Rate: " + String.format("%.2f", interestRate * 100) + "%");
        double projectedBalance = projectFutureBalance(12);
        System.out.println("Projected Balance (12 months): $" + App.formatMoney(projectedBalance));
    }
}
