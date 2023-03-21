package Game;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; 

public class Console {
    private JFrame window = null;

    private static Console _instance = null;

    private JTextField commandInput;
    private DefaultListModel<String> commandHistoryModel = new DefaultListModel<>();
    private JList<String> commandsHistory;

    public static Console Instance() {
        if (_instance == null) {
            _instance = new Console();
        }
        return _instance;
    }

    public boolean isActive() {
        return (window == null) ? false : true;
    }

    public void Open() {
        window = new JFrame("Console");
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setSize(300, 250);
        window.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        var gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        commandsHistory = new JList<String>(commandHistoryModel);

        mainPanel.add(new JScrollPane(commandsHistory), gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        commandInput = new JTextField(10);
        mainPanel.add(commandInput, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;

        var submitBtn = new JButton(">");
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InvokeCommand(commandInput.getText());
            }
        });

        mainPanel.add(submitBtn, gbc);
        window.setContentPane(mainPanel);

        window.setVisible(true);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {                
                window.dispose();
                window = null;
                System.out.println("Disposed");
            }
        });
    }

    private String CurrentTime(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        return myDateObj.format(myFormatObj);
    }

    public void ConsoleOutput(String out){
        commandHistoryModel.addElement(CurrentTime() + ": " + out);
    }

    public void InvokeCommand(String cmd){
        commandHistoryModel.addElement(CurrentTime() + ": >" + cmd);
        commandInput.setText("");
        switch (cmd) {
            case "ping":
                ConsoleOutput("pong");
                break;
            case "modify":
                ConsoleOutput("Now entered into modify mode!");
                Game.Instance().set_state(new GameStateModify());
                break;
            case "play":
                ConsoleOutput("Now entered into play mode!");
                Game.Instance().set_state(new GameStatePlay());
                break;
            default:
                ConsoleOutput("Command <" + cmd + "> not found...");
        }
    }
}
