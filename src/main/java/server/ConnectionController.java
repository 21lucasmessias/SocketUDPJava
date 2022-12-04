package server;

import java.net.Socket;

public class ConnectionController {

    private boolean isSocketInUse = false;

    private final Hangman hangman;

    public ConnectionController() {
        this.hangman = new Hangman();
    }

    public void newConnection(Socket s) {
        new NewConnection(s, this, hangman);
    }

    public boolean isSocketInUse() {
        return isSocketInUse;
    }

    public void setSocketInUse(boolean socketInUse) {
        isSocketInUse = socketInUse;
    }
}
