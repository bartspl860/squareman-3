import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends MobileGameObject implements IColorable {

    private static Player _instance = null;

    private Color _color = new Color(255, 0, 0);

    protected Player(Point pos, Integer wid, Integer hei, Double speed) {
        super(pos, wid, hei, speed);
    }

    public static Player Instance() {
        if (Player._instance == null)
            Player._instance = new Player(new Point(0, 0), 30, 30, 2d);
        return Player._instance;
    }

    @Override
    public void Draw(Graphics2D g2d) {
        
    }
}
