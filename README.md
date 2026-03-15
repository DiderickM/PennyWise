# PennyWise
## RSM Erasmus University - Master Business Information Management

This is a group project for the Erasmus University: Master Business Information management


Group Project Proposal: PennyWise Financial Manager 

## Project Description 

Our group has chosen to develop financial software, specifically a system designed to display balances and handle transactions for multiple users mimicking a bank or financial manager.  

General users of the software should be able to create new accounts and log in. Once registered, users are presented with multiple options. They should be able to conduct transactions, view their balances, and edit their profile.  

Besides general users, there will be an admin component. This admin has elevated permissions to view the entire system’s records, modify existing accounts, correct balances, and update users.  

### Why We Picked This Topic 

We selected the Financial Software theme, and specifically banking, because it lends itself to demonstrate all seven required technical elements described in the group project instructions. Different account- and saving types could allow us to demonstrate inheritance and polymorphism. Furthermore, the necessity of protecting sensitive financial data allows us to showcase Encapsulation by restricting direct access to private account variables. 

# Program design: Class descriptions



## User Classes (Abstract)

### User (abstract)
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

## Account Classes (Abstract)

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
- **Properties**: userAccountsMap<String, List<AccountData>>, accountTransactionsMap<String, List<TransactionData>>
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