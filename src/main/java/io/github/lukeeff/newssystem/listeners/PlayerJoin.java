package io.github.lukeeff.newssystem.listeners;

import io.github.lukeeff.newssystem.NewsSystem;
import io.github.lukeeff.newssystem.utils.BroadcastUtil;
import io.github.lukeeff.newssystem.utils.DatabaseUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * PlayerJoin contains an event listener that
 * listens for when a player joins (who would
 * have thought?) and will essentially
 * register the player into the database and
 * news feed.
 *
 * @author lukeeff
 * @since 4/25/2020
 */
public class PlayerJoin implements Listener {

    @Getter private final NewsSystem PLUGIN;
    @Getter private final DatabaseUtil DATABASEUTIL;
    @Getter private final BroadcastUtil BROADCASTUTIL;

    /**
     * Constructor for PlayerJoin class. Assigns
     * the main instance.
     * @param instance instance of the mian class
     */
    public PlayerJoin(NewsSystem instance) {
        this.PLUGIN = instance;
        this.DATABASEUTIL = getPLUGIN().getDatabaseUtil();
        this.BROADCASTUTIL = getPLUGIN().getBroadcastUtil();
    }

    /**
     * Listens for when a player joins and
     * adds them to the database if they are new
     * and registers them for the news messages if
     * it is enabled for them
     * @param event the event being listened for
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID PLAYERID = player.getUniqueId();

        if(!getDATABASEUTIL().playerExists(PLAYERID)) {
            getDATABASEUTIL().addPlayerToDatabase(PLAYERID);
        }
        getBROADCASTUTIL().registerPlayer(PLAYERID);
    }
}
