package io.github.lukeeff.newssystem.utils;

import io.github.lukeeff.newssystem.NewsSystem;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class BroadcastUtil {

    private static final long delay = 80; //Ticks throw in config with message
    private NewsSystem plugin;
    private final Set<UUID> newsRecipent =  new HashSet<>();
    private ArrayList<String> messageList = new ArrayList<>();
    private static boolean scheduleNewsSwitch;

    public BroadcastUtil(NewsSystem instance) {
        plugin = instance;
        addNews("");
    }

    public void addNews(String news) {
        this.messageList.add(ChatColor.AQUA + "Test message 1");
        messageList.add("Another message (2)");
        messageList.add("I am a message (3) ");
        messageList.add(ChatColor.DARK_RED + "Get ready... (4)");
        messageList.add(ChatColor.AQUA + "3... (5)");
        messageList.add(ChatColor.GOLD + "2... (6)");
        messageList.add(ChatColor.LIGHT_PURPLE + "1... (7)");
        messageList.add(ChatColor.BLUE + "Well, that was anticlimactic :I");
    }

    /**
     * Sets the boolean that determines if the
     * news will broadcast
     * @param value true to enable broadcasting
     */
    public void setScheduleNewsSwitch(boolean value) {
        scheduleNewsSwitch = value;
    }

    /**
     * Gets the value of the scheduleNewsSwitch
     * @return true when it is true
     */
    public boolean getScheduleNewsSwitch() {
        return scheduleNewsSwitch;
    }

    private ArrayList<String> getMessageList() {
        return messageList;
    }

    /**
     * Registers/Unregisters a player from the
     * news runnable depending on their value
     * in the database.
     * @param PLAYERID the UUID of the target player
     */
    public void registerPlayer(final UUID PLAYERID) {
        if(DatabaseUtil.canSeeNews(PLAYERID.toString())) {
            newsRecipent.add(PLAYERID);
        } else {
            newsRecipent.remove(PLAYERID);
        }
    }

    /**
     * Send players a packet containing a message in their respective
     * action bar
     * TODO: Explain what the obfuscated method does
     * @param message the message to be broadcast to players in the recipient list
     */
    public void sendActionBar(final String message) {
        //Bukkit.broadcastMessage(ChatColor.GREEN + "List of peeps: " + newsRecipent.toString());
        for(final UUID playerID : newsRecipent) {
            //Bukkit.broadcastMessage(ChatColor.GREEN + "Packets sending"); // Debug
            final CraftPlayer player = (CraftPlayer) Bukkit.getPlayer(playerID);
            final PacketPlayOutChat chat = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
            player.getHandle().playerConnection.sendPacket(chat);
        }
    }

    /**
     * Struggling here to build something clean. Not happy with how this method turned out. I'm sure it could
     * have been accomplished with MUCH more simple code. TODO: Explain thought process
     *
     * Broadcasts news Strings from the messageList
     * to players in the newsRecipent list at a fixed
     * interval
     */
    public void beginBroadcast() {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        ListIterator<String> msg = messageList.listIterator();
        scheduler.runTaskTimer(plugin,
                () -> { if (!msg.hasNext()) { recycleList(msg); }sendActionBar(msg.next()); }, delay, delay);
    }

    /**
     * Stupid method I had to make it infinitely cycle a list :I
     * Cycles an iterator back to its origin
     * @param lIterator the iterator stream to be recycled.
     */
    private void recycleList(ListIterator<String> lIterator) {
        while(lIterator.hasPrevious()) {
            lIterator.previous();
        }
    }

}
