package common;

import java.io.*;
import java.util.List;

public class DataManager {
    protected static <T> void saveDataToFile(String fileName, List<T> data) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(data);
        }
    }

    @SuppressWarnings("unchecked")
    protected static <T> List<T> loadDataFromFile(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<T>) ois.readObject();
        }
    }
}
