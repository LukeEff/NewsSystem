package io.github.lukeeff.newssystem.commands;

import io.github.lukeeff.newssystem.NewsSystem;
import io.github.lukeeff.newssystem.utils.DatabaseUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class ToggleNews extends AbstractCommand implements CommandExecutor {

    private NewsSystem plugin;
    private final String TOGGLEDNEWS;

    /**
     * Constructor for ToggleNews. Initializes field
     * variables
     * @param instance instance of the main class
     */
    public ToggleNews(NewsSystem instance) {
        plugin = instance;
        TOGGLEDNEWS = plugin.configUtil.getToggleMessage();
    }

    /**
     * Called when player enters the command "ToggleNews"
     * <p>ToggleNews will enable or disable the news action
     * bar message for a target player that executed
     * the command, depending on the current value</p>
     * @param player The player that executed the command
     * @param playerID the UUID of the player as String
     * @param args the arguments a player entered
     */
    @Override
    void handleCommand(Player player, String playerID, String[] args) {
        player.sendMessage(TOGGLEDNEWS + DatabaseUtil.toggleNews(playerID));
        plugin.broadcastUtil.registerPlayer(player.getUniqueId());
    }
}
