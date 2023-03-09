import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JPanel implements ActionListener {
    private static Game _game = null;
    public static Game Instance(){
        if(_game == null)
            _game = new Game();
        return _game;
    }
    public Dimension size;

    protected Game() {
        Objects.add(Player.Instance());
        new Timer(10, this).start();
    }

    public void SetSize(int width, int height){
        this.size = new Dimension(width, height);
    }

    public final static ArrayList<GameObject> Objects = new ArrayList<>();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2d = (Graphics2D)g;
        
        Toolkit.getDefaultToolkit().sync();        

        for (GameObject gobj : Objects) {
            if(gobj instanceof IColorable){
                ((IColorable)gobj).Draw(g2d);
            }
        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {        
        for (GameObject gobj : Objects) {
            if(gobj instanceof MobileGameObject){
                ((MobileGameObject)gobj).Move();
            }
        }
    }
}
