package Game;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Server.Client;
import Server.Server;

public abstract class Command {
    protected final List<String> ALIASES = new ArrayList<>();
    protected final List<String> POSSIBLE_PARAMS = new ArrayList<>();

    protected static String DEFAULT_CMD_NOT_FOUND_MSG(String cmd) {
        return "Command <" + cmd + "> not found...";
    }

    protected static String DEFAULT_CMD_TOO_MANY_PARAMETERS(String cmd) {
        return "Command <" + cmd + "> does not take as many parameters...";
    }

    protected static String DEFAULT_CMD_NEED_PARAMETERS(String cmd){
        return "Command <" + cmd + "> requires at least one parameter.\n Use help " + cmd + ".";
    }

    protected String description = "default command description";

    public String aliases() {
        var aliases = "";
        for (var alias : ALIASES) {
            aliases += alias + ", ";
        }
        return aliases;
    }

    public String possible_parameters(){
        var params = "";
        for (var param : POSSIBLE_PARAMS) {
            params += param + ", ";
        }
        return params;
    }

    public Command(String... aliases) {
        ALIASES.addAll(List.of(aliases));
    }

    public abstract void invoke(String[] parameters);
}

final class ModifyCommand extends Command {
    public ModifyCommand(String... aliases) {
        super(aliases);
        description = "Enters into modify mode, that allows player to add objects to the game";
    }

    @Override
    public void invoke(String[] parameters) {
        Console.Out("Now entered into modify mode!");
        Game.Instance().set_state(new GameStateModify());
    }
}

final class PlayCommand extends Command {
    public PlayCommand(String... aliases) {
        super(aliases);
        description = "Enters into play mode, that allows player play normally";
    }

    @Override
    public void invoke(String[] parameters) {
        Console.Out("Now entered into play mode!");
        Game.Instance().set_state(new GameStatePlay());
    }
}

final class ObjectsCommand extends Command {
    public ObjectsCommand(String... aliases) {
        super(aliases);
        description = "Allows player to see game objects state.";
        POSSIBLE_PARAMS.addAll(Arrays.asList("count", "position/pos", "clear"));
    }

    @Override
    public void invoke(String[] parameters) {

        if (parameters == null) {
            Console.Out(DEFAULT_CMD_NEED_PARAMETERS("objects"));
            return;
        }

        for (var param : parameters) {
            switch(param){
                case "count": 
                    Console.Out(Integer.toString(Game.Objects.size()));
                break;
                case "pos":
                case "position": 
                    for(var obj : Game.Objects){
                        Console.Out(obj.toString() + " " + obj._position);
                    }
                break;
                case "clear":
                    Game.Objects.removeIf(o -> !(o instanceof Player));                        
                break;
            }
        }
    }
}

final class HelpCommand extends Command {
    public HelpCommand(String... aliases) {
        super(aliases);
        description = "Wow, that was funny. You really played yourself...";
    }

    @Override
    public void invoke(String[] parameters) {
        if (parameters == null) {
            for (var cmd : Console.GetAllAvaibleCommands()) {
                Console.Out(cmd.aliases());
            }
            return;
        }
        if (parameters.length > 1) {
            Console.Out(DEFAULT_CMD_TOO_MANY_PARAMETERS(this.ALIASES.get(0)));
            return;
        }
        var command = Console.findCommand(parameters[0]);
        if (command == null) {
            Console.Out(Command.DEFAULT_CMD_NOT_FOUND_MSG(parameters[0]));
            return;
        }
        Console.Out(command.aliases());
        Console.Out(command.description);

        if(command.POSSIBLE_PARAMS.isEmpty())
            return;

        Console.Out("Parameters:");
        Console.Out(command.possible_parameters());
    }
}

final class ServerCommand extends Command{

    public ServerCommand(String... aliases) {
        super(aliases);
        description = "Allows to start local server at port 3333";
        POSSIBLE_PARAMS.addAll(Arrays.asList("run", "stop", "connect/c", "disconnect/d"));
    }

    @Override
    public void invoke(String[] parameters) {
        
        if (parameters == null) {
            Console.Out(DEFAULT_CMD_NEED_PARAMETERS(this.ALIASES.get(0)));
            return;
        }

        if(parameters.length > 1){
            Console.Out(DEFAULT_CMD_TOO_MANY_PARAMETERS(this.ALIASES.get(0)));
            return;
        }

        for (var param : parameters) {
            switch (param) {
                case "run":
                    Console.Out("Running local server at port 3333");
                    Server.Instance().start();
                    break;
                case "stop":
                    Console.Out("Stopping local server");
                    try {
                        Server.Instance().stop();
                    } catch (Exception ex) {
                        Console.Out(ex.getMessage());
                    }
                    break;
                case "c":
                case "connect":
                    Console.Out("Connecting to the server...");
                    Console.Out("Enter the server IP address:");
                    String serverIP = System.console().readLine();
                    Console.Out("Enter the server port number:");
                    int serverPort = Integer.parseInt(System.console().readLine());
                    try {
                        Socket socket = new Socket(serverIP, serverPort);
                        Client client = new Client(socket, Server.Instance());
                        client.start();
                        Server.Instance().addClient(client);
                        Console.Out("Connected to the server successfully.");
                    } catch (IOException e) {
                        Console.Out("Failed to connect to the server. Error: " + e.getMessage());
                    }
                    break;
                case "d":
                case "disconnect":
                    Console.Out("NOT IMPLEMENTED");
                    break;
            }
        }
    }
}

final class ClearCommand extends Command{

    public ClearCommand(String... aliases){
        super(aliases);
        description = "Clears console window from invoked commands.";
    }

    @Override
    public void invoke(String[] parameters) {
        
        if(parameters != null){
            Console.Out(DEFAULT_CMD_TOO_MANY_PARAMETERS(this.ALIASES.get(0)));
            return;
        }

        Console.Clear();
    }

}