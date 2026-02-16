/**
 * Created on Sun Feb 16 2026
 *
 * Copyright (c) 2026 Diderick Magermans  
 */

import java.io.*;
import java.util.*;

/**
 * DataLoader class handles loading all application data from files.
 * Demonstrates FILE I/O operations, data restoration, and POLYMORPHISM.
 * Extends DataPersistence and implements abstract methods for loading operations.
 * 
 * This class reads users, accounts, and transactions from text files
 * and reconstructs the application state.
 */
public class DataLoader extends DataPersistence {
    
    // Temporary storage for accounts and transactions during loading
    private static final Map<String, List<AccountData>> userAccountsMap = new HashMap<>();
    private static final Map<String, List<TransactionData>> accountTransactionsMap = new HashMap<>();
    
    /**
     * Helper class to store account data during loading process.
     */
    private static class AccountData {
        String accountType;
        String accountNumber;
        double balance;
        double interestRateOrOverdraftLimit; // interest rate for Savings, overdraft limit for Checking
        double overdraftFee; // unused for Savings, overdraft fee for Checking
        int maxWithdrawalsPerMonth; // max withdrawals per month for Savings, unused for Checking
        
        AccountData(String type, String number, double bal, double interestRateOrOverdraftLimit, double overdraftFee) {
            this(type, number, bal, interestRateOrOverdraftLimit, overdraftFee, 3); // Default to 3 withdrawals
        }
        
        AccountData(String type, String number, double bal, double interestRateOrOverdraftLimit, double overdraftFee, int maxWithdrawalsPerMonth) {
            this.accountType = type;
            this.accountNumber = number;
            this.balance = bal;
            this.interestRateOrOverdraftLimit = interestRateOrOverdraftLimit;
            this.overdraftFee = overdraftFee;
            this.maxWithdrawalsPerMonth = maxWithdrawalsPerMonth;
        }
    }
    
    /**
     * Helper class to store transaction data during loading process.
     */
    private static class TransactionData {
        double amount;
        String type;
        String date;
        
        TransactionData(double amt, String typ, String dt) {
            this.amount = amt;
            this.type = typ;
            this.date = dt;
        }
    }
    
    /**
     * POLYMORPHIC IMPLEMENTATION: Validates that data files exist before loading.
     * 
     * @return true if data files exist, false otherwise
     */
    @Override
    protected boolean validatePreconditions() {
        return dataFilesExist();
    }
    
    /**
     * POLYMORPHIC IMPLEMENTATION: Performs the load operation.
     * Loads configuration first, then users, accounts, and transactions from files.
     * 
     * @return true if all loads successful, false otherwise
     * @throws IOException if file operations fail
     */
    @Override
    protected boolean performOperation() throws IOException {
        // Load configuration first
        DataConfiguration.loadConfiguration();
        
        // Load in sequence: transactions first, then accounts, then users
        loadTransactions();
        loadAccounts();
        loadUsers();
        return true;
    }
    
    /**
     * POLYMORPHIC IMPLEMENTATION: Returns the operation name.
     * 
     * @return "Data Load" operation name
     */
    @Override
    protected String getOperationName() {
        return "Data Load";
    }
    
    /**
     * HOOK METHOD OVERRIDE: Prepares for loading by clearing temporary storage.
     * Calls parent's prepareOperation() then adds loader-specific preparation.
     */
    @Override
    protected void prepareOperation() {
        super.prepareOperation(); // Ensure directory exists
        
        // Clear temporary storage before loading
        userAccountsMap.clear();
        accountTransactionsMap.clear();
    }
    
    /**
     * HOOK METHOD OVERRIDE: Custom error handling with stack trace for debugging.
     * 
     * @param e the exception that occurred
     */
    @Override
    protected void handleError(Exception e) {
        super.handleError(e);
        if (!(e instanceof IOException)) {
            // Print stack trace for unexpected errors
            e.printStackTrace();
        }
    }
    
    /**
     * Loads all data from files and populates the application.
     * Uses the template method pattern from parent class.
     * Returns true if data was successfully loaded, false otherwise.
     */
    public static boolean loadAllData() {
        DataLoader loader = new DataLoader();
        return loader.execute();
    }
    
