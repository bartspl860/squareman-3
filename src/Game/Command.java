package Game;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
     protected final List<String> ALIASES = new ArrayList<>();
     protected static String DEFAULT_CMD_NOT_FOUND_MSG(String cmd) { return "Command <" + cmd + "> not found..."; }
    protected static String DEFAULT_CMD_TOO_MANY_PARAMETERS(String cmd) { return "Command <" + cmd + "> does not take as many parameters..."; }
    protected String description = "default command description";
     public String aliases(){
         var aliases = "";
         for(var alias : ALIASES){
             aliases += alias + ", ";
         }
         return aliases;
     }

    public Command(String... aliases){
        ALIASES.addAll(List.of(aliases));
    }
    public abstract void invoke(String[] parameters);
 }

 final class ModifyCommand extends Command{
    public ModifyCommand(String... aliases){
        super(aliases);
        description = "Enters into modify mode, that allows player to add objects to the game";
    }
     @Override
     public void invoke(String[] parameters) {
         Console.Out("Now entered into modify mode!");
         Game.Instance().set_state(new GameStateModify());
     }
 }
 final class PlayCommand extends Command{
     public PlayCommand(String... aliases){
         super(aliases);
         description = "Enters into play mode, that allows player play normally";
     }
     @Override
     public void invoke(String[] parameters) {
        Console.Out("Now entered into play mode!");
        Game.Instance().set_state(new GameStatePlay());
     }
 }

 final class ObjectsCommand extends Command{
     public ObjectsCommand(String... aliases){
         super(aliases);
         description = "Allows player to see game objects state. Use with parameters: <count>";
     }

     @Override
     public void invoke(String[] parameters) {

         if(parameters == null){
             //Console.Out();
         }

         for(var param : parameters){
             switch (param){
                 case "count": Console.Out(Integer.toString(Game.Objects.size()));
             }
         }
     }
 }

 final class HelpCommand extends Command{
     public HelpCommand(String... aliases){
         super(aliases);
     }

     @Override
     public void invoke(String[] parameters) {
         if(parameters == null){
             for(var cmd : Console.GetAllAvaibleCommands()){
                 Console.Out(cmd.aliases());
             }
             return;
         }
         if(parameters.length > 1){
             Console.Out(DEFAULT_CMD_TOO_MANY_PARAMETERS("help"));
             return;
         }
         var command = Console.findCommand(parameters[0]);
         if(command == null){
             Console.Out(Command.DEFAULT_CMD_NOT_FOUND_MSG(parameters[0]));
             return;
         }
         Console.Out(command.aliases());
         Console.Out(command.description);
     }
 }