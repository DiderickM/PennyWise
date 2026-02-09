/**
 * Admin class extends User with system administration capabilities.
 * Demonstrates INHERITANCE and POLYMORPHISM.
 * 
 * Administrators have elevated permissions to view and modify all user accounts.
 */
public class Admin extends User {
    // ENCAPSULATION: Private field for admin privileges
    private String adminLevel; // "BASIC" or "SUPER"

    /**
     * Constructor to initialize an Admin.
     * 
     * @param userId Unique user identifier
     * @param username User's login username
     * @param password User's login password
     * @param email User's email address
     * @param adminLevel Admin privilege level
     */
    public Admin(String userId, String username, String password, String email, String adminLevel) {
        super(userId, username, password, email);
        this.adminLevel = adminLevel;
    }

    // ENCAPSULATION: Getter and Setter for admin level
    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    /**
     * POLYMORPHISM IMPLEMENTATION: Admin displays the admin dashboard.
     * This overrides the abstract displayDashboard() method from User.
     */
    @Override
    public void displayDashboard() {
        System.out.println("\n========== Admin Dashboard ==========");
        System.out.println("Welcome, Admin " + getUsername() + "!");
        System.out.println("Admin Level: " + adminLevel);
        System.out.println("Options: View all users, Modify accounts, Generate reports");
        System.out.println("=====================================");
    }

    /**
     * VALUE RETURNING METHOD: Returns whether this admin has super privileges.
     */
    public boolean hasSuperPrivileges() {
        return adminLevel.equals("SUPER");
    }

    /**
     * VOID METHOD: Displays admin capabilities and notable actions.
     */
    public void displayAdminCapabilities() {
        System.out.println("\n--- Admin Capabilities ---");
        System.out.println("1. View all user accounts");
        System.out.println("{NOT IMPLEMENTED} 2. Modify user information");
        System.out.println("{NOT IMPLEMENTED} 3. Adjust account balances (for corrections)");
        System.out.println("{NOT IMPLEMENTED} 4. Delete user accounts");
        if (hasSuperPrivileges()) {
            System.out.println("{NOT IMPLEMENTED} 5. [SUPER] Generate system-wide reports");
            System.out.println("{NOT IMPLEMENTED} 6. [SUPER] Manage other administrators");
        }
        System.out.println("-------------------------");
    }
}
