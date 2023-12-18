package panel;

import country.Country;
import country.DataSaver;
import country.Serializator;
import org.locationtech.jts.geom.Geometry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

public class AttrPanel extends MapPanel {
    private final List<Country> addedCountries;
    private Country selectedCountry;
    private final AttrEditorPanel attrEditorPanel;
    DataSaver dataSaver = new Serializator();
    public AttrPanel() {
        try {
            countries = Country.loadCountries("src/main/resources/world.geojson");
        } catch (Exception e) {
            e.printStackTrace();
        }
        addedCountries = dataSaver.retrieveData();
        setLayout(new BorderLayout());
        attrEditorPanel = new AttrEditorPanel(new SaveButtonAction());
        add(attrEditorPanel, BorderLayout.SOUTH);
        attrEditorPanel.setVisible(false);

        addMouseListener(new AttrPanel.MapMouseListener());
        addMouseWheelListener(this::handleMouseWheel);
        addMouseMotionListener(new AttrPanel.MapMouseMotionListener());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(topPanel, BorderLayout.NORTH);
        countryInputField = new JTextField(20);
        countryInputField.addActionListener(e -> {
            String inputCountry = countryInputField.getText().trim();
            if (!inputCountry.isEmpty() && countries.containsKey(inputCountry)) {
                selectedCountry = countries.get(inputCountry);
                repaint();
            }
        });
        JLabel enterLabel = new JLabel("Enter country name: ");
        topPanel.add(enterLabel);
        topPanel.add(countryInputField);
        countryInputField.addActionListener(e -> {
            String inputCountry = countryInputField.getText().trim();
            if (!inputCountry.isEmpty() && countries.containsKey(inputCountry)) {
                selectedCountry = countries.get(inputCountry);
                attrEditorPanel.setVisible(true);
                populateAttributeFields();
                repaint();
            }
        });

    }
    private void populateAttributeFields() {
        Optional<Country> matchingCountry = addedCountries.stream()
                .filter(country -> country.getName().equals(selectedCountry.getName()))
                .findFirst();

        if (matchingCountry.isPresent()) {
            Country countryFromList = matchingCountry.get();
            attrEditorPanel.setFieldsText(countryFromList.getCapital(),
                    countryFromList.getPopulation(), countryFromList.getHdi(),
                    countryFromList.getCurrency(), countryFromList.getArea());
        } else {
            clearAttributeFields();
        }
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
    private class SaveButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedCountry != null) {
                Optional<Country> matchingCountry = addedCountries.stream()
                        .filter(country -> country.getName().equals(selectedCountry.getName()))
                        .findFirst();

                if (matchingCountry.isPresent()) {
                    Country countryFromList = matchingCountry.get();
                    countryFromList.setCapital(attrEditorPanel.getCapital());
                    countryFromList.setPopulation(attrEditorPanel.getPopulation());
                    countryFromList.setHdi(attrEditorPanel.getHdi());
                    countryFromList.setCurrency(attrEditorPanel.getCurrency());
                    countryFromList.setArea(attrEditorPanel.getArea());
                } else {
                    Country editedCountry = new Country(selectedCountry.getName(), selectedCountry.getGeometry());
                    editedCountry.setCapital(attrEditorPanel.getCapital());
                    editedCountry.setPopulation(attrEditorPanel.getPopulation());
                    editedCountry.setHdi(attrEditorPanel.getHdi());
                    editedCountry.setCurrency(attrEditorPanel.getCurrency());
                    editedCountry.setArea(attrEditorPanel.getArea());

                    addedCountries.add(editedCountry);
                }
            }
            dataSaver.saveData(addedCountries);
        }
    }

    private void clearAttributeFields() {
        attrEditorPanel.setFieldsEmpty();
    }
}
