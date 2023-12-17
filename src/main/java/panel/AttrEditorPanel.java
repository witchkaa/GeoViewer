package panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AttrEditorPanel extends JPanel {
    private JTextField capitalField;
    private JTextField populationField;
    private JTextField hdiField;
    private JTextField currencyField;
    private JTextField areaField;
    private JButton saveButton;

    public AttrEditorPanel(ActionListener saveAction) {
        setLayout(new GridLayout(6, 2));

        capitalField = new JTextField();
        populationField = new JTextField();
        hdiField = new JTextField();
        currencyField = new JTextField();
        areaField = new JTextField();

        add(new JLabel("Capital:"));
        add(capitalField);
        add(new JLabel("Population:"));
        add(populationField);
        add(new JLabel("HDI:"));
        add(hdiField);
        add(new JLabel("Currency:"));
        add(currencyField);
        add(new JLabel("Area:"));
        add(areaField);

        saveButton = new JButton("Save");
        saveButton.addActionListener(saveAction);
        add(saveButton);
    }
    public String getCapital() {
        return capitalField.getText().trim();
    }

    public long getPopulation() {
        try {
            return Long.parseLong(populationField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number and re-save");
            return 0;
        }
    }

    public float getHdi() {
        try {
            return Float.parseFloat(hdiField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid float number (e.g. 8.7) and re-save");
            return 0.0f;
        }
    }

    public String getCurrency() {
        return currencyField.getText().trim();
    }

    public long getArea() {
        try {
            return Long.parseLong(areaField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number and re-save");
            return 0;
        }
    }
    public void setFieldsEmpty() {
        String text = "";
        capitalField.setText(text);
        populationField.setText(text);
        hdiField.setText(text);
        currencyField.setText(text);
        areaField.setText(text);
    }
}