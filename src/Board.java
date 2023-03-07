import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.*;

public class Board extends JPanel{
    private Dimension size;
    public Board(int width, int height){
        this.size = new Dimension(width, height);
    }
}
