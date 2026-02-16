/**
 * Created on Sun Feb 16 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

package pennywise.config;

import java.io.*;
import pennywise.data.DataPersistence;

/**
 * DataConfiguration class handles saving and loading system configuration.
 * Extends DataPersistence and implements file I/O for configuration settings.
 * 
 * Demonstrates FILE I/O operations and POLYMORPHISM.
 */
public class DataConfiguration extends DataPersistence {
    
    // Configuration file path
    private static final String CONFIG_FILE = DATA_DIR + "/config.txt";
    
    /**
     * POLYMORPHIC IMPLEMENTATION: Validates that configuration is ready to save.
     * 
     * @return true if ready to proceed
     */
    @Override
    protected boolean validatePreconditions() {
        return true;
    }
    
    /**
     * POLYMORPHIC IMPLEMENTATION: Performs the configuration saving operation.
     * 
     * @return true if save successful, false otherwise
     * @throws IOException if file operations fail
     */
    @Override
    protected boolean performOperation() throws IOException {
        saveConfiguration();
        return true;
    }
    
    /**
     * POLYMORPHIC IMPLEMENTATION: Returns the operation name.
     * 
     * @return "Configuration Save" operation name
     */
    @Override
    protected String getOperationName() {
        return "Configuration Save";
    }
    
    /**
     * VALUE RETURNING METHOD: Check if configuration file exists.
     * 
     * @return true if config file exists
     */
    public static boolean configExists() {
        File configFile = new File(CONFIG_FILE);
        return configFile.exists();
    }
    
    /**
     * VOID METHOD: Save all configuration to file.
     * Format: key=value
     */
    private static void saveConfiguration() throws IOException {
        SystemConfiguration config = SystemConfiguration.getInstance();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(CONFIG_FILE))) {
            writer.println("DEFAULT_SAVINGS_INTEREST_RATE=" + config.getDefaultSavingsInterestRate());
            writer.println("DEFAULT_CHECKING_OVERDRAFT_LIMIT=" + config.getDefaultCheckingOverdraftLimit());
            writer.println("DEFAULT_CHECKING_OVERDRAFT_FEE=" + config.getDefaultCheckingOverdraftFee());
            writer.println("DEFAULT_SAVINGS_MAX_WITHDRAWALS=" + config.getDefaultSavingsMaxWithdrawals());
        }
    }
    
    /**
     * STATIC METHOD: Save configuration via the template method.
     * 
     * @return true if save successful
     */
    public static boolean saveConfig() {
        DataConfiguration dataCfg = new DataConfiguration();
        return dataCfg.execute();
    }
    
    /**
     * STATIC METHOD: Load configuration from file.
     * Format: key=value
     * 
     * @return true if load successful, false otherwise
     */
    public static boolean loadConfiguration() {
        if (!configExists()) {
            return true; // No config file, use defaults
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            SystemConfiguration config = SystemConfiguration.getInstance();
            String line;
            
            // LOOPS: while loop to read all configuration lines
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }
                
                // Parse key=value format
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    
                    // SELECTION: Switch on key to set appropriate configuration value
                    try {
                        switch (key) {
                            case "DEFAULT_SAVINGS_INTEREST_RATE" ->
                                config.setDefaultSavingsInterestRate(Double.parseDouble(value));
                            case "DEFAULT_CHECKING_OVERDRAFT_LIMIT" ->
                                config.setDefaultCheckingOverdraftLimit(Double.parseDouble(value));
                            case "DEFAULT_CHECKING_OVERDRAFT_FEE" ->
                                config.setDefaultCheckingOverdraftFee(Double.parseDouble(value));
                            case "DEFAULT_SAVINGS_MAX_WITHDRAWALS" ->
                                config.setDefaultSavingsMaxWithdrawals(Integer.parseInt(value));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: Invalid configuration value for " + key + ": " + value);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error loading configuration: " + e.getMessage());
            return false;
        }
    }
}
