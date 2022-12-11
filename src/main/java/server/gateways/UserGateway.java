package server.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.Mapper;
import messages.home.HomeGroupsMessage;
import messages.home.HomeUsersMessage;
import messages.login.Login;
import messages.login.LoginMessage;
import messages.register.Register;
import messages.register.RegisterMessage;
import server.database.Database;
import server.models.User;

import java.io.PrintWriter;
import java.util.Optional;

public class UserGateway {
    public static void login(String message, Mapper mapper, Database database, PrintWriter os) throws JsonProcessingException {
        final Login login = mapper.getMapper().readValue(message, LoginMessage.class).getLogin();
        final Optional<User> user = database.login(login.getUsername(), login.getPassword());

        if (user.isPresent()) {
            os.println("welcome " + user.get().getUsername());

            os.flush();

            final HomeUsersMessage homeUsersMessage = new HomeUsersMessage(database.getUsers());
            os.println(mapper.getMapper().writeValueAsString(homeUsersMessage));

            os.flush();

            final HomeGroupsMessage homeGroupsMessage = new HomeGroupsMessage(database.getGroups());
            os.println(mapper.getMapper().writeValueAsString(homeGroupsMessage));
        } else {
            os.println("user-not-found");
        }
    }

    public static void register(String message, Mapper mapper, Database database, PrintWriter os) throws JsonProcessingException {
        final Register register = mapper.getMapper().readValue(message, RegisterMessage.class).getRegister();
        final boolean success = database.register(register.getUsername(), register.getPassword());

        if (success) {
            os.println("welcome");
        } else {
            os.println("invalid-user");
        }
    }
}
