import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JPanel implements ActionListener {
    private Dimension size;

    public Game(int width, int height) {
        this.size = new Dimension(width, height);

        Objects.add(Player.Instance());
        new Timer(16, this).start();
    }

    public final static ArrayList<GameObject> Objects = new ArrayList<>();

    @Override
    public void paintComponent(Graphics g) {
        var g2d = (Graphics2D)g;
        
        // TODO Auto-generated method stub
        super.paintComponent(g);

        Toolkit.getDefaultToolkit().sync();

        g2d.setColor(new Color(255, 0 ,0));
        g2d.fillRect(
            (int)Player.Instance().GetPosition().x, 
            (int)Player.Instance().GetPosition().y, 
            10, 
            10
        );


        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (GameObject gobj : Objects) {
            if (gobj instanceof MobileGameObject) {

                var mgobj = (MobileGameObject) gobj;

                mgobj.Move();

                //System.out.println(mgobj.GetPosition());
            }
        }
    }
}
