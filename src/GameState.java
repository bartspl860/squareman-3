public abstract class GameState {
    protected void main(){}
    protected void spawn_wall(Point p){}
}

class GameStatePlay extends GameState {

    @Override
    public void main() {
        for (GameObject gobj : Game.Objects) {
            if (gobj instanceof MobileGameObject) {
                GameObject.AsMobileGameObject(gobj).Move();                
            }
            for (GameObject current_gobj : Game.Objects) {
                if(current_gobj.GetId() == gobj.GetId())
                    continue;       
                
                if(current_gobj.GetTag() == "player"){
                    if(current_gobj.IsCollidingRect(gobj)){
                        current_gobj.ResolveCollisionRect(gobj);
                    }
                }
            }         
        }
    }
}

class GameStateModify extends GameState {

    @Override
    protected void spawn_wall(Point p) {
        p = p.Add(-8, -30);

        p.x = Math.round(p.x / Wall.SpawnSize) * Wall.SpawnSize;
        p.y = Math.round(p.y / Wall.SpawnSize) * Wall.SpawnSize;

        Wall w = new Wall(p, Wall.SpawnSize, Wall.SpawnSize);
        Game.Objects.add(w);
    }
}
