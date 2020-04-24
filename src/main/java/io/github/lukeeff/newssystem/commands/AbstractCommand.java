package io.github.lukeeff.newssystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

abstract public class AbstractCommand implements CommandExecutor {

    final int minLength = 1;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        final Player player = getPlayer(sender);
        if(player != null) {
            final String playerID = player.getUniqueId().toString();
            handleCommand(player, playerID, args);
        }
        return true;
    }


    abstract void handleCommand(Player player, String playerID, String[] args);

    /**
     * Gets a player type from any type if
     * possible
     * @param sender the object being passed
     * @return true when an instance of Player and null when not.
     */
    public Player getPlayer(Object sender) {
        if(isPlayer(sender)) {
            return (Player) sender;
        } else {
            return null;
        }
    }

    /**
     * Checks if any object is an instance
     * of a player and returns false if not
     * @param type the object being checked
     * @return true if it is a player instance
     */
    private boolean isPlayer(Object type) {
        if (type instanceof Player) {
            return true;
        }
        return false;
    }

}
