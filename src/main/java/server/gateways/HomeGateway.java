package server.gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import dtos.ChatMessageDTO;
import messages.chat.*;
import messages.chat.group.*;
import messages.chat.individual.PrivateChat;
import messages.chat.individual.PrivateChatMessage;
import server.MessagesHandler;
import server.models.ChatMessage;
import server.models.Group;
import server.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeGateway {

    public synchronized static void privateChat(String message, MessagesHandler handler) throws IOException {
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

    public synchronized static void groupChat(String message, MessagesHandler handler) throws JsonProcessingException {
        final GroupChatMessage groupChatMessage = handler.mapper.getMapper().readValue(message, GroupChatMessage.class);
        final GroupChat groupChat = groupChatMessage.getGroupChat();

        final User from = handler.database.getUsers().get(groupChat.getFrom());
        final Group to = handler.database.getGroups().get(groupChat.getTo());

        handler.database.getHashMessages().get(to.getId()).add(new ChatMessage(from.toDto(), groupChat.getContent()));

        final String formattedMessage = from.getUsername() + " - " + groupChat.getContent();
        groupChatMessage.getGroupChat().setContent(formattedMessage);

        handler.broadcastToGroup(to.getId(), groupChatMessage);
    }

    public synchronized static void requestAll(String message, MessagesHandler handler) throws JsonProcessingException {
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
            if (o1.getCreatedAt().isBefore(o2.getCreatedAt())) {
                return -1;
            }
            if (o1.getCreatedAt().equals(o2.getCreatedAt())) {
                return 0;
            }
            return 1;
        });

        handler.os.println(handler.mapper.getMapper().writeValueAsString(new ResponseAllChatMessage(messages)));
        handler.os.flush();
    }

    public synchronized static void createGroup(String message, MessagesHandler handler) throws JsonProcessingException {
        final CreateGroupMessage createGroupMessage = handler.mapper.getMapper().readValue(message, CreateGroupMessage.class);
        final CreateGroup createGroup = createGroupMessage.getCreateGroupMessage();
        final Group newGroup = Group.from(createGroup.getName(), handler.user.toDto());

        handler.database.getGroups().put(newGroup.getId(), newGroup);
        handler.database.getHashMessages().put(newGroup.getId(), new ArrayList<>(List.of()));

        handler.broadcast(new CreateGroupResponseMessage(new CreateGroupResponse(handler.database.getGroupsList())));
    }

    public synchronized static void joinGroup(String message, MessagesHandler handler) throws JsonProcessingException {
        final JoinGroupMessage joinGroupMessage = handler.mapper.getMapper().readValue(message, JoinGroupMessage.class);
        final JoinGroup joinGroup = joinGroupMessage.getJoinGroup();

        final String userId = joinGroup.getUserId();
        final String groupId = joinGroup.getGroupId();

        final User user = handler.database.getUsers().get(userId);
        final Group group = handler.database.getGroups().get(groupId);
        if (group != null) {
            if (group.getUsers().stream().noneMatch(userDTO -> userDTO.getId().equals(user.getId()))) {
                group.getUsers().add(user.toDto());
            }
        }

        final List<ChatMessageDTO> messagesGroup = handler.database.getGroupHistoryMessages(groupId);

        messagesGroup.sort((o1, o2) -> {
            if (o1.getCreatedAt().isBefore(o2.getCreatedAt())) {
                return -1;
            }
            if (o1.getCreatedAt().equals(o2.getCreatedAt())) {
                return 0;
            }
            return 1;
        });

        handler.os.println(handler.mapper.getMapper().writeValueAsString(new ResponseJoinGroupMessage(messagesGroup)));
        handler.os.flush();
    }

}
