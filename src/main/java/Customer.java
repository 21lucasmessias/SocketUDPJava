public class Customer implements Runnable {
    public final int id;
    private final Bar bar;
    public boolean canAskNewOrder;
    public boolean isServed;
    public boolean isDrinking;
    public boolean isReadyForNextRound;
    public boolean wantToJumpRound;
    public boolean isWaiting;

    public Customer(final int id, final Bar bar) {
        this.id = id;
        this.bar = bar;

        this.canAskNewOrder = true;
        this.isDrinking = false;
        this.isServed = false;

        this.isReadyForNextRound = false;

        System.out.println(Util.getNow() + " - Customer " + id + " initialized");
    }

    public void drink() {
        System.out.println(Util.getNow() + " - Customer " + id + " started to drink");
        this.isDrinking = true;

        Util.randomSleepMilli(10000);

        System.out.println(Util.getNow() + " - Customer " + id + " stopped to drink");
        this.isDrinking = false;

        this.isReadyForNextRound = true;
    }

    private boolean randomlyWannaDrink() {
        int max = 10;
        int min = 1;
        int range = max - min + 1;

        return ((int) (Math.random() * range) + min) > 5;
    }

    @Override
    public void run() {
        while (this.bar.haveRounds()) {
            if (this.isWaiting) {
                continue;
            }

            if (this.isServed && !this.isReadyForNextRound) {
                this.drink();
            }

            final boolean customerCanAndWantToDrink = this.canAskNewOrder && this.randomlyWannaDrink();

            if (customerCanAndWantToDrink) {
                if (this.wantToJumpRound) {
                    this.bar.customerWantToOrderNow(this);
                }
                this.wantToJumpRound = false;
                this.canAskNewOrder = false;
                this.isWaiting = true;
                this.bar.enterOnQueueOfOrders(this);

                continue;
            }

            if (this.canAskNewOrder) {
                if(!this.wantToJumpRound) {
                    this.bar.dontWantToOrderNow(this);
                }
                this.wantToJumpRound = true;
            }

            Util.randomSleepMilli(10000);
        }
    }
}
