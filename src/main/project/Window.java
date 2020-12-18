package project;
import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private final int WIDTH = 1360;
    private final int HEIGHT = 640;
    private final int STAT_PANEL_WIDTH = 300;

    public Window(int width, int height, int initialPopulation, int nutritionalEnergy, int initialEnergy, int jungleRatio){

        Simulation simulation1 = new Simulation(width, height, nutritionalEnergy, initialPopulation, initialEnergy, jungleRatio);
        Simulation simulation2 = new Simulation(width, height, nutritionalEnergy, initialPopulation, initialEnergy, jungleRatio);

        int squareSize = Math.min((WIDTH - STAT_PANEL_WIDTH) / (2 * width), HEIGHT / height);
        MapPanel mapPanel1 = new MapPanel(simulation1, squareSize);
        MapPanel mapPanel2 = new MapPanel(simulation2, squareSize);

        JPanel statPanel = new JPanel();
        statPanel.setLayout(new GridLayout(2,1,10,11));
        StatPanel statPanel1 = new StatPanel(simulation1, mapPanel1, "left map");
        StatPanel statPanel2 = new StatPanel(simulation2, mapPanel2, "right map");

        setTitle("Evolution Project");
//        setIconImage(new ImageIcon("pics/chick-down.png").getImage().getScaledInstance(10,10, Image.SCALE_DEFAULT));
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
