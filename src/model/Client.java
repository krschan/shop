package model;

import main.Payable;

public class Client extends Person implements Payable {
    private final int MEMBER_ID = 456;
    private final double BALANCE = 50.00;
    private int memberId;
    private Amount amount = new Amount(BALANCE);

    public Client(String name) {
        super(name);
    }
    
    public Amount getAmount() {
        return amount;
    }

    public int getMember_id() {
        return memberId;
    }

    public void setMember_id(int member_id) {
        this.memberId = member_id;
    }

    public int getMEMBER_ID() {
        return MEMBER_ID;
    }

    public double getBALANCE() {
        return BALANCE;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    @Override
    public boolean pay(Amount amount) {
        double total = BALANCE - amount.getValue();
        Amount totalAmount = new Amount(total);

        if (total > 0) {
            System.out.println("Remaining balance: " + totalAmount);
            return true;
        } else {
            System.out.println("Outstanding debt: " + totalAmount);
            return false;
        }
    }
}
