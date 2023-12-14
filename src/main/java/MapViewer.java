import javax.swing.*;
import java.util.Map;

public class MapViewer extends JFrame {

    private Map<String, Country> countries;

    public MapViewer() {
        try {
            this.countries = Country.loadCountries("src/main/java/world.geojson");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("World Map Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        MapPanel mapPanel = new MapPanel(countries);
        JScrollPane scrollPane = new JScrollPane(mapPanel);
        add(scrollPane);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MapViewer());
    }
}

