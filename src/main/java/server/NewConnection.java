package server;

import helpers.Mapper;
import messages.login.Login;
import messages.login.LoginMessage;
import server.database.Database;
import server.models.User;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

public class NewConnection extends Thread {
    private final Socket socket;
    private final Database database;
    private final Mapper mapper;
    private BufferedReader is;
    private PrintWriter os;

    public NewConnection(Socket s, Facade dependencyInjector) {
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
                    final Login login = mapper.getMapper().readValue(str, LoginMessage.class).getLogin();
                    final Optional<User> user = database.login(login.getUsername(), login.getPassword());

                    if (user.isPresent()) {
                        os.println("welcome " + user.get().getName());
                    } else {
                        os.println("user-not-found");
                    }
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
