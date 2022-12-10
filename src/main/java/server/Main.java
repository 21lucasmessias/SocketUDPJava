package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    static final int porta = 8181;

    public static void main(String[] args) {
        Facade facade = new Facade();

        try {
            ServerSocket s = new ServerSocket(porta);
            System.out.println("Servidor iniciado na porta " + s);

            while (true) {
                Socket socket = s.accept();
                facade.newConnection(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
