package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static final int porta = 8181;

    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(porta);
            System.out.println("Servidor de cadastro iniciado na porta " + s);

            FortuneDB fortuneDb = new FortuneDB();

            while (true) {
                Socket socket = s.accept();
                new NewConnection(socket, fortuneDb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
