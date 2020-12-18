package project;
import javax.swing.*;
import java.awt.*;

public class SetupInputLabel extends JPanel {

    public JLabel textLabel;
    public JSpinner inputSpinner;

    public SetupInputLabel(String text){
        setLayout(null);
        setSize(300, 100);
        setBackground(Color.white);

        textLabel = new JLabel(text);
        textLabel.setBounds(10,10, 200, 30);
        textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(textLabel);


        inputSpinner = new JSpinner();
        inputSpinner.setValue(10);
        inputSpinner.setBounds(220, 10, 50, 30);
        add(inputSpinner);
    }

    public int getValue(){
        return (int)inputSpinner.getValue();
    }

    public void setValue(int value){
        inputSpinner.setValue(value);
    }
}
