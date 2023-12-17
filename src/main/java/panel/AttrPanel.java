package panel;

import country.Country;
import org.locationtech.jts.geom.Geometry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AttrPanel extends MapPanel {
    private List<Country> addedCountries;
    private Country selectedCountry;
    private AttrEditorPanel attrEditorPanel;
    private JLabel enterLabel;
    public AttrPanel() {
        try {
            countries = Country.loadCountries("src/main/resources/world.geojson");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        enterLabel = new JLabel("Enter country name: ");
        topPanel.add(enterLabel);
        topPanel.add(countryInputField);
        countryInputField.addActionListener(e -> {
            String inputCountry = countryInputField.getText().trim();
            if (!inputCountry.isEmpty() && countries.containsKey(inputCountry)) {
                selectedCountry = countries.get(inputCountry);

                // Set the visibility of the attribute editor panel
                attrEditorPanel.setVisible(true);

                // Clear attribute fields when a new country is selected
                clearAttributeFields();

                repaint();
            }
        });

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
                // Create a new Country object with entered attributes
                Country editedCountry = new Country(selectedCountry.getName(), selectedCountry.getGeometry());
                editedCountry.setCapital(attrEditorPanel.getCapital());
                editedCountry.setPopulation(attrEditorPanel.getPopulation());
                editedCountry.setHdi(attrEditorPanel.getHdi());
                editedCountry.setCurrency(attrEditorPanel.getCurrency());
                editedCountry.setArea(attrEditorPanel.getArea());

                // Add the edited country to the list
                addedCountries.add(editedCountry);

                // Reset the attribute editor panel
                attrEditorPanel.setVisible(false);
                clearAttributeFields();
            }
        }
    }
    private void clearAttributeFields() {
        // Clear the text fields in the attribute editor panel
        attrEditorPanel.setFieldsEmpty();
    }
}
