package io.github.lukeeff.newssystem.commands;

import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * Interface for all News sub command objects. The
 * purpose here is to store sub commands in the
 * same map and still be able to call important
 * overridden methods used in their respective class.
 *
 * @author lukeeff
 * @since 4/25/2020
 */
public interface InterfaceNews {

    /**
     * Modifies the news map relative to the
     * inherited classes' override
     * @param player the player sending the command
     * @param args the arguments of the command
     */
    void modifyNewsMap(@NonNull Player player, @NonNull String[] args);

    /**
     * Gets the minimum sub command length for
     * the target sub command
     * @return the minimum length required in the
     * logic for this sub command.
     */
    int getMINSUBCMDLENGTH();
}
