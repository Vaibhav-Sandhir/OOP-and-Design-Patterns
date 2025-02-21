import java.util.*;

class Bank{
    public static void main(String args[]){
        Teller teller1 = new Teller(3, "vaibahv");
        Teller teller2 = new Teller(4, "andy");
        Map<Integer, Account> accounts = new HashMap<>();
        Map<Integer, Transaction> transactions = new HashMap<>();
        Map<Integer, Teller> tellers = new HashMap<>();
        tellers.put(3, teller1);
        tellers.put(4, teller2);
        BankSystem bs = new BankSystem(accounts, transactions, tellers, "Main branch");
        bs.run();
    }
}

abstract class Transaction{
    Teller teller;
    int transactionId;
    
    Transaction(Teller teller, int transactionId){
        this.teller = teller;
        this.transactionId = transactionId;
    }

    abstract String getDescription();

    abstract void activate() throws Exception;
}

class Deposit extends Transaction{
    int amount;
    Account account;

    Deposit(Account account, Teller teller, int transactionId, int amount){
        super(teller, transactionId);
        this.account = account;
        this.amount = amount;
    }

    void activate(){
        this.account.balance += this.amount;
        System.out.println(getDescription());
    }
    
    @Override
    String getDescription(){
        return "Teller " + super.teller.tellerId + " deposited " + this.amount + " to " + this.account.customerId;
    }
}

class Withdraw extends Transaction{
    int amount;
    Account account;

    Withdraw(Account account, Teller teller, int transactionId, int amount){
        super(teller, transactionId);
        this.account = account;
        this.amount = amount;
    }

    void activate() throws InsufficientBalanceException{
        if(this.amount > this.account.balance){
            throw new InsufficientBalanceException("Low Balance!");
        }
        else{
            this.account.balance -= this.amount;
            System.out.println(getDescription());
        }
    }

    @Override
    String getDescription(){
        return "Teller " + super.teller.tellerId + " withdrawn " + this.amount + " to " + this.account.customerId;
    }
}

class OpenAccount extends Transaction{
    int customerId;

    OpenAccount(Teller teller, int transactionId, int customerId){
        super(teller, transactionId);
        this.customerId = customerId;
    }

    void activate(){
        System.out.println(getDescription());
        return;
    }

    @Override
    String getDescription(){
        return "Teller " + super.teller.tellerId + " opened an account for " + this.customerId;
    }

}

class Account{
    int customerId;
    int balance;

    Account(int customerId, int balance){
        this.customerId = customerId;
        this.balance = balance;
    }
}

class Teller{
    int tellerId;
    String name;

    Teller(int tellerId, String name){
        this.tellerId = tellerId;
        this.name = name;
    }
}

class BankSystem{
    Map<Integer, Account> accounts;
    Map<Integer, Transaction> transactions;
    Map<Integer, Teller> tellers;
    Scanner sc;
    String branchName;
    Random random;

    BankSystem(Map<Integer, Account> accounts, Map<Integer, Transaction> transactions, Map<Integer, Teller> tellers, String branchName){
        this.accounts = accounts;
        this.transactions = transactions;
        this.branchName = branchName;
        this.tellers = tellers;
        this.random = new Random();
        sc = new Scanner(System.in);
    }

    void run() {
        System.out.println("Welcome to " + this.branchName);

        while(true){
            System.out.println("Select your option \n 1.) Withdraw Money \n 2.) Deposit Money \n 3.) Open Account \n 4.) View tarnsactions \n 5.) Exit");
            int option = sc.nextInt();
            int customerId;
            int amount;
            Account account;
            switch(option){
                case 1:
                    System.out.println("Enter customerId");
                    customerId = sc.nextInt();
                    System.out.println("Enter amount to withdraw");
                    amount = sc.nextInt();
                    try {
                        account = getAccount(customerId);
                        withdrawMoney(account, amount);
                    }
                    catch(Exception e){
                        System.out.println(e);
                        continue;
                    }
                    break;
                case 2:
                    System.out.println("Enter customerId");
                    customerId = sc.nextInt();
                    System.out.println("Enter amount to deposit");
                    amount = sc.nextInt();
                    try{
                        account = getAccount(customerId);
                        depositMoney(account, amount);
                    }
                    catch(Exception e){
                        System.out.println(e);
                        continue;
                    }
                    break;
                case 3:
                    openAccount();
                    break;
                case 4:
                    for(int key : this.transactions.keySet()){
                        System.out.println(key + " " + this.transactions.get(key).getDescription());
                    }
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Please try again");
            }
        }
    }

    int getNewTransactionId(){
        int id = random.nextInt(10001);
        while(this.transactions.containsKey(id)){
            id = random.nextInt();
        }
        return id;
    }

    int getNewCustomerId(){
        int id = random.nextInt(10001);
        while(this.accounts.containsKey(id)){
            id = random.nextInt();
        }
        return id;
    }

    Account getAccount(int customerId) throws AccountNotFoundException{
        if(!this.accounts.containsKey(customerId)){
            throw new AccountNotFoundException("Account not found");
        }
        else{
            return this.accounts.get(customerId);
        }
    }

    Teller getTeller(){
        List<Integer> keys = new ArrayList<>(this.tellers.keySet());
        Random r = new Random();
        int tellerId = keys.get(random.nextInt(keys.size()));
        return this.tellers.get(tellerId);
    }
    
    void withdrawMoney(Account account, int amount) throws Exception{
        int transactionId = getNewTransactionId();
        Teller teller = getTeller();
        Transaction transaction = new Withdraw(account, teller, transactionId, amount);
        this.transactions.put(transactionId, transaction);
        try{
            transaction.activate();
        }
        catch(Exception e){
            System.out.println(e);
            throw e;
        }
        return;
    }

    void depositMoney(Account account, int amount) throws Exception{
        int transactionId = getNewTransactionId();
        Teller teller = getTeller();
        Transaction transaction = new Deposit(account, teller, transactionId, amount);
        this.transactions.put(transactionId, transaction);
        transaction.activate();
        return;
    }

    void openAccount(){
        int transactionId = getNewTransactionId();
        Teller teller = getTeller();
        int customerId = getNewCustomerId();
        Transaction transaction = new OpenAccount(teller, transactionId, customerId);
        Account account = new Account(customerId, 0);
        this.transactions.put(transactionId, transaction);
        this.accounts.put(customerId, account);
        return;
    }

}

class InsufficientBalanceException extends Exception{
    InsufficientBalanceException(String msg){
        super(msg);
    }
}

class AccountNotFoundException extends Exception{
    AccountNotFoundException(String msg){
        super(msg);
    }
}
