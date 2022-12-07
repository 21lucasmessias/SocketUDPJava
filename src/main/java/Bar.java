import java.util.ArrayList;
import java.util.List;

public class Bar {
    private final int quantityOfCustomers;
    private final int quantityOfWaiters;
    private final int limitOfClientsPerWaiter;
    private int numberOfRounds;
    private int ordersInThisRound;
    private final List<Customer> customers = new ArrayList<>(List.of());
    private final List<Customer> orders = new ArrayList<>(List.of());
    private final List<Thread> threads = new ArrayList<>(List.of());

    public Bar(final int quantityOfCustomers, final int quantityOfWaiters, final int limitOfClientsPerWaiter, final int numberOfRounds) {
        this.quantityOfCustomers = quantityOfCustomers;
        this.quantityOfWaiters = quantityOfWaiters;
        this.limitOfClientsPerWaiter = limitOfClientsPerWaiter;
        this.numberOfRounds = numberOfRounds;
        this.ordersInThisRound = 0;

        System.out.println(Util.getNow() + " - Bar initialized");

    }

    public void run() {
        this.initializeWaiters();
        this.initializeCustomers();

        while(true) {
            if(canGoToTheNextRound()) {
                this.numberOfRounds--;
                if(!this.haveRounds()) {
                    break;
                }

                System.out.println(Util.getNow() + " - Next round started");

                this.ordersInThisRound = 0;

                this.customers.forEach(customer -> {
                    customer.canAskNewOrder = true;
                    customer.isReadyForNextRound = false;
                    customer.wantToJumpRound = false;
                    customer.isServed = false;
                });
            }
        }

        System.out.println(Util.getNow() + " - End of rounds");

        System.out.println(Util.getNow() + " - Waiting customers to leave");

        for (Thread thread: this.threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(Util.getNow() + " - Closing bar");
    }

    private synchronized void followThread(Thread thread) {
        this.threads.add(thread);
    }

    private void initializeWaiters() {
        for (int i = 0; i < quantityOfWaiters; i++) {
            final var thread = new Thread(new Waiter(i, this.limitOfClientsPerWaiter, this));
            thread.start();
            this.followThread(thread);
        }
    }

    private void initializeCustomers() {
        for (int i = 0; i < quantityOfCustomers; i++) {
            final var customer = new Customer(i, this);
            final var thread = new Thread(customer);
            thread.start();
            this.customers.add(customer);
            this.followThread(thread);
        }
    }

    public synchronized boolean haveRounds() {
        return this.numberOfRounds > 0;
    }

    public synchronized boolean canGoToTheNextRound() {
        return this.customers.stream().allMatch(customer -> customer.isReadyForNextRound || customer.wantToJumpRound || customer.isDrinking);
    }

    public synchronized void enterOnQueueOfOrders(final Customer customer) {
        this.ordersInThisRound++;
        this.orders.add(customer);
        System.out.println(Util.getNow() + " - Customer " + customer.id + " entered in the order queue");
    }

    public synchronized void dontWantToOrderNow(final Customer customer) {
        this.ordersInThisRound++;
        System.out.println(Util.getNow() + " - Customer " + customer.id + " said he dont want to drink right now");
    }

    public synchronized void customerWantToOrderNow(final Customer customer) {
        this.ordersInThisRound--;
        System.out.println(Util.getNow() + " - Customer " + customer.id + " said he want to drink");
    }

    public synchronized Customer getAnOrder() {
        if(this.orders.size() > 0) {
            return this.orders.remove(0);
        }

        return null;
    }

    public synchronized boolean allCustomersOrderedInThisRound() {
        return this.ordersInThisRound == this.quantityOfCustomers && this.orders.size() == 0;
    }
}
