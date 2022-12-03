package server;

import java.net.Socket;

public class ConnectionController {

    private boolean isSocketInUse = false;

    private Counter counter;

    public ConnectionController() {
        this.counter = new Counter();
    }

    public void newConnection(Socket s) {
        new NewConnection(s, this, counter);
    }

    public boolean isSocketInUse() {
        return isSocketInUse;
    }

    public void setSocketInUse(boolean socketInUse) {
        isSocketInUse = socketInUse;
    }
}
