import frame.AttrFrame;
import frame.GameFrame;

import javax.swing.*;
import java.awt.*;

public class MapViewer extends JFrame {
    public MapViewer() {
        setTitle("Country Explorer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons
        JButton gameButton = new JButton("Play Game");
        JButton attributesButton = new JButton("Edit country Attributes");

        gameButton.addActionListener(e -> openGameWindow());

        attributesButton.addActionListener(e -> openAttributesWindow());

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(gameButton);
        panel.add(attributesButton);

        getContentPane().add(panel);
        pack();

    }

    private void openGameWindow() {
        SwingUtilities.invokeLater(GameFrame::new);

    }

    private void openAttributesWindow() {
        SwingUtilities.invokeLater(AttrFrame::new);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MapViewer().setVisible(true));
    }
}