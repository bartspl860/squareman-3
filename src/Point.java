public class Point {
    public Integer x;
    public Integer y;

    public Point(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void SetLocation(Point loc) {
        this.x = loc.x;
        this.y = loc.y;
    }

    public Point Add(Integer m) {
        return new Point(this.x + m, this.y + m);
    }

    public Point Add(Integer x, Integer y) {
        return new Point(this.x + x, this.y + y);
    }

    public Point Multiply(Integer m) {
        return new Point(this.x * m, this.y * m);
    }

    public Point Multiply(Point m) {
        return new Point(this.x * m.x, this.y * m.y);
    }

    public Point Range(Point axis_x, Point axis_y){
        int x_in_range = Math.min(Math.max(axis_x.x, this.x), axis_x.y);
        int y_in_range = Math.min(Math.max(axis_y.x, this.y), axis_y.y);
        return new Point(x_in_range, y_in_range);
    }

    public static Point FromAwtPoint(java.awt.Point p){
        return new Point(p.x, p.y);
    }

    @Override
    public String toString() {        
        return "x: " + x + " y: " + y;
    }
}
