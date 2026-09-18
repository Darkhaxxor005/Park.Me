package smartparking.manager;

import smartparking.model.ParkingSlot;

import java.util.ArrayList;

import smartparking.model.ParkingRecord;

public class ParkingManager {

    private static ArrayList<ParkingSlot> carSlots =
            new ArrayList<>();

    private static ArrayList<ParkingSlot> truckSlots =
            new ArrayList<>();

    private static ArrayList<ParkingSlot> busSlots =
            new ArrayList<>();

    private static ArrayList<ParkingSlot> bikeSlots =
            new ArrayList<>();


    // Initial capacities
    private static int carCapacity = 20;
    private static int truckCapacity = 35;
    private static int busCapacity = 25;
    private static int bikeCapacity = 10;


    // Load saved data when program starts
    static {
        loadSavedSlots();
    }


    // =========================
    // INITIAL / LOAD
    // =========================

    private static void createInitialSlots() {

        createSlots(carSlots, "C", carCapacity);
        createSlots(truckSlots, "T", truckCapacity);
        createSlots(busSlots, "BU", busCapacity);
        createSlots(bikeSlots, "B", bikeCapacity);
    }


    private static void createSlots(
            ArrayList<ParkingSlot> slots,
            String prefix,
            int capacity) {

        slots.clear();

        for (int i = 1; i <= capacity; i++) {

            slots.add(
                    new ParkingSlot(
                            prefix + i
                    )
            );
        }
    }


    private static void loadSavedSlots() {

        ArrayList<String> lines =
                DataStorage.loadParkingSlots();

        // First run
        if (lines.isEmpty()) {

            createInitialSlots();

            saveSlotData();

            return;
        }


        // Start from default structure
        createInitialSlots();


        for (String line : lines) {

            String[] parts =
                    line.split("\\|");

            if (parts.length < 2) {
                continue;
            }

            String type = parts[0];

            if (!isNumber(parts[1])) {
                continue;
            }

            int capacity =
                    Integer.parseInt(parts[1]);


            if (capacity < 1) {
                continue;
            }


            setCategoryCapacity(
                    type,
                    capacity
            );


            ArrayList<ParkingSlot> slots =
                    getSlots(type);


            if (slots == null) {
                continue;
            }


            createSlots(
                    slots,
                    getPrefix(type),
                    capacity
            );


            // Restore occupied slots
            for (int i = 2;
                 i < parts.length;
                 i++) {

                String[] slotData =
                        parts[i].split("=", 2);

                if (slotData.length != 2) {
                    continue;
                }

                String slotId =
                        slotData[0];

                String registration =
                        slotData[1];


                ParkingSlot slot =
                        findSlot(
                                slots,
                                slotId
                        );


                if (slot != null &&
                        !registration.isEmpty()) {

                    slot.occupy(
                            registration
                    );
                }
            }
        }
    }


    private static boolean isNumber(
            String value) {

        if (value.isEmpty()) {
            return false;
        }

        for (int i = 0;
             i < value.length();
             i++) {

            char ch =
                    value.charAt(i);

            if (ch < '0' || ch > '9') {
                return false;
            }
        }

        return true;
    }


    private static ParkingSlot findSlot(
            ArrayList<ParkingSlot> slots,
            String slotId) {

        for (ParkingSlot slot : slots) {

            if (slot.getSlotId()
                    .equals(slotId)) {

                return slot;
            }
        }

        return null;
    }


    // =========================
    // STORAGE
    // =========================

    public static void saveSlotData() {

        ArrayList<String> lines =
                new ArrayList<>();

        lines.add(
                createStorageLine(
                        "Car",
                        carCapacity,
                        carSlots
                )
        );

        lines.add(
                createStorageLine(
                        "Truck",
                        truckCapacity,
                        truckSlots
                )
        );

        lines.add(
                createStorageLine(
                        "Bus",
                        busCapacity,
                        busSlots
                )
        );

        lines.add(
                createStorageLine(
                        "Bike",
                        bikeCapacity,
                        bikeSlots
                )
        );


        DataStorage.saveParkingSlots(
                lines
        );
    }


    private static String createStorageLine(
            String type,
            int capacity,
            ArrayList<ParkingSlot> slots) {

        String line =
                type + "|" + capacity;


        for (ParkingSlot slot : slots) {

            if (slot.isOccupied()) {

                line += "|"
                        + slot.getSlotId()
                        + "="
                        + slot.getVehicleRegistration();
            }
        }


        return line;
    }


    // =========================
    // GET CAPACITY
    // =========================

    public static int getCapacity(
            String type) {

        switch (type) {

            case "Car":
                return carCapacity;

            case "Truck":
                return truckCapacity;

            case "Bus":
                return busCapacity;

            case "Bike":
                return bikeCapacity;
        }

        return 0;
    }


    // =========================
    // GET SLOTS
    // =========================

    public static ArrayList<ParkingSlot>
    getSlots(String type) {

        switch (type) {

            case "Car":
                return carSlots;

            case "Truck":
                return truckSlots;

            case "Bus":
                return busSlots;

            case "Bike":
                return bikeSlots;
        }

        return null;
    }


    private static String getPrefix(
            String type) {

        switch (type) {

            case "Car":
                return "C";

            case "Truck":
                return "T";

            case "Bus":
                return "BU";

            case "Bike":
                return "B";
        }

        return "";
    }


