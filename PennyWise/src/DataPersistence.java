/**
 * Created on Sun Feb 16 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

import java.io.File;
import java.io.IOException;

/**
 * DataPersistence abstract base class defines shared configuration for data storage.
 * This ensures DataLoader and DataStorage always use the same file paths and stay in sync.
 * 
 * Demonstrates INHERITANCE, POLYMORPHISM, and the TEMPLATE METHOD pattern.
 * Subclasses must implement abstract methods for their specific persistence operations.
 */
public abstract class DataPersistence {
    
    // Shared constants for data file locations
    protected static final String DATA_DIR = "data";
    protected static final String USERS_FILE = DATA_DIR + "/users.txt";
    protected static final String ACCOUNTS_FILE = DATA_DIR + "/accounts.txt";
    protected static final String TRANSACTIONS_FILE = DATA_DIR + "/transactions.txt";
    
    /**
     * TEMPLATE METHOD: Defines the algorithm structure for persistence operations.
     * Subclasses customize behavior by implementing abstract methods.
     * Demonstrates POLYMORPHISM - same method, different implementations.
     * 
     * @return true if operation successful, false otherwise
     */
    public boolean execute() {
        try {
            // Step 1: Validate preconditions (polymorphic)
            if (!validatePreconditions()) {
                System.out.println(getOperationName() + " failed: preconditions not met.");
                return false;
            }
            
            // Step 2: Prepare for operation (polymorphic)
            prepareOperation();
            
            // Step 3: Perform the main operation (polymorphic - must be implemented by subclass)
            boolean result = performOperation();
            
            // Step 4: Cleanup after operation (polymorphic)
            cleanupOperation(result);
            
            return result;
        } catch (Exception e) {
            handleError(e);
            return false;
        }
    }
    
    /**
     * ABSTRACT METHOD: Validates preconditions before operation.
     * POLYMORPHISM - Each subclass implements its own validation logic.
     * 
     * @return true if ready to proceed, false otherwise
     */
    protected abstract boolean validatePreconditions();
    
    /**
     * ABSTRACT METHOD: Performs the main persistence operation.
     * POLYMORPHISM - DataStorage saves, DataLoader loads.
     * 
     * @return true if operation successful, false otherwise
     * @throws IOException if file operations fail
     */
    protected abstract boolean performOperation() throws IOException;
    
    /**
     * ABSTRACT METHOD: Returns a descriptive name for this operation.
     * POLYMORPHISM - Each subclass provides its own name.
     * 
     * @return operation name (e.g., "Data Save", "Data Load")
     */
    protected abstract String getOperationName();
    
    /**
     * HOOK METHOD: Prepares for the operation.
     * Default implementation ensures data directory exists.
     * Subclasses can override to add their own preparation logic.
     */
    protected void prepareOperation() {
        ensureDataDirectory();
    }
    
    /**
     * HOOK METHOD: Cleanup after operation completes.
     * Default implementation does nothing.
     * Subclasses can override to add their own cleanup logic.
     * 
     * @param success whether the operation was successful
     */
    protected void cleanupOperation(boolean success) {
        // Default: no cleanup needed
        // Subclasses can override
    }
    
    /**
     * HOOK METHOD: Handles errors that occur during operation.
     * Default implementation prints error message.
     * Subclasses can override for custom error handling.
     * 
     * @param e the exception that occurred
     */
    protected void handleError(Exception e) {
        System.out.println("Error during " + getOperationName() + ": " + e.getMessage());
    }
    
    /**
     * Ensures the data directory exists, creates it if necessary.
     * Protected so both subclasses can use it.
     */
    protected static void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
    
    /**
     * Checks if the data files exist.
     * 
     * @return true if users file exists (indicates data has been saved before)
     */
    protected static boolean dataFilesExist() {
        File usersFile = new File(USERS_FILE);
        return usersFile.exists();
    }
    
    /**
     * Gets a File object for the users file.
     * 
     * @return File object for users.txt
     */
    protected static File getUsersFile() {
        return new File(USERS_FILE);
    }
    
    /**
     * Gets a File object for the accounts file.
     * 
     * @return File object for accounts.txt
     */
    protected static File getAccountsFile() {
        return new File(ACCOUNTS_FILE);
    }
    
    /**
     * Gets a File object for the transactions file.
     * 
     * @return File object for transactions.txt
     */
    protected static File getTransactionsFile() {
        return new File(TRANSACTIONS_FILE);
    }
}
