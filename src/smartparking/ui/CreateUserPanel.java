package smartparking.ui;

import javax.swing.*;
import java.awt.*;

import smartparking.manager.UserManager;
import smartparking.manager.PasswordValidator;
import smartparking.model.User;

public class CreateUserPanel {

    public CreateUserPanel() {

        JFrame frame = new JFrame("Park.Me | Create New User");

        frame.setSize(400, 190);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        JButton createButton = new JButton("Create");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton);

        createButton.addActionListener(f -> {

            String username =
                    usernameField.getText().trim();

            String password =
                    passwordField.getText().trim();


            // Check empty fields
            if (username.isEmpty() ||
                    password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Username and password cannot be empty!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // Check invalid characters
            if (username.contains("|") ||
                    username.contains("\n") ||
                    password.contains("|") ||
                    password.contains("\n")) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Username and password cannot contain | or new lines!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // Check password strength
            String passwordError =
                    PasswordValidator.validate(password);

            if (!passwordError.equals("")) {

                JOptionPane.showMessageDialog(
                        frame,
                        passwordError,
                        "Invalid Password",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }


            // Create user
            User user =
                    new User(username, password);


            boolean added =
                    UserManager.addUser(user);


            // Username already exists
            if (!added) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Username already exists!",
                        "Create User",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // Success
            JOptionPane.showMessageDialog(
                    frame,
                    "User created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );


            usernameField.setText("");
            passwordField.setText("");
        });

        // Pressing Enter will activate the Create button
        frame.getRootPane().setDefaultButton(createButton);

        frame.add(formPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}