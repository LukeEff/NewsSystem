package io.github.lukeeff.newssystem.commands;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

/**
 * Remove news is an administrative command
 * for removing news from the news map.
 *
 * @author lukeeff
 * @since 4/25/2020
 */
public class RemoveNews extends AbstractNews implements InterfaceNews{

    @Getter private static final int MIN_LENGTH = 2;

    /**
     * Removes a news message from the news map based
     * on a specified key.
     * @param player the player sending the command.
     * @param args the arguments of the command.
     */
    @Override
    public void modifyNewsMap(@Nullable Player player, @NonNull String[] args) {
        final String newsMessageKey = args[getKEY_INDEX()];
        getConfigUtil().removeFromMessageMap(newsMessageKey);
    }

    /**
     * Gets the minimum sub command length for
     * the remove sub command.
     * @return the minimum length required in the
     * logic for the news sub command.
     */
    @Override
    public int getMIN_SUB_CMD_LENGTH() {
        return getMIN_LENGTH();
    }

}
