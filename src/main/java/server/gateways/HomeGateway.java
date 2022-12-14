package server.gateways;

import messages.chat.PrivateChat;
import messages.chat.PrivateChatMessage;
import server.MessagesHandler;
import server.models.ChatMessage;
import server.models.User;

import java.io.IOException;

public class HomeGateway {

    public static void privateChat(String message, MessagesHandler handler) throws IOException {
        final PrivateChatMessage privateMessage = handler.mapper.getMapper().readValue(message, PrivateChatMessage.class);
        final PrivateChat privateChat = privateMessage.getPrivateChat();

        final User from = handler.database.getUsers().get(privateChat.getFrom());
        final User to = handler.database.getUsers().get(privateChat.getTo());

        handler.database.getMessages().put(from.getId(), new ChatMessage(to, privateChat.getContent()));
        handler.database.getMessages().put(to.getId(), new ChatMessage(from, privateChat.getContent()));

        final String formattedMessage = from.getUsername() + " - " + privateChat.getContent();
        privateMessage.getPrivateChat().setContent(formattedMessage);

        if (to.getSocket() != null && !to.getSocket().isClosed()) {
            to.getWriter().println(handler.mapper.getMapper().writeValueAsString(privateMessage));
            to.getWriter().flush();
        }

        if (from.getSocket() != null && !from.getSocket().isClosed()) {
            from.getWriter().println(handler.mapper.getMapper().writeValueAsString(privateMessage));
            from.getWriter().flush();
        }
    }
}
