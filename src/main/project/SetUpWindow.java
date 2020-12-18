package project;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SetUpWindow extends JFrame implements ActionListener {

    private final JButton goButton;
    private final SetupInputLabel labelNutritionalEnergy;
    private final SetupInputLabel labelInitialEnergy;
    private final SetupInputLabel labelInitialPopulation;
    private final SetupInputLabel labelWidth;
    private final SetupInputLabel labelHeight;
    private final SetupInputLabel labelJungleRatio;


    public SetUpWindow() {

        labelWidth = new SetupInputLabel("map's width: ");
        add(labelWidth);

        labelHeight = new SetupInputLabel("map's height: ");
        add(labelHeight);

        labelInitialPopulation = new SetupInputLabel("initial population count: ");
        add(labelInitialPopulation);

        labelNutritionalEnergy = new SetupInputLabel("nutritional energy of grass: ");
        add(labelNutritionalEnergy);

        labelInitialEnergy = new SetupInputLabel("initial energy of gen 0: ");
        add(labelInitialEnergy);

        labelJungleRatio = new SetupInputLabel("jungle ratio (in percents): ");
        add(labelJungleRatio);

        goButton = new JButton();
        goButton.setText("GO");
        goButton.setSize(80, 80);
        goButton.addActionListener(this);
        add(goButton);

        try{
            String json = Files.readString(Path.of("parameters.json"));
            JSONObject jsonObject = new JSONObject(json);

            labelJungleRatio.setValue(jsonObject.getInt("jungleRatio"));
            labelWidth.setValue(jsonObject.getInt("width"));
            labelHeight.setValue(jsonObject.getInt("height"));
            labelInitialPopulation.setValue(jsonObject.getInt("initialPopulation"));
            labelInitialEnergy.setValue(jsonObject.getInt("initialEnergy"));
            labelNutritionalEnergy.setValue(jsonObject.getInt("nutritionalEnergy"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle("Evolution Simulation Setup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7,1));
        setSize(300, 400);
        setVisible(true);

        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenDim.width/2-getSize().width/2, screenDim.height/2-getSize().height/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == goButton){
            int nutritionalEnergy = labelNutritionalEnergy.getValue();
            int initialPopulation = labelInitialPopulation.getValue();
            int initialEnergy = labelInitialEnergy.getValue();
            int width = labelWidth.getValue();
            int height = labelHeight.getValue();
            int jungleRatio = labelJungleRatio.getValue();

            Window window = new Window(width, height, initialPopulation, nutritionalEnergy, initialEnergy, jungleRatio);
            dispose();
        }
    }
}
