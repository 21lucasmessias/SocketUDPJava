package client.screens;

import client.Controller;
import dtos.GroupDTO;
import dtos.UserDTO;
import messages.home.HomeGroupsMessage;
import messages.home.HomeUsersMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Home extends Screen {
    private final Controller controller;
    private final GridLayout messagesLayout;
    private JPanel container;
    private JList<String> listOfUsers;
    private JList<String> listOfGroups;
    private JPanel chatContainer;
    private JTextField chatField;
    private JButton sendButton;
    private JPanel messagesContainer;

    public Home(Controller controller) {
        this.controller = controller;

        this.setContentPane(this.container);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1080, 720);

        this.messagesLayout = new GridLayout(0, 1, 0, 4);
        this.messagesContainer.setLayout(this.messagesLayout);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        final String message = chatField.getText();
        chatField.setText("");

        final JLabel textComponent = new JLabel(message);

        messagesContainer.add(textComponent);
        messagesContainer.validate();
        messagesContainer.repaint();
    }

    @Override
    public void handleMessage(String message) {
        if (message.startsWith("list-of-users")) {
            System.out.println(message);
        } else if (message.startsWith("{\"homeUsersMessage")) {
            try {
                final HomeUsersMessage homeUsersMessage = this.controller.getMapper().readValue(message, HomeUsersMessage.class);
                final java.util.List<String> users = new ArrayList<>(java.util.List.of());

                for (UserDTO user : homeUsersMessage.getHomeUsersMessage()) {
                    if (!user.getId().equals(this.controller.getUser().getId())) {
                        users.add(user.getId() + " - " + user.getUsername());
                    }
                }

                listOfUsers.setListData(users.toArray(String[]::new));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (message.startsWith("{\"homeGroupsMessage")) {
            try {
                final HomeGroupsMessage homeGroupsMessage = this.controller.getMapper().readValue(message, HomeGroupsMessage.class);
                final java.util.List<String> groups = new ArrayList<>(java.util.List.of());

                for (GroupDTO group : homeGroupsMessage.getHomeGroupsMessage()) {
                    groups.add(group.getId() + " - " + group.getName());
                }

                listOfGroups.setListData(groups.toArray(String[]::new));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
