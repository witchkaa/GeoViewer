package country;

import java.io.*;
import java.util.*;

public class Serializator implements DataSaver{
    private static final String SERIALIZED_FILE = "src/main/resources/addedCountries.ser";
    @Override
    public void saveData(List<Country> addedCountries) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SERIALIZED_FILE))) {
            oos.writeObject(addedCountries);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public List<Country> retrieveData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SERIALIZED_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<Country>) obj;
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }
}
