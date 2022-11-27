package server;

import java.io.*;
import java.net.Socket;

public class NewConnection extends Thread {
    private final Socket socket;
    private BufferedReader is;
    private PrintWriter os;
    private FortuneDB fortuneDB;

    public NewConnection(Socket s, FortuneDB fortuneDB) {
        this.socket = s;
        this.fortuneDB = fortuneDB;

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
            os.println("Bem vindo ao servidor fortune. (Digite fim para encerrar a conexao)");
            os.flush();

            int cont = 0;
            String str = is.readLine();

            System.out.println("Mesangem recebida: " + str);

            while (!str.toUpperCase().equals("FIM")) {
                try {
                    if(str.startsWith("GET-FORTUNE")) {
                        os.println(fortuneDB.getRandomFortune());
                    } else if (str.startsWith("SET-FORTUNE")) {
                        final int firstSeparator = str.indexOf("\\n");
                        final int lastSeparator = str.lastIndexOf("\\n");

                        if(firstSeparator == -1 || lastSeparator == -1) {
                            throw new Exception("Separadores não encontrados");
                        }

                        final int fortuneIndex = fortuneDB.getRandomFortuneIndex();
                        final String newFortune = str.substring(firstSeparator + 2, lastSeparator);
                        final String oldFortune = fortuneDB.updateFortune(fortuneIndex, newFortune);

                        os.println(oldFortune + " foi sobreescrito por: " + newFortune);
                    } else {
                        os.println("Mensagem não aceita.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    os.println("Houve um problema ao interpretar sua mensagem");
                }

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
