package io.github.lukeeff.newssystem.commands;

import io.github.lukeeff.newssystem.NewsSystem;
import io.github.lukeeff.newssystem.utils.BroadcastUtil;
import io.github.lukeeff.newssystem.utils.ConfigUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

abstract public class AbstractNews extends AbstractCommand implements CommandExecutor {


     private static final int MINSUBLENGTH = 1;
     final int KEYINDEX = 1;
     private static Map<String, InterfaceNews> subCommands;
     private static NewsSystem plugin;
     private static ConfigUtil configUtil;
     private static BroadcastUtil broadcastUtilInstance;
     private static int minLength;

     public AbstractNews(NewsSystem instance) {
        plugin = instance;
        subCommands = new HashMap<>();
     }

     public AbstractNews(int minLength) {
        this.minLength = minLength;
        broadcastUtilInstance = plugin.getBroadcastUtil();
        configUtil = plugin.getConfigUtil();
     }

     @Override
     void handleCommand(Player player, String playerID, String[] args) {
        if(isMinLength(args) && isValidSubCommand(args, getSubCommand(args[0]))) {
            subCommands.get(args[0]).modifyNewsMap(player, args);
            getBroadcastUtilInstance().setNewsMap();
        } else {
            player.sendMessage(getConfigUtil().getInvalidArgMsg());
        }
     }

     int getKeyIndex() {
         return KEYINDEX;
     }

     private Map<String, InterfaceNews> getSubCommands() {
         return this.subCommands;
     }

     private InterfaceNews getSubCommand(String key) {
         return getSubCommands().get(key);
     }

     private boolean isValidSubCommand(String[] args, InterfaceNews subCommand) {
         return isSubCommand(args[0]) && isSubCmdMinLength(args, subCommand);
     }

     private boolean isSubCommand(String command) {
         return getSubCommands().containsKey(command);
     }

     private boolean isSubCmdMinLength(String[] args, InterfaceNews subCommand) {
         return args.length >= subCommand.getMinLength();
     }

     private boolean isMinLength(String[] args) {
         return args.length >= getMinSubLength();
     }

     private int getMinSubLength() {
         return this.MINSUBLENGTH;
     }

    protected ConfigUtil getConfigUtil() {
        return configUtil;
    }

    protected BroadcastUtil getBroadcastUtilInstance() {
        return broadcastUtilInstance;
    }

    public void registerNewsSubCommands(String commandName, InterfaceNews subCommand) {
         subCommands.put(commandName, subCommand);
    }



}
