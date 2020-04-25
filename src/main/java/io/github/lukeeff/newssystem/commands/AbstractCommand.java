package io.github.lukeeff.newssystem.commands;

import com.sun.istack.internal.NotNull;
import io.github.lukeeff.newssystem.NewsSystem;
import io.github.lukeeff.newssystem.utils.BroadcastUtil;
import io.github.lukeeff.newssystem.utils.ConfigUtil;
import io.github.lukeeff.newssystem.utils.DatabaseUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Abstract command is the highest level of command abstraction. It looks
 * for the most common traits of all child classes, such as dependencies
 * and checks, does it all here so code duplication is minimized, and
 * becomes the parent of each command.
 *
 * @author lukeeff
 * @since 4/25/2020
 */
abstract public class AbstractCommand implements CommandExecutor {

    @Getter private static NewsSystem plugin;
    @Getter @Setter private static ConfigUtil configUtil;
    @Getter @Setter private static BroadcastUtil broadcastUtilInstance;
    @Getter @Setter private static DatabaseUtil databaseUtil;

    /**
     * Assignment constructor for abstract news. Begins static
     * assignment. News sub commands each define new instances
     * of this class, so it makes sense to have static variables here
     * for efficiency.
     * @param instance instance of the main class
     */
    AbstractCommand(@NotNull NewsSystem instance) {
        plugin = instance;
        setBroadcastUtilInstance(getPlugin().getBroadcastUtil());
        setConfigUtil(getPlugin().getConfigUtil());
        setDatabaseUtil(getPlugin().getDatabaseUtil());
    }

    /**
     * Default constructor for AbstractCommand.
     */
    AbstractCommand() {}

    /**
     * Overriden onCommand from CommandExecutor. Ensures a player sent a
     * command and calls an abstract method for sub classes to override
     * @param sender the sender of the command
     * @param cmd the command that was executed
     * @param str the name of the command
     * @param args the arguments that follow the command
     * @return true to not show the player what they typed.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if(isPlayer(sender)) {
            Player player = (Player) sender;
            handleCommand(player, args);
        }
        return true;
    }

    /**
     * Essentially another onCommand that is defined by a sub class that
     * checks special requirements for each command
     * @param player the player that sent the command
     * @param args the arguments of the command
     */
    abstract void handleCommand(Player player, String[] args);

    /**
     * Checks if any object is an instance
     * of a player and returns false if not
     * @param type the object being checked
     * @return true if it is a player instance
     */
    private boolean isPlayer(@NonNull Object type) {
        return type instanceof Player;
    }

}
