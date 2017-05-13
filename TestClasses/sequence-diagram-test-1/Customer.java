
public class Customer {
    private int balance;
    private void depositMoney() {
        ATM atm = new ATM();
        atm.insertCard("5458885452123659");
        atm.enterMoney(5000);
        atm.printReceipt();
}
    public void addBalance(int amount){
        balance+=amount;
        System.out.println("I am richer by "+amount);
    }
    
}
