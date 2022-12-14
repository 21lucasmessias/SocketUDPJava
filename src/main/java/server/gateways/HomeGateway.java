package server.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import dtos.ChatMessageDTO;
import messages.chat.*;
import server.MessagesHandler;
import server.models.ChatMessage;
import server.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeGateway {

    public static void privateChat(String message, MessagesHandler handler) throws IOException {
        final PrivateChatMessage privateMessage = handler.mapper.getMapper().readValue(message, PrivateChatMessage.class);
        final PrivateChat privateChat = privateMessage.getPrivateChat();

        final User from = handler.database.getUsers().get(privateChat.getFrom());
        final User to = handler.database.getUsers().get(privateChat.getTo());

        handler.database.getHashMessages().get(to.getId()).add(new ChatMessage(from.toDto(), privateChat.getContent()));

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

    public static void requestAll(String message, MessagesHandler handler) throws JsonProcessingException {
        final RequestAllChatMessage requestAllChatMessage = handler.mapper.getMapper().readValue(message, RequestAllChatMessage.class);
        final RequestAllChat requestAllChat = requestAllChatMessage.getRequestAllChat();

        final String fromId = requestAllChat.getFrom();
        final String toId = requestAllChat.getTo();

        final List<ChatMessageDTO> messagesFrom = handler.database.getPrivateHistoryMessages(fromId, toId);
        final List<ChatMessageDTO> messagesTo = handler.database.getPrivateHistoryMessages(toId, fromId);

        final List<ChatMessageDTO> messages = new ArrayList<>();
        messages.addAll(messagesFrom);
        messages.addAll(messagesTo);
        messages.sort((o1, o2) -> {
            if(o1.getCreatedAt().isBefore(o2.getCreatedAt())){
                return -1;
            }
            if(o1.getCreatedAt().equals(o2.getCreatedAt())) {
                return 0;
            }
            return 1;
        });

        handler.os.println(handler.mapper.getMapper().writeValueAsString(new ResponseAllChatMessage(messages)));
        handler.os.flush();
    }
}
