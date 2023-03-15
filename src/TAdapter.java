import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TAdapter extends KeyAdapter implements MouseListener{
    private List<Integer> _pressed_keys = new LinkedList<>();
    @Override
    public void keyPressed(KeyEvent e) {
        var keycode = e.getKeyCode();
        if (!_pressed_keys.contains(keycode))
            _pressed_keys.add(keycode);

        if (keycode == KeyEvent.VK_A) {
            Player.Instance()._direction.x = -1;
        } else if (keycode == KeyEvent.VK_D) {
            Player.Instance()._direction.x = 1;
        } else if (keycode == KeyEvent.VK_W) {
            Player.Instance()._direction.y = -1;
        } else if (keycode == KeyEvent.VK_S) {
            Player.Instance()._direction.y = 1;
        }

        if(_pressed_keys.containsAll(Arrays.asList(KeyEvent.VK_SHIFT, KeyEvent.VK_T)))
        {
            _pressed_keys.clear();            
            if(!Console.Instance().isActive()){
                Console.Instance().Open();
            }           
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {        
        var keycode = (Integer) e.getKeyCode();
        _pressed_keys.remove(keycode);

        if(keycode == KeyEvent.VK_A){
            if(!_pressed_keys.contains(KeyEvent.VK_D))
                Player.Instance()._direction.x = 0;
        }
        else if(keycode == KeyEvent.VK_D){
            if(!_pressed_keys.contains(KeyEvent.VK_A))
                Player.Instance()._direction.x = 0;
        }
        else if(keycode == KeyEvent.VK_W){
            if(!_pressed_keys.contains(KeyEvent.VK_S))
                Player.Instance()._direction.y = 0;
        }
        else if(keycode == KeyEvent.VK_S){
            if(!_pressed_keys.contains(KeyEvent.VK_W))
                Player.Instance()._direction.y = 0;
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {        
        
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mousePressed(MouseEvent e) {
        Game.Instance().get_state().spawn_wall(Point.FromAwtPoint(e.getPoint()));
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub        
    }
}