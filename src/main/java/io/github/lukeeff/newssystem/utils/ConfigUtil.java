package io.github.lukeeff.newssystem.utils;

import io.github.lukeeff.newssystem.NewsSystem;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration Utility class. Takes care of retrieval,
 * storage, and modification of data stored in the
 * configuration file.
 *
 * PS: I kept my mind open to future features when building this,
 * leading to some slightly more complicated logic in respect to
 * retrieval of news messages.
 *
 * @author lukeeff
 * @since 4/25/2020
 */
public class ConfigUtil {

    @Getter private final NewsSystem plugin;
    @Getter private final FileConfiguration config;

    //Paths.
    @Getter private static final String DELAY_PATH = "delay";
    @Getter private static final String TOGGLE_PATH = "toggle-news";
    @Getter private static final String MESSAGE_LIST_PATH = "action-messages";
    @Getter private static final String INVALID_ARGS_PATH = "invalid-input";

    /**
     * Constructor for the config Util class.
     *
     * @param instance instance of the main class.
     * @param config config file reference.
     */
    public ConfigUtil(@NonNull final NewsSystem instance, @NonNull final FileConfiguration config) {
        this.plugin = instance;
        this.config = config;
    }

    /**
     * Gets the delay of the action bar messages.
     *
     * @return the delay of the action bar message depending
     * with each increment representing a tick.
     */
    int getDelay() {
        return getConfig().getInt(getDELAY_PATH());
    }

    /**
     * Retrieves the invalid arguments message from the config file.
     *
     * @return the invalid arguments message in color as a String.
     */
    public String getInvalidArgMsg() {
        return getColoredConfigString(getINVALID_ARGS_PATH());
    }

    /**
     * Gets the message sent to a player when news is toggled.
     *
     * @return the message sent to a player when the news action
     * bar message is toggled.
     */
    public String getToggleMessage() {
        return getColoredConfigString(getTOGGLE_PATH());
    }

    /**
     * Gets a config string in color.
     *
     * @param path the path of the string.
     * @return the string from the config according to the colors defined by it.
     */
    private String getColoredConfigString(@NonNull String path) {
        final String configString = getConfig().getString(path);
        return toColor(configString);
    }

    /**
     * Finds color keys in a string and sets the key to the appropriate color.
     *
     * @param string the string to be converted to color.
     * @return the string in color.
     */
    private String toColor(@NonNull String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * TODO: Utilize this index for things like holiday/special news messages!
     * Retrieves the message list from the config file.
     *
     * This logic might look a little complex, but
     * it is rather simple. Essentially, it retrieves a
     * list of HashMaps from the config, each map being named
     * whats unique about it. I build it this way for future
     * special event implementation, such as a special action bar
     * list for holiday-specific messages. All I'd have to do is
     * have a nullable int with a default value of the index of
     * a news map. The maps contain a key for adding/removing that
     * can be set by the user.
     *
     * @return the message map.
     */
    Map<String, String> getMessageMap() {
        final Map<String, String> coloredMessages = new HashMap<>();
        final Map<?,?> newsEvents = getConfig().getMapList(getMESSAGE_LIST_PATH()).get(0);
        newsEvents.forEach(
                (k, m) -> ((Map<?,?>) m).forEach(
                (key, msg) -> coloredMessages.put((String) key, toColor((String) msg))));
        return coloredMessages;
    }

    /**
     * Adds a message to the news list.
     *
     * When a target key already exists,
     * an underscore is appended to it to avoid
     * overwriting. I didn't bother making that
     * fancy or anything, but wanted it to be
     * a thing.
     *
     * @param message the message to be added.
     */
    public void addToMessageList(@NonNull String key, @NonNull final String message) {
        @NonNull final Map<String, String> messageMap = getMessageMap();
        while(messageMap.containsKey(key)) {
            key += "_";
        }
        messageMap.put(key, message);
        setMessageMap(messageMap);
    }

    /**
     * Removes a message from the message map.
     *
     * @param key the key of the message to be removed.
     */
    public void removeFromMessageMap(@NonNull final String key) {
        @NonNull Map<String, String> newsMap = getMessageMap();
        newsMap.remove(key);
        setMessageMap(newsMap);
    }

    /**
     * Converts a Map to the format used for storing news messages into
     * the config.
     *
     * This would be done differently if other events were added.
     *
     * @param newsMap the news messages map.
     * @return the map inside of a map inside of a list. Created in this manner
     * for future features.
     */
    private List<Map> toConfigListMap(@NonNull final Map<String, String> newsMap) {
        List<Map> mapList = new ArrayList<>(); //Object to be put in config
        Map<String, Map<String,String>> newsSection = new HashMap<>(); //Object to be put in list
        newsSection.put("news", newsMap); //This is only hard coded for the purpose of this activity.
        mapList.add(newsSection);
        return mapList;
    }

    /**
     * Sets a new message map to the config.
     *
     * A message map represents a hashmap that
     * contains a key and a message. The key is simply
     * a way to identify a message and the message
     * is what a player receives in their action bar.
     *
     * @param newsMap the new message map.
     */
    private void setMessageMap(@NonNull final Map<String,String> newsMap) {
        final List<Map> mapList = toConfigListMap(newsMap);
        getConfig().set(getMESSAGE_LIST_PATH(), mapList); //Adds it to config
        getPlugin().getConfigManager().saveConfig(); //Saves config
        getPlugin().getBroadcastUtil().restartNewsTask(); //Restarts scheduler task with updated messages.
    }


}
