package frame;

import panel.GamePanel;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {

        setTitle("Country Game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        GamePanel mapPanel = new GamePanel();
        JScrollPane scrollPane = new JScrollPane(mapPanel);
        add(scrollPane);

        setVisible(true);
    }
}