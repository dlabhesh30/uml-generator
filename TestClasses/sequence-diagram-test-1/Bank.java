
public class Bank {
    ATM atm = new ATM();
    
    public void depMoney(int i){
        System.out.println("Bank receives money" + i);
    }
    
    public void account(String cardNumber){
        System.out.println("Card Number is "+cardNumber);
        atm.openMoneySlot();
    }
    
    
}
