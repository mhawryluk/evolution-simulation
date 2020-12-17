package project;
import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public Window(int width, int height, int initialPopulation, int nutritionalEnergy, int initialEnergy, int jungleRatio){

        Simulation simulation1 = new Simulation(width, height, nutritionalEnergy, initialPopulation, initialEnergy, jungleRatio);
        Simulation simulation2 = new Simulation(width, height, nutritionalEnergy, initialPopulation, initialEnergy, jungleRatio);

        MapPanel mapPanel1 = new MapPanel(simulation1);
        MapPanel mapPanel2 = new MapPanel(simulation2);
        mapPanel1.setName("1");
        mapPanel2.setName("2");

        StatPanel statPanel1 = new StatPanel(simulation1, mapPanel1);
        StatPanel statPanel2 = new StatPanel(simulation2, mapPanel2);

        setTitle("Evolution Project");
//        setIconImage(new ImageIcon("pics/chick-down.png").getImage().getScaledInstance(10,10, Image.SCALE_DEFAULT));
        setLayout(new BorderLayout(10, 10));

        JPanel maps = new JPanel();
        add(maps, BorderLayout.CENTER);

        maps.add(mapPanel1, BorderLayout.EAST);
        maps.add(mapPanel2, BorderLayout.WEST);

        add(statPanel1, BorderLayout.WEST);
        add(statPanel2, BorderLayout.EAST);

        setBackground(Color.BLACK);
        setSize(10000, 10000);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
}
