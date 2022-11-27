package server;

import java.io.*;
import java.net.Socket;

public class NewConnection extends Thread {
    private Socket socket;
    private BufferedReader is;
    private PrintWriter os;

    public NewConnection(Socket s) {
        this.socket = s;
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
            os.println("Bem vindo ao servidor. (Digite fim para encerrar a conexao)");
            os.flush();

            int cont = 0;
            String str = is.readLine();

            System.out.println("Mesangem recebida: " + str);

            while (!str.toUpperCase().equals("FIM")) {
                os.println("Oi, voce escreveu " + str + " e esta é a " + (cont++) + " resposta que estou mandando.");
                os.flush();
                str = is.readLine();

                System.out.println("Mesangem recebida: " + str);
            }

            System.out.println("Finalizando conexão do cliente: " + socket);

            is.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
