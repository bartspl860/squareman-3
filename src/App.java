import javax.swing.JFrame;
import java.awt.EventQueue;
import java.io.IOException;

public class App extends JFrame{
    private final static int WIN_DIM = 640;
    public static Game _board = new Game(WIN_DIM, WIN_DIM);
    public App(){
        add(App._board);
        setTitle("InDev");
        setSize(WIN_DIM, WIN_DIM);
        setLocationRelativeTo(null); //center window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(new TAdapter());
    }
    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(() -> {
            App ex = new App();
            ex.setVisible(true);
        });   
    }
}
