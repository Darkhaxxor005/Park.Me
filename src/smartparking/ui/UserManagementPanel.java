package smartparking.ui;

import smartparking.manager.UserManager;
import smartparking.model.User;

import javax.swing.*;
import java.awt.*;

public class UserManagementPanel {

    JFrame frame;
    JPanel userPanel;

    public UserManagementPanel() {

        frame = new JFrame("Park.Me | Delete / Edit User");
        frame.setSize(450, 150);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(0, 3, 10, 10));

        userPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        addUsers();

        JScrollPane scrollPane =
                new JScrollPane(userPanel);

        frame.add(scrollPane);

        frame.setVisible(true);
    }


    private void addUsers() {

        if (UserManager.getUserCount() == 0) {

            userPanel.add(
                    new JLabel("No user accounts found.")
            );

            return;
        }


        for (int i = 0;
             i < UserManager.getUserCount();
             i++) {

            User user =
                    UserManager.getUser(i);

            JLabel usernameLabel =
                    new JLabel(user.getUsername());

            JButton editButton =
                    new JButton("Edit");

            JButton deleteButton =
                    new JButton("Delete");


            final int index = i;


            // Edit
            editButton.addActionListener(f -> {

                new EditUserPanel(index);

                frame.dispose();
            });


            // Delete
            deleteButton.addActionListener(f -> {

                int confirm =
                        JOptionPane.showConfirmDialog(
                                frame,
                                "Delete user \""
                                        + user.getUsername()
                                        + "\"?",
                                "Confirm Delete",
                                JOptionPane.YES_NO_OPTION
                        );


                if (confirm ==
                        JOptionPane.YES_OPTION) {

                    UserManager.deleteUser(index);

                    frame.dispose();

                    new UserManagementPanel();
                }
            });


            userPanel.add(usernameLabel);
            userPanel.add(editButton);
            userPanel.add(deleteButton);
        }
    }
}