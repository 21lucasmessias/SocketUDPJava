package server;

import server.database.DB;

import java.io.*;
import java.net.Socket;

public class NewConnection extends Thread {
    private final Socket socket;
    private final ConnectionController connectionController;

    private final server.database.DB DB;

    private BufferedReader is;
    private PrintWriter os;

    public NewConnection(Socket s, ConnectionController connectionController, DB DB) {
        this.socket = s;
        this.connectionController = connectionController;
        this.DB = DB;

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
       /* try {
            if (!connectionController.isSocketInUse()) {
                connectionController.setSocketInUse(true);

                DB.refresh();

                os.println("Bem vindo ao banco. Digite FIM para sair.");
                os.flush();

                os.println("Digite o número da conta");
                os.flush();

                String str = is.readLine();

                //DB.login(str);

                os.println("Login realizado com sucesso");
                os.flush();

                str = is.readLine();

                while (!str.equalsIgnoreCase("FIM")) {
                    try {
                        if (str.startsWith("depositar")) {
                            final Double value = Double.parseDouble(str.split(" ")[1]);

                            if (DB.deposit(value)) {
                                os.println("Valor depositado com sucesso, novo saldo: R$" + DB.currentValue());
                            } else {
                                os.println("Valor incorreto");

                            }
                        } else if (str.startsWith("sacar")) {
                            final Double value = Double.parseDouble(str.split(" ")[1]);

                            if (DB.withdraw(value)) {
                                os.println("Retirado: R$" + value);
                            } else {
                                os.println("Saldo insuficiente");
                            }
                        } else if (str.startsWith("checar")) {
                            final Double value = DB.currentValue();

                            os.println("Saldo: R$" + value);
                        } else {
                            throw new Exception();
                        }
                    } catch (Exception e) {
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

            os.println("fim");
            os.flush();

            is.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            connectionController.setSocketInUse(false);

            os.println("fim");
            os.flush();

            try {
                is.close();
                os.close();
                socket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (NotFoundAccountException e) {
            connectionController.setSocketInUse(false);

            os.println("Numero de conta não encontrado.");
            os.flush();

            os.println("fim");
            os.flush();

            try {
                is.close();
                os.close();
                socket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }*/
    }
}
