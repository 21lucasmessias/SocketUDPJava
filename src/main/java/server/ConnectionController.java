package server;

import java.net.Socket;

public class ConnectionController {

    private boolean isSocketInUse = false;

    private final Bank bank;

    public ConnectionController() {
        this.bank = new Bank();
    }

    public void newConnection(Socket s) {
        new NewConnection(s, this, bank);
    }

    public boolean isSocketInUse() {
        return isSocketInUse;
    }

    public void setSocketInUse(boolean socketInUse) {
        isSocketInUse = socketInUse;
    }
}
