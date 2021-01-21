package project.graphics;

import javax.swing.*;
import java.awt.*;

public class Pictures {

    private final Image[] pics;

    public Pictures(int squareSize){
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
    }

    public Image getPicture(int index){
        return pics[index];
    }
}