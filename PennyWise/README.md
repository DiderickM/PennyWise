# PennyWise Financial Manager - Comprehensive Project Documentation
# ⚠️ <span style="color: blue">*This ReadMe is automatically generated based on the code through Visual Studio Code*</span>.⚠️

## Group Project for Erasmus University: Master in Business Information Management

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Features](#features)
3. [System Architecture](#system-architecture)
4. [Technical Concepts Demonstrated](#technical-concepts-demonstrated)
5. [Detailed Class Structure](#detailed-class-structure)
6. [How to Compile and Run](#how-to-compile-and-run)
7. [User Guide](#user-guide)
8. [Admin Guide](#admin-guide)
10. [Project Scope and Requirements](#project-scope-and-requirements)
11. [Future Enhancements](#future-enhancements)

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

The system follows a hierarchical architecture:

```
                          User (Abstract)
                         /              \
                    RegularUser      Admin
                        |               |
                    Account          Admin-only
                    /      \         Features
              Savings   Checking
              Account    Account

                    Transaction
                   (Value Object)
```

### Data Flow Architecture

```
┌─────────────────────────────────────────────────┐
│                    App (Main)                    │
│            Entry Point & Session Manager        │
└─────────────────────────────────────────────────┘
              ↓                 ↓
        ┌─────────┐         ┌──────────┐
        │ Regular │         │  Admin   │
        │  Mode   │         │  Mode    │
        └─────────┘         └──────────┘
             ↓                   ↓
    ┌──────────────┐    ┌──────────────────────┐
    │ RegularUser  │    │  Admin Operations    │
    │   Object     │    │  (Users, Reports)    │
    └──────────────┘    └──────────────────────┘
             ↓
    ┌──────────────────┐
    │  Account Object  │
    │ (Savings/Check)  │
    └──────────────────┘
             ↓
    ┌──────────────────┐
    │ Transactions     │
    │    Array [100]   │
    └──────────────────┘
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
- Account selection (type selection 1-2)
- Login credentials verification
- Deposit amount entry with NumberFormatException handling
- Withdrawal amount entry with amount validation
- Admin operations with various numeric and string inputs
- Menu navigation throughout sessions
- Balance adjustment amounts
- User information modifications

**Input Validation:**
- NumberFormatException catching for invalid numeric input
- Range validation for menu selections
- Positive amount validation for transactions
- Email format consideration
- Password strength handling (simulated)

**Error Handling:**
- Try-catch blocks for numeric input validation
- Null pointer checking for user retrieval
- Array bounds checking before access
- Transaction amount validation

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

**Key Methods:**
- `displayDashboard()` - Shows user dashboard with account info
- `getBalance()` - Returns current account balance
- `viewTransactionHistory()` - Displays all transactions from account

**Relationships:**
- Extends User
- Contains Account (composition)
- Has-a relationship to Account instance

**Features:**
- User registration and login
- Account type selection during registration
- Transaction access through account interface

---

### Admin.java

**Purpose:** Provides system administration and oversight capabilities

**Key Methods:**
- `displayDashboard()` - Shows admin dashboard with privileges
- `hasSuperPrivileges()` - Checks for SUPER level access
- `launchAdmin()` - Static method for admin session initialization
- `runAdminSession()` - Interactive admin menu and operations
- Various admin operation methods:
  - `displayAllUsers()` - List all users
  - `modifyUserInformation()` - Edit user details
  - `adjustAccountBalance()` - Correct balances
  - `deleteUserAccount()` - Remove users
  - `generateSystemReport()` - System-wide analytics
  - `applyAccountFeaturesToAllSavings()` - Batch interest application
  - `applyAccountFeaturesToAllChecking()` - Batch fee application
  - `manageAdmins()` - Create/modify admin accounts

**Privilege Levels:**
- BASIC: Basic user and account management
- SUPER: All features plus advanced reporting and admin management

---

### Account.java (Abstract)

**Purpose:** Base class for all account types with core banking operations

**Key Properties:**
- accountNumber: Unique account identifier
- balance: Current account balance
- accountType: Type designation (SAVINGS/CHECKING)
- transactions: Array of Transaction objects (100 capacity)
- transactionCount: Current number of transactions

**Key Methods:**
- `deposit()` - Add funds to account
- `withdraw()` - Remove funds with subclass-specific rules
- `checkBalance()` - Return current balance
- `recordTransaction()` - Audit trail recording
- `displayTransactionHistory()` - Full transaction display
- `applyAccountFeatures()` - Abstract method for account-specific operations
- `displayAccountInfo()` - Account overview

**Transaction Recording:**
- Every transaction (deposit, withdrawal, initial balance) is recorded
- Each transaction stores: amount, type (DEPOSIT/WITHDRAWAL), date
- Maximum 100 transactions per account
- Accessible for audit and reporting

---

### SavingsAccount.java

**Purpose:** Specialized account type emphasizing wealth accumulation with interest benefits

**Key Features:**
- **Interest Calculation:** Automatic monthly interest application (annual rate / 12)
- **Withdrawal Limits:** Maximum 3 withdrawals per month
- **Interest Projection:** Calculate 12-month projected balance
- **Compound Interest:** Support for interest-on-interest calculations

**Key Methods:**
- `applyAccountFeatures()` - Apply monthly interest to balance
- `withdraw()` - Enforce monthly withdrawal limit before allowing operation
- `countWithdrawalsThisMonth()` - Count current month's withdrawals
- `projectFutureBalance()` - Project balance after N months with compound interest
- `displaySavingsInfo()` - Show account details including interest rate and projection

**Polymorphic Behavior:**
- Overrides `withdraw()` to add withdrawal limit
- Overrides `applyAccountFeatures()` to calculate and apply interest
- Different withdrawal rules than parent class
- Different feature application than checking accounts

---

### CheckingAccount.java

**Purpose:** High-frequency transaction account with overdraft protection

**Key Features:**
- **Overdraft Protection:** Borrow up to overdraft limit when balance negative
- **Overdraft Fees:** Assessment when account goes negative
- **Available Funds:** Balance + overdraft limit for transactions
- **Overdraft Status:** Real-time status display

**Key Methods:**
- `applyAccountFeatures()` - Apply overdraft fees if account negative
- `withdraw()` - Allow overdraft protection up to limit
- `isInOverdraft()` - Check if account is in negative balance
- `getAvailableFunds()` - Calculate total available (balance + limit)
- `displayCheckingInfo()` - Show overdraft details and status
- `displayOverdraftHistory()` - Show withdrawal transaction history

**Polymorphic Behavior:**
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

### App.java (Main/Driver)

**Purpose:** Application entry point and main flow controller

**Key Static Members:**
- `users[]` - Array storing all registered users (capacity 100)
- `userCount` - Current number of registered users

**Key Methods:**
- `main()` - Application entry point
- `displayWelcome()` - Welcome screen
- `displayMainMenu()` - Main menu display
- `regularUserMode()` - Regular user flow
- `loginRegularUser()` - User authentication
- `registerRegularUser()` - New user registration
- `userAccountMenu()` - User transaction menu
- `findRegularUser()` - User lookup
- User accessor methods for controlled array access

**Application Flow:**
1. Display welcome message
2. Main menu loop (1: Regular User, 2: Admin, 3: Exit)
3. Route to appropriate mode
4. Display mode-specific menu
5. Execute selected operations
6. Return to main menu or exit

---

## How to Compile and Run

### Prerequisites

- Java Development Kit (JDK) 14 or higher
- Command-line terminal or IDE (IntelliJ IDEA, Eclipse, NetBeans)

### File Structure

```
PennyWise/
├── src/
│   ├── Account.java
│   ├── Admin.java
│   ├── App.java
│   ├── CheckingAccount.java
│   ├── RegularUser.java
│   ├── SavingsAccount.java
│   ├── Transaction.java
│   └── User.java
├── bin/          (compiled .class files)
└── README.md
```

### Compilation

**Option 1: From PennyWise/PennyWise Directory**

```bash
javac -d bin src/*.java
```

**Option 2: Compile Specific Files**

```bash
cd PennyWise/PennyWise
javac -d bin src/App.java src/Account.java src/SavingsAccount.java \
  src/CheckingAccount.java src/User.java src/RegularUser.java \
  src/Admin.java src/Transaction.java
```

### Execution

```bash
cd PennyWise/PennyWise
java -cp bin App
```

### Expected Output

The application will display:
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

#### Option 9: Logout
**Purpose:** End admin session and return to main menu

**Effect:**
- Admin menu closes
- Returns to main menu
- Session data preserved

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

### : Advanced Features

#### Data Persistence
- File-based storage (serialization)
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

### : Enterprise Features

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

The project successfully balances educational objectives with practical functionality, providing clear demonstrations of:
- Clean code architecture
- Proper encapsulation and data protection
- Polymorphic design patterns
- Comprehensive user interface
- Administrative oversight capabilities

This codebase serves as an excellent foundation for further development and learning in enterprise software development practices.
Note4: Submit the final version of your project by Wednesday, March 18th, at 9:00am through
Canvas.

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