    private static void setCategoryCapacity(
            String type,
            int capacity) {

        switch (type) {

            case "Car":
                carCapacity = capacity;
                break;

            case "Truck":
                truckCapacity = capacity;
                break;

            case "Bus":
                busCapacity = capacity;
                break;

            case "Bike":
                bikeCapacity = capacity;
                break;
        }
    }


    // =========================
    // OCCUPIED / AVAILABLE
    // =========================

    public static int getOccupiedCount(
            ArrayList<ParkingSlot> slots) {

        int count = 0;

        for (ParkingSlot slot : slots) {

            if (slot.isOccupied()) {
                count++;
            }
        }

        return count;
    }


    public static int getAvailableCount(
            ArrayList<ParkingSlot> slots) {

        return slots.size()
                - getOccupiedCount(slots);
    }


    // =========================
    // FIRST AVAILABLE SLOT
    // =========================

    public static ParkingSlot
    getFirstAvailableSlot(
            ArrayList<ParkingSlot> slots) {

        for (ParkingSlot slot : slots) {

            if (!slot.isOccupied()) {
                return slot;
            }
        }

        return null;
    }


    // =========================
    // OCCUPIED OUTSIDE RANGE
    // =========================

    public static boolean
    hasOccupiedOutsideRange(
            String type,
            int newCapacity) {

        ArrayList<ParkingSlot> slots =
                getSlots(type);


        if (slots == null) {
            return false;
        }


        for (int i = newCapacity;
             i < slots.size();
             i++) {

            if (slots.get(i).isOccupied()) {

                return true;
            }
        }

        return false;
    }


    // =========================
    // UPDATE CAPACITY
    // =========================

    public static boolean updateCapacity(
            String type,
            int newCapacity) {

        ArrayList<ParkingSlot> slots =
                getSlots(type);

        if (slots == null ||
                newCapacity < 1) {

            return false;
        }


        int occupiedCount =
                getOccupiedCount(slots);


        // Cannot fit existing vehicles
        if (newCapacity <
                occupiedCount) {

            return false;
        }


        int oldCapacity =
                slots.size();


        // Increase capacity
        if (newCapacity > oldCapacity) {

            String prefix =
                    getPrefix(type);

            for (int i = oldCapacity + 1;
                 i <= newCapacity;
                 i++) {

                slots.add(
                        new ParkingSlot(
                                prefix + i
                        )
                );
            }

            setCategoryCapacity(
                    type,
                    newCapacity
            );

            saveSlotData();

            return true;
        }


        // Same capacity
        if (newCapacity == oldCapacity) {

            return true;
        }


        // Cannot silently remove occupied
        // slots outside the new range.
        if (hasOccupiedOutsideRange(
                type,
                newCapacity)) {

            return false;
        }


        while (slots.size() >
                newCapacity) {

            slots.remove(
                    slots.size() - 1
            );
        }


        setCategoryCapacity(
                type,
                newCapacity
        );

        saveSlotData();

        return true;
    }


    // =========================
    // RELOCATE + REDUCE
    // =========================

    public static boolean
    relocateAndReduceCapacity(
            String type,
            int newCapacity) {

        ArrayList<ParkingSlot> slots =
                getSlots(type);


        if (slots == null ||
                newCapacity < 1) {

            return false;
        }


        int occupiedCount =
                getOccupiedCount(slots);


        if (newCapacity <
                occupiedCount) {

            return false;
        }


        // =========================
        // FIND OUTSIDE VEHICLES
        // =========================

        ArrayList<ParkingSlot> outsideSlots =
                new ArrayList<>();


        for (int i = newCapacity;
             i < slots.size();
             i++) {

            if (slots.get(i).isOccupied()) {

                outsideSlots.add(
                        slots.get(i)
                );
            }
        }


        // =========================
        // RELOCATE VEHICLES
        // =========================

        for (ParkingSlot oldSlot :
                outsideSlots) {

            ParkingSlot newSlot =
                    getFirstAvailableWithinRange(
                            slots,
                            newCapacity
                    );


            if (newSlot == null) {

                return false;
            }


            String registration =
                    oldSlot.getVehicleRegistration();


            newSlot.occupy(
                    registration
            );


            oldSlot.release();
        }


        // =========================
        // REMOVE EXTRA SLOTS
        // =========================

        while (slots.size() >
                newCapacity) {

            slots.remove(
                    slots.size() - 1
            );
        }


        setCategoryCapacity(
                type,
                newCapacity
        );


        // =========================
        // SYNCHRONIZE RECORDS
        // =========================

        ArrayList<ParkingRecord> records =
                ParkingRecordManager.getRecords();


        for (ParkingRecord record :
                records) {

            // Only currently parked
            // vehicles need their slot updated.
            if (record.getVehicleType()
                    .equals(type)
                    &&
                    record.getOutTime()
                            .equals("--")) {

                for (ParkingSlot slot :
                        slots) {

                    if (slot.isOccupied()
                            &&
                            slot.getVehicleRegistration()
                                    .equalsIgnoreCase(
                                            record.getRegistration()
                                    )) {

                        record.setAllocatedSlot(
                                slot.getSlotId()
                        );

                        break;
                    }
                }
            }
        }


        // =========================
        // SAVE EVERYTHING
        // =========================

        saveSlotData();

        ParkingRecordManager.saveRecords();


        return true;
    }

    private static ParkingSlot
    getFirstAvailableWithinRange(
            ArrayList<ParkingSlot> slots,
            int limit) {

        for (int i = 0;
             i < limit;
             i++) {

            if (!slots.get(i).isOccupied()) {

                return slots.get(i);
            }
        }

        return null;
    }
}