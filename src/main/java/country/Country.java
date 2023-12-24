package country;

import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Country implements Serializable {
    private String name;
    private Geometry geometry;
    private String capital;
    private long population;
    private long area;
    private String currency;
    private float hdi;

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public long getArea() {
        return area;
    }

    public void setArea(long area) {
        this.area = area;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getHdi() {
        return hdi;
    }

    public void setHdi(float hdi) {
        this.hdi = hdi;
    }

    public Country(String name, Geometry geometry) {
        this.name = name;
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public Geometry getGeometry() {
        return geometry;
    }
    public static Map<String, Country> loadCountries(String filePath) throws IOException {
        Map<String, Country> countries = new HashMap<>();

        try (FileReader reader = new FileReader(filePath)) {
            FeatureJSON featureJSON = new FeatureJSON();
            try (FeatureIterator<SimpleFeature> iterator = featureJSON.streamFeatureCollection(reader)) {
                while (iterator.hasNext()) {
                    SimpleFeature feature = iterator.next();
                    String countryName = (String) feature.getAttribute("name");
                    Geometry countryGeometry = (Geometry) feature.getDefaultGeometry();
                    countries.put(countryName, new Country(countryName, countryGeometry));
                }
            }
        }
        return countries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return name.equals(country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", capital='" + capital + '\'' +
                ", population=" + population +
                ", area=" + area +
                ", currency='" + currency + '\'' +
                ", hdi=" + hdi +
                '}';
    }
    public void copyValuesFrom(Country sourceCountry) {
        this.setCapital(sourceCountry.getCapital());
        this.setCurrency(sourceCountry.getCurrency());
        this.setArea(sourceCountry.getArea());
        this.setHdi(sourceCountry.getHdi());
        this.setPopulation(sourceCountry.getPopulation());
    }
}