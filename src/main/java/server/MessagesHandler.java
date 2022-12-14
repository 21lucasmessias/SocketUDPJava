package server;

import helpers.Mapper;
import messages.Message;
import server.database.Database;
import server.gateways.HomeGateway;
import server.gateways.UserGateway;
import server.models.User;

import java.io.*;
import java.net.Socket;

public class MessagesHandler extends Thread {
    public final Socket socket;
    public final Database database;
    public final Mapper mapper;
    public BufferedReader is;
    public PrintWriter os;

    public User user;

    public MessagesHandler(Socket s, Facade dependencyInjector) {
        this.socket = s;
        this.database = dependencyInjector.getDB();
        this.mapper = new Mapper();

        try {
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            start();

            System.out.println("Nova connexão: " + socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcast(Message message) {
        database.getOnlineUsersList().forEach(u -> {
            try {
                final User _user = database.getUsers().get(u.getId());
                _user.getWriter().println(mapper.getMapper().writeValueAsString(message));
                _user.getWriter().flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void run() {
        try {
            os.println("connected");
            os.flush();

            String str = is.readLine();

            while (!str.equalsIgnoreCase("end")) {
                System.out.println(str);

                if (str.startsWith("{\"login")) { // {"login":{"username":"lucas","password":"123456"}}
                    UserGateway.login(str, this);
                } else if (str.startsWith("{\"register")) { // {"register":{"username":"lucas","password":"123456"}}
                    UserGateway.register(str, this);
                } else if (str.startsWith("{\"privateChat")) { // {"privateChat":{"from":"123","to":"456","content":"ablublé"}}
                    HomeGateway.privateChat(str, this);
                } else {
                    throw new Exception();
                }
                str = is.readLine();
            }

            UserGateway.disconnect(this);

            os.println("end");
            os.flush();

            is.close();
            os.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();

            UserGateway.disconnect(this);

            os.println("end");
            os.flush();

            try {
                is.close();
                os.close();
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
