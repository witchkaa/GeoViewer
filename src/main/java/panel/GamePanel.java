package panel;

import country.Country;
import org.locationtech.jts.geom.Geometry;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends MapPanel {
    private Map<String, Boolean> selectedCountries;
    private int correctCountryCounter = 0;
    private JLabel countryCounterLabel;

    public GamePanel() {
        try {
            countries = Country.loadCountries("src/main/resources/world.geojson");
        } catch (Exception e) {
            e.printStackTrace();
        }
        selectedCountries = new HashMap<>();

        addMouseListener(new MapMouseListener());
        addMouseWheelListener(this::handleMouseWheel);
        addMouseMotionListener(new MapMouseMotionListener());

        countryInputField = new JTextField(20);
        countryInputField.addActionListener(e -> {
            String inputCountry = countryInputField.getText().trim();
            if (!inputCountry.isEmpty()) {
                if (highlightCountry(inputCountry)) {
                    correctCountryCounter++;
                    updateCountryCounterLabel();
                }
                countryInputField.setText("");
            }
        });

        add(countryInputField);

        countryCounterLabel = new JLabel("Кількість країн: 0/197");
        add(countryCounterLabel);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        for (Country country : countries.values()) {
            Geometry geometry = country.getGeometry();
            if (geometry != null) {
                drawCountry(g2d, geometry);
            }
        }

        for (String selectedCountry : selectedCountries.keySet()) {
            if (Boolean.TRUE.equals(selectedCountries.get(selectedCountry))) {
                g2d.setColor(Color.RED);
                Country country = countries.get(selectedCountry);
                if (country != null) {
                    Geometry geometry = country.getGeometry();
                    if (geometry != null) {
                        drawFilledCountry(g2d, geometry);
                        g2d.setColor(Color.BLACK);
                        drawCountry(g2d, geometry);
                    }
                }
                g2d.setColor(Color.BLACK);
            }
        }
    }


    private void drawFilledCountry(Graphics2D g2d, Geometry geometry) {
        Path2D path = new Path2D.Double();
        for (int i = 0; i < geometry.getNumGeometries(); i++) {
            org.locationtech.jts.geom.Coordinate[] coordinates = geometry.getGeometryN(i).getCoordinates();
            for (int j = 0; j < coordinates.length; j++) {
                double transformedX = scale * (coordinates[j].x - offsetX);
                double transformedY = scale * (-coordinates[j].y - offsetY);

                if (j == 0) {
                    path.moveTo(transformedX, transformedY);
                } else {
                    path.lineTo(transformedX, transformedY);
                }
            }
        }
        g2d.fill(path);
    }

    private boolean highlightCountry(String countryName) {
        if (countries.containsKey(countryName) && !selectedCountries.containsKey(countryName)) {
            selectedCountries.put(countryName, true);
            repaint();
            return true;
        }
        return false;
    }

    private void updateCountryCounterLabel() {
        countryCounterLabel.setText("Кількість країн: " + correctCountryCounter + "/197");
    }
}



