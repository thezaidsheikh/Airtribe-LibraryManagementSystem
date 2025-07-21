package common;

import java.io.*;
import java.util.List;

/**
 * Provides utility methods for saving and loading data to/from files using Java Serialization.
 * This class handles the serialization and deserialization of generic lists of objects,
 * making it easier to persist application data between program executions.
 */
public class DataManager {
    
    /**
     * Saves a list of objects to a file using Java Serialization.
     * The method creates or overwrites the specified file with the serialized data.
     * 
     * @param <T>      the type of objects in the list
     * @param fileName the name of the file to save the data to (path included)
     * @param data     the list of objects to be serialized and saved
     * @throws IOException if an I/O error occurs while writing to the file
     *                    or if the file exists but is a directory rather than a regular file,
     *                    does not exist but cannot be created, or cannot be opened for any other reason
     */
    protected static <T> void saveDataToFile(String fileName, List<T> data) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(data);
        }
    }

    /**
     * Loads a list of objects from a file using Java Deserialization.
     * The file must have been previously created by the saveDataToFile method.
     * 
     * @param <T>      the expected type of objects in the list
     * @param fileName the name of the file to load the data from (path included)
     * @return a List containing the deserialized objects
     * @throws IOException            if an I/O error occurs while reading from the file
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     * @throws ClassCastException     if the deserialized object is not of type List<T>
     */
    @SuppressWarnings("unchecked")
    protected static <T> List<T> loadDataFromFile(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<T>) ois.readObject();
        }
    }
}
