package Game;
public abstract class GameObject {
    private static Long InstanceCounter = 0L;
    private Long _id;
    private String _tag = null;

    protected Point _position;
    protected Integer _width;
    protected Integer _height;

    public GameObject(Point pos, Integer wid, Integer hei) {
        this._id = InstanceCounter++;
        this._position = pos;
        this._width = wid;
        this._height = hei;
    }

    public Long GetId(){
        return this._id;
    }

    public void SetTag(String tag){
        this._tag = tag;
    }

    public String GetTag(){
        return this._tag;
    }
    
    public Point GetCenter() {
        Point center = new Point(
                this._position.x + this._width / 2,
                this._position.y + this._width / 2);

        return center;
    }

    public void SetPosition(Point pos) {
        this._position = pos;
    }

    public Point GetPosition() {
        return this._position;
    }

    public void TransformPosition(Point vector) {
        this._position.x += vector.x;
        this._position.y += vector.y;
    }

    public Integer GetWidth() {
        return this._width;
    }

    public Integer GetHeight() {
        return this._height;
    }

    public void Scale(Point size) {
        this._width += (int)size.x;
        this._height += (int)size.y;
    }

    public Boolean IsCollidingRect(GameObject obj) {
        Boolean collision_detected = this._position.x < obj._position.x + obj._width &&
                this._position.y < obj._position.y + obj._height &&
                this._position.x + this._width > obj._position.x &&
                this._position.y + this._height > obj._position.y;
        return collision_detected;
    }

    public void ResolveCollisionRect(GameObject obj) {
        double vector_x, vector_y;

        // get the distance between center points
        vector_x = GetCenter().x - obj.GetCenter().x;
        vector_y = GetCenter().y - obj.GetCenter().y;

        // is the y vector longer than the x vector?
        if (Math.pow(vector_y, 2) > Math.pow(vector_x, 2)) {// square to remove negatives

            // is the y vector pointing down?
            if (vector_y > 0) {

                this._position.y = obj._position.y + obj._height;

            } else { // the y vector is pointing up

                this._position.y = obj._position.y - this._height;

            }
        } else { // the x vector is longer than the y vector

            // is the x vector pointing right?
            if (vector_x > 0) {

                this._position.x = obj._position.x + obj._width;

            } else { // the x vector is pointing left

                this._position.x = obj._position.x - this._width;

            }
        }
    }
    
    public static MobileGameObject AsMobileGameObject(GameObject go){
        return (MobileGameObject)go;
    }
}

