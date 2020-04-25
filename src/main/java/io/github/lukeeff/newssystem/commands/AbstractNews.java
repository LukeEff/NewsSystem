package io.github.lukeeff.newssystem.commands;

import com.sun.istack.internal.NotNull;
import io.github.lukeeff.newssystem.NewsSystem;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * AbstractNews subcommand handling class.
 * Purpose of this class is to avoid duplicate code.
 * Because there are multiple sub classes with
 * different instances, field variables are
 * declared static to save memory.
 *
 * @author lukeeff
 * @since 4/25/2020
 */
abstract public class AbstractNews extends AbstractCommand implements CommandExecutor {


     @Getter private static final int MINSUBLENGTH = 1;
     @Getter private static final int KEYINDEX = 1;

     @Getter @Setter private static Map<String, InterfaceNews> subCommands = new HashMap<>();

    /**
     * Assignment constructor for abstract news. Begins static
     * assignment. News sub commands each define new instances
     * of this class, so it makes sense to have static variables here
     * for efficiency.
     * @param instance instance of the main class
     */
    AbstractNews(@NotNull NewsSystem instance) {
        super(instance);
    }

    /**
     * Default constructor for AbstractNews
     */
    AbstractNews() { }

    /**
     * Called directly after superclass onCommand is called.
     * Checks for additional requirements that are exclusive to
     * a target sub class
     * @param player the player that sent the command
     * @param args the arguments a player inputted.
     */
     @Override
     void handleCommand(@NonNull Player player, @NonNull String[] args) {
        if(isMinLength(args) && isValidSubCommand(args)) {
            subCommands.get(args[0]).modifyNewsMap(player, args);
            getBroadcastUtilInstance().restartNewsTask(); //Updates the news task with player's changes
        } else {
            player.sendMessage(getConfigUtil().getInvalidArgMsg());
        }
     }

    /**
     * Gets an InterfaceNews object from the subCommands HashMap
     * in respect to the key specified
     * <p>The InterfaceNews object is an interface that is inherited by
     * all sub commands. It allows us to group all of these commands
     * together in a tidy fashion and call their methods.</p>
     * @param key the key to the sub command
     * @return the InterfaceNews object that is mapped to that key
     */
     private InterfaceNews getSubCommand(@NotNull String key) {
         return getSubCommands().get(key);
     }

    /**
     * Checks to see if the arguments specified by a player
     * meet the requirements of a sub command.
     * @param args the arguments of the player
     * @return true if all requirements are met
     */
     private boolean isValidSubCommand(@NotNull String[] args) {
         final String SUBCOMMANDKEY = args[0];
         return isSubCommand(args[0]) && isSubCmdMinLength(args, getSubCommand(SUBCOMMANDKEY));
     }

    /**
     * Checks to see if the string a player inputted that represents the sub command
     * key exists in the map of registered sub commands
     * @param command the command key that will query the map
     * @return true if the sub command was found
     */
     private boolean isSubCommand(@NotNull String command) {
         return getSubCommands().containsKey(command);
     }

    /**
     * Checks if the sub command entered is a valid length according to the sub class's
     * requirements for arguments.
     * @param args the arguments of the command sender
     * @param subCommand the sub command that will be called
     * @return true if the arguments are a valid length for the sub command
     */
     private boolean isSubCmdMinLength(String[] args, InterfaceNews subCommand) {
         return args.length >= subCommand.getMINSUBCMDLENGTH();
     }

    /**
     * Checks if a command sent by user is long enough to have a sub command
     * <p>This is done to avoid an out of bounds exception with the length
     * of the arguments that could happen when checking for a valid sub command</p>
     * @param args arguments specified by the player
     * @return true if the player's arguments meet the required length check.
     */
     private boolean isMinLength(String[] args) {
         return args.length >= getMINSUBLENGTH();
     }

    /**
     * Registers new sub commands by adding them with a key command name to a map
     * @param commandName the key that a player will input to call a sub command
     * @param subCommand the sub command that is linked to the key
     */
    public void registerNewsSubCommands(@NotNull String commandName, @NotNull InterfaceNews subCommand) {
         getSubCommands().put(commandName, subCommand);
    }

}
