package smartparking.ui;

import smartparking.manager.UserManager;
import smartparking.manager.PasswordValidator;
import smartparking.model.User;

import javax.swing.*;
import java.awt.*;

public class EditUserPanel {

    public EditUserPanel(int index) {

        User user =
                UserManager.getUser(index);

        JFrame frame =
                new JFrame("Park.Me | Edit User");

        frame.setSize(400, 220);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );
        frame.setLocationRelativeTo(null);


        // Form
        JPanel formPanel =
                new JPanel(
                        new GridLayout(
                                2, 2, 10, 10
                        )
                );

        JLabel usernameLabel =
                new JLabel("Username:");

        JLabel passwordLabel =
                new JLabel("Password:");


        // Old values appear automatically
        JTextField usernameField =
                new JTextField(
                        user.getUsername()
                );

        JTextField passwordField =
                new JTextField(
                        user.getPassword()
                );


        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        formPanel.add(passwordLabel);
        formPanel.add(passwordField);


        // Update button
        JButton updateButton =
                new JButton("Update");

        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(updateButton);


        // Update action
        updateButton.addActionListener(f -> {

            String newUsername =
                    usernameField.getText().trim();

            String newPassword =
                    passwordField.getText().trim();


            // Validation
            if (newUsername.isEmpty()
                    || newPassword.isEmpty()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Username and password cannot be empty!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // Prevent unsupported file format characters
            if (newUsername.contains("|")
                    || newUsername.contains("\n")
                    || newPassword.contains("|")
                    || newPassword.contains("\n")) {

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
                    PasswordValidator.validate(newPassword);

            if (!passwordError.equals("")) {

                JOptionPane.showMessageDialog(
                        frame,
                        passwordError,
                        "Invalid Password",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }


            // Check duplicate username
            if (UserManager.usernameExists(
                    newUsername,
                    index)) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Username already exists!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // Update user
            UserManager.updateUser(
                    index,
                    newUsername,
                    newPassword
            );


            JOptionPane.showMessageDialog(
                    frame,
                    "User updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );


            frame.dispose();

            new UserManagementPanel();
        });


        // Pressing Enter will activate the Update button
        frame.getRootPane().setDefaultButton(updateButton);


        frame.add(
                formPanel,
                BorderLayout.CENTER
        );

        frame.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        frame.setVisible(true);
    }
}