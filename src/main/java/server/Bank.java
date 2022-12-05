package server;

import java.util.List;

public class Bank {

    final private List<Account> accounts = List.of(new Account("123456"), new Account("654321"));

    private Account actualAccount = null;

    public void login(String number) throws NotFoundAccountException {
        this.actualAccount = accounts.stream()
                .filter(customer -> number.equals(customer.getNumber()))
                .findAny()
                .orElse(null);

        if(actualAccount == null) {
           throw new NotFoundAccountException();
        }
    }

    public synchronized boolean deposit(Double value) {
        if(value > 0) {
            actualAccount.setCash(actualAccount.getCash() + value);
            return true;
        }

        return false;
    }

    public synchronized boolean withdraw(Double value) {
        if (actualAccount.getCash() >= value) {
            this.actualAccount.setCash(actualAccount.getCash() - value);
            return true;
        }

        return false;
    }

    public synchronized Double currentValue() {
        return this.actualAccount.getCash();
    }

    public void refresh() {
        this.actualAccount = null;
    }
}
