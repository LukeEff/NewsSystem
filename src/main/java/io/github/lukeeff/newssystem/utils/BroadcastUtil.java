package io.github.lukeeff.newssystem.utils;

import io.github.lukeeff.newssystem.NewsSystem;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Handles the action bar logic. Runs a BukkitTask to send action bars with custom
 * messages found in the configuration file at a configurable interval.
 *
 * Struggled to build something clean here as I was going for something
 * easy to read, maintainable and as efficient as I could get it.
 *
 * @author lukeeff
 * @since 4/25/2020
 */
public class BroadcastUtil {

    @Getter private final NewsSystem plugin;
    @Getter private final ConfigUtil configUtil;
    @Getter private final DatabaseUtil databaseUtil;

    @Getter @Setter private long delay;
    @Getter @Setter private Map<String, String> newsMap;
    @Getter @Setter private BukkitTask newsTask;
    @Getter private final BukkitScheduler scheduler;

    @Getter private final Set<UUID> newsRecipient =  new HashSet<>();
    @Getter private final List<String> newsMessages = new ArrayList<>();

    public BroadcastUtil(@NonNull NewsSystem instance) {
        this.plugin = instance;
        this.configUtil = getPlugin().getConfigUtil();
        this.databaseUtil = getPlugin().getDatabaseUtil();
        this.scheduler = getPlugin().getServer().getScheduler();

        setNewsMap(getConfigUtil().getMessageMap());
    }

    /**
     * Updates the messages that will be broadcasted to the
     * players from the config.
     */
    private void updateNewsMessages() {
        getNewsMessages().clear();
        getNewsMessages().addAll(getNewsMap().values());
    }

    /**
     * Registers/Unregisters a player from the
     * news runnable depending on their value
     * in the database.
     *
     * @param playerID the UUID of the target player.
     */
    public void registerPlayer(@NonNull final UUID playerID) {
        if(getDatabaseUtil().canSeeNews(playerID)) {
            getNewsRecipient().add(playerID);
        } else {
            getNewsRecipient().remove(playerID);
        }
    }

    /**
     * Send players a packet containing a message in their respective
     * action bar.
     *
     * @param message the message to be broadcast to players in the recipient list.
     */
    private void sendActionBar(@NonNull final String message) {
        for(UUID playerID : getNewsRecipient()) {
            if(isOnline(playerID)) {
            @NonNull final CraftPlayer player = (CraftPlayer) Bukkit.getPlayer(playerID);
            final String actionMsg = "{\"text\":\"" + message + "\"}";
            final PacketPlayOutChat PACKET = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(actionMsg), (byte) 2);
            player.getHandle().playerConnection.sendPacket(PACKET);
        }  else {
            getNewsRecipient().remove(playerID);
        }}

    }

    /**
     * Checks to see if a player is online.
     *
     * @param playerID the UUID of the player.
     * @return false if the player is not online.
     */
    private boolean isOnline(@NonNull final UUID playerID) {
        if(Bukkit.getPlayer(playerID) == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Struggling here to build something clean. Not super happy
     * with how this feature turned out. I'm sure it could
     * have been accomplished with MUCH more simple code.
     *
     * Broadcasts news Strings from the messageList
     * to players in the newsRecipient list at a fixed
     * interval.
     */
    public void beginBroadcast() {
        updateNewsMessages();
        setDelay(getConfigUtil().getDelay());
        setNewsTask(initiateNewsTask(getNewsMessages().listIterator()));
    }

    /**
     * Called to restart the news task, typically when
     * news is added/removed from the map.
     */
    public void restartNewsTask() {
        getNewsTask().cancel();
        setNewsMap(getConfigUtil().getMessageMap());
        beginBroadcast();
    }

    /**
     * Initiates the action bar task at a specified interval.
     *
     * @param msgs the messages that will be broadcasted
     * @return the task. Returned for cancelling functionality
     */
    private BukkitTask initiateNewsTask(@NonNull ListIterator<String> msgs) {
        return getScheduler().runTaskTimer(getPlugin(), () -> {
            if (!msgs.hasNext()) { recycleList(msgs); } sendActionBar(msgs.next()); }, getDelay(), getDelay());
    }

    /**
     * Stupid method, I had to make it infinitely cycle a list. :I
     *
     * Cycles an iterator back to its origin.
     *
     * @param lIterator the iterator stream to be recycled.
     */
    private void recycleList(@NonNull ListIterator<String> lIterator) {
        while(lIterator.hasPrevious()) {
            lIterator.previous();
        }
    }

}
