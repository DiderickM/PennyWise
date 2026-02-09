
/**
 * Simple test to demonstrate the transfer functionality between accounts
 */
public class TransferTest {
    public static void main(String[] args) {
        // Create a user with two accounts
        RegularUser user = new RegularUser("user123", "testuser", "pass123", "test@example.com");
        
        // Add two accounts
        Account savingsAccount = new SavingsAccount("SA-user123", 1000.0, 0.03);
        Account checkingAccount = new CheckingAccount("CA-user123", 500.0, 300.0, 35.0);
        
        user.addAccount(savingsAccount);
        user.addAccount(checkingAccount);
        
        // Display initial state
        System.out.println("=== BEFORE TRANSFER ===");
        System.out.println("Savings Account Balance: $" + String.format("%.2f", savingsAccount.getBalance()));
        System.out.println("Checking Account Balance: $" + String.format("%.2f", checkingAccount.getBalance()));
        
        // Perform transfer
        System.out.println("\n=== PERFORMING TRANSFER ===");
        boolean transferred = user.transferBetweenAccounts(0, 1, 250.0);
        
        if (transferred) {
            System.out.println("Transfer successful!");
        }
        
        // Display final state
        System.out.println("\n=== AFTER TRANSFER ===");
        System.out.println("Savings Account Balance: $" + String.format("%.2f", savingsAccount.getBalance()));
        System.out.println("Checking Account Balance: $" + String.format("%.2f", checkingAccount.getBalance()));
        
        // Show transaction history
        System.out.println("\n=== TRANSACTION HISTORY ===");
        savingsAccount.displayTransactionHistory();
        checkingAccount.displayTransactionHistory();
    }
}
