package frame;

import panel.AttrPanel;

import javax.swing.*;

public class AttrFrame extends JFrame {

    public AttrFrame() {

        setTitle("Country Attributes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        AttrPanel mapPanel = new AttrPanel();
        JScrollPane scrollPane = new JScrollPane(mapPanel);
        add(scrollPane);

        setVisible(true);
    }

}
