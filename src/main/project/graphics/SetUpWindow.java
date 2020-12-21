package project.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.json.JSONObject;

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

            int jungleRatio = jsonObject.getInt("jungleRatio");
            if (jungleRatio > 1 && jungleRatio <= 100) labelJungleRatio.setValue(jsonObject.getInt("jungleRatio"));

            int width = jsonObject.getInt("width");
            if (width > 0) labelWidth.setValue(width);

            int height = jsonObject.getInt("height");
            if (height > 0) labelHeight.setValue(jsonObject.getInt("height"));

            int initialPopulation = jsonObject.getInt("initialPopulation");
            if (initialPopulation > 0) labelInitialPopulation.setValue(initialPopulation);

            int initialEnergy = jsonObject.getInt("initialEnergy");
            if (initialEnergy > 0) labelInitialEnergy.setValue(jsonObject.getInt("initialEnergy"));

            labelNutritionalEnergy.setValue(jsonObject.getInt("nutritionalEnergy"));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
        }

        setTitle("Evolution Simulation Setup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7,1));
        setSize(300, 400);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenDim.width/2-getSize().width/2, screenDim.height/2-getSize().height/2);
        setVisible(true);
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

            if (initialPopulation > width * height){
                JOptionPane.showMessageDialog(null, "initial population is bigger than available fields on map", "error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Window window = new Window(width, height, initialPopulation, nutritionalEnergy, initialEnergy, jungleRatio);
            dispose();
        }
    }
}
