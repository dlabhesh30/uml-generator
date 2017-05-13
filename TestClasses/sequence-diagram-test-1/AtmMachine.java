
public class ATM {
    Customer cust = new Customer();
    Bank bank = new Bank();
    public void insertCard(String cardNum){
        System.out.println("Card Inserted");
        bank.account(cardNum);
}
    public void openMoneySlot(){
        System.out.println("Money Slot Opened, please insert cash now");
    }
    public void enterMoney(int amount) {
        System.out.println("Money inserted " + i);
        bank.depMoney(amount);
        cust.addBalance(amount);
    }
    public void printReceipt() {
        System.out.println("Print receipt");
        
    }
}
