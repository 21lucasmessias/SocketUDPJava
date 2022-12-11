package server;

import server.database.Database;

import java.net.Socket;

public class Facade {
    private final Database Database;

    public Facade() {
        this.Database = new Database();
    }

    public void newConnection(Socket s) {
        new MessagesHandler(s, this);
    }

    public Database getDB() {
        return Database;
    }
}
