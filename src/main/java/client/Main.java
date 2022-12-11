package client;

import java.net.InetAddress;
import java.net.Socket;

public class Main {
    static final int port = 8181;

    public static void main(String[] args) {
        try {
            Socket socket;

            InetAddress addr = InetAddress.getByName("localhost"); // pega o ip do servidor
            socket = new Socket(addr, port);
            new Controller(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
