package server;

import server.database.DB;

import java.net.Socket;

public class ConnectionController {
    private final DB DB;

    public ConnectionController() {
        this.DB = new DB();
    }

    public void newConnection(Socket s) {
        new NewConnection(s, this, DB);
    }
}
