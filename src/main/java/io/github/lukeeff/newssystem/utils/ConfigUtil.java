package io.github.lukeeff.newssystem.utils;

import io.github.lukeeff.newssystem.NewsSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigUtil {

    //Config files
    NewsSystem plugin;
    FileConfiguration config;

    //Paths
    private final String DELAYPATH = "delay";
    private final String TOGGLEPATH = "toggle-news";
    private final String MESSAGELISTPATH = "action-messages";
    private final String INVALIDARGSPATH = "invalid-input";

    public ConfigUtil(NewsSystem instance, FileConfiguration config) {
        plugin = instance;
        this.config = config;
    }

    /**
     * Gets the delay of the action bar messages
     * @return the delay of the action bar message depending
     * with each increment representing a tick
     */
    public int getDelay() {
        return config.getInt(getDelayPath());
    }

    private String getInvalidArgsPath() {
        return this.INVALIDARGSPATH;
    }

    public String getInvalidArgMsg() {
        return getColoredConfigString(getInvalidArgsPath());
    }

    /**
     * Gets the message sent to a player when news is toggled
     * @return the message sent to a player when the news action
     * bar message is toggled
     */
    public String getToggleMessage() {
        return getColoredConfigString(getTogglePath());
    }

    /**
     * Gets the delay path from the config
     * @return the path to the delay value in the config
     */
    private String getDelayPath() {
        return this.DELAYPATH;
    }

    /**
     * Retrieves the path to the toggle value in the config
     * @return the path to the toggle value in the config
     */
    private String getTogglePath() {
        return this.TOGGLEPATH;
    }

    /**
     * Gets a config string in color
     * @param path the path of the string
     * @return the string from the config according to the colors defined by it
     */
    private String getColoredConfigString(String path) {
        final String configString = config.getString(path);
        return toColor(configString);
    }

    /**
     * Finds color keys in a string and sets the key to the appropriate color
     * @param string the string to be converted to color
     * @return the string in color
     */
    private String toColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * TODO: Utilize this index for things like holiday/special news messages!
     * Retrieves the message list from the config file
     * @return the message list
     */
    public Map<String, String> getMessageMap() {
        Map<String, String> coloredMessages = new HashMap<>();
        Map<?,?> newsEvents = config.getMapList(getMessageListPath()).get(0);
        newsEvents.forEach(
                (k, m) -> ((Map<?,?>) m).forEach(
                (key, msg) -> coloredMessages.put((String) key, toColor((String) msg))));
        return coloredMessages;
    }

    /**
     * Retrieves the path of the message list
     * @return the path to the message list in the config
     */
    private String getMessageListPath() {
        return this.MESSAGELISTPATH;
    }

    /**
     * Adds a message to the news list
     * @param message the message to be added
     */
    public void addToMessageList(String key, String message) {
        Map<String, String> messageMap = getMessageMap();
        while(messageMap.containsKey(key)) {
            key += "_";
        }
        messageMap.put(key, message);
        setMessageMap(messageMap);
    }

    public void removeFromMessageMap(String key) {
        Map<String, String> newsMap = getMessageMap();
        newsMap.remove(key);
        setMessageMap(newsMap);
    }

    private void setMessageMap(Map<String,String> newsMap) {
        List<Map> mapList = new ArrayList<>();
        Map<String, Map<String,String>> newsSection = new HashMap<>();
        newsSection.put("news", newsMap);
        mapList.add(newsSection);
        config.set(getMessageListPath(), mapList);
        plugin.getConfigManager().saveConfig();
        plugin.getBroadcastUtil().restartNewsTask();
    }


}
