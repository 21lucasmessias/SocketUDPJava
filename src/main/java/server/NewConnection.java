package server;

import java.io.*;
import java.net.Socket;

public class NewConnection extends Thread {
    private final Socket socket;
    private final ConnectionController connectionController;

    private final Hangman hangman;

    private BufferedReader is;
    private PrintWriter os;

    public NewConnection(Socket s, ConnectionController connectionController, Hangman hangman) {
        this.socket = s;
        this.connectionController = connectionController;
        this.hangman = hangman;

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

                hangman.refresh();

                os.println("Bem vindo ao jogo da forca. Digite FIM para sair.");
                os.flush();

                os.println("Tamanho da palavra: " + hangman.getPartialWord().length());
                os.flush();

                String str = is.readLine();

                while (!str.toUpperCase().equals("FIM")) {
                    try {
                        if(str.length() != 1) {
                            os.println("Precisamos de uma letra por vez");
                        } else {
                            hangman.characters.add(str);

                            if(hangman.hasCharInWord(str)) {
                                os.println("Letra encontrada na palavra: " + hangman.getPartialWord());
                            } else {
                                os.println("Letra não encontrada na palavra: " + hangman.getPartialWord());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        os.println("Houve um problema ao interpretar sua mensagem");
                    } finally {
                        os.flush();
                        str = is.readLine();
                    }
                }

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
            connectionController.setSocketInUse(false);

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
