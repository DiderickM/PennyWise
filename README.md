# PennyWise - Financial Management System
**RSM Erasmus University | Master Business Information Management | Group Project**

---

## What is PennyWise?

PennyWise is a console-based financial management system -- think of it as a simplified bank. Users can register, open savings or checking accounts, deposit and withdraw money, and track their transaction history. An admin tier gives elevated access to view and manage all accounts across the system.

We picked the **Financial Software** theme because it naturally maps to all seven required technical elements: different account types demonstrate inheritance and polymorphism, sensitive data (passwords, balances) motivates encapsulation, menus and transaction flows exercise selections and loops, and a multi-user registry calls for arrays and proper method design.

---

## Sample Run

A full annotated sample run is embedded in the source file `App.java`. Here is the gist:

```
=========================================
 Welcome to PennyWise Financial Manager!
=========================================
1. Regular User Mode
2. Admin Mode
3. Exit

Please select an option (1-3): 1

--- Regular User Mode ---
1. Register New Account
2. Login
Select option (1-2): 1

--- Registration ---
Enter Username: john_doe
Enter Password: ********
Enter Email: john@example.com

Select Account Type:
1. Savings Account (3% interest)
2. Checking Account (with overdraft)
Select account type (1-2): 1

Account created! Starting balance: $0.00

--- User Dashboard ---
1. Deposit
2. Withdraw
3. View Balance
4. View Transactions
5. Logout
```

Admin login (`admin` / `admin123`) unlocks a separate dashboard to view all users, modify balances, and generate reports. See the full sample run in `App.java`.

---

## Required Technical Elements

The seven elements required by the project instructions are all present:

| # | Element | Where it appears |
|---|---------|-----------------|
| 1 | **Selections** (if-else, switch) | `App.java` main switch, `CheckingAccount.withdraw()` overdraft check, `InputValidator` validation logic |
| 2 | **Loops** (while, for) | Main menu loop in `App.java`, transaction history loops in `UserInterface`, user search in `UserManager` |
| 3 | **Methods** (value-returning & void) | Every class -- e.g. `deposit()` returns `boolean`; `displayDashboard()` is void |
| 4 | **Arrays** | `transactions[]` in `Account`, `accounts[]` in `User`, `regularUsers[]` and `admins[]` in `UserManager` |
| 5 | **Inheritance & Polymorphism** | `User -> RegularUser / Admin -> SuperAdmin` and `Account -> CheckingAccount / SavingsAccount`; `displayDashboard()` overridden per subclass |
| 6 | **User interaction** (System.in) | All input goes through `InputValidator` wrapping a `Scanner` -- menus, amounts, credentials |
| 7 | **Encapsulation** | All fields are `private`; access is through getters/setters across `User`, `Account`, and `Transaction` |

### Code quality notes
- Names are descriptive and follow Java conventions (`accountNumber`, `overdraftLimit`, `hashPassword`).
- Comments throughout the code call out each required element explicitly (look for `// LOOPS:`, `// SELECTION:`, etc.).
- Passwords are hashed via `PasswordUtil` -- plain-text credentials are never stored.
- Redundant logic is avoided by centralising input validation in `InputValidator` and data persistence in `DataStorage`.

---

## How to Run

1. Compile from the project root:
   ```
   javac -d PennyWise/bin PennyWise/src/pennywise/*.java PennyWise/src/pennywise/**/*.java
   ```
2. Run the application:
   ```
   java -cp PennyWise/bin pennywise.App
   ```

Data is automatically saved on exit and reloaded on the next launch.

---

# Class Overview

## User Classes

### User (abstract class)
- **Properties**: userId, username, password, email, accounts[]
- **Methods**: displayDashboard() (abstract), addAccount(), getAccounts()

#### RegularUser (extends User)
- **Constructor**: RegularUser(userId, username, password, email)
- **Methods**: displayDashboard()
- **Capabilities**: View own accounts, manage own transactions

#### Admin (extends User)
- **Constructor**: Admin(userId, username, password, email)
- **Methods**: displayDashboard()
- **Capabilities**: View/modify all user accounts, view system reports

#### SuperAdmin (extends Admin)
- **Methods**: displayDashboard(), generateSystemReport()
- **Capabilities**: System administration, configuration management

---

## Account Classes

### Account (abstract)
- **Properties**: accountNumber, balance, accountType, transactions[]
- **Methods**: 
  - deposit(amount): boolean (abstract)
  - withdraw(amount): boolean (abstract)
  - getBalance(): double
  - recordTransaction(amount, type, date): void

#### CheckingAccount (extends Account)
- **Properties**: overdraftLimit, overdraftFee
- **Constructor**: CheckingAccount(number, balance, limit, fee)
- **Methods**: 
  - deposit(amount): boolean
  - withdraw(amount): boolean
  - isInOverdraft(): boolean

#### SavingsAccount (extends Account)
- **Properties**: interestRate, maxWithdrawalsPerMonth, withdrawalCount
- **Constructor**: SavingsAccount(number, balance, rate, maxWithdrawals)
- **Methods**: 
  - deposit(amount): boolean
  - withdraw(amount): boolean
  - compoundInterest(): void

---

## Transaction Class

### Transaction
- **Properties**: amount, type (DEPOSIT/WITHDRAWAL), date
- **Constructor**: Transaction(amount, type, date)
- **Methods**: getAmount(), getType(), getDate(), getTransactionDetails()

---

## Manager Classes

### UserManager
- **Properties**: regularUsers[], admins[]
- **Methods**: 
  - addRegularUser(user): boolean
  - authenticateRegularUser(username, password): RegularUser
  - usernameExists(username): boolean
  - getUserCount(): int

### AccountManager
- **Methods**: 
  - createAccount(userId, type, balance): Account
  - calculateInterest(account): void
  - displayAccountTypeMenu(): void

---

## UI & Controller Layer

### UserInterface
- **Properties**: scanner
- **Methods**: 
  - displayWelcome(): void
  - displayMainMenu(): void
  - regularUserMode(): void
  - userAccountMenu(user): void
  - adminMenu(admin): void

### App (Main)
- **Entry Point**: main(args): void
- **Initialization**: Sets up UserManager, AccountManager, UserInterface
- **Flow Control**: Main menu loop handling regular user, admin, and exit options

---

## Data Persistence Layer

### DataStorage (static utility)
- **Methods**: 
  - saveAllData(): void
  - loadAllData(): void
  - deleteAllData(): void

### DataPersistence (abstract parent)
- **Abstract Methods**:
  - loadUsers(): void
  - loadAccounts(): void
  - saveUsers(): void
  - saveAccounts(): void

### DataLoader (extends DataPersistence)
- **Properties**: userAccountsMap, accountTransactionsMap
- **Methods**: 
  - loadUsers(): void
  - loadAccounts(): void
  - loadTransactions(): void
  - Implements all DataPersistence abstract methods

---

## Utility Classes

### InputValidator (static utility)
- **Methods**: 
  - getIntInput(scanner): int
  - getValidatedUsername(scanner, prompt): String
  - getValidatedPassword(scanner, prompt): String
  - getValidatedEmail(scanner, prompt): String

### PasswordUtil (static utility)
- **Methods**: 
  - hashPassword(password): String
  - verifyPassword(plain, hashed): boolean

### AppConstants (static constants)
- **Constants**: 
  - MAX_TRANSACTIONS_PER_ACCOUNT: int
  - MAX_ACCOUNTS_PER_USER: int
  - Transaction type constants (DEPOSIT, WITHDRAWAL, etc.)

---
