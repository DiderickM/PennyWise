# PennyWise Financial Manager - Comprehensive Project Documentation
# ⚠️ <span style="color: blue">*This ReadMe is automatically generated based on the code through Visual Studio Code*</span>.⚠️

## Group Project for Erasmus University: Master in Business Information Management

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Features](#features)
3. [System Architecture](#system-architecture)
4. [Class Quick Reference](#class-quick-reference)
5. [Design Patterns & Architectural Concepts](#design-patterns--architectural-concepts)
6. [Technical Concepts Demonstrated](#technical-concepts-demonstrated)
7. [Detailed Class Structure](#detailed-class-structure)
8. [How to Compile and Run](#how-to-compile-and-run)
9. [User Guide](#user-guide)
10. [Admin Guide](#admin-guide)
11. [Data Persistence](#data-persistence)
12. [Implementation Summary](#implementation-summary)
13. [Project Scope and Requirements](#project-scope-and-requirements)
14. [Future Enhancements](#future-enhancements)
15. [Conclusion](#conclusion)
12. [Future Enhancements](#future-enhancements)

---

## Project Overview

**PennyWise Financial Manager** is a comprehensive Java-based financial management system designed to demonstrate advanced object-oriented programming principles and software engineering best practices. The system handles user account management, financial transactions, and administrative oversight in a secure and scalable manner.

### Why Financial Software?

We selected the Financial Software theme because it provides an ideal framework to demonstrate all required technical elements through practical, real-world application scenarios:

- **Inheritance & Polymorphism:** Banking naturally lends itself to different account types with shared behavior, making it perfect for demonstrating class hierarchies
- **Encapsulation:** Financial data protection requires restricting access to private variables, showcasing proper data hiding
- **Arrays & Collections:** Multiple users and transactions can be stored and managed efficiently
- **Control Flow:** Complex business logic requires extensive use of selections (switch statements) and loops
- **Methods:** Extensive use of both value-returning and void methods for different operations
- **User Interaction:** The Scanner class is used throughout for seamless user input handling

### Project Goals

The PennyWise system aims to:
- Provide regular users with secure account management and transaction capabilities
- Enable administrators to oversee the entire system with comprehensive reporting
- Demonstrate enterprise-level software design patterns
- Showcase proper code organization, documentation, and best practices
- Provide a scalable foundation for additional financial features
- Follow clean architecture principles with separation of concerns

---

## Features

### Regular User Features

#### Account Management
- **User Registration:** Create new accounts with unique user IDs, usernames, and email addresses
- **Multiple Accounts:** Each user can hold multiple accounts simultaneously (savings, checking, or a combination)
- **Account Creation:** Add new accounts at any time during or after registration
- **Account Selection:** Choose between Savings Account (with interest) or Checking Account (with overdraft protection)
- **Account Switching:** Easily switch between multiple accounts to perform different operations
- **Secure Login:** Multi-factor identification using username and password credential verification
- **Profile Management:** View and edit personal user information including email and password, with access to all accounts

#### Account Operations
- **Balance Checking:** Real-time account balance display formatted to two decimal places
- **Deposits:** Add funds to accounts with validation for positive amounts
- **Withdrawals:** Remove funds with account-type-specific rules:
  - Savings accounts: Limited to 3 withdrawals per month
  - Checking accounts: Withdraw up to balance + overdraft limit
- **Transaction History:** Complete audit trail of all account operations with dates and amounts
- **Money Transfers:** Transfer funds between user's own accounts
  - Select source and target accounts
  - Specify transfer amount with validation
  - Automatic transaction recording (TRANSFER OUT / TRANSFER IN)
  - Real-time balance updates on both accounts

#### Account-Specific Features

**Savings Account Features:**
- **Interest Earning:** Automatic monthly interest calculation and application (configurable rate, default 3% annually)
- **Balance Projection:** Calculate projected balance after 12 months with compound interest
- **Withdrawal Restrictions:** Enforce monthly withdrawal limits to encourage saving
- **Account Information:** View interest rate, transaction count, and projected future balance

**Checking Account Features:**
- **Overdraft Protection:** Access funds beyond account balance up to overdraft limit
- **Overdraft Fees:** Automatic fee assessment when account goes negative
- **Overdraft Status:** Real-time display of overdraft status (normal vs. overdraft)
- **Available Funds Display:** Show total available funds including overdraft limit
- **Overdraft History:** Detailed history of withdrawal transactions and overdraft usage patterns
- **Detailed Account Information:** View overdraft limit, overdraft fee, and available funds

### Administrative Features

#### User Management
- **View All Users:** Display comprehensive list of all registered users with IDs
- **User Information Modification:** Edit username, email address, and password for any user account
- **Account Balance Adjustment:** Correct balance errors or apply manual adjustments
- **User Deletion:** Remove accounts from the system with confirmation
- **User Search:** Locate specific users by username for management operations

#### System Operations
- **Account Feature Application:** Trigger interest calculations for all savings accounts at once
- **Overdraft Fee Processing:** Apply overdraft fees to all checking accounts system-wide
- **System-wide Reporting:** Generate comprehensive financial reports (Super Admin only)
- **Administrator Management:** Create and manage other administrators with privilege levels (Super Admin only)

#### Super Admin Exclusive Features
- **Advanced Reporting:** Generate detailed system-wide financial reports including:
  - Total system balance across all accounts
  - Account type distribution (savings vs. checking)
  - Total transaction count
  - Average balance per user
  - User-specific account details
- **Batch Operations:** Apply account features to all accounts of a specific type simultaneously
- **Administrator Management:** Create and modify other admin accounts with different privilege levels

---

## System Architecture

### High-Level Design Diagram

The system follows a hierarchical architecture with data persistence layers:

```
                              App (Main Driver)
                                    |
                  __________________|
                 |                  |
            DataPersistence    User (Abstract)
           (Abstract Base)      /           \
             /         \   RegularUser   Admin (extends User)
        DataLoader   DataStorage |
          (Load)      (Save)     | 
                                 |
                           Account (Abstract)
                             /              \
                  SavingsAccount    CheckingAccount
                           
                                Transaction
                             (Value Object)
```

**Presentation & UI Layer:**
- `UserInterface`: Handles all user interaction and menu displays
- `InputValidator`: Centralized input validation and parsing

**Business Logic Layer:**
- `UserManager`: Manages user storage, authentication, and CRUD operations
- `AccountManager`: Handles account creation, selection, and operations
- `PasswordUtil`: Secure password hashing using SHA-256 with salt

**Domain Model Layer:**
- `User`: Abstract base with common user properties
- `RegularUser`: Extends User, manages personal accounts
- `Admin`: Extends User, basic system administration
- `SuperAdmin`: Extends Admin, elevated privileges (reports, configuration)

**Account Hierarchy:**
- `Account`: Abstract base with core banking operations
- `SavingsAccount`: Earns interest, withdrawal limits
- `CheckingAccount`: Overdraft protection, fees

**Configuration & Utilities:**
- `AppConstants`: Centralized constants and magic number elimination
- `SystemConfiguration`: System-wide settings (Singleton pattern)
- `Transaction`: Immutable value object for audit trail

**Persistence Layer:**
- `DataPersistence`: Abstract base class with template method pattern
- `DataLoader`: Loads data from files on startup
- `DataStorage`: Saves data to files during operation
- `DataConfiguration`: Configuration for data file paths

**Other Components:**
- `Transaction`: Immutable value object for audit trail

### Data Flow Architecture

```
                    ┌──────────────────────────────┐
                    │       File System            │
                    │  data/users.txt              │
                    │  data/accounts.txt           │
                    │  data/transactions.txt       │
                    └──────────┬───────────────────┘
                              ↑ / ↓
                    ┌──────────────────────────────┐
                    │   DataPersistence (Abstract) │
                    │    (Template Method)         │
                    ├──────────────────────────────┤
                    │    DataLoader │ DataStorage  │
                    │    (Load)     │  (Save)      │
                    └──────┬────────────────┬──────┘
                          ↑ / ↓            ↑ / ↓
                    
                    ┌─────────────────────────────────┐
                    │         App (Main)              │
                    │   Entry Point & Session Manager │
                    └─────────────────────────────────┘
                           ↓                 ↓
                    ┌──────────────┐    ┌──────────────┐
                    │ Regular      │    │ Admin        │
                    │ User Mode    │    │ Mode         │
                    └──────────────┘    └──────────────┘
                           ↓                   ↓
                    ┌──────────────┐    ┌──────────────────┐
                    │ RegularUser  │    │ Admin            │
                    │ Object       │    │ Operations       │
                    └──────────────┘    └──────────────────┘
                           ↓
                    ┌─────────────────────────┐
                    │   Account Object        │
                    │ (Savings/Checking)      │
                    └─────────────────────────┘
                           ↓
                    ┌─────────────────────────┐
                    │ Transactions Array      │
                    │    [100 slots]          │
                    └─────────────────────────┘

Data Flow:
1. Startup: File System → DataLoader → App.users[]
2. Operation: App.users[] ↔ Account ↔ Transaction[]
3. Shutdown: App.users[] → DataStorage → File System
```

### State Management

The system maintains application state through:

- **User Array:** Static array of up to 100 registered users
- **User Count:** Tracker for number of registered users
- **Account Instances:** Each user holds reference to their account object
- **Transaction Arrays:** Each account maintains array of up to 100 transactions
- **Session Management:** Scanner-based input handling throughout application lifecycle

---

## Technical Concepts Demonstrated

### 1. **SELECTIONS (if-else, switch statements)**

The project extensively demonstrates conditional logic:

**Switch Statements (Rule Switch - Modern Java 14+):**
```
- Main menu navigation (1-3 options)
- User mode selection (1-2 options)
- Account type selection (1-2 account types)
- Account menu operations (up to 8 options for checking accounts)
- Admin menu operations (up to 9 options)
- Account balance adjustment options (1-4 options)
- Nested conditional logic in deposit/withdrawal validation
```

**If-Else Statements:**
- Account type checking (instanceof statements for polymorphic behavior)
- Balance validation before withdrawal
- Transaction array capacity checking
- Monthly withdrawal limit enforcement
- Overdraft status verification
- Admin privilege level checking

**Ternary Operators:**
- Dynamic menu option display based on account type
- Available funds calculation for checking accounts
- Interest validation in savings accounts

### 2. **LOOPS (while, for loops)**

Comprehensive loop implementations:

**While Loops:**
- Main application loop (until user exits)
- Regular user session loop (until logout)
- Admin session loop (until logout)
- Account menu loop (until logout)
- User registration/login loop

**For Loops:**
- Iterating through user array to find specific users
- Iterating through transaction arrays to display history
- Counting withdrawals in current month (savings accounts)
- Calculating projections over multiple months (savings accounts)
- Processing all users for batch operations (interest/fee application)
- Searching and filtering operations

**Loop Control:**
- Break statements for early exit from search loops
- Continue statements for conditional iteration
- Counter variables for array position tracking

### 3. **METHODS (value returning and void)**

Over 40 methods demonstrating different patterns:

**Value-Returning Methods:**
- `getBalance()` - Returns numeric value
- `checkBalance()` - Returns current balance
- `withdraw(double)` - Returns boolean success status
- `deposit(double)` - Returns boolean success status
- `getTransaction(int)` - Returns Transaction object
- `countWithdrawalsThisMonth()` - Returns integer count
- `projectFutureBalance(int)` - Returns projected balance
- `getUserCount()` - Returns count of users
- `getRegularUserByUsername(String)` - Returns User object
- `hasSuperPrivileges()` - Returns boolean permission status
- `isInOverdraft()` - Returns overdraft status
- `getAvailableFunds()` - Returns available balance

**Void Methods:**
- `displayWelcome()` - Console output
- `displayMainMenu()` - Console output
- `regularUserMode(Scanner)` - Flow control
- `userAccountMenu(Scanner, RegularUser)` - Interactive menu
- `recordTransaction(...)` - Data recording
- `displayTransactionHistory()` - Console output
- `displayDashboard()` - Polymorphic display
- `runAdminSession(Scanner)` - Interactive admin session
- `displayAllUsers()` - Console output
- `applyAccountFeaturesToAllSavings()` - Batch processing
- `applyAccountFeaturesToAllChecking()` - Batch processing

**Method Overriding (Polymorphism):**
- `displayDashboard()` - Different implementations in RegularUser and Admin
- `withdraw()` - Different implementations in Account, SavingsAccount, CheckingAccount
- `applyAccountFeatures()` - Different implementations in SavingsAccount and CheckingAccount

### 4. **ARRAYS (single-dimensional)**

Multiple array implementations:

**User Array:**
- Type: `User[]` with capacity of 100
- Purpose: Store all registered users (regular and admin)
- Implementation: Static array in App class
- Access: Through controlled accessor methods

**Transaction Arrays:**
- Type: `Transaction[]` with capacity of 100 per account
- Purpose: Store transaction history for each account
- Implementation: Instance variable in Account class
- Access: Through protected recordTransaction method and public getters

**Array Operations Demonstrated:**
- Array initialization and capacity management
- Element assignment and retrieval
- Index tracking with counter variables
- Bounds checking before adding elements
- Linear search through arrays
- Filtering and counting operations

### 5. **INHERITANCE and POLYMORPHISM**

Extensive object-oriented hierarchy:

**Inheritance Hierarchy:**

```
User (Abstract)
├── RegularUser
│   └── Has-a: Account
│       ├── SavingsAccount
│       └── CheckingAccount
└── Admin

Account (Abstract)
├── SavingsAccount
└── CheckingAccount
```

**Polymorphic Methods:**
- `displayDashboard()` - Different implementations for RegularUser and Admin
- `withdraw()` - Different implementations for Account, SavingsAccount, and CheckingAccount
- `applyAccountFeatures()` - Interest for Savings, fees for Checking
- `displayAccountInfo()` - Extended by subclasses

**Method Overriding:**
- SavingsAccount overrides `withdraw()` to enforce monthly limits
- CheckingAccount overrides `withdraw()` to allow overdraft
- Both override `applyAccountFeatures()` for account-specific logic

**Interface-like Contracts:**
- Abstract methods in Account force implementation in subclasses
- Abstract methods in User force implementation in subclasses

### 6. **ENCAPSULATION (private fields, getters/setters)**

Comprehensive data protection:

**User Class Encapsulation:**
- Private: userId, username, password, email
- Public getters/setters for controlled access

**Account Class Encapsulation:**
- Private: accountNumber, balance, transactions (array), transactionCount
- Protected: getCurrentDate() and recordTransaction() for subclass access
- Public getters for safe display operations

**RegularUser Encapsulation:**
- Private: account
- Public getter/setter for account management

**Admin Class Encapsulation:**
- Private: adminLevel
- Public getter/setter for privilege level

**SavingsAccount Encapsulation:**
- Private: interestRate
- Public getter/setter with validation

**CheckingAccount Encapsulation:**
- Private: overdraftLimit, overdraftFee
- Public getters/setters for configuration

**Transaction Class Encapsulation:**
- Private: amount, type, date
- Public getters only (immutable after creation)

### 7. **USER INTERACTION (Scanner for System.in)**

User input handling throughout:

**Input Scenarios:**
- Main menu selection (number input with exception handling)
- User registration (string inputs for ID, username, password, email)
- Account selection (type selection 1-2 for Savings/Checking)
- Login credentials verification
- Deposit amount entry with NumberFormatException handling
- Withdrawal amount entry with amount validation
- Transfer destination and amount selection
- Admin operations with various numeric and string inputs
- Menu navigation throughout sessions
- Balance adjustment amounts
- User information modifications
- Confirmation inputs (yes/no, DELETE, YES for deletions)

**Input Validation:**
- NumberFormatException catching for invalid numeric input
- Range validation for menu selections (1-3, 1-2, etc.)
- Positive amount validation for transactions
- Email format consideration
- Password strength handling (simulated)
- String matching for confirmation prompts

**Error Handling:**
- Try-catch blocks for numeric input validation
- Null pointer checking for user retrieval
- Array bounds checking before access
- Transaction amount validation
- Insufficient funds validation
- Withdrawal limit enforcement (savings accounts)
- Overdraft limit enforcement (checking accounts)

**User Interaction Flow:**
1. Scanner initialization in main
2. Menu display with numbered options
3. Input retrieval via scanner.nextLine() or scanner.next()
4. Input parsing and validation
5. Error messages for invalid input
6. Confirmation prompts for destructive operations
7. Session logging

---

## Class Quick Reference

### Core Application Classes

| Class | Purpose | Key Feature |
|-------|---------|-------------|
| `App.java` | Main driver, entry point | Orchestrates application flow and delegates to managers |
| `UserInterface.java` | Presentation & UI layer | Separates UI concerns from business logic; displays all menus and prompts using clean architecture principles |
| `UserManager.java` | User lifecycle management | Encapsulates user storage (static array) and handles user authentication, lookup, registration, and deletion |
| `AccountManager.java` | Account lifecycle management | Abstracts account creation (based on type selection), account selection from user's portfolio, and unique account number generation |
| `InputValidator.java` | Input validation | Centralized parsing and validation logic |

### Security & Configuration

| Class | Purpose | Key Feature |
|-------|---------|-------------|
| `PasswordUtil.java` | Secure password management | SHA-256 hashing with unique random salt per password; prevents rainbow table and dictionary attacks |
| `SystemConfiguration.java` | System settings | Singleton pattern for configurable defaults |
| `AppConstants.java` | Constants | Eliminates magic numbers, centralized values |
| `DataConfiguration.java` | Data paths | Configuration for persistence file locations |

### Domain Model - Users

| Class | Purpose | Key Feature |
|-------|---------|-------------|
| `User.java` | Abstract base user | Defines displayDashboard() contract |
| `RegularUser.java` | Regular account holder | Manages multiple accounts |
| `Admin.java` | Basic administrator | User and account management capabilities |
| `SuperAdmin.java` | Elevated administrator | Extends Admin with reports and system config |

### Domain Model - Accounts

| Class | Purpose | Key Feature |
|-------|---------|-------------|
| `Account.java` | Abstract account base | Core banking operations |
| `SavingsAccount.java` | Savings account | 3% interest, 3 withdrawals/month |
| `CheckingAccount.java` | Checking account | Overdraft protection, fees |
| `Transaction.java` | Immutable transaction object | Audit trail for accounts |

### Data Persistence

| Class | Purpose | Key Feature |
|-------|---------|-------------|
| `DataPersistence.java` | Abstract persistence base | Template Method Pattern |
| `DataStorage.java` | Saves to files | Persists users/accounts/transactions |
| `DataLoader.java` | Loads from files | Reconstructs application state |

---



## Design Patterns & Architectural Concepts

### 1. **Template Method Pattern (DataPersistence)**

**Location:** `DataPersistence.java` with implementations in `DataLoader.java` and `DataStorage.java`

**Purpose:** Defines the skeleton of persistence algorithm while letting subclasses fill in specific steps

**Structure:**
```
DataPersistence (Abstract)
├── execute() [TEMPLATE METHOD]
│   1. validatePreconditions() [ABSTRACT]
│   2. prepareOperation() [HOOK]
│   3. performOperation() [ABSTRACT]
│   4. cleanupOperation() [HOOK]
│   5. handleError()
├── DataLoader [CONCRETE]
│   └── performOperation() → Load from files
└── DataStorage [CONCRETE]
    └── performOperation() → Save to files
```

**Benefits:**
- Code reuse between DataLoader and DataStorage
- Consistent algorithm structure
- Easy to add new persistence strategies
- Ensures both classes use same file paths

### 2. **Inheritance Hierarchy (User Base Classes)**

**Pattern:** Polymorphic hierarchy with abstract base class

```
User (Abstract)
├── RegularUser
│   └── Can own multiple accounts
│       ├── SavingsAccount (parent: Account)
│       └── CheckingAccount (parent: Account)
└── Admin
    └── System management privileges
        ├── BASIC level
        └── SUPER level
```

**Polymorphic Methods:**
- `displayDashboard()` - Different for each user type
- `getUsername()`, `setPassword()` - Inherited common behavior

### 3. **Inheritance Hierarchy (Account Base Classes)**

**Pattern:** Polymorphic hierarchy with specialized behavior

```
Account (Abstract)
├── SavingsAccount
│   ├── Overrides: withdraw() [add withdrawal limit]
│   ├── Overrides: applyAccountFeatures() [apply interest]
│   └── Special: projectFutureBalance()
└── CheckingAccount
    ├── Overrides: withdraw() [allow overdraft]
    ├── Overrides: applyAccountFeatures() [apply fees]
    └── Special: isInOverdraft(), getAvailableFunds()
```

**Polymorphic Methods:**
- `withdraw()` - Different rules per account type
- `applyAccountFeatures()` - Interest vs. fees
- `deposit()` - Same implementation (inherited)

### 4. **Value Object Pattern (Transaction)**

**Location:** `Transaction.java`

**Characteristics:**
- Immutable after creation (no setters)
- Encapsulates transaction data
- Equality based on values
- Used in collections (transaction arrays)

**Benefits:**
- Thread-safe (immutable)
- Simple to use and understand
- Perfect for audit trails

### 5. **Encapsulation with Controlled Access**

**Pattern:** Private fields with public getters/setters

```java
// Example: Account encapsulation
private String accountNumber;  // Private
private double balance;        // Private
public double getBalance() {}  // Controlled access
public void setBalance(double b) {} // Validation possible
```

**Protected Access for Subclasses:**
```java
protected void recordTransaction() {}  // Available to SavingsAccount, CheckingAccount
protected Date getCurrentDate() {}     // Available to subclasses
```

---

## Detailed Class Structure

### User.java (Abstract)

**Purpose:** Base class for all system users, establishing common interface and shared properties

**Key Methods:**
- `displayDashboard()` - Abstract method implemented by subclasses
- `displayProfile()` - Shows user information

**Encapsulation:**
- Private: userId, username, password, email
- Public: getters and setters

**Polymorphism:**
- Abstract method enforces different dashboard implementations

---

### RegularUser.java

**Purpose:** Represents standard financial account holders with transaction capabilities

**Parent Class:** Extends User

**Key Constructors:**
- `RegularUser(userId, username, password, email)` - User without initial accounts
- `RegularUser(userId, username, password, email, account)` - User with initial account

**Key Methods:**
- `displayDashboard()` - Shows user dashboard with all account info
- `getBalance()` - Returns balance of first account (deprecated)
- `getAccountByIndex(int)` - Returns account at specified index
- `getAccount()` - Returns first account (backward compatibility)
- `setAccount(Account)` - Sets first account (backward compatibility)
- `viewTransactionHistory()` - Displays all transactions from account
- `addAccount(Account)` - Adds new account to user's account list
- `getAccounts()` - Returns array of all user accounts

**Features:**
- Multiple account support (array of accounts)
- View all account details on dashboard
- Access individual accounts by index
- Transaction history viewing
- Account management during session

**Relationships:**
- Extends User (inherits userId, username, password, email)
- Composition with Account (has-a relationship)
- Can have multiple Account instances (SavingsAccount or CheckingAccount)

**Auto-Save Integration:**
- Registration triggers save (via App.registerRegularUser)
- Each account operation triggers save (deposit, withdraw, transfer)
- Account creation triggers save

---

### Admin.java

**Purpose:** Provides basic system administration capabilities with user and account management

**Parent Class:** Extends User

**Architecture Note:** In the new architecture, Admin is a basic administrator class with core management functions. SuperAdmin extends Admin with elevated privileges.

**Key Methods:**
- `displayDashboard()` - Shows admin dashboard (POLYMORPHIC override)
- `launchAdmin()` - Static method for admin authentication and session initialization
- `runAdminSession()` - Interactive admin menu (basic 5-option menu)
- `displayAdminCapabilities()` - Lists available operations (POLYMORPHIC, overridden in SuperAdmin)

**Basic Admin Operation Methods:**
- `displayAllUsers()` - List all users with IDs and usernames
- `modifyUserInformation()` - Edit user details (username, email, password with hashing)
- `adjustAccountBalance()` - Correct balances (add, subtract, set exact amount)
- `deleteUserAccount()` - Remove users from system with confirmation

**Key Features:**
- Secure authentication using PasswordUtil for password verification
- All password changes use SHA-256 hashing
- Auto-save integration on all user modifications
- Comprehensive user management capabilities
- Read-only access to transaction history

**Admin Session Menu:**
```
1. View All Users
2. Modify User Information
3. Adjust Account Balance
4. Delete User Account
5. Logout
```

**Authentication:**
- Uses hashed password storage
- Verifies credentials using PasswordUtil.verifyPassword()
- Demo credentials: username "admin", password "admin123"

**Auto-Save Integration:**
Each admin operation automatically triggers data persistence:
- After modifying user information
- After adjusting account balance
- After deleting user accounts

---

### SuperAdmin.java

**Purpose:** Extends Admin with elevated privileges for system-wide operations and configuration

**Parent Class:** Extends Admin

**Type:** Specialized administrator with full system access

**Key Inheritance:**
- Inherits all Admin methods (displayAllUsers, modifyUserInformation, etc.)
- Overrides `displayDashboard()` - Shows "Super Admin Dashboard" with enhanced messaging
- Overrides `displayAdminCapabilities()` - Shows all 10 capabilities
- Overrides `runAdminSession()` - Provides extended 11-option menu

**Exclusive SuperAdmin Methods:**
- `generateSystemReport()` - Comprehensive system-wide financial analytics
- `applyAccountFeaturesToAllSavings()` - Batch interest application to all savings accounts
- `applyAccountFeaturesToAllChecking()` - Batch overdraft fee application to all checking accounts
- `manageAdmins()` - Administrator management (simulated demo)
- `manageSystemConfiguration()` - Modify system defaults (interest rates, overdraft limits, fees)
- `deleteAllStoredData()` - Nuclear option to reset entire system (requires double confirmation)

**SuperAdmin Session Menu:**
```
1. View All Users
2. Modify User Information
3. Adjust Account Balance
4. Delete User Account
5. Generate System-wide Report
6. Apply Account Features (Interest) to All Savings Accounts
7. Apply Account Features (Overdraft Fees) to All Checking Accounts
8. Manage Administrators (Simulated)
9. Manage System Configuration
10. [DANGER] Delete All Stored Data
11. Logout
```

**System-wide Report Includes:**
- Total number of users
- Total number of accounts (savings and checking breakdown)
- Total balance across all accounts
- Average balance per user
- Total transaction count
- Per-user account details with transaction counts

**Configuration Management:**
SuperAdmin can modify system-wide defaults through SystemConfiguration:
- Default savings interest rate
- Default checking overdraft limit
- Default checking overdraft fee
- Default maximum withdrawals per month for savings accounts

**Batch Operations:**
- Interest Application: Iterates through all users, applies monthly interest to savings accounts
- Fee Application: Assesses overdraft fees to checking accounts with negative balance
- Both operations provide detailed per-account feedback and summary statistics

**Security Features:**
- Double confirmation for destructive operations (DELETE ALL DATA)
- All password modifications use secure hashing
- Comprehensive audit trail through transaction history

**⚠️ Implementation Note:**
The `manageAdmins()` method currently has simulated behavior for demonstration purposes:
- Displays demo admin information
- Allows creation of new admins with validation
- **Limitation:** Newly created admins are not persisted across sessions

**Demo Credentials:**
```
Username: admin
Password: admin123
Level: SUPER
Email: admin@pennywise.com
```

---### Account.java (Abstract)

**Purpose:** Base class for all account types with core banking operations

**Parent Class:** Java Object

**Key Properties:**
- accountNumber: Unique account identifier (e.g., SA-user001-1)
- balance: Current account balance
- accountType: Type designation (SAVINGS/CHECKING)
- transactions: Array of Transaction objects (capacity 100)
- transactionCount: Current number of transactions

**Key Constructors:**
- `Account(accountNumber, initialBalance, accountType)` - Creates account with initial balance

**Key Methods:**
- `deposit(double)` - Add funds to account (value-returning boolean)
- `withdraw(double)` - Remove funds with subclass-specific rules (value-returning boolean)
- `checkBalance()` - Return current balance (value-returning double)
- `recordTransaction(amount, type, date)` - Create audit trail entry (protected)
- `displayTransactionHistory()` - Full transaction display (void)
- `applyAccountFeatures()` - Abstract method for account-specific operations
- `displayAccountInfo()` - Account overview (void)
- `getTransaction(int)` - Retrieve transaction by index
- `getCurrentDate()` - Get formatted current date

**Transaction Recording:**
- Every transaction (deposit, withdrawal, transfer, initial) is recorded
- Each transaction stores: amount, type (DEPOSIT/WITHDRAWAL/TRANSFER), date
- Maximum 100 transactions per account
- Transactions array is protected for subclass access
- Transactions are persisted to `data/transactions.txt`

**ENCAPSULATION:**
- Private: accountNumber, balance, transactions, transactionCount
- Protected: recordTransaction(), getCurrentDate() for subclass access
- Public: getters/setters for controlled access

**POLYMORPHISM:**
- `withdraw()` - Overridden by SavingsAccount and CheckingAccount
- `applyAccountFeatures()` - Abstract, implemented by subclasses
- `displayAccountInfo()` - Can be overridden for account-type-specific display

---

### SavingsAccount.java

**Purpose:** Specialized account type emphasizing wealth accumulation with interest benefits

**Parent Class:** Extends Account

**Key Properties:**
- interestRate: Annual interest rate as decimal (e.g., 0.03 for 3%)
- Inherited: accountNumber, balance, transactions, transactionCount

**Key Constructors:**
- `SavingsAccount(accountNumber, initialBalance, interestRate)`

**Key Methods:**
- `applyAccountFeatures()` - Apply monthly interest to balance (POLYMORPHIC)
- `withdraw(double)` - Enforce monthly withdrawal limit before allowing (POLYMORPHIC)
- `countWithdrawalsThisMonth()` - Count current month's withdrawals (value-returning int)
- `projectFutureBalance(int months)` - Project balance after N months with compound interest
- `displaySavingsInfo()` - Show account details including interest rate and projection
- `getInterestRate()` - Get annual interest rate
- `setInterestRate(double)` - Set annual interest rate

**Key Features:**
- **Interest Calculation:** Automatic monthly interest application (annual rate / 12)
  - Applied when admin runs "Apply Account Features to All Savings"
  - Compounds monthly: Interest = Balance × (annualRate / 12)
  - Interest is added as a DEPOSIT transaction
  
- **Withdrawal Limits:** Maximum 3 withdrawals per calendar month
  - Resets on month boundary
  - Attempts beyond limit are rejected with message
  - Encourages saving behavior
  
- **Interest Projection:** Calculate 12-month projected balance
  - Shows compound interest effects
  - Useful for financial planning
  - Displayed on dashboard

- **Default Interest Rate:** 3% annual (0.03)

**POLYMORPHIC BEHAVIOR:**
- Overrides `withdraw()` to add monthly withdrawal limit check
- Overrides `applyAccountFeatures()` to calculate and apply interest
- Different withdrawal rules than parent class
- Different feature application than checking accounts

**Auto-Save Integration:**
- Admin interest application triggers save
- Withdrawal attempts (successful or not) trigger saves
- Monthly withdrawal limit is checked each time

---

### CheckingAccount.java

**Purpose:** High-frequency transaction account with overdraft protection

**Parent Class:** Extends Account

**Key Properties:**
- overdraftLimit: Maximum negative balance allowed (default $500)
- overdraftFee: Fee charged when overdraft is used (default $35)
- Inherited: accountNumber, balance, transactions, transactionCount

**Key Constructors:**
- `CheckingAccount(accountNumber, initialBalance, overdraftLimit, overdraftFee)`

**Key Methods:**
- `applyAccountFeatures()` - Apply overdraft fees if account negative (POLYMORPHIC)
- `withdraw(double)` - Allow overdraft protection up to limit (POLYMORPHIC)
- `isInOverdraft()` - Check if account is in negative balance (value-returning boolean)
- `getAvailableFunds()` - Calculate total available (balance + limit) (value-returning double)
- `displayCheckingInfo()` - Show overdraft details and status
- `displayOverdraftHistory()` - Show withdrawal transaction history
- `getOverdraftLimit()` - Get overdraft limit
- `setOverdraftLimit(double)` - Set overdraft limit
- `getOverdraftFee()` - Get overdraft fee amount
- `setOverdraftFee(double)` - Set overdraft fee amount

**Key Features:**
- **Overdraft Protection:** Borrow up to overdraft limit when balance negative
  - Available Funds = Balance + Overdraft Limit
  - Example: -$100 balance + $500 limit = $400 available
  - No denial of transactions up to limit
  - Flexibility for immediate expenses
  
- **Overdraft Fees:** Assessment when account goes negative
  - Triggered when admin runs "Apply Account Features to All Checking"
  - Fee ($35 default) is deducted from balance
  - Applied as WITHDRAWAL transaction
  - Multiple overdraft uses accumulate fees
  
- **Available Funds Indicator:**
  - Shows total drawable balance
  - Calculation: Current Balance + Overdraft Limit
  - Updated in real-time on account details
  - Helps users understand spending capacity

- **Overdraft Status Display:**
  - NORMAL: Balance >= $0
  - OVERDRAFT: Balance < $0
  - Displayed on dashboard and in account info

- **Default Values:**
  - Overdraft Limit: $500
  - Overdraft Fee: $35

**POLYMORPHIC BEHAVIOR:**
- Overrides `withdraw()` to allow overdraft up to limit
- Overrides `applyAccountFeatures()` to apply overdraft fees
- Different withdrawal rules (allows negative up to limit)
- Different feature application (fees vs. interest)

---

### Transaction.java

**Purpose:** Immutable value object representing individual financial transactions

**Key Properties:**
- amount: Transaction amount
- type: DEPOSIT or WITHDRAWAL
- date: Transaction date in MM/dd/yyyy format

**Key Methods:**
- `getTransactionDetails()` - Return formatted transaction string
- `displayTransaction()` - Output transaction to console
- Getters for amount, type, date

**Design Pattern:**
- Immutable after creation (no setters)
- Value object pattern
- Used by Account for transaction history

---

### DataPersistence.java (Abstract Base)

**Purpose:** Abstract base class that defines the template method pattern for all data persistence operations

**Design Pattern:** Template Method Pattern
- Defines the algorithm structure for persistence operations
- Ensures DataLoader and DataStorage follow consistent steps
- Demonstrates POLYMORPHISM through abstract methods

**File Locations (Shared Constants):**
```
DATA_DIR = "data"
USERS_FILE = "data/users.txt"
ACCOUNTS_FILE = "data/accounts.txt"
TRANSACTIONS_FILE = "data/transactions.txt"
```

**Key Methods:**
- `execute()` - Template method that orchestrates persistence operation
  1. Validate preconditions
  2. Prepare operation (ensure data directory exists)
  3. Perform actual operation (implemented by subclasses)
  4. Cleanup after operation
  5. Handle any errors

**Abstract Methods (Implemented by Subclasses):**
- `validatePreconditions()` - Checks if operation can proceed
- `performOperation()` - Actual persistence logic (DataLoader loads, DataStorage saves)
- `getOperationName()` - Returns descriptive operation name

**Hook Methods:**
- `prepareOperation()` - Ensures data directory exists
- `cleanupOperation(boolean)` - Post-operation cleanup (optional override)
- `handleError(Exception)` - Error handling

**Key Feature:** Ensures both DataLoader and DataStorage use identical file paths and stay synchronized

---

### DataStorage.java

**Purpose:** Handles saving all application data to persistent storage in text files

**Location:** `PennyWise/src/DataStorage.java` (202 lines)

**Parent Class:** Extends DataPersistence

**Key Responsibilities:**
- Saves users (regular and admin) to `data/users.txt`
- Saves accounts (savings and checking) to `data/accounts.txt`
- Saves transaction history to `data/transactions.txt`
- Auto-creates `data/` directory if needed
- Implements abstract methods from DataPersistence

**Key Methods:**
- `saveAllData()` - Main entry point (calls execute())
- `saveUsers()` - Saves all users in pipe-delimited format
- `saveAccounts()` - Saves all accounts with parameters
- `saveTransactions()` - Saves all transactions with details
- `deleteAllData()` - Deletes all stored data files (admin only)
- `dataExists()` - Checks if saved data exists

**POLYMORPHIC IMPLEMENTATIONS:**
- `validatePreconditions()` - Returns true (can save even with 0 users)
- `performOperation()` - Saves users, accounts, and transactions
- `getOperationName()` - Returns "Data Save"

**File Format Details:**

Users:
```
REGULAR|userId|username|password|email
ADMIN|userId|username|password|email|adminLevel
```

Accounts:
```
userId|SAVINGS|accountNumber|balance|interestRate
userId|CHECKING|accountNumber|balance|overdraftLimit|overdraftFee
```

Transactions:
```
accountNumber|amount|type|date
```

**Usage Example:**
```java
DataStorage.saveAllData();  // Saves all current data to files
DataStorage.deleteAllData(); // Deletes all stored files (admin only)
```

---

### DataLoader.java

**Purpose:** Handles loading all application data from files on program startup

**Location:** `PennyWise/src/DataLoader.java` (280 lines)

**Parent Class:** Extends DataPersistence

**Key Responsibilities:**
- Reads and parses data files on startup
- Reconstructs User objects (RegularUser and Admin)
- Rebuilds Account objects (SavingsAccount, CheckingAccount)
- Restores complete transaction history
- Maintains relationships between users, accounts, and transactions
- Uses temporary Maps to organize data during loading

**Helper Classes:**
- `AccountData` - Stores account information during loading process
- `TransactionData` - Stores transaction information during loading process

**Key Methods:**
- `loadAllData()` - Main entry point (calls execute())
- `loadUsers()` - Reads and parses users from file
- `loadAccounts()` - Reads and parses accounts from file
- `loadTransactions()` - Reads and parses transactions from file
- `reconstructionPhase()` - Rebuilds object relationships from parsed data

**POLYMORPHIC IMPLEMENTATIONS:**
- `validatePreconditions()` - Checks if data files exist
- `performOperation()` - Loads users, accounts, and transactions
- `getOperationName()` - Returns "Data Load"

**Data Reconstruction Process:**

1. **Parse Users:** Read users.txt and create RegularUser/Admin objects
2. **Parse Accounts:** Read accounts.txt and organize by userId
3. **Parse Transactions:** Read transactions.txt and organize by accountNumber
4. **Reconstruction Phase:**
   - For each user, attach their accounts
   - For each account, attach its transactions
   - Rebuild complete object hierarchy

**Temporary Data Structures:**
```java
Map<String, List<AccountData>> userAccountsMap
Map<String, List<TransactionData>> accountTransactionsMap
```

**Usage Example:**
```java
if (DataStorage.dataExists()) {
    if (DataLoader.loadAllData()) {
        System.out.println("Data loaded successfully!");
    }
}
```

---

### App.java (Main/Driver - Updated)

**Purpose:** Application entry point and main flow controller with data persistence integration

**Key Changes for Data Persistence:**

**Startup:**
```java
if (DataStorage.dataExists()) {
    System.out.println("Loading saved data...");
    if (DataLoader.loadAllData()) {
        System.out.println("Data loaded successfully! (" + userCount + " users)");
    }
}
```

**Shutdown:**
```java
System.out.println("Saving data before exit...");
DataStorage.saveAllData();
System.out.println("Application terminated.");
```

**Auto-Save Points (9 total):**
1. After user registration
2. After account creation
3. After deposit
4. After withdrawal
5. After internal transfer
6. After external transfer
7. After user addition
8. After user deletion
9. Before program exit

**Key Static Members:**
- `users[]` - Array storing all registered users (capacity 100)
- `userCount` - Current number of registered users

**Key Methods:**
- `main()` - Application entry point with data loading/saving
- `displayWelcome()` - Welcome screen
- `displayMainMenu()` - Main menu display
- `regularUserMode()` - Regular user flow
- `loginRegularUser()` - User authentication
- `registerRegularUser()` - New user registration with auto-save
- `userAccountMenu()` - User transaction menu with auto-save options
- `findRegularUser()` - User lookup
- User accessor methods for controlled array access
- `formatMoney()` - Static utility for currency formatting

**Application Flow With Data Persistence:**
1. Check if data exists
2. Load persisted data (users, accounts, transactions)
3. Display welcome message
4. Main menu loop (1: Regular User, 2: Admin, 3: Exit)
5. Route to appropriate mode
6. After each operation: Save data automatically
7. On exit: Save data and terminate

---

## How to Compile and Run

### Prerequisites

- Java Development Kit (JDK) 14 or higher
- Command-line terminal or IDE (IntelliJ IDEA, Eclipse, NetBeans)

### File Structure

```
PennyWise/
├── src/
│   ├── Account.java              (Abstract base account)
│   ├── SavingsAccount.java       (Savings account with interest)
│   ├── CheckingAccount.java      (Checking account with overdraft)
│   ├── User.java                 (Abstract base user)
│   ├── RegularUser.java          (Regular account holder)
│   ├── Admin.java                (System administrator)
│   ├── Transaction.java          (Transaction value object)
│   ├── App.java                  (Main driver class with persistence)
│   ├── DataPersistence.java      (Abstract base for persistence)
│   ├── DataLoader.java           (Loads data from files)
│   ├── DataStorage.java          (Saves data to files)
│   ├── DataPersistence.java      (Shared constants & template method)
│   └── TransferTest.java         (Testing class)
├── bin/                          (Compiled .class files)
├── data/                         (Auto-created directory for persistent data)
│   ├── users.txt                 (Saved user data)
│   ├── accounts.txt              (Saved account data)
│   └── transactions.txt          (Saved transaction history)
├── README.md                     (This file)
└── lib/                          (External libraries, if any)
```

### Compilation

**Option 1: From PennyWise/PennyWise Directory (Recommended)**

```bash
javac -d bin src/*.java
```

This compiles all Java files including:
- Core classes (User, Account, RegularUser, Admin, Transaction)
- Data persistence classes (DataPersistence, DataLoader, DataStorage)
- Main application driver (App)

**Option 2: Compile Specific Files**

```bash
cd PennyWise/PennyWise
javac -d bin src/App.java src/Account.java src/SavingsAccount.java \
  src/CheckingAccount.java src/User.java src/RegularUser.java \
  src/Admin.java src/Transaction.java src/DataPersistence.java \
  src/DataLoader.java src/DataStorage.java
```

### Execution

```bash
cd PennyWise/PennyWise
java -cp bin App
```

**First Run Behavior:**
- System checks if `data/` directory exists
- Since no saved data exists, starts with empty user list
- Ready for new user registration

**Subsequent Runs:**
- System checks if `data/` directory exists and contains data
- Loads all saved users, accounts, and transaction history
- Displays count of loaded users
- System state is restored from previous session

### Expected Output

**First time running:**
```
=========================================
  Welcome to PennyWise Financial Manager!
=========================================

--- Main Menu ---
1. Regular User Mode
2. Admin Mode
3. Exit

Please select an option (1-3):
```

**After running once and saving data:**
```
Loading saved data...
Data loaded successfully! (3 users)
=========================================
  Welcome to PennyWise Financial Manager!
=========================================

--- Main Menu ---
...
```

---

## User Guide

### Getting Started as a Regular User

#### Step 1: Select Regular User Mode
From the main menu, enter `1` to access Regular User Mode

#### Step 2: Choose Action
You'll see two options:
1. **Register New Account** - Create a new account
2. **Login** - Access existing account

### Creating an Account

**Process:**
1. Enter a unique User ID (e.g., `user001`)
2. Create a username for login
3. Set a strong password
4. Provide an email address
5. Select first account type:
   - **Option 1: Savings Account** - Earns 3% annual interest, limited to 3 withdrawals per month
   - **Option 2: Checking Account** - Overdraft protection up to $500, overdraft fees apply
6. When prompted, choose whether to add another account (Yes/No)
   - If Yes: Repeat account type selection for additional accounts
   - If No: Complete registration with current accounts

**Initial Setup:**
- All new accounts start with $1000.00 initial balance
- Account numbers are automatically generated with sequence numbers
- First transaction recorded as "INITIAL DEPOSIT" in each account
- Example account numbers: SA-user001-1, SA-user001-2, CA-user001-3

**Multiple Accounts Feature:**
- Users can start with 1 or more accounts at registration
- Additional accounts can be added later through the account menu
- Each account is independent with separate balances and transaction history
- Users can switch between accounts and perform transfers between their own accounts

### Account Operations Menu

Once logged in, you'll access the Account Selection and Operations interface.

#### Account Selection (Multiple Accounts)

**If you have multiple accounts:**
1. System displays all your accounts with current balances
2. Each account is numbered (1, 2, 3, etc.) with:
   - Account type (Savings/Checking)
   - Current balance
3. Select the account number to operate on it
4. Option to add a new account appears at the bottom
5. Logout option always available

**Example display:**
```
--- Select Account ---
1. Savings - Balance: $750.00
2. Checking - Balance: $1250.00
3. Add New Account
4. Logout
Select account (1-4):
```

**If you have a single account:**
- System automatically selects your account
- Proceeds directly to Account Menu
- Account Menu shows all available options

### Account Operations

After selecting an account (or if you have only one), you'll access the Account Menu with these options:

#### Option 1: Check Balance
- Instantly view current account balance
- Balance is formatted to 2 decimal places
- Shows exact amount available

#### Option 2: Deposit Money
- Enter amount to deposit (must be positive)
- Balance updates immediately
- Transaction is recorded with current date
- Confirmation message shows new balance

#### Option 3: Withdraw Money
- Enter withdrawal amount
- System validates:
  - Amount is positive
  - (Savings): Less than 3 withdrawals this month
  - (Checking): Amount doesn't exceed available funds (balance + overdraft limit)
- Withdrawal is executed
- New balance displayed
- (Checking): Overdraft warning if applicable

#### Option 4: View Transaction History
- Displays complete list of all transactions
- Shows transaction date, type (DEPOSIT/WITHDRAWAL), and amount
- Transactions are in chronological order (oldest first)
- Useful for reconciliation and audit purposes

#### Option 5: View Profile
- Shows User ID, username, and registered email address
- Displays all accounts associated with this user
- Shows account type and balance for each account
- Verify personal information accuracy
- Return to account menu to make changes (requires admin)

#### Option 6: Transfer Money
- Transfer funds between your own accounts
- **Requirements:**
  - User must have at least 2 accounts to perform transfers
  - Source account must have sufficient balance
- **Process:**
  1. Select source account (currently selected)
  2. View available target accounts with their balances
  3. Choose target account from the list
  4. Enter transfer amount
  5. System validates the transfer is possible
  6. Balance updates immediately on both accounts
  7. Both accounts record transaction details
- **Transaction Recording:**
  - Source account records "TRANSFER OUT"
  - Target account records "TRANSFER IN"
  - Both with identical amounts and dates
- **Error Handling:**
  - Cannot transfer to same account
  - Must exceed $0
  - Requires sufficient balance in source account

#### Options 7-9 (Checking Accounts Only)

**Option 7: View Checking Account Details**
- Account number and type
- Current balance with formatting
- Overdraft limit (e.g., $500)
- Overdraft fee amount (e.g., $35)
- Available funds calculation (balance + limit)
- Current overdraft status (NORMAL or OVERDRAFT)

**Option 8: View Overdraft History**
- List of all withdrawal transactions
- Helps track overdraft usage patterns
- See total number of withdrawal transactions

**Option 9: Back to Account Selection / Logout**
- For users with multiple accounts: Return to account selection menu
- For users with single account: Logout and return to main menu

### Savings Account Specific Features

**Monthly Withdrawal Limit**
- Maximum 3 withdrawals per calendar month
- Attempts beyond limit are rejected
- Resets at month change
- Encourages saving behavior

**Interest Accrual**
- 3% annual interest (0.25% monthly)
- Applied only when admin processes account features
- Amount = (current balance) × (annual rate / 12)
- Automatically deposited to account
- Full transaction history maintained

**Future Balance Projection**
- Accessible through "View Checking Account Details" menu
- Shows projected balance after 12 months
- Calculation includes compound interest
- Useful for financial planning

### Checking Account Specific Features

**Overdraft Protection**
- Access funds beyond current balance
- Up to $500 overdraft limit by default
- Enables flexibility for immediate expenses
- No denial of transactions up to limit

**Overdraft Fees**
- $35 fee applied when balance goes negative
- Fee is deducted automatically when admin processes account features
- Multiple overdraft uses accumulate fees
- Visible in transaction history

**Available Funds Indicator**
- Shows total drawable balance
- Calculation: Current Balance + Overdraft Limit
- For example: -$100 balance + $500 limit = $400 available
- Updated in account details display

### Adding New Accounts After Login

Users can create additional accounts at any time without logging out:

**During Account Selection Menu:**
- Choose "Add New Account" option (displayed as the next available number)
- Follow account type selection (Savings or Checking)
- New account created with $1000 initial balance
- Returns to account selection menu with new account available

**Access New Account Immediately:**
- Just-created account appears in account list
- Can perform transactions immediately
- Separate transaction history maintained
- Interest/overdraft processing applies independently

**Account Naming Convention:**
- Format: `[Type]-[UserID]-[Sequence]`
- Example: `SA-user001-1`, `SA-user001-2`, `CA-user001-3`
- Ensures each account is uniquely identifiable

---

## Admin Guide

### Admin Access

**Demo Credentials:**
- Username: `admin`
- Password: `admin123`
- Level: SUPER (full privileges)

**Note:** In this implementation, admin users are predefined for demonstration. In production, admin accounts would be created through secure administrative processes.

### Accessing Admin Mode

1. From main menu, select `2` (Admin Mode)
2. Enter admin username and password
3. Successful authentication displays Admin Dashboard
4. Admin menu becomes available

### Admin Menu Options

**📌 Note on Feature Implementation:**
Most admin features are fully functional and integrated with data persistence. However, some advanced features are partially implemented with simulated behavior:
- **Option 8 (Manage Administrators):** Admin creation displays confirmation but does not persist across sessions. In a full implementation, newly created admins would be stored in the user array and data files. Currently, only the hardcoded demo admin (username: admin, password: admin123) is available.

All other options (1-7, 9-10) are fully implemented with complete data persistence integration.

---

#### Option 1: View All Users
**Purpose:** Display list of all registered users in the system

**Display shows:**
- User number (index)
- Username
- User ID

**Use case:** Verify user registration, identify specific users for management operations

#### Option 2: Modify User Information
**Purpose:** Update user account details

**Available modifications:**
1. Change Username - Alter login name
2. Change Email - Update contact information
3. Change Password - Reset user password

**Process:**
1. Enter username of user to modify
2. Select which field to change
3. Enter new value
4. Confirmation message displays
5. Changes take effect immediately

**Use case:** Correct user information, update contact details, reset forgotten passwords

#### Option 3: Adjust Account Balance
**Purpose:** Correct account balances for discrepancies or adjustments

**Options:**
1. Add Amount - Increase balance
2. Subtract Amount - Decrease balance
3. Set Exact Balance - Override current balance

**Process:**
1. Enter username of account to adjust
2. Current balance is displayed
3. Select adjustment type
4. Enter amount
5. New balance calculated and displayed

**Use case:** Correct errors, process refunds, adjust for bank fees

#### Option 4: Delete User Account
**Purpose:** Remove user and associated account from system

**Process:**
1. Enter username to delete
2. Confirmation required (type 'yes')
3. User and all account data removed
4. Confirmation message displays

**Use case:** Remove inactive accounts, handle user requests, system maintenance

#### Option 5: Generate System-wide Report (Super Admin Only)
**Purpose:** Comprehensive financial analysis across entire system

**Report includes:**
- Generated timestamp
- Account summary per user:
  - Username, account type
  - Current balance
  - Transaction count
- System totals:
  - Total number of users
  - Savings vs. checking account count
  - System total balance
  - Overall transaction count
  - Average balance per user

**Use case:** Financial auditing, system analysis, stakeholder reporting

#### Option 6: Apply Account Features to All Savings Accounts (Super Admin Only)
**Purpose:** Process monthly interest for all savings accounts system-wide

**Process:**
- Iterates through all users
- Identifies savings accounts
- Calculates monthly interest (annual rate / 12)
- Adds interest as transaction
- Displays per-account processing:
  - Username
  - Account number
  - Balance before and after
  - Interest amount

**Summary shows:**
- Total savings accounts processed
- Total interest applied system-wide

**Use case:** Monthly interest application, financial close process

#### Option 7: Apply Account Features to All Checking Accounts (Super Admin Only)
**Purpose:** Process overdraft fees for checking accounts with negative balance

**Process:**
- Iterates through all users
- Identifies checking accounts
- Assesses fees if in overdraft
- Updates balance accordingly
- Displays per-account processing:
  - Username
  - Account number
  - Overdraft status
  - Fees applied

**Summary shows:**
- Total checking accounts processed
- Total fees applied system-wide

**Use case:** Monthly fee processing, revenue collection, financial reporting

#### Option 8: Manage Administrators (Super Admin Only)
**Purpose:** View, create, and modify administrator accounts

**Options:**
1. View Admins - List current administrators
2. Create New Admin - Add new admin with privilege level
3. Modify Admin - Change admin details
4. Cancel - Return to admin menu

**Admin Levels:**
- BASIC: User and account management
- SUPER: All privileges including reports and admin management

**Use case:** Admin team coordination, privilege delegation

#### Option 9: [DANGER] Delete All Stored Data (Super Admin Only)
**Purpose:** Completely reset the system and remove all stored data

**Security Measures:**
1. **Privilege Check:** Only SUPER admins can access
2. **Warning Display:** Shows what will be deleted
3. **First Confirmation:** Must type "DELETE" exactly
4. **Second Confirmation:** Must type "YES" to confirm
5. **Feedback:** Clear messages about success/failure

**What Gets Deleted:**
- `data/users.txt` - All user accounts
- `data/accounts.txt` - All account information
- `data/transactions.txt` - All transaction history

**What Remains:**
- Current session data stays active until exit
- Data won't be saved on next exit
- Clean slate on next program start

**Use case:** System reset, testing, cleanup

#### Option 10: Logout
**Purpose:** End admin session and return to main menu

**Effect:**
- Admin menu closes
- Returns to main menu
- Session data preserved

---

## Data Persistence

### Overview

PennyWise includes a complete data persistence system that saves all application data to files. This means you can stop and restart the program without losing any user data, accounts, or transaction history.

### Key Features

#### Automatic Data Persistence
Data is automatically saved after:
- User registration
- Account creation
- Deposits and withdrawals
- Money transfers
- Admin modifications (balance adjustments, user info changes)
- Account feature applications (interest, fees)
- User deletion
- Program exit

#### On Program Start
1. Application checks if `data/` directory exists
2. If saved data is found, loads:
   - All users (RegularUser objects)
   - All accounts with current balances
   - Complete transaction history
3. Displays count of loaded users

#### On Program Exit
- Data is saved one final time
- All current state is preserved for next session

### File Formats

#### users.txt
```
REGULAR|userId|username|password|email
ADMIN|userId|username|password|email|adminLevel
```

#### accounts.txt
```
userId|SAVINGS|accountNumber|balance|interestRate
userId|CHECKING|accountNumber|balance|overdraftLimit|overdraftFee
```

#### transactions.txt
```
accountNumber|amount|type|date
```

### Benefits

✅ **Persistence** - No data loss between sessions  
✅ **Testing** - Easy to reset and start over  
✅ **Debugging** - Text files are human-readable  
✅ **Safety** - Multiple confirmations for destructive operations  
✅ **Automatic** - No manual save required  
✅ **Complete** - Saves users, accounts, and transactions  

### Testing the Persistence System

1. **Create data:**
   - Register 2-3 users with multiple accounts
   - Perform various transactions
   - Exit the program

2. **Verify persistence:**
   - Restart the program
   - Check that all users are loaded
   - Login and verify balances are correct
   - Check transaction history is intact

3. **Test reset:**
   - Login as admin
   - Use "Delete All Stored Data" option
   - Confirm deletion
   - Exit and restart
   - Verify system starts with no users

---

### Application Flow with Persistence

```
┌─────────────────────────────────────┐
│     Program Starts                  │
│                                     │
│  1. Check if data/ exists           │
│  2. If yes, load all data:          │
│     - Users                         │
│     - Accounts                      │
│     - Transactions                  │
│  3. Display main menu               │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   User Performs Operations          │
│                                     │
│  - Register                         │
│  - Login                            │
│  - Deposit/Withdraw                 │
│  - Transfer                         │
│  - Admin operations                 │
│                                     │
│  After EACH operation:              │
│  → DataStorage.saveAllData()        │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│     Program Exits                   │
│                                     │
│  1. Save all data one final time   │
│  2. Display goodbye message         │
│  3. Terminate                       │
└─────────────────────────────────────┘
```

### Data Persistence Trigger Points

✓ User registration  
✓ Account creation  
✓ Deposit transaction  
✓ Withdrawal transaction  
✓ Transfer (internal)  
✓ Transfer (external)  
✓ User deletion (admin)  
✓ User info modification (admin)  
✓ Balance adjustment (admin)  
✓ Apply interest (admin)  
✓ Apply fees (admin)  
✓ Program exit  
---

## Project Scope and Requirements

### Course Requirements Met

#### 1. **Selections (if-else, switch statements)** ✓
- Rule switch statements for menu navigation (modern Java syntax)
- If-else for conditional account creation
- Conditional operator for account type checking
- Nested conditionals for complex validation

#### 2. **Loops (while, do-while, for)** ✓
- While loops for main app, user session, admin session, and account menu
- For loops for user array iteration, transaction history, batch operations
- Break statements for loop control

#### 3. **Methods (value returning and void)** ✓
- 40+ total methods demonstrating both styles
- Value-returning methods for lookup, calculation, status checking
- Void methods for display, processing, and operations
- Method overloading and overriding

#### 4. **Arrays (single-dimensional, multi-dimensional)** ✓
- User array: `User[] users = new User[100]`
- Transaction arrays: `Transaction[] transactions = new Transaction[100]` per account
- Array element access and assignment
- Bounds checking and capacity management

#### 5. **Inheritance and Polymorphism** ✓
- User hierarchy: User → RegularUser, Admin
- Account hierarchy: Account → SavingsAccount, CheckingAccount
- Abstract methods: displayDashboard(), applyAccountFeatures(), withdraw()
- Polymorphic method calls and instanceof pattern matching

#### 6. **User Interaction (System.in via Scanner)** ✓
- Extensive input handling throughout application
- NumberFormatException handling for numeric input
- String input for registration, login, modifications
- Interactive menu-driven interface

#### 7. **Encapsulation (private fields, getters/setters)** ✓
- All data fields properly encapsulated as private
- Protected methods for subclass access where appropriate
- Controlled accessor methods for external access
- Data validation in setters where appropriate

---

## Future Enhancements 

#### Data Persistence
- Database integration (SQL)
- Data export to CSV/JSON
- Account recovery from backup

#### Enhanced Security
- Encrypted password storage (hashing)
- Session tokens and timeouts
- Audit logging for all administrative actions
- Role-based access control (RBAC) expansion

#### Advanced Transaction Features
- Scheduled transactions
- Recurring payments
- Transaction categorization and tagging
- Multi-currency support
- Cross-account transfers between different users

#### Financial Analytics
- Spending patterns and reports
- Budget tracking
- Savings goals
- Investment portfolio tracking
- Financial forecasting with machine learning

#### Mobile Integration
- Mobile app development
- REST API for backend services
- Cloud synchronization
- Notification system

#### Compliance and Regulation
- PCI DSS compliance for payment data
- GDPR compliance for data privacy
- SOX compliance for financial reporting
- Audit trail enhancements
- Regulatory reporting generation

#### Multi-user Account Management
- Joint accounts with dual authorized users
- Guardian accounts for minors
- Beneficiary designations
- Trust account management

#### Advanced Admin Features
- Audit trail with full access logs
- Compliance reporting
- System monitoring and analytics
- Batch import/export tools
- Advanced search and filtering

#### Integration and APIs
- Third-party payment processing
- Banking APIs for real transaction processing
- Stock market data integration
- Bill payment services
- Account aggregation

---

## Conclusion

PennyWise Financial Manager demonstrates a complete, well-architected financial software system that showcases all required object-oriented programming concepts in a practical, real-world context. The system is designed to be extensible, maintainable, and scalable for future enhancements.

##### Gradeing (Checklist)

Project Grading:
Correct data types and convenient variable naming
Sample run at the top 
Appropriate comments in the code
Avoiding redundant commands and variables 
Correctly interacting with the user from console
Correctly using at least one selection type (if-else, switch, and/or conditional
operators)
Correctly using at least one loop type (while, do-while, and/or for loops)
Correctly defining methods at least one value returning and one void methods (the
main method doesn’t count)
Correctly using arrays 
Correctly implementing a driver/main and object classes 
Correctly implementing inheritance and polymorphism 
Correctly implementing encapsulation (using private modifier) with getter and setter
methods
