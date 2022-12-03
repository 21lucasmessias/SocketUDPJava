package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    static final int port = 8181;

    public static void main(String[] args) {
        try {
            Socket socket;

            InetAddress addr = InetAddress.getByName("localhost"); // pega o ip do servidor
            socket = new Socket(addr, port);

            System.out.println("Socket:" + socket);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())));

            // Recebe as boas vindas
            String s = in.readLine();
            System.out.println(s);

            if(s.equals("Número de clientes máximo atingido, tente novamente mais tarde")) {
                in.close();
                out.close();
                socket.close();
                return;
            }

            // interage com o usuario
            BufferedReader keyboard = new BufferedReader(
                    new InputStreamReader(System.in));

            s = keyboard.readLine();

            out.println(s); // manda o que o usuario escreveu
            out.flush();

            s = in.readLine(); // pega a resposta do servidor
            System.out.println(s); // mostra para o usuario

            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
