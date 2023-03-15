import java.awt.Color;
import java.awt.Graphics2D;

public class Wall extends GameObject implements IColorable {

    public static int SpawnSize = 30;
    public static Color SpawnColor = new Color(0,128,255);
    private Color _color;

    public Wall(Point pos, Integer wid, Integer hei) {
        super(pos, wid, hei);
        _color = SpawnColor;
    }

    @Override
    public void DrawRect(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0 ,0));
        g2d.drawRect(
            _position.x - 1,
            _position.y - 1, 
            _width + 1,
            _height + 1
        );              
    }

    @Override
    public void FillRect(Graphics2D g2d) {
        g2d.setColor(_color);
        g2d.fillRect(
            _position.x,
            _position.y, 
            _width,
            _height
        );  
    }
}
