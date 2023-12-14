import org.locationtech.jts.geom.Geometry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.Map;

public class AttrPanel extends JPanel {

    private static final double ZOOM_FACTOR = 1.1;

    private Map<String, Country> countries;
    private Point lastDragPoint;
    private Country selectedCountry;
    private double scale = 1.0;
    private double offsetX = 0.0;
    private double offsetY = 0.0;
    private JTextField countryInputField;


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

    private void handleMouseWheel(MouseWheelEvent e) {
        double scaleFactor = (e.getWheelRotation() > 0) ? 1.0 / ZOOM_FACTOR : ZOOM_FACTOR;
        scale *= scaleFactor;
        repaint();
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

    private void drawCountry(Graphics2D g2d, Geometry geometry) {
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
        g2d.draw(path);
    }



    private class MapMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            lastDragPoint = e.getPoint();
        }
    }

    private class MapMouseMotionListener extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (lastDragPoint != null) {
                double dx = e.getPoint().getX() - lastDragPoint.getX();
                double dy = e.getPoint().getY() - lastDragPoint.getY();
                offsetX += dx / scale;
                offsetY += dy / scale;
                repaint();
                lastDragPoint = e.getPoint();
            }
        }
    }
}