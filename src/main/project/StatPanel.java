package project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatPanel extends JPanel implements ActionListener {

    private final Simulation simulation;
    private final EvolutionMap map;
    private final MapPanel mapPanel;
    private final JButton startStopButton;
    private final JButton showDominantButton;
    private final JLabel animalsCountLabel;
    private final JLabel grassCountLabel;
    private final JLabel dominantGenomeLabel;
    private final JLabel averageLifespanLabel;
    private final JLabel averageOffspringCount;
    private final int squareSize = 40;
    private final int panelWidth = 200;
    private boolean stopped = false;
    private boolean showingDominant = false;

    public StatPanel(Simulation simulation, MapPanel mapPanel){

        this.mapPanel = mapPanel;
        this.simulation = simulation;
        this.map = simulation.map;
        mapPanel.statPanel = this;

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(panelWidth, squareSize* map.height));
        setLayout(new FlowLayout());

        startStopButton = new JButton();
        startStopButton.setText("STOP");
        startStopButton.addActionListener(this);
        add(startStopButton);

        animalsCountLabel = new JLabel();
        animalsCountLabel.setText("animals on map: " + simulation.stats.countAnimals());
        animalsCountLabel.setForeground(Color.white);
        add(animalsCountLabel);

        grassCountLabel = new JLabel();
        grassCountLabel.setText("grass on map: " + simulation.stats.countGrass());
        grassCountLabel.setForeground(Color.white);
        add(grassCountLabel);

        dominantGenomeLabel = new JLabel();
        dominantGenomeLabel.setText("dominant genome: []");
        dominantGenomeLabel.setForeground(Color.white);
        add(dominantGenomeLabel);

        averageLifespanLabel = new JLabel();
        averageLifespanLabel.setText("average lifespan: " + simulation.stats.getAverageLifespan());
        averageLifespanLabel.setForeground(Color.white);
        add(averageLifespanLabel);

        averageOffspringCount = new JLabel();
        averageOffspringCount.setText("average offspring count: " + simulation.stats.getAverageOffspringCount());
        averageOffspringCount.setForeground(Color.white);
        add(averageOffspringCount);

        showDominantButton = new JButton();
        showDominantButton.setText("show animals \nwith dominant genome");
        showDominantButton.addActionListener(this);
        add(showDominantButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startStopButton){
            if (stopped){
                mapPanel.timer.start();
                startStopButton.setText("STOP");
                stopped = false;
            } else {
                mapPanel.timer.stop();
                startStopButton.setText("START");
                stopped = true;
            }
        } else if (e.getSource() == showDominantButton){
            if (showingDominant){
                showDominantButton.setText("show animals \nwith dominant genome");
                mapPanel.setDominantGenome(null);
                showingDominant = false;
            } else {
                String dominantGenome = simulation.stats.getDominantGenome();
                mapPanel.setDominantGenome(dominantGenome);
                showDominantButton.setText("don't show animals \nwith dominant genome");
                showingDominant = true;
            }
        }
        repaint();
    }

    public void updateLabels() {
        animalsCountLabel.setText("animals on map: " + simulation.stats.countAnimals());
        averageOffspringCount.setText("average offspring count: " + simulation.stats.getAverageOffspringCount());
        averageLifespanLabel.setText("average lifespan: " + simulation.stats.getAverageLifespan());
        dominantGenomeLabel.setText("dominant genome: " + simulation.stats.getDominantGenome());
        grassCountLabel.setText("grass on map: " + simulation.stats.countGrass());
        repaint();
    }
}
