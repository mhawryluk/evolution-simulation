package project;
import javax.jms.IllegalStateException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.TreeSet;

public class MapPanel extends JLayeredPane implements ActionListener {

    private final Simulation simulation;
    private final EvolutionMap map;
    private final int squareSize = 40;
    public Timer timer;
    public StatPanel statPanel;
    private Animal animalSelected = null;
    private int animalObservationDays;
    private boolean isAnimalObserved = false;
    private int currentObservationDay = 0;
    private final JFormattedTextField genomeField = new JFormattedTextField();
    private String dominantGenome = null;

    private final Image[] pics = {
            new ImageIcon("pics/chick-up.png").getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT),
            new ImageIcon("pics/chick-up-right.png").getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT),
            new ImageIcon("pics/chick-right.png").getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT),
            new ImageIcon("pics/chick-down-right.png").getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT),
            new ImageIcon("pics/chick-down.png").getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT),
            new ImageIcon("pics/chick-down-left.png").getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT),
            new ImageIcon("pics/chick-left.png").getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT),
            new ImageIcon("pics/chick-up-left.png").getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT),
            new ImageIcon("pics/carrot1.png").getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT),
            new ImageIcon("pics/carrot2.png").getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT)
    };

    Vector2d mousePressedPosition = null;

    public MapPanel(Simulation simulation) {
        this.simulation = simulation;
        this.map = simulation.map;
        setBackground(Color.WHITE);
        timer = new Timer(1000, this);
        timer.start();
        setBounds(0, 0, squareSize * map.width, squareSize * map.height);
        setPreferredSize(new Dimension(map.width*squareSize, map.height*squareSize));
        setLayout(null);
        add(genomeField, DRAG_LAYER);

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX(), y = e.getY();
                if (x==0 && y==0) return;
                TreeSet <Animal> animalsOnPosition = map.animalsAt(new Vector2d(x/squareSize, y/squareSize));
                if (animalsOnPosition == null) return;
                if (animalsOnPosition.size() == 0) return;

                Animal animalHoveredOver = animalsOnPosition.first(); // last?

                try {
                    genomeField.setBounds(getMousePosition().x + 10, getMousePosition().y+10, 300, 30);
                    genomeField.setText(animalHoveredOver.getGenomeString());
                    genomeField.setLocation(getMousePosition().x+10, getMousePosition().y+10);
                    genomeField.setForeground(Color.blue);
                    genomeField.setBackground(Color.white);
                    genomeField.setVisible(true);
                    genomeField.setOpaque(true);
                    genomeField.repaint();
                } catch (NullPointerException exception){
                    genomeField.setVisible(false);
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (animalSelected == null){
                    Vector2d pressedField = new Vector2d(e.getX()/squareSize, e.getY()/squareSize);
                    Animal animal = map.getPressedAnimal(pressedField);
                    if (animal != null){
                        animalSelected = animal;

                        String input = (String) JOptionPane.showInputDialog(null,
                                                        "enter number of generations",
                                                        "Observe selected animal", JOptionPane.QUESTION_MESSAGE,
                                                            new ImageIcon(pics[animalSelected.getOrientation().getIndex()]),
                                                null, "10");
                        if (input == null) return;

                        if (!input.matches("\\d+")){
                            JOptionPane.showMessageDialog(null, "wrong num of generations", "error", JOptionPane.ERROR_MESSAGE);
                            animalSelected = null;
                            return;
                        }
                        else {
                            int watchedGenerations = Integer.parseInt(input);
                            if (watchedGenerations > 0){
                                animalObservationDays = watchedGenerations;
                                isAnimalObserved = true;
                                currentObservationDay = 0;
                                try{
                                    simulation.setObservedAnimal(animalSelected, animalObservationDays);
                                } catch (IllegalStateException exception){
                                    System.out.println(exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        }
                    }
                    repaint();
                }
            }
        });
    }

    public void paint(Graphics g){

        Graphics2D g2D = (Graphics2D) g;
        super.repaint();

        for (int i = 0; i < map.width; i++){
            for (int j = 0; j < map.height; j++){
                Vector2d position = new Vector2d(i, j);
                if (position.follows(map.jungleLowerLeft) && position.precedes(map.jungleUpperRight)){
                    g2D.setPaint(new Color(35, 144, 35));
                }
                else {
                    g2D.setPaint(new Color(255, 194, 102));
                }
                g2D.fillRect(position.x*squareSize,position.y*squareSize, squareSize, squareSize);
                g2D.setPaint(Color.black);
                g2D.drawRect(position.x*squareSize,position.y*squareSize, squareSize, squareSize);

                int centerX = position.x*squareSize + squareSize/2;
                int centerY = position.y*squareSize + squareSize/2;

                Grass grass = map.grassAt(position);
                if (grass != null){
                    g2D.setPaint(Color.red);
                    int radius = squareSize/4;
                   // g2D.fillOval(centerX-radius, centerY-radius, 2*radius, 2*radius);
                    g2D.drawImage(pics[8+ ((position.x + position.y)%2)],  position.x*squareSize, position.y*squareSize, null, this);
                }

                TreeSet<Animal> animalsAtPosition = map.animalsAt(position);
                if (animalsAtPosition != null){
                    for (Animal animal : animalsAtPosition) {
                        if (dominantGenome != null && animal.getGenomeString().equals(dominantGenome)){
                            g2D.setPaint(Color.red);
                            g2D.fillRect(position.x*squareSize,position.y*squareSize, squareSize, squareSize);
                        }
                        int radius = Math.min(squareSize/2, animal.getEnergy() * squareSize / (10*simulation.minimumEnergy));
                        g2D.setPaint(Color.getHSBColor(((float)(squareSize-2*radius)/squareSize)*200+126, 100,50));
                        g2D.fillOval(centerX - radius, centerY - radius, 2*radius, 2*radius);

                        int orientation = animal.getOrientation().getIndex();
                        g2D.drawImage(pics[orientation],  position.x*squareSize, position.y*squareSize, null, this);
                    }
                }
            }
        }

        //genomeField.paint(g);
        mousePressedPosition = null;
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if (e.getSource() == timer){
            if (isAnimalObserved){
                if (currentObservationDay == animalObservationDays){
                    String observedAnimalInfo = simulation.getObservedAnimalInfo();
                    JOptionPane.showMessageDialog(this, observedAnimalInfo, "Observed animal information",
                            JOptionPane.INFORMATION_MESSAGE, new ImageIcon(pics[animalSelected.getOrientation().getIndex()]));
                    simulation.unsetObservedAnimal();
                    isAnimalObserved = false;
                    animalSelected = null;
                }
                else
                    currentObservationDay++;
            }

            simulation.run();
            statPanel.updateLabels();
        }
        repaint();
    }

    public void setDominantGenome(String genome){
        dominantGenome = genome;
    }
}