    /**
     * Loads all users from file and creates User objects.
     */
    private static void loadUsers() throws IOException {
        File file = getUsersFile();
        if (!file.exists()) {
            return; // No data to load
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 5) continue;
                
                String userType = parts[0];
                String userId = parts[1];
                String username = parts[2];
                String password = parts[3];
                String email = parts[4];
                
                if (userType.equals("REGULAR")) {
                    RegularUser user = new RegularUser(userId, username, password, email);
                    
                    // Load accounts for this user
                    List<AccountData> accountDataList = userAccountsMap.get(userId);
                    if (accountDataList != null) {
                        for (AccountData accData : accountDataList) {
                            Account account = createAccountFromData(accData);
                            if (account != null) {
                                // Load transactions for this account
                                loadTransactionsForAccount(account, accData.accountNumber);
                                user.addAccount(account);
                            }
                        }
                    }
                    
                    App.addRegularUser(user);
                } else if (userType.equals("ADMIN")) {
                    // Admin users are hardcoded, skip loading from file
                    // Or you could load them if you want persistent admin accounts
                }
            }
        }
    }
    
    /**
     * Loads account data from file into temporary storage.
     */
    private static void loadAccounts() throws IOException {
        File file = getAccountsFile();
        if (!file.exists()) {
            return; // No data to load
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;
                
                String userId = parts[0];
                String accountType = parts[1];
                String accountNumber = parts[2];
                double balance = Double.parseDouble(parts[3]);
                
                double interestRateOrOverdraftLimit = 0, overdraftFee = 0;
                int maxWithdrawalsPerMonth = 3; // Default max withdrawals
                
                if (accountType.equals("SAVINGS") && parts.length >= 5) {
                    interestRateOrOverdraftLimit = Double.parseDouble(parts[4]); // interestRate
                    if (parts.length >= 6) {
                        maxWithdrawalsPerMonth = Integer.parseInt(parts[5]); // maxWithdrawalsPerMonth
                    }
                } else if (accountType.equals("CHECKING") && parts.length >= 6) {
                    interestRateOrOverdraftLimit = Double.parseDouble(parts[4]); // overdraftLimit
                    overdraftFee = Double.parseDouble(parts[5]); // overdraftFee
                }
                
                AccountData accData = new AccountData(accountType, accountNumber, balance, interestRateOrOverdraftLimit, overdraftFee, maxWithdrawalsPerMonth);
                
                // Store in map for later association with user
                userAccountsMap.putIfAbsent(userId, new ArrayList<>());
                userAccountsMap.get(userId).add(accData);
            }
        }
    }
    
    /**
     * Loads transaction data from file into temporary storage.
     */
    private static void loadTransactions() throws IOException {
        File file = getTransactionsFile();
        if (!file.exists()) {
            return; // No data to load
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;

                String accountNumber = parts[0];
                double amount = Double.parseDouble(parts[1]);
                String type = parts[2];
                String date = parts[3];
                
                TransactionData transData = new TransactionData(amount, type, date);
                
                // Store in map for later association with account
                accountTransactionsMap.putIfAbsent(accountNumber, new ArrayList<>());
                accountTransactionsMap.get(accountNumber).add(transData);
            }
        }
    }
    
    /**
     * Creates an Account object from AccountData.
     * Note: We create the account with 0 initial balance to avoid duplicate INITIAL DEPOSIT,
     * then set the balance directly and load transactions.
     */
    private static Account createAccountFromData(AccountData data) {
        Account account = null;
        
        if (data.accountType.equals("SAVINGS")) {
            account = new SavingsAccount(data.accountNumber, 0, data.interestRateOrOverdraftLimit, data.maxWithdrawalsPerMonth);
        } else if (data.accountType.equals("CHECKING")) {
            account = new CheckingAccount(data.accountNumber, 0, data.interestRateOrOverdraftLimit, data.overdraftFee);
        }
        
        // Set the balance directly (after creating with 0 to avoid initial deposit transaction)
        if (account != null) {
            account.setBalance(data.balance);
        }
        
        return account;
    }
    
    /**
     * Loads transactions for a specific account from temporary storage.
     */
    private static void loadTransactionsForAccount(Account account, String accountNumber) {
        List<TransactionData> transactions = accountTransactionsMap.get(accountNumber);
        if (transactions != null) {
            for (TransactionData transData : transactions) {
                // Manually add transaction to account (bypass recordTransaction to avoid duplicates)
                // We need to use the protected recordTransaction method
                account.recordTransaction(transData.amount, transData.type, transData.date);
            }
        }
    }
}
