import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class PlayerSprite {
    private double x,y, size;
     private Color color;

    public PlayerSprite(double x, double y, double size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    public void drawSprite(Graphics2D g2d, BufferedImage image) {
        g2d.setColor(color);
        g2d.drawImage(image, (int) x, (int) y, (int) size, (int) size, null);  
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
