package project;
import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class StatPanel extends JPanel implements ActionListener {

    private final Simulation simulation;
    private final MapPanel mapPanel;
    private final JButton startStopButton;
    private final JButton showDominantButton;
    private final JButton saveLongTermStatistics;
    private final JLabel animalsCountLabel;
    private final JLabel grassCountLabel;
    private final JLabel dominantGenomeLabel;
    private final JLabel averageLifespanLabel;
    private final JLabel averageOffspringCount;
    private final JLabel showDominantLabel;
    private final JLabel averageEnergyField;
    public int squareSize = 40;
    private final int panelWidth = 300;
    private final Color BACKGROUND = Color.white;
    private final Color TEXT_COLOR = Color.black;
    private boolean stopped = false;
    private boolean showingDominant = false;
    private DecimalFormat df = new DecimalFormat("#.#");

    public StatPanel(Simulation simulation, MapPanel mapPanel, String name){

        this.mapPanel = mapPanel;
        this.simulation = simulation;
        EvolutionMap map = simulation.map;
        mapPanel.statPanel = this;

        setBackground(BACKGROUND);
        setPreferredSize(new Dimension(panelWidth, squareSize * map.height));
        setLayout(new GridLayout(12,1,5,5));

        JLabel nameLabel = new JLabel();
        nameLabel.setText(name);
        add(nameLabel);

        startStopButton = new JButton();
        startStopButton.setText("STOP");
        startStopButton.addActionListener(this);
        add(startStopButton);

        animalsCountLabel = new JLabel();
        animalsCountLabel.setText("animals on map:" + simulation.stats.countAnimals());
        add(animalsCountLabel);

        grassCountLabel = new JLabel();
        grassCountLabel.setText("grass on map:" + simulation.stats.countGrass());
        add(grassCountLabel);

        dominantGenomeLabel = new JLabel();
        dominantGenomeLabel.setText("dominant genome: ");
        add(dominantGenomeLabel);

        averageLifespanLabel = new JLabel();
        averageLifespanLabel.setText("average lifespan:" + simulation.stats.getAverageLifespan());
        add(averageLifespanLabel);

        averageOffspringCount = new JLabel();
        averageOffspringCount.setText("average offspring count:" + simulation.stats.getAverageOffspringCount());
        add(averageOffspringCount);

        averageEnergyField = new JLabel();
        averageEnergyField.setText("average energy: "+ simulation.stats.getAverageEnergy());
        add(averageEnergyField);

        showDominantLabel = new JLabel("highlight animals with dominant genome");
        add(showDominantLabel);

        showDominantButton = new JButton();
        showDominantButton.setText("SHOW");
        showDominantButton.addActionListener(this);
        add(showDominantButton);

        saveLongTermStatistics = new JButton();
        saveLongTermStatistics.setText("GENERATE STATISTICS");
        saveLongTermStatistics.addActionListener(this);
        add(saveLongTermStatistics);

        for (Component component: this.getComponents()){
            component.setForeground(TEXT_COLOR);
            component.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            if (component instanceof JLabel){
                ((JLabel)component).setHorizontalAlignment(SwingConstants.CENTER);
            }
        }

        nameLabel.setFont(new Font(Font.MONOSPACED,  Font.BOLD, 15));
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
                showDominantLabel.setText("highlight animals with dominant genome");
                showDominantButton.setText("SHOW");
                mapPanel.setDominantGenome(null);
                showingDominant = false;
            } else {
                String dominantGenome = simulation.stats.getDominantGenome();
                mapPanel.setDominantGenome(dominantGenome);
                showDominantLabel.setText("don't highlight animals");
                showDominantButton.setText("HIDE");
                showingDominant = true;
            }
        } else if (e.getSource() == saveLongTermStatistics){
            String input = (String) JOptionPane.showInputDialog(null,
                    "enter number of generations after which average statistics will be saved to file",
                    "Long term statistics", JOptionPane.QUESTION_MESSAGE,
                    null, null, "10");

            try {
                int numGenerations = Integer.parseInt(input);
                mapPanel.simulation.setLongTermStatistics(new LongTermStatistics(simulation.stats, numGenerations, "statistics_results.txt"));
            } catch (Exception exception){
                exception.printStackTrace();
                System.out.println(exception.getMessage());
            }

        }
        repaint();
    }

    public void updateLabels() {
        animalsCountLabel.setText("animals on map: " + simulation.stats.countAnimals());
        averageOffspringCount.setText("average offspring count: " + df.format(simulation.stats.getAverageOffspringCount()));
        averageLifespanLabel.setText("average lifespan: " + df.format(simulation.stats.getAverageLifespan()));
        dominantGenomeLabel.setText("dominant genome: " + simulation.stats.getDominantGenome());
        grassCountLabel.setText("grass on map: " + simulation.stats.countGrass());
        averageEnergyField.setText("average energy: "+ df.format(simulation.stats.getAverageEnergy()));
        repaint();
    }
}
