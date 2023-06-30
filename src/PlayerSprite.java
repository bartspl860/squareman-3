import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PlayerSprite {
    private double x,y, size;
     private Color color;

    public PlayerSprite(double x, double y, double size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    public void drawSprite(Graphics2D graphics2D){
        Rectangle2D.Double square = new Rectangle2D.Double(x,y,size,size);
        graphics2D.setColor(color);
        graphics2D.fill(square);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, (int)size, (int)size);
    }


    public void moveH(double n){
        x += n;
    }

    public void moveV(double n){ y += n; }

    public void setX(double n){
        x = n;
    }

    public void setY(double n){
        y = n;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

    public double getSize() {
        return size;
    }
      public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
