package io.github.lukeeff.newssystem.utils;

import io.github.lukeeff.newssystem.NewsSystem;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class BroadcastUtil {

    //Plan
    //Map of Broadcast/Boolean messages.
    //Create stream for the list of true objects

    private static final long delay = 40; //Ticks throw in config with message
    private static final String message = "This is a message";



    /**
     * Send a player a packet containing a message in their respective
     * action bar
     * TODO: Explain what the obfuscated method does
     * @param player the target player
     * @param message the target message
     */
    public static void sendActionBar(Player player, String message) {
        PacketPlayOutChat chat = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(chat);

    }



    /**
     *  This method is not even close to finished.
     *  I'm not certain how exactly I plan to do this,
     *  I'm thinking fetching all players in a database where
     *  some recieveMessage column is true for respective player
     * @param plugin
     */
    public static void beginBroadCast(NewsSystem plugin) {

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, () -> Bukkit.getOnlinePlayers().stream().forEach(player -> sendActionBar(player, message)), delay, delay);

    }

}
