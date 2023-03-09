public abstract class MobileGameObject extends GameObject {

    protected Point _direction = new Point(0, 0);
    protected Double _speed;

    public MobileGameObject(Point pos, Integer wid, Integer hei, Double speed) {
        super(pos, wid, hei);
        this._speed = speed;
    }

    public void Move(){
        Point velocity = this._direction.Multiply(_speed);
        this._position = this._position.Add(velocity);
        System.out.println(velocity);        
    }

    public Point GetDirection(){
        return _direction;
    }
}
