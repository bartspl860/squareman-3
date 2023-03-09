public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void SetLocation(Point loc) {
        this.x = loc.x;
        this.y = loc.y;
    }

    public Point Add(Double m) {
        return new Point(this.x + m, this.y + m);
    }

    public Point Add(Point m) {
        return new Point(this.x + m.x, this.y + m.y);
    }

    public Point Multiply(Double m) {
        return new Point(this.x * m, this.y * m);
    }

    public Point Multiply(Point m) {
        return new Point(this.x * m.x, this.y * m.y);
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "x: " + x + " y: " + y;
    }
}
