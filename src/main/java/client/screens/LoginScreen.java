package client.screens;

import client.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    private JButton loginButton;
    private JButton registerButton;
    private JPasswordField passwordField;
    private JTextField textField;
    private JPanel container;

    private final Controller controller;

    public LoginScreen(Controller controller) {
        this.setContentPane(this.container);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1080, 720);
        this.setVisible(true);

        this.controller = controller;

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = textField.getText();
                String password = String.valueOf(passwordField.getPassword());

                controller.login(login, password);
            }
        });
    }
}
