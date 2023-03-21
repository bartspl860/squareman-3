package Game;
import javax.swing.JFrame;
import java.awt.EventQueue;
import java.io.IOException;

public class App extends JFrame {
    public final static int WIN_DIM = 640;
    private static App _instance = null;

    public static App Instance() {
        if (_instance == null)
            _instance = new App();
        return _instance;
    }

    private App() {
        add(Game.Instance());
        setTitle("InDev");
        setSize(WIN_DIM, WIN_DIM);
        setLocationRelativeTo(null); // center window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        var tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        addMouseListener(tAdapter);
    }

    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(() -> {
            App ex = new App();
            ex.setVisible(true);
        });
    }
}
