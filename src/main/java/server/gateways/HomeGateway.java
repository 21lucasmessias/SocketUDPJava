package server.gateways;

import helpers.Mapper;
import messages.chat.PrivateChat;
import messages.chat.PrivateChatMessage;
import server.database.Database;
import server.models.ChatMessage;
import server.models.User;

import java.io.IOException;

public class HomeGateway {

    public static void privateChat(String message, Mapper mapper, Database database) throws IOException {
        final PrivateChatMessage privateMessage = mapper.getMapper().readValue(message, PrivateChatMessage.class);
        final PrivateChat privateChat = privateMessage.getPrivateChat();

        final User from = database.getUsers().get(privateChat.getFrom());
        final User to = database.getUsers().get(privateChat.getTo());

        if (to.getSocket() != null) {
            database.getMessages().put(from.getId(), new ChatMessage(to, privateChat.getContent()));
            database.getMessages().put(to.getId(), new ChatMessage(from, privateChat.getContent()));

            if(!to.getSocket().isClosed()) {
                to.getWriter().println(mapper.getMapper().writeValueAsString(privateMessage));
            }
        }
    }
}
