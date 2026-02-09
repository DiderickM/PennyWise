/**
 * Created on Mon Feb 09 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

/**
 * Abstract Account class represents the base account in PennyWise system.
 * Demonstrates INHERITANCE, POLYMORPHISM, ENCAPSULATION, and ARRAYS for transaction storage.
 * 
 * This is the parent class for SavingsAccount and CheckingAccount subclasses.
 */
public abstract class Account {
    // ENCAPSULATION: Private fields with getters and setters
    private String accountNumber;
    private double balance;
    private final String accountType; // Will be overridden by subclasses
    
    // ARRAYS: Store transaction history
    private final Transaction[] transactions;
    private int transactionCount; // Track number of transactions

    /**
     * Constructor to initialize Account.
     * 
     * @param accountNumber Unique account identifier
     * @param initialBalance Initial account balance
     * @param accountType Type of account
     */
    public Account(String accountNumber, double initialBalance, String accountType) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.accountType = accountType;
        
        // Initialize transaction array to store up to 100 transactions
        this.transactions = new Transaction[100];
        this.transactionCount = 0;
        
        // Record initial deposit as a transaction
        if (initialBalance > 0) {
            recordTransaction(initialBalance, "INITIAL DEPOSIT", getCurrentDate());
        }
    }

    // ENCAPSULATION: Getter and Setter methods
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    /**
     * VALUE RETURNING METHOD: Returns current account balance.
     */
    public double checkBalance() {
        return balance;
    }

    /**
     * VALUE RETURNING METHOD: Deposits money into the account.
     * 
     * @param amount Amount to deposit
     * @return true if deposit successful, false otherwise
     */
    public boolean deposit(double amount) {
        // SELECTION: if-else statement to validate deposit
        if (amount > 0) {
            balance += amount;
            recordTransaction(amount, "DEPOSIT", getCurrentDate());
            return true;
        }
        return false;
    }

    /**
     * VALUE RETURNING METHOD: Withdraws money from the account.
     * POLYMORPHISM IMPLEMENTATION: Subclasses can override for different rules.
     * 
     * @param amount Amount to withdraw
     * @return true if withdrawal successful, false otherwise
     */
    public boolean withdraw(double amount) {
        // SELECTION: if-else statement to validate withdrawal
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawal successful - requested: $" + String.format("%.2f", amount));
            recordTransaction(amount, "WITHDRAWAL", getCurrentDate());
            return true;
        }
        return false;
    }

    /**
     * VALUE RETURNING METHOD: Transfers money to another account.
     * Demonstrates ENCAPSULATION and interaction between objects.
     * 
     * @param amount Amount to transfer
     * @param targetAccount The account to transfer money to
     * @return true if transfer successful, false otherwise
     */
    public boolean transfer(double amount, Account targetAccount) {
        // SELECTION: Validate transfer conditions
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");
            return false;
        }
        
        if (targetAccount == null) {
            System.out.println("Target account is invalid.");
            return false;
        }
        
        if (this.equals(targetAccount)) {
            System.out.println("Cannot transfer to the same account.");
            return false;
        }
        
        if (this.balance < amount) {
            System.out.println("Insufficient funds for transfer.");
            return false;
        }
        
        // Perform the transfer
        this.balance -= amount;
        targetAccount.balance += amount;
        
        // Record transactions in both accounts
        this.recordTransaction(amount, "TRANSFER OUT", getCurrentDate());
        targetAccount.recordTransaction(amount, "TRANSFER IN", getCurrentDate());
        
        return true;
    }

    /**
     * VOID METHOD: Records a transaction in the transaction array.
     * Demonstrates ARRAYS usage.
     * 
     * @param amount Transaction amount
     * @param type Transaction type
     * @param date Transaction date
     */
    protected void recordTransaction(double amount, String type, String date) {
        // SELECTION: Check if array has space
        if (transactionCount < transactions.length) {
            transactions[transactionCount] = new Transaction(amount, type, date);
            transactionCount++;
        }
    }

    /**
     * VOID METHOD: Displays all transaction history using LOOPS and ARRAYS.
     * Demonstrates LOOPS: for loop to iterate through array.
     */
    public void displayTransactionHistory() {
        System.out.println("\n========== Transaction History ==========");
        System.out.println("Account: " + accountNumber);
        
        // LOOPS: for loop to iterate through transactions array
        if (transactionCount == 0) {
            System.out.println("No transactions yet.");
        } else {
            for (int i = 0; i < transactionCount; i++) {
                System.out.print("  ");
                transactions[i].displayTransaction();
            }
        }
        System.out.println("=========================================");
    }

    /**
     * VALUE RETURNING METHOD: Returns the number of transactions.
     */
    public int getTransactionCount() {
        return transactionCount;
    }

    /**
     * VALUE RETURNING METHOD: Gets a transaction at a specific index.
     * 
     * @param index Index of the transaction
     * @return Transaction object or null if not found
     */
    public Transaction getTransaction(int index) {
        if (index >= 0 && index < transactionCount) {
            return transactions[index];
        }
        return null;
    }

    /**
     * VALUE RETURNING METHOD: Gets current date as string.
     */
    protected String getCurrentDate() {
        return new java.text.SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date());
    }

    /**
     * ABSTRACT METHOD: To be implemented by subclasses for specific account rules.
     */
    public abstract void applyAccountFeatures();

    /**
     * VOID METHOD: Displays account information.
     */
    public void displayAccountInfo() {
        System.out.println("\n========== Account Information ==========");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Type: " + accountType);
        System.out.println("Current Balance: $" + App.formatMoney(balance));
        System.out.println("Total Transactions: " + transactionCount);
        System.out.println("=========================================");
    }
}
