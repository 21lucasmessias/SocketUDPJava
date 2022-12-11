package client.screens;

import client.Controller;
import messages.login.Login;
import messages.login.LoginMessage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends Screen {
    private JButton loginButton;
    private JButton registerButton;
    private JPasswordField passwordField;
    private JTextField textField;
    private JPanel container;
    private JLabel notFoundAlert;

    private final Controller controller;

    public LoginScreen(Controller controller) {
        this.setContentPane(this.container);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1080, 720);
        this.setVisible(true);

        this.notFoundAlert.setVisible(false);

        this.controller = controller;

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loginText = textField.getText();
                String passwordText = String.valueOf(passwordField.getPassword());

                login(loginText, passwordText);
            }
        });
    }

    public void login(String username, String password) {
        try {
            LoginMessage loginMessage = new LoginMessage(new Login(username, password));
            this.controller.getWriter().println(this.controller.getMapper().writeValueAsString(loginMessage));
            this.controller.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(String message) {
        if(message.equals("user-not-found")) {
            this.notFoundAlert.setVisible(true);
        }
    }
}
