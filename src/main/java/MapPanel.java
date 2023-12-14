import org.locationtech.jts.geom.Geometry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Map;

import java.awt.event.MouseWheelEvent;

public class MapPanel extends JPanel {

    private static final double ZOOM_FACTOR = 1.1;

    private Map<String, Country> countries;
    private AffineTransform transform;

    private Point lastDragPoint;

    public MapPanel(Map<String, Country> countries) {
        this.countries = countries;
        this.transform = new AffineTransform();

        addMouseListener(new MapMouseListener());
        addMouseWheelListener(this::handleMouseWheel);
        addMouseMotionListener(new MapMouseMotionListener());
    }

    private void handleMouseWheel(MouseWheelEvent e) {
        double scaleFactor = (e.getWheelRotation() > 0) ? 1.0 / ZOOM_FACTOR : ZOOM_FACTOR;
        transform.scale(scaleFactor, scaleFactor);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setTransform(transform);

        for (Country country : countries.values()) {
            Geometry geometry = country.getGeometry();
            if (geometry != null) {
                drawCountry(g2d, geometry, country.getName());
            }
        }
    }

    private void drawCountry(Graphics2D g2d, Geometry geometry, String countryName) {
        Path2D path = new Path2D.Double();
        for (int i = 0; i < geometry.getNumGeometries(); i++) {
            org.locationtech.jts.geom.Coordinate[] coordinates = geometry.getGeometryN(i).getCoordinates();
            for (int j = 0; j < coordinates.length; j++) {
                double[] transformedPoint = new double[2];
                transform.transform(new double[]{coordinates[j].x, -coordinates[j].y}, 0, transformedPoint, 0, 1);
                if (j == 0) {
                    path.moveTo(transformedPoint[0], transformedPoint[1]);
                } else {
                    path.lineTo(transformedPoint[0], transformedPoint[1]);
                }
            }
        }

        g2d.draw(path);
        Rectangle bounds = path.getBounds();
        //g2d.drawString(countryName, (int) bounds.getCenterX(), (int) bounds.getCenterY());
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
                transform.translate(dx, dy);
                repaint();
                lastDragPoint = e.getPoint();
            }
        }
    }
}