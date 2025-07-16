package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Utility class for common operations
 */
public class utils {
    /**
     * Generates a random number of a specified size
     * 
     * @param size the size of the number to generate
     * @return a random number of the specified size
     */
    public static int generateId(int size) {
        int randomNum = 0;

        // generate a random number of 'size' digits
        for (int i = 0; i < size; i++) {
            randomNum = (randomNum * 10) + (int) (Math.random() * 10);
        }

        return Math.abs(randomNum);
    }

    /**
     * Returns the current epoch time in milliseconds
     * 
     * @return the current epoch time in milliseconds
     */
    public static long getEpochTime() {
        return Instant.now().toEpochMilli();
    }

    /**
     * Converts an epoch time to a LocalDate
     * 
     * @param number the epoch time to convert
     * @return the LocalDate representation of the epoch time
     */
    public static LocalDate convertEpochToDate(long number) {
        Instant instant = Instant.ofEpochMilli(number);
        ZoneId zoneId = ZoneId.systemDefault(); // Use the system default time zone
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate;
    }

    public static long getDateAfterDays(long epochTime, int days) {
        return Instant.ofEpochMilli(epochTime).plus(Duration.ofDays(days)).toEpochMilli();
    }

    public static int getDayDiff(long epochMillis1, long epochMillis2) {
        long millisPerDay = 24 * 60 * 60 * 1000;
        long diffInMillis = Math.abs(epochMillis2 - epochMillis1);
        long dayDifference = diffInMillis / millisPerDay;
        return (int) Math.abs(dayDifference);
    }

    /**
     * Saves a list of objects to a file
     * 
     * @param fileName the name of the file to save to
     * @param al       the list of objects to save
     */
    public static <T> void saveData(String fileName, List<T> al) throws Exception {
        int extensionIndex = fileName.lastIndexOf(".");
        if (extensionIndex == -1) {
            throw new Exception("Invalid file name");
        }
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }
        try (FileWriter writer = new FileWriter(fileName)) {
            for (int i = 0; i < al.size(); i++) {
                String content = al.get(i).toString();
                writer.write(content);
                writer.write("\n");
            }
            System.out.println("WRITING IN FILE DONE");

            String serializedFileName = fileName.substring(0, extensionIndex) + ".ser";
            System.out.println("SERIALIZED FILE NAME: " + serializedFileName);
            DataManager.saveDataToFile(serializedFileName, al);
        } catch (IOException e) {
            throw new Exception("Error reading file: " + e.getMessage());
        }
    }

    public static <T> List<T> loadData(String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                return new ArrayList<T>();
            }
            return DataManager.loadDataFromFile(fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data from file: " + e.getMessage());
            return new ArrayList<T>();
        }
    }

    public static Stream<String> readData(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }
        Stream<String> stream = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            stream = reader.lines().collect(Collectors.toList()).stream();
            return stream;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            throw new Error("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Creates a zip file from a directory
     * 
     * @param folder     the directory to zip
     * @param zipDirName the name of the zip file to create
     */
    public static void createZip(String folder, String zipDirName) {
        File folderName = new File(folder);
        File zipDir = new File(zipDirName);
        if (!folderName.isDirectory()) {
            System.out.println("Error: Directory not found");
            return;
        }
        File[] files = folderName.listFiles();

        // create ZipOutputStream to write to the zip file
        try {
            FileOutputStream fos = new FileOutputStream(zipDir);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (File file : files) {
                System.out.println("Zipping " + file);
                // for ZipEntry we need to keep only relative file path, so we used substring on
                // absolute path
                ZipEntry ze = new ZipEntry(
                        file.getName());
                try {
                    zos.putNextEntry(ze);
                } catch (Exception e) {
                    System.out.println("Error creating zip file: " + e.getMessage());
                }
                // read the file and write to ZipOutputStream
                try {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    zos.closeEntry();
                } catch (Exception e) {
                    System.out.println("Error creating zip file: " + e.getMessage());
                }
            }
            try {
                zos.close();
                fos.close();
            } catch (Exception e) {
                System.out.println("Error creating zip file: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error creating zip file: " + e.getMessage());
        }
    }
}
