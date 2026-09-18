package smartparking.manager;

import smartparking.model.User;
import smartparking.model.ParkingRecord;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class DataStorage {

    private static final String DATA_FOLDER = "data";

    private static final String USERS_FILE =
            DATA_FOLDER + "/users.txt";

    private static final String ADMIN_FILE =
            DATA_FOLDER + "/admin.txt";

    private static final String SLOTS_FILE =
            DATA_FOLDER + "/slots.txt";

    private static final String FEES_FILE =
            DATA_FOLDER + "/fees.txt";

    private static final String PARKING_FILE =
            DATA_FOLDER + "/parking.txt";


    // Used to encrypt the local files.
    // This is part of the Park.Me application.
    private static final String STORAGE_KEY =
            "Park.Me Local Storage Key";

    private static final int IV_LENGTH = 12;


    // =========================
    // CREATE DATA FOLDER
    // =========================

    private static void createDataFolder() {

        File folder =
                new File(DATA_FOLDER);

        if (!folder.exists()) {
            folder.mkdir();
        }
    }


    // =========================
    // CREATE AES KEY
    // =========================

    private static SecretKeySpec getSecretKey() {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            STORAGE_KEY.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            // Use first 16 bytes = 128-bit AES key
            byte[] keyBytes =
                    Arrays.copyOf(hash, 16);

            return new SecretKeySpec(
                    keyBytes,
                    "AES"
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Could not create encryption key."
            );
        }
    }


    // =========================
    // ENCRYPT TEXT
    // =========================

    private static String encrypt(String text) {

        try {

            byte[] iv =
                    new byte[IV_LENGTH];

            SecureRandom random =
                    new SecureRandom();

            random.nextBytes(iv);


            Cipher cipher =
                    Cipher.getInstance(
                            "AES/GCM/NoPadding"
                    );


            GCMParameterSpec spec =
                    new GCMParameterSpec(
                            128,
                            iv
                    );


            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    getSecretKey(),
                    spec
            );


            byte[] encrypted =
                    cipher.doFinal(
                            text.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );


            // Store IV + encrypted data together
            byte[] result =
                    new byte[
                            iv.length + encrypted.length
                            ];


            System.arraycopy(
                    iv,
                    0,
                    result,
                    0,
                    iv.length
            );


            System.arraycopy(
                    encrypted,
                    0,
                    result,
                    iv.length,
                    encrypted.length
            );


            // Base64 is only for storing encrypted bytes as text.
            return Base64.getEncoder()
                    .encodeToString(result);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Could not encrypt data."
            );
        }
    }


    // =========================
    // DECRYPT TEXT
    // =========================

    private static String decrypt(
            String encryptedText) {

        try {

            byte[] data =
                    Base64.getDecoder()
                            .decode(encryptedText);


            byte[] iv =
                    Arrays.copyOfRange(
                            data,
                            0,
                            IV_LENGTH
                    );


            byte[] encrypted =
                    Arrays.copyOfRange(
                            data,
                            IV_LENGTH,
                            data.length
                    );


            Cipher cipher =
                    Cipher.getInstance(
                            "AES/GCM/NoPadding"
                    );


            GCMParameterSpec spec =
                    new GCMParameterSpec(
                            128,
                            iv
                    );


            cipher.init(
                    Cipher.DECRYPT_MODE,
                    getSecretKey(),
                    spec
            );


            byte[] decrypted =
                    cipher.doFinal(
                            encrypted
                    );


            return new String(
                    decrypted,
                    StandardCharsets.UTF_8
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Could not decrypt data."
            );
        }
    }


    // =========================
    // SAVE ENCRYPTED FILE
    // =========================

    private static void saveEncryptedFile(
            String fileName,
            String content) {

        createDataFolder();

        try {

            String encrypted =
                    encrypt(content);


            FileWriter writer =
                    new FileWriter(fileName);


            writer.write(encrypted);

            writer.close();

        } catch (IOException e) {

            System.out.println(
                    "Error saving file: "
                            + fileName
            );
        }
    }


    // =========================
    // LOAD ENCRYPTED FILE
    // =========================

    private static String loadEncryptedFile(
            String fileName) {

        File file =
                new File(fileName);


        if (!file.exists()) {
            return null;
        }


        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );


            String encryptedText =
                    reader.readLine();


            reader.close();


            if (encryptedText == null
                    || encryptedText.isEmpty()) {

                return "";
            }


            return decrypt(encryptedText);

        } catch (Exception e) {

            System.out.println(
                    "Error loading file: "
                            + fileName
            );

            return null;
        }
    }


    // =========================
    // USER STORAGE
    // =========================

    public static void saveUsers(
            ArrayList<User> users) {

        StringBuilder data =
                new StringBuilder();


        for (User user : users) {

            data.append(
                            user.getUsername()
                    )
                    .append("|")
                    .append(
                            user.getPassword()
                    )
                    .append("\n");
        }


        saveEncryptedFile(
                USERS_FILE,
                data.toString()
        );
    }


    public static ArrayList<User> loadUsers() {

        createDataFolder();


        ArrayList<User> users =
                new ArrayList<>();


        String data =
                loadEncryptedFile(
                        USERS_FILE
                );


        if (data == null || data.isEmpty()) {
            return users;
        }


        String[] lines =
                data.split("\\R");


        for (String line : lines) {

            if (line.isEmpty()) {
                continue;
            }


            String[] parts =
                    line.split(
                            "\\|",
                            2
                    );


            if (parts.length == 2) {

                User user =
                        new User(
                                parts[0],
                                parts[1]
                        );

                users.add(user);
            }
        }


        return users;
    }


    // =========================
    // ADMIN STORAGE
    // =========================

    public static void saveAdminCredentials(
            String username,
            String password) {

        String data =
                username
                        + "\n"
                        + password;


        saveEncryptedFile(
                ADMIN_FILE,
                data
        );
    }


    public static String[] loadAdminCredentials() {

        createDataFolder();


        String data =
                loadEncryptedFile(
                        ADMIN_FILE
                );


        // First run
        if (data == null || data.isEmpty()) {

            saveAdminCredentials(
                    "admin",
                    "admin"
            );


            return new String[]{
                    "admin",
                    "admin"
            };
        }


        String[] lines =
                data.split("\\R");


        if (lines.length >= 2) {

            return new String[]{
                    lines[0],
                    lines[1]
            };
        }


        // Fallback
        return new String[]{
                "admin",
                "admin"
        };
    }


    // =========================
    // PARKING SLOT STORAGE
    // =========================

    public static void saveParkingSlots(
            ArrayList<String> lines) {

        StringBuilder data =
                new StringBuilder();


        for (String line : lines) {

            data.append(line)
                    .append("\n");
        }


        saveEncryptedFile(
                SLOTS_FILE,
                data.toString()
        );
    }


    public static ArrayList<String> loadParkingSlots() {

        createDataFolder();


        ArrayList<String> lines =
                new ArrayList<>();


        String data =
                loadEncryptedFile(
                        SLOTS_FILE
                );


        if (data == null || data.isEmpty()) {
            return lines;
        }


        String[] savedLines =
                data.split("\\R");


        for (String line : savedLines) {

            if (!line.isEmpty()) {
                lines.add(line);
            }
        }


        return lines;
    }


    // =========================
    // PARKING FEE STORAGE
    // =========================

    public static void saveFees(
            ArrayList<String> lines) {

        StringBuilder data =
                new StringBuilder();


        for (String line : lines) {

            data.append(line)
                    .append("\n");
        }


        saveEncryptedFile(
                FEES_FILE,
                data.toString()
        );
    }


    public static ArrayList<String> loadFees() {

        createDataFolder();


        ArrayList<String> lines =
                new ArrayList<>();


        String data =
                loadEncryptedFile(
                        FEES_FILE
                );


        if (data == null || data.isEmpty()) {
            return lines;
        }


        String[] savedLines =
                data.split("\\R");


        for (String line : savedLines) {

            if (!line.isEmpty()) {
                lines.add(line);
            }
        }


        return lines;
    }


    // =========================
    // PARKING RECORD STORAGE
    // =========================

    public static void saveParkingRecords(
            ArrayList<ParkingRecord> records) {

        StringBuilder data =
                new StringBuilder();


        for (ParkingRecord record : records) {

            data.append(
                            record.getVehicleType()
                    )
                    .append("|")
                    .append(
                            record.getRegistration()
                    )
                    .append("|")
                    .append(
                            record.getInTime()
                    )
                    .append("|")
                    .append(
                            record.getOutTime()
                    )
                    .append("|")
                    .append(
                            record.getAllocatedSlot()
                    )
                    .append("|")
                    .append(
                            record.getBill()
                    )
                    .append("\n");
        }


        saveEncryptedFile(
                PARKING_FILE,
                data.toString()
        );
    }


    public static ArrayList<ParkingRecord>
    loadParkingRecords() {

        createDataFolder();


        ArrayList<ParkingRecord> records =
                new ArrayList<>();


        String data =
                loadEncryptedFile(
                        PARKING_FILE
                );


        if (data == null || data.isEmpty()) {
            return records;
        }


        String[] lines =
                data.split("\\R");


        for (String line : lines) {

            if (line.isEmpty()) {
                continue;
            }


            String[] parts =
                    line.split(
                            "\\|",
                            -1
                    );


            if (parts.length != 6) {
                continue;
            }


            ParkingRecord record =
                    new ParkingRecord(
                            parts[0],
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4],
                            parts[5]
                    );


            records.add(record);
        }


        return records;
    }
}