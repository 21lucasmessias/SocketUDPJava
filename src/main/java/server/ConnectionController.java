package server;

import java.net.Socket;

public class ConnectionController {
    private final Bank bank;

    public ConnectionController() {
        this.bank = new Bank();
    }

    public void newConnection(Socket s) {
        new NewConnection(s, bank);
    }
}
