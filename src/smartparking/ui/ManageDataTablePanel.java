package smartparking.ui;

import smartparking.manager.ParkingRecordManager;
import smartparking.model.ParkingRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import smartparking.manager.ParkingManager;
import smartparking.model.ParkingSlot;


public class ManageDataTablePanel {

    private JFrame frame;

    private JTable table;
    private DefaultTableModel tableModel;

    private RegistrationPanel registrationPanel;

    private int highlightedRow = -1;
    private int lastFoundRow = -1;
    private String lastSearchedRegistration = "";


    public ManageDataTablePanel() {

        frame = new JFrame(
                "Park.Me | Manage Data Table"
        );

        frame.setSize(1100, 600);
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
                                10,
                                10
                        )
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );


        // =========================
        // SEARCH PANEL
        // =========================

        JPanel searchPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        searchPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Search Registration"
                )
        );


        registrationPanel =
                new RegistrationPanel();


        JButton findButton =
                new JButton("Find");


        JPanel findButtonPanel =
                new JPanel();

        findButtonPanel.add(
                findButton
        );


        searchPanel.add(
                registrationPanel,
                BorderLayout.CENTER
        );

        searchPanel.add(
                findButtonPanel,
                BorderLayout.SOUTH
        );


        // =========================
        // TABLE
        // =========================

        String[] columns = {
                "Vehicle Type",
                "Registration",
                "In Time",
                "Out Time",
                "Allocated Slot",
                "Bill"
        };


        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };


        table =
                new JTable(tableModel);


        table.setRowHeight(28);

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );


        // Center all table values

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        for (int i = 0;
             i < table.getColumnCount();
             i++) {

            table.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(
                            new DataTableRenderer()
                    );
        }


        // Column widths

        table.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(100);

        table.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(210);

        table.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(180);

        table.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(180);

        table.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(110);

        table.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(80);


        JScrollPane scrollPane =
                new JScrollPane(table);


        // =========================
        // BUTTON PANEL
        // =========================

        JButton updateButton =
                new JButton(
                        "Update Selected"
                );


        JButton clearAllButton =
                new JButton(
                        "Clear All"
                );

        JButton clearUnparkedButton =
                new JButton(
                        "Clear Unparked"
                );


        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(
                updateButton
        );

        buttonPanel.add(
                clearAllButton
        );

        buttonPanel.add(
                clearUnparkedButton
        );


        // =========================
        // ADD TO MAIN PANEL
        // =========================

        mainPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        // =========================
        // FIND ACTION
        // =========================

        findButton.addActionListener(e -> {

            // =========================
            // VALIDATE REGISTRATION
            // =========================

            if (!registrationPanel.isNumberValid()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Registration number must contain exactly 6 digits.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // =========================
            // GET SEARCH REGISTRATION
            // =========================

            String registration =
                    registrationPanel
                            .getRegistrationNumber();


            // =========================
            // CHECK WHETHER NEW SEARCH
            // =========================

            if (!registration.equalsIgnoreCase(
                    lastSearchedRegistration
            )) {

                lastFoundRow = -1;

                lastSearchedRegistration =
                        registration;
            }


            // =========================
            // START SEARCH
            // =========================

            int rowCount =
                    tableModel.getRowCount();


            if (rowCount == 0) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Vehicle not found.",
                        "Search Result",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            int startRow =
                    lastFoundRow + 1;


            int foundRow = -1;


            // =========================
            // SEARCH FROM NEXT ROW
            // =========================

            for (int i = startRow;
                 i < rowCount;
                 i++) {

                String tableRegistration =
                        tableModel
                                .getValueAt(
                                        i,
                                        1
                                )
                                .toString();


                if (tableRegistration
                        .equalsIgnoreCase(
                                registration
                        )) {

                    foundRow = i;

                    break;
                }
            }


            // =========================
            // WRAP AROUND
            // =========================

            if (foundRow == -1
                    && startRow > 0) {

                for (int i = 0;
                     i < startRow;
                     i++) {

                    String tableRegistration =
                            tableModel
                                    .getValueAt(
                                            i,
                                            1
                                    )
                                    .toString();


                    if (tableRegistration
                            .equalsIgnoreCase(
                                    registration
                            )) {

                        foundRow = i;

                        break;
                    }
                }
            }


            // =========================
            // NOT FOUND
            // =========================

            if (foundRow == -1) {

                lastFoundRow = -1;

                highlightedRow = -1;

                table.clearSelection();

                table.repaint();


                JOptionPane.showMessageDialog(
                        frame,
                        "Vehicle not found.",
                        "Search Result",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // =========================
            // SAVE FOUND ROW
            // =========================

            lastFoundRow =
                    foundRow;

            highlightedRow =
                    foundRow;


            // =========================
            // SELECT ROW
            // =========================

            table.setRowSelectionInterval(
                    foundRow,
                    foundRow
            );


            // =========================
            // SCROLL TO ROW
            // =========================

            table.scrollRectToVisible(
                    table.getCellRect(
                            foundRow,
                            0,
                            true
                    )
            );


            // =========================
            // REPAINT
            // =========================

            table.repaint();
        });


        // =========================
        // UPDATE BUTTON
        // =========================

        updateButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please select a vehicle first."
                );
                return;
            }

            int modelRow =
                    table.convertRowIndexToModel(selectedRow);

            ParkingRecord record =
                    ParkingRecordManager.getRecords()
                            .get(modelRow);

            new UpdateParkingRecordPanel(
                    record,
                    this::loadTableData
            );
        });


        // =========================
        // CLEAR ALL
        // =========================

        clearAllButton.addActionListener(e -> {

            ArrayList<ParkingRecord> records =
                    ParkingRecordManager.getRecords();


            if (records.isEmpty()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "There are no vehicle records to clear.",
                        "Clear All",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }


            int confirm =
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Clear all vehicle records?\n"
                                    + "This will remove all parked and unparked records.",
                            "Confirm Clear All",
                            JOptionPane.YES_NO_OPTION
                    );


            if (confirm != JOptionPane.YES_OPTION) {

                return;
            }


            // =========================
            // RELEASE PARKED VEHICLES
            // =========================

            for (ParkingRecord record :
                    records) {

                if (record.getOutTime()
                        .equals("--")) {

                    ArrayList<ParkingSlot> slots =
                            ParkingManager.getSlots(
                                    record.getVehicleType()
                            );


                    if (slots != null) {

                        for (ParkingSlot slot :
                                slots) {

                            if (slot.getSlotId()
                                    .equals(
                                            record.getAllocatedSlot()
                                    )) {

                                slot.release();

                                break;
                            }
                        }
                    }
                }
            }


            // =========================
            // CLEAR RECORDS
            // =========================

            records.clear();


            // =========================
            // SAVE DATA
            // =========================

            ParkingManager.saveSlotData();

            ParkingRecordManager.saveRecords();


            // =========================
            // RESET SEARCH
            // =========================

            highlightedRow = -1;

            lastFoundRow = -1;

            lastSearchedRegistration = "";


            // =========================
            // REFRESH TABLE
            // =========================

            loadTableData();


            JOptionPane.showMessageDialog(
                    frame,
                    "All vehicle records have been cleared.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        // =========================
// CLEAR UNPARKED
// =========================

        clearUnparkedButton.addActionListener(e -> {

            ArrayList<ParkingRecord> records =
                    ParkingRecordManager.getRecords();


            if (records.isEmpty()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "There are no vehicle records to clear.",
                        "Clear Unparked",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }


            // =========================
            // CHECK FOR UNPARKED RECORDS
            // =========================

            boolean hasUnparked =
                    false;


            for (ParkingRecord record :
                    records) {

                if (!record.getOutTime()
                        .equals("--")) {

                    hasUnparked = true;

                    break;
                }
            }


            if (!hasUnparked) {

                JOptionPane.showMessageDialog(
                        frame,
                        "There are no unparked vehicle records.",
                        "Clear Unparked",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }


            // =========================
            // CONFIRM
            // =========================

            int confirm =
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Clear all unparked vehicle records?",
                            "Confirm Clear Unparked",
                            JOptionPane.YES_NO_OPTION
                    );


            if (confirm != JOptionPane.YES_OPTION) {

                return;
            }


            // =========================
            // REMOVE UNPARKED RECORDS
            // =========================

            for (int i =
                 records.size() - 1;
                 i >= 0;
                 i--) {

                ParkingRecord record =
                        records.get(i);


                /*
                 * Out Time is not "--"
                 * means vehicle has already
                 * checked out.
                 */

                if (!record.getOutTime()
                        .equals("--")) {

                    records.remove(i);
                }
            }


            // =========================
            // SAVE
            // =========================

            ParkingRecordManager.saveRecords();


            // =========================
            // RESET SEARCH
            // =========================

            highlightedRow = -1;

            lastFoundRow = -1;

            lastSearchedRegistration = "";


            // =========================
            // REFRESH TABLE
            // =========================

            loadTableData();


            JOptionPane.showMessageDialog(
                    frame,
                    "All unparked vehicle records have been cleared.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });


        // =========================
        // SHOW DATA
        // =========================

        loadTableData();


        // =========================
        // SHOW WINDOW
        // =========================

        frame.add(mainPanel);

        frame.setVisible(true);
    }


    // =========================
    // LOAD TABLE DATA
    // =========================

    private void loadTableData() {

        tableModel.setRowCount(0);


        ArrayList<ParkingRecord> records =
                ParkingRecordManager
                        .getRecords();


        for (ParkingRecord record :
                records) {

            tableModel.addRow(
                    new Object[]{

                            record.getVehicleType(),

                            record.getRegistration(),

                            record.getInTime(),

                            record.getOutTime(),

                            record.getAllocatedSlot(),

                            record.getBill()
                    }
            );
        }
    }


    // =========================
    // TABLE RENDERER
    // =========================

    private class DataTableRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {


            Component component =
                    super.getTableCellRendererComponent(
                            table,
                            value,
                            isSelected,
                            hasFocus,
                            row,
                            column
                    );


            setHorizontalAlignment(
                    SwingConstants.CENTER
            );


            // Convert view row to model row

            int modelRow =
                    table.convertRowIndexToModel(
                            row
                    );


            // Bold the searched row

            if (modelRow ==
                    highlightedRow) {

                setFont(
                        getFont().deriveFont(
                                Font.BOLD
                        )
                );

            } else {

                setFont(
                        getFont().deriveFont(
                                Font.PLAIN
                        )
                );
            }


            return component;
        }
    }
}