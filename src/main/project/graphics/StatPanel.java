package project.graphics;
import project.engine.EvolutionMap;
import project.engine.LongTermStatistics;
import project.engine.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
    private final JLabel dominantGenomeTextLabel;
    private final JLabel averageOffspringCount;
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
        setPreferredSize(new Dimension(panelWidth, squareSize * map.dimensions.height));
        setLayout(new GridLayout(11,1,5,5));

        JLabel nameLabel = new JLabel();
        nameLabel.setText(name);
        nameLabel.setBackground(Color.darkGray);
        add(nameLabel);

        startStopButton = new JButton();
        startStopButton.setText("STOP");
        startStopButton.addActionListener(this);
        add(startStopButton);

        showDominantButton = new JButton();
        showDominantButton.setText("HIGHLIGHT DOMINANT GENOME");
        showDominantButton.addActionListener(this);
        add(showDominantButton);

        saveLongTermStatistics = new JButton();
        saveLongTermStatistics.setText("GENERATE STATISTICS");
        saveLongTermStatistics.addActionListener(this);
        add(saveLongTermStatistics);

        animalsCountLabel = new JLabel();
        animalsCountLabel.setText("animals on map:" + simulation.statistics.countAnimals());
        add(animalsCountLabel);

        grassCountLabel = new JLabel();
        grassCountLabel.setText("grass on map:" + simulation.statistics.countGrass());
        add(grassCountLabel);

        dominantGenomeTextLabel = new JLabel();
        dominantGenomeTextLabel.setText("dominant genome:");
        add(dominantGenomeTextLabel);

        dominantGenomeLabel = new JLabel("-");
        add(dominantGenomeLabel);

        averageLifespanLabel = new JLabel();
        averageLifespanLabel.setText("average lifespan:" + simulation.statistics.getAverageLifespan());
        add(averageLifespanLabel);

        averageOffspringCount = new JLabel();
        averageOffspringCount.setText("average offspring count:" + simulation.statistics.getAverageChildrenCount());
        add(averageOffspringCount);

        averageEnergyField = new JLabel();
        averageEnergyField.setText("average energy: "+ simulation.statistics.getAverageEnergy());
        add(averageEnergyField);

        for (Component component: this.getComponents()){
            component.setForeground(TEXT_COLOR);
            component.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            if (component instanceof JLabel){
                ((JLabel)component).setHorizontalAlignment(SwingConstants.CENTER);
            }
        }

        nameLabel.setFont(new Font(Font.MONOSPACED,  Font.BOLD, 15));
        nameLabel.setForeground(new Color(0, 102, 0));
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
                showDominantButton.setText("HIGHLIGHT DOMINANT GENOME");
                mapPanel.setDominantGenome(null);
                showingDominant = false;
            } else {
                String dominantGenome = simulation.statistics.getDominantGenome();
                mapPanel.setDominantGenome(dominantGenome);
                showDominantButton.setText("DON'T HIGHLIGHT DOMINANT GENOME");
                showingDominant = true;
            }
        } else if (e.getSource() == saveLongTermStatistics){

            JTextField fileField = new JTextField();
            JTextField nField = new JTextField();

            JPanel inputPanel = new JPanel();
            JLabel nLabel = new JLabel("number of generations after which average statistics will be saved to file: ");
            nLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            inputPanel.add(nLabel);
            inputPanel.add(nField);
            inputPanel.setSize(500, 200);
            inputPanel.setLayout(new GridLayout(2,1));
            JLabel fileLabel = new JLabel("new file name: ");
            fileLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            inputPanel.add(fileLabel);
            inputPanel.add(fileField);

            int result = JOptionPane.showConfirmDialog(null, inputPanel,
                    "Long term statistics", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION){
                try {
                    int numGenerations = Integer.parseInt(nField.getText());
                    if (numGenerations < 1) throw new NumberFormatException();

                    String fileName = fileField.getText();
                    File file = new File(fileName);
                    if(file.exists()) throw new IllegalArgumentException("file already exists, please enter a new file name");

                    mapPanel.simulation.setLongTermStatistics(new LongTermStatistics(simulation.statistics, numGenerations, fileName));
                } catch (NumberFormatException exception){
                    JOptionPane.showMessageDialog(null, "wrong num of generations", "error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException exception){
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception exception){
                    JOptionPane.showMessageDialog(null, "wrong arguments", "error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        repaint();
    }

    public void updateLabels() {
        animalsCountLabel.setText("animals on map: " + simulation.statistics.countAnimals());
        averageOffspringCount.setText("average offspring count: " + df.format(simulation.statistics.getAverageChildrenCount()));
        averageLifespanLabel.setText("average lifespan: " + df.format(simulation.statistics.getAverageLifespan()));
        grassCountLabel.setText("grass on map: " + simulation.statistics.countGrass());
        averageEnergyField.setText("average energy: "+ df.format(simulation.statistics.getAverageEnergy()));

        String dominantGenome = simulation.statistics.getDominantGenome();
        dominantGenomeLabel.setText(dominantGenome);
        if (showingDominant){
            mapPanel.setDominantGenome(dominantGenome);
        }
        repaint();
    }
}
