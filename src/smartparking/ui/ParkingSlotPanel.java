package smartparking.ui;

import smartparking.manager.ParkingManager;
import smartparking.model.ParkingSlot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ParkingSlotPanel {

    JFrame frame;

    JLabel carCapacity;
    JLabel carOccupied;
    JLabel carAvailable;

    JLabel truckCapacity;
    JLabel truckOccupied;
    JLabel truckAvailable;

    JLabel busCapacity;
    JLabel busOccupied;
    JLabel busAvailable;

    JLabel bikeCapacity;
    JLabel bikeOccupied;
    JLabel bikeAvailable;


    public ParkingSlotPanel() {

        frame = new JFrame(
                "Park.Me | Manage Parking Slot"
        );

        frame.setSize(700, 350);
        frame.setResizable(false);

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);


        JPanel mainPanel =
                new JPanel(
                        new GridLayout(
                                5,
                                5,
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
                createHeader("Capacity")
        );

        mainPanel.add(
                createHeader("Occupied")
        );

        mainPanel.add(
                createHeader("Available")
        );

        mainPanel.add(
                createHeader("Action")
        );


        // Car
        mainPanel.add(
                new JLabel("Car",
                        SwingConstants.CENTER)
        );

        carCapacity =
                new JLabel("", SwingConstants.CENTER);

        carOccupied =
                new JLabel("", SwingConstants.CENTER);

        carAvailable =
                new JLabel("", SwingConstants.CENTER);

        mainPanel.add(carCapacity);
        mainPanel.add(carOccupied);
        mainPanel.add(carAvailable);

        JButton carUpdate =
                new JButton("Update");

        mainPanel.add(carUpdate);


        // Truck
        mainPanel.add(
                new JLabel("Truck",
                        SwingConstants.CENTER)
        );

        truckCapacity =
                new JLabel("", SwingConstants.CENTER);

        truckOccupied =
                new JLabel("", SwingConstants.CENTER);

        truckAvailable =
                new JLabel("", SwingConstants.CENTER);

        mainPanel.add(truckCapacity);
        mainPanel.add(truckOccupied);
        mainPanel.add(truckAvailable);

        JButton truckUpdate =
                new JButton("Update");

        mainPanel.add(truckUpdate);


        // Bus
        mainPanel.add(
                new JLabel("Bus",
                        SwingConstants.CENTER)
        );

        busCapacity =
                new JLabel("", SwingConstants.CENTER);

        busOccupied =
                new JLabel("", SwingConstants.CENTER);

        busAvailable =
                new JLabel("", SwingConstants.CENTER);

        mainPanel.add(busCapacity);
        mainPanel.add(busOccupied);
        mainPanel.add(busAvailable);

        JButton busUpdate =
                new JButton("Update");

        mainPanel.add(busUpdate);


        // Bike
        mainPanel.add(
                new JLabel("Bike",
                        SwingConstants.CENTER)
        );

        bikeCapacity =
                new JLabel("", SwingConstants.CENTER);

        bikeOccupied =
                new JLabel("", SwingConstants.CENTER);

        bikeAvailable =
                new JLabel("", SwingConstants.CENTER);

        mainPanel.add(bikeCapacity);
        mainPanel.add(bikeOccupied);
        mainPanel.add(bikeAvailable);

        JButton bikeUpdate =
                new JButton("Update");

        mainPanel.add(bikeUpdate);


        // Update actions
        carUpdate.addActionListener(f ->
                updateCapacity("Car")
        );

        truckUpdate.addActionListener(f ->
                updateCapacity("Truck")
        );

        busUpdate.addActionListener(f ->
                updateCapacity("Bus")
        );

        bikeUpdate.addActionListener(f ->
                updateCapacity("Bike")
        );


        refreshData();


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


    private void refreshData() {

        updateRow(
                "Car",
                carCapacity,
                carOccupied,
                carAvailable
        );

        updateRow(
                "Truck",
                truckCapacity,
                truckOccupied,
                truckAvailable
        );

        updateRow(
                "Bus",
                busCapacity,
                busOccupied,
                busAvailable
        );

        updateRow(
                "Bike",
                bikeCapacity,
                bikeOccupied,
                bikeAvailable
        );
    }


    private void updateRow(
            String type,
            JLabel capacityLabel,
            JLabel occupiedLabel,
            JLabel availableLabel) {

        ArrayList<ParkingSlot> slots =
                ParkingManager.getSlots(type);

        int capacity =
                ParkingManager.getCapacity(type);

        int occupied =
                ParkingManager.getOccupiedCount(slots);

        int available =
                ParkingManager.getAvailableCount(slots);


        capacityLabel.setText(
                String.valueOf(capacity)
        );

        occupiedLabel.setText(
                String.valueOf(occupied)
        );

        availableLabel.setText(
                String.valueOf(available)
        );
    }


    private void updateCapacity(
            String type) {

        int current =
                ParkingManager.getCapacity(type);


        String input =
                JOptionPane.showInputDialog(
                        frame,
                        "Enter new capacity for "
                                + type + ":",
                        String.valueOf(current)
                );


        // Cancel
        if (input == null) {
            return;
        }


        input = input.trim();


        // Empty input
        if (input.isEmpty()) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Capacity cannot be empty!",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // Check digits
        for (int i = 0;
             i < input.length();
             i++) {

            char ch =
                    input.charAt(i);

            if (ch < '0' || ch > '9') {

                JOptionPane.showMessageDialog(
                        frame,
                        "Capacity must be a number!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }
        }


        int newCapacity =
                Integer.parseInt(input);


        if (newCapacity < 1) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Capacity must be at least 1!",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        ArrayList<ParkingSlot> slots =
                ParkingManager.getSlots(type);

        int occupied =
                ParkingManager.getOccupiedCount(slots);


        // Refuse if capacity cannot hold
        // currently parked vehicles.
        if (newCapacity < occupied) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Cannot reduce capacity.\n\n"
                            + "Currently parked vehicles: "
                            + occupied
                            + "\nRequested capacity: "
                            + newCapacity
                            + "\n\nCapacity cannot be smaller "
                            + "than the number of currently "
                            + "parked vehicles.",
                    "Capacity Update Refused",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // Check for occupied slots outside
        // the new capacity range.
        if (newCapacity < current
                && ParkingManager
                .hasOccupiedOutsideRange(
                        type,
                        newCapacity)) {


            int choice =
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Some parked vehicles are "
                                    + "currently assigned to "
                                    + "slots outside the new "
                                    + "capacity range.\n\n"
                                    + "They will be automatically "
                                    + "moved to the first "
                                    + "available slots within "
                                    + type + " 1-"
                                    + newCapacity
                                    + ".\n\n"
                                    + "Continue?",
                            "Relocate Parked Vehicles",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );


            if (choice != JOptionPane.YES_OPTION) {
                return;
            }


            boolean success =
                    ParkingManager
                            .relocateAndReduceCapacity(
                                    type,
                                    newCapacity
                            );


            if (!success) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Capacity update failed.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }


        } else {

            boolean success =
                    ParkingManager.updateCapacity(
                            type,
                            newCapacity
                    );


            if (!success) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Capacity update refused.",
                        "Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }
        }


        refreshData();


        JOptionPane.showMessageDialog(
                frame,
                type
                        + " capacity updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}