package Game;
import java.util.ArrayList;
import java.util.List;

public abstract class GameState {
    protected void main(){}
    protected void spawn_item(Point p){}
    protected void remove_item(Point p){}
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
    protected void spawn_item(Point p) {
        p = p.Add(-8, -30);

        p.x = Math.round(p.x / Wall.SpawnSize) * Wall.SpawnSize;
        p.y = Math.round(p.y / Wall.SpawnSize) * Wall.SpawnSize;

        Wall w = new Wall(p, Wall.SpawnSize, Wall.SpawnSize);
        Game.Objects.add(w);
    }

    @Override
    protected void remove_item(Point p){
        p = p.Add(-8, -30);

        p.x = Math.round(p.x / Wall.SpawnSize) * Wall.SpawnSize;
        p.y = Math.round(p.y / Wall.SpawnSize) * Wall.SpawnSize;

        final Point const_p = p;

        Game.Objects.removeIf(
            (o) -> (o.GetTag() != "player" && o._position.Equals(const_p))
        );
    }
}
