/**
 * Created on Mon Feb 09 2026
 *
 * Copyright (c) 2026 Diderick Magermans
 */

/**
 * Transaction class represents a single financial transaction.
 * Demonstrates ENCAPSULATION with private fields and getter methods.
 */
public class Transaction {
    // ENCAPSULATION: Private variables with getters
    private double amount;
    private String type; // "DEPOSIT" or "WITHDRAWAL"
    private String date;

    /**
     * Constructor to initialize a transaction.
     * 
     * @param amount The transaction amount
     * @param type The type of transaction (DEPOSIT or WITHDRAWAL)
     * @param date The date of the transaction
     */
    public Transaction(double amount, String type, String date) {
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    // ENCAPSULATION: Getter methods
    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    /**
     * VALUE RETURNING METHOD: Returns formatted transaction string.
     * Demonstrates a method that returns a value (not void).
     */
    public String getTransactionDetails() {
        return String.format("[%s] %s: $%.2f", date, type, amount);
    }

    /**
     * VOID METHOD: Displays transaction information on console.
     */
    public void displayTransaction() {
        System.out.println(getTransactionDetails());
    }
}
