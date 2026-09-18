package smartparking.ui;

import smartparking.manager.ParkingManager;
import smartparking.manager.ParkingRecordManager;
import smartparking.model.ParkingRecord;
import smartparking.model.ParkingSlot;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EntryPanel {

    public EntryPanel() {

        JFrame frame =
                new JFrame("Park.Me | Vehicle Entry");

        frame.setSize(500, 380);
        frame.setResizable(false);

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);


        // =========================
        // MAIN PANEL
        // =========================

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                10, 10
                        )
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 15, 20
                )
        );


        // =========================
        // VEHICLE TYPE
        // =========================

        JPanel vehicleTypePanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        vehicleTypePanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Vehicle Type"
                )
        );


        JRadioButton carButton =
                new JRadioButton("Car");

        JRadioButton truckButton =
                new JRadioButton("Truck");

        JRadioButton busButton =
                new JRadioButton("Bus");

        JRadioButton bikeButton =
                new JRadioButton("Bike");


        ButtonGroup vehicleGroup =
                new ButtonGroup();

        vehicleGroup.add(carButton);
        vehicleGroup.add(truckButton);
        vehicleGroup.add(busButton);
        vehicleGroup.add(bikeButton);


        vehicleTypePanel.add(carButton);
        vehicleTypePanel.add(truckButton);
        vehicleTypePanel.add(busButton);
        vehicleTypePanel.add(bikeButton);


        // =========================
        // REGISTRATION
        // =========================

        RegistrationPanel registrationPanel =
                new RegistrationPanel();


        // =========================
        // IN-TIME
        // =========================

        JPanel timePanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        JLabel timeLabel =
                new JLabel("In-Time:");

        JLabel currentTimeLabel =
                new JLabel("--");

        timePanel.add(timeLabel);
        timePanel.add(currentTimeLabel);


        // Show current time when window opens
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm:ss"
                );

        LocalDateTime now =
                LocalDateTime.now();

        currentTimeLabel.setText(
                now.format(formatter)
        );


        // =========================
        // PARK BUTTON
        // =========================

        JButton parkButton =
                new JButton("Park Vehicle");


        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(parkButton);


        // =========================
        // PARK ACTION
        // =========================

        parkButton.addActionListener(e -> {

            // -------------------------
            // Check vehicle type
            // -------------------------

            String vehicleType = "";

            if (carButton.isSelected()) {

                vehicleType = "Car";

            } else if (truckButton.isSelected()) {

                vehicleType = "Truck";

            } else if (busButton.isSelected()) {

                vehicleType = "Bus";

            } else if (bikeButton.isSelected()) {

                vehicleType = "Bike";
            }


            if (vehicleType.isEmpty()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Please select a vehicle type.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // -------------------------
            // Check registration number
            // -------------------------

            if (!registrationPanel.isNumberValid()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Registration number must contain exactly 6 digits.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            String registration =
                    registrationPanel
                            .getRegistrationNumber();


            // -------------------------
            // Check duplicate vehicle
            // -------------------------

            if (ParkingRecordManager.isAlreadyParked(
                    registration)) {

                JOptionPane.showMessageDialog(
                        frame,
                        "This vehicle is already parked.",
                        "Vehicle Already Parked",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // -------------------------
            // Find first available slot
            // -------------------------

            ParkingSlot slot =
                    ParkingManager.getFirstAvailableSlot(
                            ParkingManager.getSlots(
                                    vehicleType
                            )
                    );


            if (slot == null) {

                JOptionPane.showMessageDialog(
                        frame,
                        "No Slot Available for "
                                + vehicleType + ".",
                        "Parking Full",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // -------------------------
            // Get current system time
            // -------------------------

            LocalDateTime entryTime =
                    LocalDateTime.now();

            String inTime =
                    entryTime.format(formatter);


            // -------------------------
            // Occupy slot
            // -------------------------

            slot.occupy(registration);

            ParkingManager.saveSlotData();


            // -------------------------
            // Create parking record
            // -------------------------

            ParkingRecord record =
                    new ParkingRecord(
                            vehicleType,
                            registration,
                            inTime,
                            "--",
                            slot.getSlotId(),
                            "--"
                    );


            // -------------------------
            // Save record
            // -------------------------

            ParkingRecordManager.addRecord(
                    record
            );


            // -------------------------
            // Success receipt
            // -------------------------

            JOptionPane.showMessageDialog(
                    frame,
                    "Vehicle Entry Successful!\n\n"
                            + "Vehicle Type: "
                            + vehicleType + "\n"
                            + "Registration: "
                            + registration + "\n"
                            + "In-Time: "
                            + inTime + "\n"
                            + "Allocated Slot: "
                            + slot.getSlotId(),
                    "Entry Receipt",
                    JOptionPane.INFORMATION_MESSAGE
            );


            // Update displayed time
            currentTimeLabel.setText(
                    inTime
            );


            // Clear registration number
            registrationPanel.clearNumber();

            // Remove vehicle type selection
            vehicleGroup.clearSelection();
        });


        // =========================
        // ENTER = PARK VEHICLE
        // =========================

        frame.getRootPane()
                .setDefaultButton(
                        parkButton
                );


        // =========================
        // CENTER PANEL
        // =========================

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(
                                10, 10
                        )
                );

        centerPanel.add(
                vehicleTypePanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                registrationPanel,
                BorderLayout.CENTER
        );

        centerPanel.add(
                timePanel,
                BorderLayout.SOUTH
        );


        // =========================
        // ADD TO MAIN PANEL
        // =========================

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        // =========================
        // SHOW WINDOW
        // =========================

        frame.add(mainPanel);

        frame.setVisible(true);
    }
}