package server;

import helpers.Mapper;
import server.database.Database;
import server.models.User;

import java.io.*;
import java.net.Socket;

public class MessagesHandler extends Thread {
    private final Socket socket;
    private final Database database;
    private final Mapper mapper;
    private BufferedReader is;
    private PrintWriter os;

    public MessagesHandler(Socket s, Facade dependencyInjector) {
        this.socket = s;
        this.database = dependencyInjector.getDB();
        this.mapper = new Mapper();

        try {
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            start();

            System.out.println("Nova connexão: " + socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            os.println("connected");
            os.flush();

            String str = is.readLine();

            while (!str.equalsIgnoreCase("end")) {
                System.out.println(str);

                if (str.startsWith("{\"login")) { // {"login":{"username":"lucas","password":"123456"}}
                    User.Login(str, mapper, database, os);
                } else {
                    throw new Exception();
                }

                os.flush();
                str = is.readLine();
            }

            os.println("end");
            os.flush();

            is.close();
            os.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();

            os.println("end");
            os.flush();

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
