package io.github.lukeeff.newssystem.listeners;

import io.github.lukeeff.newssystem.NewsSystem;
import io.github.lukeeff.newssystem.utils.BroadcastUtil;
import io.github.lukeeff.newssystem.utils.DatabaseUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoin implements Listener {

    private NewsSystem plugin;

    public PlayerJoin(NewsSystem instance) {
        plugin = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID PLAYERID = player.getUniqueId();

        if(DatabaseUtil.playerExists(PLAYERID.toString())) {
            plugin.broadcastUtil.registerPlayer(PLAYERID);
        } else {
            DatabaseUtil.addPlayerToDatabase(PLAYERID.toString());
            Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + ChatColor.AQUA + " is joining for the first time!");

        }
    }

}
