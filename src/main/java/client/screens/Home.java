package client.screens;

import client.Controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import dtos.ChatMessageDTO;
import dtos.GroupDTO;
import dtos.UserDTO;
import messages.chat.*;
import messages.chat.group.*;
import messages.chat.individual.PrivateChat;
import messages.chat.individual.PrivateChatMessage;
import messages.login.DisconnectMessage;
import messages.home.HomeGroupsMessage;
import messages.home.HomeUsersMessage;
import messages.login.NewConnectionMessage;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    private String selectedChatId;
    private Boolean isGroup = false;

    public Home(Controller controller) {
        this.controller = controller;

        this.setContentPane(this.container);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1080, 720);
        this.setTitle(this.controller.getUser().getUsername());

        GridLayout messagesLayout = new GridLayout(0, 1, 0, 4);
        this.messagesContainer.setLayout(messagesLayout);

        sendButton.addActionListener(e -> {
            sendMessage();
        });

        createGroupButton.addActionListener(e -> {
            createGroup();
        });

        listOfGroups.addListSelectionListener(evt -> {
            if (evt.getValueIsAdjusting()) {
                openGroupChat();
            }
        });

        listOfUsers.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                openPrivateChat();
            }
        });
    }

    private void createGroup() {
        if (newGroupInput.getText().isBlank() || newGroupInput.getText().isEmpty()) {
            return;
        }

        try {
            final CreateGroupMessage createGroupMessage = new CreateGroupMessage(new CreateGroup(newGroupInput.getText()));
            this.controller.getWriter().println(this.controller.getMapper().writeValueAsString(createGroupMessage));
            this.controller.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        newGroupInput.setText("");
    }

    private void openPrivateChat() {
        final String selectedOption = listOfUsers.getSelectedValue();

        if (selectedOption != null) {
            listOfGroups.clearSelection();

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
            setSelectedChatId(targetId);
            setGroup(false);
        }
    }

    private void openGroupChat() {
        final String selectedOption = listOfGroups.getSelectedValue();

        if (selectedOption != null) {
            listOfUsers.clearSelection();
            final String groupId = selectedOption.split(" - ")[0];
            final String groupName = selectedOption.split(" - ")[1];

            final JoinGroup joinGroup = new JoinGroup(controller.getUser().getId(), groupId);
            final JoinGroupMessage joinGroupMessage = new JoinGroupMessage(joinGroup);

            try {
                controller.getWriter().println(controller.getMapper().writeValueAsString(joinGroupMessage));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
            controller.getWriter().flush();

            messagesContainer.removeAll();

            targetLabel.setText(groupName);
            setSelectedChatId(groupId);
            setGroup(true);
        }
    }

    private void sendMessage() {
        if (!targetLabel.getText().equals("Select someone to chat")) {
            try {
                if (isGroup) {
                    this.controller.getWriter().println(this.controller.getMapper().writeValueAsString(
                            new GroupChatMessage(
                                    new GroupChat(
                                            this.controller.getUser().getId(),
                                            getSelectedChatId(),
                                            chatField.getText()
                                    )
                            )
                    ));
                } else {
                    this.controller.getWriter().println(this.controller.getMapper().writeValueAsString(
                            new PrivateChatMessage(
                                    new PrivateChat(
                                            this.controller.getUser().getId(),
                                            getSelectedChatId(),
                                            chatField.getText()
                                    )
                            )
                    ));
                }

                this.controller.getWriter().flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

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

                if (privateChat.getFrom().equals(getSelectedChatId()) || controller.getUser().getId().equals(privateChat.getFrom())) {
                    final String newMessage = privateChatMessage.getPrivateChat().getContent();
                    final JLabel textComponent = new JLabel(newMessage);

                    messagesContainer.add(textComponent);
                    messagesContainer.validate();
                    messagesContainer.repaint();
                }
            } else if (message.startsWith("{\"groupChat")) {
                final GroupChatMessage groupChatMessage = this.controller.getMapper().readValue(message, GroupChatMessage.class);
                final GroupChat groupChat = groupChatMessage.getGroupChat();

                if (groupChat.getTo().equals(getSelectedChatId())) {
                    final String newMessage = groupChatMessage.getGroupChat().getContent();
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
            } else if (message.startsWith("{\"responseJoinGroup")) {
                final ResponseJoinGroupMessage responseJoinGroupMessage = this.controller.getMapper().readValue(message, ResponseJoinGroupMessage.class);
                final List<ChatMessageDTO> messages = responseJoinGroupMessage.getResponseJoinGroupMessage();

                for (ChatMessageDTO newMessage : messages) {
                    final JLabel textComponent = new JLabel(newMessage.getUser().getUsername() + " - " + newMessage.getContent());
                    messagesContainer.add(textComponent);
                }

                messagesContainer.validate();
                messagesContainer.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSelectedChatId() {
        return selectedChatId;
    }

    public void setSelectedChatId(String selectedChatId) {
        this.selectedChatId = selectedChatId;
    }

    public void setGroup(Boolean group) {
        isGroup = group;
    }
}
