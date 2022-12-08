package server;

public class Account {
    private final String number;

    private Double cash;

    public Account(String number) {
        this.number = number;
        this.cash = (double) 0;
    }

    public Double getCash() {
        return cash;
    }

    public synchronized void setCash(Double cash) {
        this.cash = cash;
    }

    public String getNumber() {
        return number;
    }
}
