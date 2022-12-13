package server.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.Mapper;
import messages.home.HomeGroupsMessage;
import messages.home.HomeUsersMessage;
import messages.login.Login;
import messages.login.LoginMessage;
import messages.login.SetUser;
import messages.login.SetUserMessage;
import messages.register.Register;
import messages.register.RegisterMessage;
import server.database.Database;
import server.models.User;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

public class UserGateway {
    public static void login(String message, Mapper mapper, Database database, PrintWriter os, Socket socket) throws JsonProcessingException {
        final Login login = mapper.getMapper().readValue(message, LoginMessage.class).getLogin();
        final Optional<User> user = database.login(login.getUsername(), login.getPassword());

        if (user.isPresent()) {
            user.get().setWriter(os);
            user.get().setSocket(socket);

            os.println(mapper.getMapper().writeValueAsString(new SetUserMessage(new SetUser(user.get().getUsername(), user.get().getId()))));
            os.flush();

            os.println("welcome " + user.get().getUsername());
            os.flush();

            final HomeUsersMessage homeUsersMessage = new HomeUsersMessage(database.getUsersList());
            os.println(mapper.getMapper().writeValueAsString(homeUsersMessage));

            os.flush();

            final HomeGroupsMessage homeGroupsMessage = new HomeGroupsMessage(database.getGroups());
            os.println(mapper.getMapper().writeValueAsString(homeGroupsMessage));
        } else {
            os.println("user-not-found");
        }
    }

    public static void register(String message, Mapper mapper, Database database, PrintWriter os, Socket socket) throws JsonProcessingException {
        final Register register = mapper.getMapper().readValue(message, RegisterMessage.class).getRegister();
        final boolean success = database.register(register.getUsername(), register.getPassword());
        final Optional<User> user = database.login(register.getUsername(), register.getPassword());

        if (success && user.isPresent()) {
            user.get().setWriter(os);
            user.get().setSocket(socket);

            os.println(mapper.getMapper().writeValueAsString(new SetUserMessage(new SetUser(user.get().getUsername(), user.get().getId()))));
            os.flush();

            os.println("welcome " + user.get().getUsername());
            os.flush();

            final HomeUsersMessage homeUsersMessage = new HomeUsersMessage(database.getUsersList());
            os.println(mapper.getMapper().writeValueAsString(homeUsersMessage));

            os.flush();

            final HomeGroupsMessage homeGroupsMessage = new HomeGroupsMessage(database.getGroups());
            os.println(mapper.getMapper().writeValueAsString(homeGroupsMessage));
        } else {
            os.println("invalid-user");
        }
    }
}
