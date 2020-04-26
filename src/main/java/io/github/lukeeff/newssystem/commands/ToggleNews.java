package io.github.lukeeff.newssystem.commands;

import lombok.NonNull;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * This class will be utilized when a player
 * wishes to enable or disable a news feed
 * message.
 * The handleCommand method will be
 * called and toggle the preference from the
 * database and update the news recipient list
 * accordingly.
 *
 * @author lukeeff
 * @since 4/25/2020
 */
public class ToggleNews extends AbstractCommand implements CommandExecutor {

    /**
     * Called when player enters the command "ToggleNews".
     * ToggledNews is defined in here in case the message is
     * ever changed so it can update without a reload being
     * necessary.
     *
     * ToggleNews will enable or disable the news action
     * bar message for a target player that executed
     * the command, depending on the current value. The
     * player will then be sent a message that states
     * the current state of the toggle (on or off).
     *
     * @param player The player that executed the command.
     * @param args the arguments a player entered.
     */
    @Override
    void handleCommand(@NonNull final Player player, @Nullable String[] args) {
        final String toggledNews = getPlugin().getConfigUtil().getToggleMessage();
        final UUID playerID = player.getUniqueId();
        player.sendMessage(toggledNews + getDatabaseUtil().toggleNews(playerID));
        getBroadcastUtilInstance().registerPlayer(playerID);
    }
}
