package smartparking.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class RegistrationDataManager {

    // Load data from a text file
    private static ArrayList<String> loadData(String fileName) {

        ArrayList<String> data = new ArrayList<>();

        File file = new File("data/" + fileName);

        try {

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine().trim();

                // Ignore blank lines
                if (!line.isEmpty()) {
                    data.add(line);
                }
            }

            scanner.close();

        } catch (Exception e) {

            System.out.println(
                    "Could not read " + fileName
            );
        }

        return data;
    }


    // Load all area names
    public static ArrayList<String> getAreas() {

        return loadData("areas.txt");
    }


    // Load all initial options
    public static ArrayList<String> getInitials() {

        return loadData("initials.txt");
    }
}