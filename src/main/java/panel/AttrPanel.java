package panel;

import country.Country;
import org.locationtech.jts.geom.Geometry;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class AttrPanel extends MapPanel {
    private Map<String, Boolean> addedCountries;
    private Country selectedCountry;
    public AttrPanel() {
        try {
            countries = Country.loadCountries("src/main/resources/world.geojson");
        } catch (Exception e) {
            e.printStackTrace();
        }

        addMouseListener(new AttrPanel.MapMouseListener());
        addMouseWheelListener(this::handleMouseWheel);
        addMouseMotionListener(new AttrPanel.MapMouseMotionListener());

        countryInputField = new JTextField(20);
        countryInputField.addActionListener(e -> {
            String inputCountry = countryInputField.getText().trim();
            if (!inputCountry.isEmpty() && countries.containsKey(inputCountry)) {
                selectedCountry = countries.get(inputCountry);
                repaint();
            }
        });

        add(countryInputField);

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
        g2d.setColor(Color.RED);
                if (selectedCountry != null) {
                    Geometry geometry = selectedCountry.getGeometry();
                    if (geometry != null) {
                        drawCountry(g2d, geometry);
                    }
                }
                g2d.setColor(Color.BLACK);
            }
}
