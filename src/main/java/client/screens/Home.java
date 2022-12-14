package client.screens;

import client.Controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import dtos.GroupDTO;
import dtos.UserDTO;
import messages.chat.PrivateChat;
import messages.chat.PrivateChatMessage;
import messages.home.DisconnectMessage;
import messages.home.HomeGroupsMessage;
import messages.home.HomeUsersMessage;
import messages.home.NewConnectionMessage;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Home extends Screen {
    private final Controller controller;
    private final GridLayout messagesLayout;
    private JPanel container;
    private JTextField chatField;
    private JButton sendButton;
    private JPanel messagesContainer;
    private JLabel targetLabel;
    private JList<String> listOfUsers;
    private JList<String> listOfGroups;
    private String selectedUserId;

    public Home(Controller controller) {
        this.controller = controller;

        this.setContentPane(this.container);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1080, 720);
        this.setTitle(this.controller.getUser().getUsername());

        this.messagesLayout = new GridLayout(0, 1, 0, 4);
        this.messagesContainer.setLayout(this.messagesLayout);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!targetLabel.getText().equals("Select someone to chat")) {
                    try {
                        sendMessage();
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        listOfUsers.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setSelectedUserId(listOfUsers.getSelectedValue().split(" - ")[0]);
                final String nameOfTarget = listOfUsers.getSelectedValue().split(" - ")[1];

                targetLabel.setText(nameOfTarget);
            }
        });
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

                final String newMessage = privateChatMessage.getPrivateChat().getContent();
                final JLabel textComponent = new JLabel(newMessage);

                messagesContainer.add(textComponent);
                messagesContainer.validate();
                messagesContainer.repaint();
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
