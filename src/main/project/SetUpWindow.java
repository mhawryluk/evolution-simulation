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
    private final JSpinner nutritionalEnergyField;
    private final JSpinner initialEnergyField;
    private final JSpinner initialPopulationField;
    private final JSpinner widthField;
    private final JSpinner heightField;
    private final JSpinner jungleRatioField;


    public SetUpWindow() {

        JLabel labelNutritionalEnergy = new JLabel();
        labelNutritionalEnergy.setText("set nutritional energy of grass: ");
        add(labelNutritionalEnergy);

        nutritionalEnergyField = new JSpinner();
        nutritionalEnergyField.setValue(10);
        nutritionalEnergyField.setBounds(0,0,200,300);
        add(nutritionalEnergyField);

        JLabel labelInitialEnergy = new JLabel();
        labelInitialEnergy.setText("set initial energy of gen 0: ");
        add(labelInitialEnergy);

        initialEnergyField = new JSpinner();
        initialEnergyField.setValue(10);
        initialEnergyField.setBounds(0,0,80,300);
        add(initialEnergyField);

        JLabel labelInitialPopulation = new JLabel();
        labelInitialPopulation.setText("set initial population count: ");
        add(labelInitialPopulation);

        initialPopulationField = new JSpinner();
        initialPopulationField.setValue(10);
        add(initialPopulationField);

        JLabel labelWidthField = new JLabel();
        labelWidthField.setText("set map's width: ");
        add(labelWidthField);

        widthField = new JSpinner();
        widthField.setValue(10);
        widthField.setSize(new Dimension(100, 50));
        add(widthField);

        JLabel labelHeightField = new JLabel();
        labelHeightField.setText("set map's height: ");
        add(labelHeightField);

        heightField = new JSpinner();
        heightField.setValue(15);
        heightField.setBounds(0,0,80,300);
        add(heightField);

        JLabel labelJungleRatioField = new JLabel();
        labelJungleRatioField.setText("set jungle ration (in percents): ");
        add(labelJungleRatioField);

        jungleRatioField = new JSpinner();
        jungleRatioField.setValue(10);
        jungleRatioField.setBounds(0,0,80,300);
        add(jungleRatioField);

        goButton = new JButton();
        goButton.setText("GO");
        goButton.setBounds(0,0, 80, 200);
        goButton.addActionListener(this);
        add(goButton);

        try{
            String json = Files.readString(Path.of("parameters.json"));
            JSONObject jsonObject = new JSONObject(json);

            jungleRatioField.setValue(jsonObject.getInt("jungleRatio"));
            widthField.setValue(jsonObject.getInt("width"));
            heightField.setValue(jsonObject.getInt("height"));
            initialPopulationField.setValue(jsonObject.getInt("initialPopulation"));
            initialEnergyField.setValue(jsonObject.getInt("initialEnergy"));
            nutritionalEnergyField.setValue(jsonObject.getInt("nutritionalEnergy"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(400, 200);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == goButton){
            int nutritionalEnergy = (int)nutritionalEnergyField.getValue();
            int initialPopulation = (int)initialPopulationField.getValue();
            int initialEnergy = (int)initialEnergyField.getValue();
            int width = (int)widthField.getValue();
            int height = (int)heightField.getValue();
            int jungleRatio = (int)jungleRatioField.getValue();

            project.Window window = new Window(width, height, initialPopulation, nutritionalEnergy, initialEnergy, jungleRatio);
            dispose();
        }
    }
}
