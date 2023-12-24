package data;

import country.Country;

import java.util.List;

public interface DataSaver {
    void saveData(Country editedCountry);
    List<Country> retrieveData();
    Country isPresent(String countryName);
}
