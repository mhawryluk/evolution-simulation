package project.graphics;

import project.engine.*;
import javax.swing.*;
import java.awt.*;

public class SimulationWindow extends JFrame {

    public SimulationWindow(int width, int height, int initialPopulation, int nutritionalEnergy, int initialEnergy, int jungleRatio, int moveEnergy) {

        MapDimensions dimensions = new MapDimensions(width, height, jungleRatio);
        Simulation simulation1 = new Simulation(dimensions, nutritionalEnergy, initialPopulation, initialEnergy, moveEnergy);
        Simulation simulation2 = new Simulation(dimensions, nutritionalEnergy, initialPopulation, initialEnergy, moveEnergy);

        int WIDTH = 1360;
        int HEIGHT = 640;
        int STAT_PANEL_WIDTH = 300;

        int squareSize = Math.min((WIDTH - STAT_PANEL_WIDTH) / (2 * width), HEIGHT / height);
        MapPanel.pics = new Pictures(squareSize);
        MapPanel mapPanel1 = new MapPanel(simulation1, squareSize);
        MapPanel mapPanel2 = new MapPanel(simulation2, squareSize);

        JPanel statPanel = new JPanel();
        statPanel.setLayout(new GridLayout(2, 1, 10, 11));
        StatPanel statPanel1 = new StatPanel(simulation1, mapPanel1, "left map");
        StatPanel statPanel2 = new StatPanel(simulation2, mapPanel2, "right map");

        setTitle("Evolution Project");
        setLayout(new BorderLayout(10, 10));

        JPanel maps = new JPanel();
        maps.setLayout(new FlowLayout());

        maps.add(mapPanel1);
        maps.add(mapPanel2);

        add(maps, BorderLayout.CENTER);

        statPanel.add(statPanel1);
        statPanel.add(statPanel2);

        statPanel1.squareSize = squareSize;
        statPanel2.squareSize = squareSize;
        add(statPanel, BorderLayout.WEST);

        setBackground(Color.BLACK);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
}
