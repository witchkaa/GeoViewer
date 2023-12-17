package panel;

import country.Country;
import org.locationtech.jts.geom.Geometry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Path2D;
import java.util.Map;

public abstract class MapPanel extends JPanel {
    protected static final double ZOOM_FACTOR = 1.1;
    protected Map<String, Country> countries;
    protected Point lastDragPoint;
    protected double scale = 3.5;
    protected double offsetX = getWidth();
    protected double offsetY = getHeight();
    protected JTextField countryInputField;
    protected void handleMouseWheel(MouseWheelEvent e) {
        double scaleFactor = (e.getWheelRotation() > 0) ? 1.0 / ZOOM_FACTOR : ZOOM_FACTOR;
        scale *= scaleFactor;
        repaint();
    }
    protected void drawCountry(Graphics2D g2d, Geometry geometry) {
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
    class MapMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            lastDragPoint = e.getPoint();
        }
    }

    class MapMouseMotionListener extends MouseAdapter {
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
