package server;

import java.io.*;
import java.net.Socket;

public class NewConnection extends Thread {
    private final Socket socket;
    private final ConnectionController connectionController;

    private final Counter counter;

    private BufferedReader is;
    private PrintWriter os;

    public NewConnection(Socket s, ConnectionController connectionController, Counter counter) {
        this.socket = s;
        this.connectionController = connectionController;
        this.counter = counter;

        try {
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            start(); // dispara a Thread

            System.out.println("Nova connexão: " + socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            if (!connectionController.isSocketInUse()) {
                connectionController.setSocketInUse(true);
                os.println("Bem vindo ao servidor. (Digite o número a ser adicionado no somatório)");
                os.flush();

                int intToAdd = Integer.parseInt(is.readLine());
                counter.setSum(intToAdd);

                os.println(counter.getSum());
                os.flush();
                connectionController.setSocketInUse(false);
            } else {
                os.println("Número de clientes máximo atingido, tente novamente mais tarde");
                os.flush();
            }

            is.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();

            try {
                is.close();
                os.close();
                socket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
