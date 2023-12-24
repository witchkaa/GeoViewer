package data;

import country.Country;

import java.io.*;
import java.util.*;

public class Serializator implements DataSaver {
    private List<Country> addedCountries;
    private static final String SERIALIZED_FILE = "src/main/resources/addedCountries.ser";
    @Override
    public void saveData(Country editedCountry) {
        Country currCountry = addedCountries.stream()
                .filter(country -> country.getName().equals(editedCountry.getName()))
                .findFirst()
                .orElse(null);
        if (currCountry != null) {
            currCountry.copyValuesFrom(editedCountry);
        } else {
            addedCountries.add(editedCountry);
        }
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
                addedCountries = (List<Country>) obj;
                return (List<Country>) obj;
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        addedCountries = new ArrayList<>();
        return new ArrayList<>();
    }

    @Override
    public Country isPresent(String countryName) {
        return addedCountries.stream()
                .filter(country -> country.getName().equals(countryName))
                .findFirst()
                .orElse(null);
    }
}
