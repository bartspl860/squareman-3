import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JPanel implements ActionListener {
    private static Game _game = null;

    public static Game Instance() {
        if (_game == null)
            _game = new Game();
        return _game;
    }

    public final static ArrayList<GameObject> Objects = new ArrayList<>();

    private GameState _state = new GameStatePlay();

    // private Point _cursor_position

    protected Game() {
        Objects.add(Player.Instance());
        new Timer(10, this).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2d = (Graphics2D) g;

        Toolkit.getDefaultToolkit().sync();

        for (GameObject gobj : Objects) {
            if (gobj instanceof IColorable) {
                ((IColorable) gobj).DrawRect(g2d);                
            }
        }
        for (GameObject gobj : Objects) {
            if (gobj instanceof IColorable) {
                ((IColorable) gobj).FillRect(g2d);                
            }
        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        _state.main();
    }

    public GameState get_state() {
        return _state;
    }

    public void set_state(GameState _state) {
        this._state = _state;
    }

    public static ArrayList<GameObject> getObjects() {
        return Objects;
    }
}
