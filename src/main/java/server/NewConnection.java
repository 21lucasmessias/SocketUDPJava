package server;

import server.database.Database;
import server.helpers.Mapper;
import server.messages.LoginMessage;
import server.models.Login;
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
            os.println("welcome");
            os.flush();

            String str = is.readLine();

            while (!str.equalsIgnoreCase("end")) {
                if (str.startsWith("login")) { // login: { username: "", password: "" }
                    final Login login = mapper.getMapper().readValue(str, LoginMessage.class).getLogin();
                    final Optional<User> user = database.login(login.getUsername(), login.getPassword());
                    
                    if (user.isPresent()) {
                        os.println("welcome " + user.get().getName());
                    } else {
                        throw new NotFoundAccountException();
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
        } catch (NotFoundAccountException e) {
            os.println("user-not-found.");
            os.flush();

            os.println("end");
            os.flush();

            try {
                is.close();
                os.close();
                socket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
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
