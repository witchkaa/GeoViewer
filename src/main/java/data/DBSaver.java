package data;

import country.Country;

import java.util.List;

public class DBSaver implements DataSaver {
    @Override
    public void saveData(Country editedCountry) {

    }

    @Override
    public List<Country> retrieveData() {
        return null;
    }

    @Override
    public Country isPresent(String countryName) {
        return null;
    }
}
