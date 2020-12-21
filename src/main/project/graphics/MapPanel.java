package project.graphics;
import project.engine.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MapPanel extends JLayeredPane implements ActionListener {

    public final Simulation simulation;
    private final EvolutionMap map;
    public int squareSize;
    public Timer timer;
    public StatPanel statPanel;
    private Animal animalSelected = null;
    private int animalObservationDays;
    private boolean isAnimalObserved = false;
    private int currentObservationDay = 0;
    private String dominantGenome = null;
    private final Image[] pics;
    private Popup popup;
    private int popupX;
    private int popupY;


    Vector2d mousePressedPosition = null;

    public MapPanel(Simulation simulation, int squareSize) {
        this.simulation = simulation;
        this.map = simulation.map;
        this.squareSize = squareSize;
        setBackground(Color.WHITE);
        timer = new Timer(1000, this);
        timer.start();
        setBounds(0, 0, squareSize * map.dimensions.width, squareSize * map.dimensions.height);
        setPreferredSize(new Dimension(map.dimensions.width*squareSize, map.dimensions.height*squareSize));
        setLayout(null);

        pics = new Image[]{
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

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX(), y = e.getY();
                int newPopupX = x/squareSize;
                int newPopupY = y/squareSize;

                ArrayList<Animal> animalsAtPosition = map.animalsAt(new Vector2d(newPopupX, newPopupY));
                if (animalsAtPosition == null || animalsAtPosition.size() == 0) {
                    if (popup != null){
                        popup.hide();
                    }
                    popupX = -1;
                    popupY = -1;
                    return;
                }

                Animal animalHoveredOver = map.animalsAtSortedByEnergy(new Vector2d(newPopupX, newPopupY)).get(0);

                if (newPopupX != popupX && newPopupY != popupY){
                    if (popup != null){
                        popup.hide();
                    }
                    popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), new JLabel(animalHoveredOver.getGenomeString()), e.getXOnScreen() + 20, e.getYOnScreen()+squareSize/2);
                    popup.show();
                    popupX = newPopupX;
                    popupY = newPopupY;
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (animalSelected == null){
                    Vector2d clickedField = new Vector2d(e.getX()/squareSize, e.getY()/squareSize);
                    Animal animal = map.getClickedAnimal(clickedField);
                    if (animal != null){
                        animalSelected = animal;

                        Object paneInput = JOptionPane.showInputDialog(null,
                                                        "enter number of generations",
                                                        "Observe selected animal", JOptionPane.QUESTION_MESSAGE,
                                                            new ImageIcon(pics[animalSelected.getOrientation().getIndex()]),
                                                null, "10");

                        if (paneInput == null) {
                            animalSelected = null;
                            return;
                        }
                        String input = (String) paneInput;

                        if (!input.matches("^[1-9]\\d*$") ||  Integer.parseInt(input) < 1){
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
                                    simulation.setObservedAnimal(animalSelected);
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

        for (int i = 0; i < map.dimensions.width; i++){
            for (int j = 0; j < map.dimensions.height; j++){
                Vector2d position = new Vector2d(i, j);
                if (position.follows(map.dimensions.jungleLowerLeft) && position.precedes(map.dimensions.jungleUpperRight)){
                    g2D.setPaint(new Color(35, 144, 35));
                }
                else {
                    g2D.setPaint(new Color(255, 194, 102));
                }
                g2D.fillRect(position.x*squareSize,position.y*squareSize, squareSize, squareSize);
                g2D.setPaint(Color.black);
                g2D.drawRect(position.x*squareSize,position.y*squareSize, squareSize, squareSize);

                Grass grass = map.grassAt(position);
                if (grass != null){
                    g2D.drawImage(pics[8+ ((position.x + position.y)%2)],  position.x*squareSize, position.y*squareSize, null, this);
                }

                ArrayList<Animal> animalsAtPosition = map.animalsAt(position);
                if (animalsAtPosition != null){
                    for (Animal animal: animalsAtPosition) {
                        if (dominantGenome != null && animal.getGenomeString().equals(dominantGenome)){
                            g2D.setPaint(new Color(153, 187, 255));
                            g2D.fillRect(position.x*squareSize,position.y*squareSize, squareSize, squareSize);
                        }

                        float energyLevel = Math.min(1, (float)animal.getEnergy()/(4*simulation.minimumEnergy));
                        g2D.setPaint(Color.getHSBColor((energyLevel*120)/360, (float)1,(float)1));

                        int barWidth = squareSize / 5;
                        int barHeight = (int) (squareSize * energyLevel);
                        int x = (position.x + 1) * squareSize - barWidth;
                        int y = (position.y + 1) * squareSize - barHeight;
                        g2D.fillRect(x, y , barWidth, barHeight);

                        int orientation = animal.getOrientation().getIndex();
                        g2D.drawImage(pics[orientation],  position.x*squareSize, position.y*squareSize, null, this);
                    }
                }
            }
        }
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
