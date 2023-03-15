import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends MobileGameObject implements IColorable {

    private static Player _instance = null;

    private Color _color = new Color(255, 0, 0);

    private Player(Point pos, Integer wid, Integer hei, Integer speed) {
        super(pos, wid, hei, speed);
    }

    public static Player Instance() {
        if (Player._instance == null){
            Player._instance = new Player(new Point(App.WIN_DIM / 2 - 15, App.WIN_DIM / 2 - 15), 30, 30, 3);
            Player._instance.SetTag("player");
        }                        
        return Player._instance;
    }

    @Override
    public void DrawRect(Graphics2D g2d) {                
        g2d.setColor(new Color(0, 0 ,0));
        g2d.drawRect(
            _position.x,
            _position.y, 
            _width - 1,
            _height - 1 
        );
    }

    @Override
    public void FillRect(Graphics2D g2d) {
        g2d.setColor(_color);
        g2d.fillRect(
            _position.x + 1,
            _position.y + 1, 
            _width - 2,
            _height - 2
        );
    }
        
}
