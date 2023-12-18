package country;

import java.util.List;

public interface DataSaver {
    void saveData(List <Country> addedCountries);
    List<Country> retrieveData();
}
