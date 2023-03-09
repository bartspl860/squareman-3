import java.awt.Color;
import java.awt.Graphics2D;

public class Wall extends GameObject implements IColorable {

    private Color _color = null;

    public Wall(Point pos, Integer wid, Integer hei) {
        super(pos, wid, hei);
    }

    public Color GetColor() {
        return _color;
    }

    public void SetColor(Color _color) {
        this._color = _color;
    }

    @Override
    public void Draw(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0 ,0));
        g2d.drawRect(
            _position.x,
            _position.y, 
            _width,
            _height
        );
        g2d.setColor(_color);
        g2d.fillRect(
            _position.x + 1,
            _position.y + 1, 
            _width - 1,
            _height - 2
        );        
    }

}
