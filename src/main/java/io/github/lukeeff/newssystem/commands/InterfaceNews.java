package io.github.lukeeff.newssystem.commands;

import org.bukkit.entity.Player;

public interface InterfaceNews {

    void modifyNewsMap(Player player, String[] args);

    int getMinLength();
}
