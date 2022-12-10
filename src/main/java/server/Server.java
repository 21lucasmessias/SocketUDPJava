package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static final int porta = 8181;

    public static void main(String[] args) {
        ConnectionController controller = new ConnectionController();

        try {
            ServerSocket s = new ServerSocket(porta);
            System.out.println("Servidor iniciado na porta " + s);

            while (true) {
                Socket socket = s.accept();
                controller.newConnection(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
