import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;


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
        window.setSize(300, 300);
        window.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        var gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        commandsHistory = new JList<String>(commandHistoryModel);
        mainPanel.add(commandsHistory, gbc);

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
            }
        });
    }

    public void InvokeCommand(String cmd){
        switch (cmd) {
            case "TEST":
                System.out.println("Test OK");
                break;        
            default:
                cmd = "Command <"+ cmd +"> not found...";
        }
        commandHistoryModel.addElement(cmd);
        commandInput.setText("");
    }
}
