import java.util.ArrayList;
import java.util.List;

public class Waiter implements Runnable {
    private final int id;
    private final int limitOfClientsPerWaiter;
    private final Bar bar;
    private final List<Customer> customers = new ArrayList<>(List.of());

    public Waiter(final int id, final int limitOfClientsPerWaiter, final Bar bar) {
        this.id = id;
        this.limitOfClientsPerWaiter = limitOfClientsPerWaiter;
        this.bar = bar;

        System.out.println(Util.getNow() + " - Waiter " + id + " initialized");
    }

    private boolean canWaiterServeDrinks() {
        return this.customers.size() == this.limitOfClientsPerWaiter || this.bar.allCustomersOrderedInThisRound();
    }

    public void serveOrders() {
        this.customers.forEach(customer -> {
            System.out.println(Util.getNow() + " - Waiter " + id + " is serving customer " + customer.id);
            customer.isWaiting = false;
            customer.isServed = true;

            Util.randomSleepMilli(5000);
        });

        this.customers.clear();
    }

    public void getAnOrder() {
        final Customer customer = this.bar.getAnOrder();

        if (customer != null) {
            System.out.println(Util.getNow() + " - Customer " + customer.id + " was answered by waiter " + this.id);
            this.customers.add(customer);
        }
    }

    @Override
    public void run() {
        while (this.bar.haveRounds()) {
            if (this.canWaiterServeDrinks()) {
                this.serveOrders();
            } else {
                this.getAnOrder();
            }
        }
    }
}
