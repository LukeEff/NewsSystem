package io.github.lukeeff.newssystem.commands;

import io.github.lukeeff.newssystem.NewsSystem;
import io.github.lukeeff.newssystem.utils.DatabaseUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class ToggleNews extends AbstractCommand implements CommandExecutor {

    private NewsSystem plugin;

    public ToggleNews(NewsSystem instance) {
        plugin = instance;
    }

    //This will be in config
    private final String TOGGLEDNEWS = ChatColor.GOLD + "News has been toggled " + ChatColor.GRAY;

    @Override
    void handleCommand(Player player, String playerID, String[] args) {
        player.sendMessage(TOGGLEDNEWS + DatabaseUtil.toggleNews(playerID));
        plugin.broadcastUtil.registerPlayer(player.getUniqueId());
    }
}
