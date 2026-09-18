package smartparking.ui;

import smartparking.manager.CashManager;
import smartparking.model.ParkingFee;

import javax.swing.*;
import java.awt.*;

public class CashValuePanel {

    JFrame frame;


    public CashValuePanel() {

        frame = new JFrame(
                "Park.Me | Edit Cash Values"
        );

        frame.setSize(700, 330);
        frame.setResizable(false);

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);


        JPanel mainPanel =
                new JPanel(
                        new GridLayout(
                                5,
                                4,
                                10,
                                10
                        )
                );


        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );


        // Header
        mainPanel.add(
                createHeader("Vehicle Type")
        );

        mainPanel.add(
                createHeader("Basic Fee")
        );

        mainPanel.add(
                createHeader("Fee Per Hour")
        );

        mainPanel.add(
                createHeader("Action")
        );


        addFeeRow(
                mainPanel,
                "Car"
        );

        addFeeRow(
                mainPanel,
                "Truck"
        );

        addFeeRow(
                mainPanel,
                "Bus"
        );

        addFeeRow(
                mainPanel,
                "Bike"
        );


        frame.add(mainPanel);

        frame.setVisible(true);
    }


    private JLabel createHeader(
            String text) {

        JLabel label =
                new JLabel(
                        text,
                        SwingConstants.CENTER
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        return label;
    }


    private void addFeeRow(
            JPanel panel,
            String vehicleType) {

        ParkingFee fee =
                CashManager.getFee(vehicleType);


        JLabel typeLabel =
                new JLabel(
                        vehicleType,
                        SwingConstants.CENTER
                );


        JTextField basicField =
                new JTextField(
                        String.valueOf(
                                fee.getBasicFee()
                        )
                );

        basicField.setHorizontalAlignment(
                JTextField.CENTER
        );


        JTextField hourlyField =
                new JTextField(
                        String.valueOf(
                                fee.getFeePerHour()
                        )
                );

        hourlyField.setHorizontalAlignment(
                JTextField.CENTER
        );


        JButton updateButton =
                new JButton("Update");


        updateButton.addActionListener(f -> {

            String basicText =
                    basicField.getText().trim();

            String hourlyText =
                    hourlyField.getText().trim();


            // Basic fee validation
            if (!CashManager.isWholeNumber(
                    basicText)) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Basic Fee for "
                                + vehicleType
                                + " must be a whole number!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // Hourly fee validation
            if (!CashManager.isWholeNumber(
                    hourlyText)) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Fee Per Hour for "
                                + vehicleType
                                + " must be a whole number!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            int basicFee =
                    Integer.parseInt(
                            basicText
                    );

            int feePerHour =
                    Integer.parseInt(
                            hourlyText
                    );


            // Update and save
            CashManager.updateFee(
                    vehicleType,
                    basicFee,
                    feePerHour
            );


            JOptionPane.showMessageDialog(
                    frame,
                    vehicleType
                            + " cash values updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });


        panel.add(typeLabel);
        panel.add(basicField);
        panel.add(hourlyField);
        panel.add(updateButton);
    }
}