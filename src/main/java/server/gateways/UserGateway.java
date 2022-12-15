package server.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import messages.home.*;
import messages.login.*;
import messages.register.Register;
import messages.register.RegisterMessage;
import server.MessagesHandler;
import server.models.User;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class UserGateway {
    public static void login(String message, MessagesHandler handler) throws JsonProcessingException {
        final Login login = handler.mapper.getMapper().readValue(message, LoginMessage.class).getLogin();
        final Optional<User> user = handler.database.login(login.getUsername(), login.getPassword());

        if (user.isPresent()) {
            user.get().setWriter(handler.os);
            user.get().setSocket(handler.socket);

            handler.broadcast(new NewConnectionMessage(handler.database.getOnlineUsersList()));

            handler.os.println(handler.mapper.getMapper().writeValueAsString(new SetUserMessage(new SetUser(user.get().getUsername(), user.get().getId()))));
            handler.os.flush();

            handler.os.println("welcome " + user.get().getUsername());
            handler.os.flush();

            final HomeUsersMessage homeUsersMessage = new HomeUsersMessage(handler.database.getOnlineUsersList());
            handler.os.println(handler.mapper.getMapper().writeValueAsString(homeUsersMessage));
            handler.os.flush();

            final HomeGroupsMessage homeGroupsMessage = new HomeGroupsMessage(handler.database.getGroupsList());
            handler.os.println(handler.mapper.getMapper().writeValueAsString(homeGroupsMessage));
            handler.os.flush();

            handler.user = user.get();
        } else {
            handler.os.println("user-not-found");
            handler.os.flush();
        }
    }

    public static void register(String message, MessagesHandler handler) throws JsonProcessingException {
        final Register register = handler.mapper.getMapper().readValue(message, RegisterMessage.class).getRegister();
        final boolean success = handler.database.register(register.getUsername(), register.getPassword());
        final Optional<User> user = handler.database.login(register.getUsername(), register.getPassword());

        if (success && user.isPresent()) {
            user.get().setWriter(handler.os);
            user.get().setSocket(handler.socket);

            handler.database.getHashMessages().put(user.get().getId(), new ArrayList<>());

            handler.broadcast(new NewConnectionMessage(handler.database.getOnlineUsersList()));

            handler.os.println(handler.mapper.getMapper().writeValueAsString(new SetUserMessage(new SetUser(user.get().getUsername(), user.get().getId()))));
            handler.os.flush();

            handler.os.println("welcome " + user.get().getUsername());
            handler.os.flush();

            final HomeUsersMessage homeUsersMessage = new HomeUsersMessage(handler.database.getOnlineUsersList());
            handler.os.println(handler.mapper.getMapper().writeValueAsString(homeUsersMessage));
            handler.os.flush();

            final HomeGroupsMessage homeGroupsMessage = new HomeGroupsMessage(handler.database.getGroupsList());
            handler.os.println(handler.mapper.getMapper().writeValueAsString(homeGroupsMessage));
            handler.os.flush();
        } else {
            handler.os.println("invalid-user");
            handler.os.flush();
        }
    }

    public static void disconnect(MessagesHandler handler) {
        if (handler.user != null) {
            handler.broadcast(
                    new DisconnectMessage(
                            handler.database.getOnlineUsersList()
                                    .stream().filter(userDTO -> !Objects.equals(userDTO.getId(), handler.user.getId()))
                                    .toList()
                    )
            );
        }
    }
}
