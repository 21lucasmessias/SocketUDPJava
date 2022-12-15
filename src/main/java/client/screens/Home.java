package client.screens;

import client.Controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import dtos.ChatMessageDTO;
import dtos.GroupDTO;
import dtos.UserDTO;
import messages.chat.*;
import messages.chat.group.CreateGroup;
import messages.chat.group.CreateGroupMessage;
import messages.chat.group.CreateGroupResponseMessage;
import messages.chat.individual.PrivateChat;
import messages.chat.individual.PrivateChatMessage;
import messages.login.DisconnectMessage;
import messages.home.HomeGroupsMessage;
import messages.home.HomeUsersMessage;
import messages.login.NewConnectionMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Home extends Screen {
    private final Controller controller;
    private JPanel container;
    private JTextField chatField;
    private JButton sendButton;
    private JPanel messagesContainer;
    private JLabel targetLabel;
    private JList<String> listOfUsers;
    private JList<String> listOfGroups;
    private JTextField newGroupInput;
    private JButton createGroupButton;
    private String selectedUserId;

    public Home(Controller controller) {
        this.controller = controller;

        this.setContentPane(this.container);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1080, 720);
        this.setTitle(this.controller.getUser().getUsername());

        GridLayout messagesLayout = new GridLayout(0, 1, 0, 4);
        this.messagesContainer.setLayout(messagesLayout);

        sendButton.addActionListener(e -> {
            if (!targetLabel.getText().equals("Select someone to chat")) {
                try {
                    sendMessage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        listOfUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                sendPrivateMessage();
            }
        });

        createGroupButton.addActionListener(e -> {
            if (newGroupInput.getText().isBlank() || newGroupInput.getText().isEmpty()) {
                return;
            }

            try {
                createGroup();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void createGroup() throws JsonProcessingException {
        final CreateGroupMessage createGroupMessage = new CreateGroupMessage(new CreateGroup(newGroupInput.getText()));

        this.controller.getWriter().println(this.controller.getMapper().writeValueAsString(createGroupMessage));
        this.controller.getWriter().flush();

        newGroupInput.setText("");
    }

    private void sendPrivateMessage() {
        final String selectedOption = listOfUsers.getSelectedValue();
        final String targetId = selectedOption.split(" - ")[0];
        final String targetName = selectedOption.split(" - ")[1];

        final RequestAllChat requestAllChat = new RequestAllChat(controller.getUser().getId(), targetId);
        final RequestAllChatMessage requestAllChatMessage = new RequestAllChatMessage(requestAllChat);

        try {
            controller.getWriter().println(controller.getMapper().writeValueAsString(requestAllChatMessage));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        controller.getWriter().flush();

        messagesContainer.removeAll();

        targetLabel.setText(targetName);
        setSelectedUserId(targetId);
    }

    private void sendMessage() throws JsonProcessingException {
        this.controller.getWriter().println(this.controller.getMapper().writeValueAsString(
                new PrivateChatMessage(
                        new PrivateChat(
                                this.controller.getUser().getId(),
                                getSelectedUserId(),
                                chatField.getText()
                        )
                )
        ));

        this.controller.getWriter().flush();

        chatField.setText("");
    }

    @Override
    public void handleMessage(String message) {
        try {
            if (message.startsWith("{\"homeUsers")) {
                final HomeUsersMessage homeUsersMessage = this.controller.getMapper().readValue(message, HomeUsersMessage.class);
                final List<String> users = new ArrayList<>(List.of());

                for (UserDTO user : homeUsersMessage.getHomeUsersMessage()) {
                    if (!user.getId().equals(this.controller.getUser().getId())) {
                        users.add(user.getId() + " - " + user.getUsername());
                    }
                }

                this.listOfUsers.setListData(users.toArray(String[]::new));
            } else if (message.startsWith("{\"homeGroups")) {
                final HomeGroupsMessage homeGroupsMessage = this.controller.getMapper().readValue(message, HomeGroupsMessage.class);
                final List<String> groups = new ArrayList<>(List.of());

                for (GroupDTO group : homeGroupsMessage.getHomeGroupsMessage()) {
                    groups.add(group.getId() + " - " + group.getName());
                }

                listOfGroups.setListData(groups.toArray(String[]::new));
            } else if (message.startsWith("{\"newConnection")) {
                final NewConnectionMessage newConnectionMessage = this.controller.getMapper().readValue(message, NewConnectionMessage.class);
                final List<String> users = new ArrayList<>(List.of());

                for (UserDTO user : newConnectionMessage.getNewConnectionMessage()) {
                    if (!user.getId().equals(this.controller.getUser().getId())) {
                        users.add(user.getId() + " - " + user.getUsername());
                    }
                }

                this.listOfUsers.setListData(users.toArray(String[]::new));
            } else if (message.startsWith("{\"disconnect")) {
                final DisconnectMessage newConnectionMessage = this.controller.getMapper().readValue(message, DisconnectMessage.class);
                final List<String> users = new ArrayList<>(List.of());

                for (UserDTO user : newConnectionMessage.getDisconnectMessage()) {
                    if (!user.getId().equals(this.controller.getUser().getId())) {
                        users.add(user.getId() + " - " + user.getUsername());
                    }
                }

                this.listOfUsers.setListData(users.toArray(String[]::new));
            } else if (message.startsWith("{\"privateChat")) {
                final PrivateChatMessage privateChatMessage = this.controller.getMapper().readValue(message, PrivateChatMessage.class);
                final PrivateChat privateChat = privateChatMessage.getPrivateChat();

                if (privateChat.getFrom().equals(getSelectedUserId()) || controller.getUser().getId().equals(privateChat.getFrom())) {
                    final String newMessage = privateChatMessage.getPrivateChat().getContent();
                    final JLabel textComponent = new JLabel(newMessage);

                    messagesContainer.add(textComponent);
                    messagesContainer.validate();
                    messagesContainer.repaint();
                }
            } else if (message.startsWith("{\"responseAllChat")) {
                final ResponseAllChatMessage responseAllChatMessage = this.controller.getMapper().readValue(message, ResponseAllChatMessage.class);
                final List<ChatMessageDTO> messages = responseAllChatMessage.getResponseAllChat();

                for (ChatMessageDTO newMessage : messages) {
                    final JLabel textComponent = new JLabel(newMessage.getUser().getUsername() + " - " + newMessage.getContent());
                    messagesContainer.add(textComponent);
                }

                messagesContainer.validate();
                messagesContainer.repaint();
            } else if (message.startsWith("{\"createGroupResponse")) {
                final CreateGroupResponseMessage createGroupResponseMessage = this.controller.getMapper().readValue(message, CreateGroupResponseMessage.class);
                final List<String> groups = new ArrayList<>(List.of());

                for (GroupDTO group : createGroupResponseMessage.getCreateGroupResponse().getGroups()) {
                    groups.add(group.getId() + " - " + group.getName());
                }

                listOfGroups.setListData(groups.toArray(String[]::new));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }
}
