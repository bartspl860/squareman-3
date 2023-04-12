package Game;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private Console(){
        INIT_COMMANDS();
    }
    public static boolean isActive() {
        return Instance().window != null;
    }

    public static void Open(){
        Instance().IOpen();
    }

    public void IOpen() {
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
        commandInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Console.InvokeCommand();
                }
            }
        });
        mainPanel.add(commandInput, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;

        var submitBtn = new JButton(">");
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InvokeCommand();
            }
        });

        mainPanel.add(submitBtn, gbc);
        window.setContentPane(mainPanel);

        window.setVisible(true);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {   
                window.setVisible(false);             
                window.dispose();
                window = null;
            }
        });        
    }
    private String CurrentTime(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        return myDateObj.format(myFormatObj);
    }
    public static void Out(String out){
        Instance().commandHistoryModel.addElement(out);
    }
    private static final List<Command> AVAILABLE_COMMANDS = new ArrayList<Command>();
    private void INIT_COMMANDS(){ //later from file
        AVAILABLE_COMMANDS.add(new ModifyCommand("modify", "mod"));
        AVAILABLE_COMMANDS.add(new PlayCommand("play", "pl"));
        AVAILABLE_COMMANDS.add(new HelpCommand("help"));
        AVAILABLE_COMMANDS.add(new ObjectsCommand("objects", "objs"));
        AVAILABLE_COMMANDS.add(new ServerCommand("server"));
        AVAILABLE_COMMANDS.add(new ClearCommand("clear", "cls"));
    }
    public static void InvokeCommand(){
        String cmd = Instance().commandInput.getText();
        Instance().commandHistoryModel.addElement(Instance().CurrentTime() + ": >" + cmd);
        Instance().commandsHistory.ensureIndexIsVisible(Instance().commandHistoryModel.getSize());
        Instance().commandInput.setText("");
        var chain = cmd.split(" ");
        if(chain.length == 0){
            cmd = "";
            chain = cmd.split(" ");
        }
        var main_cmd = chain[0];

        String[] parameters = null;
        if(chain.length > 1){
            parameters = Arrays.copyOfRange(chain, 1, chain.length);
        }

        var command = findCommand(main_cmd);

        if(command == null){
            Console.Out(Command.DEFAULT_CMD_NOT_FOUND_MSG(main_cmd));
            return;
        }
        command.invoke(parameters);
    }

    public static Command findCommand(String cmd){
        for(var command : AVAILABLE_COMMANDS){
            for(var alias : command.ALIASES){
                if(cmd.equals(alias)){
                    return command;
                }
            }
        }
        return null;
    }
    public static Command[] GetAllAvaibleCommands(){
        Command[] arr = new Command[AVAILABLE_COMMANDS.size()];
        AVAILABLE_COMMANDS.toArray(arr);
        return arr;
    }
    public static void Clear(){
        Instance().commandHistoryModel.clear();
    }
}
