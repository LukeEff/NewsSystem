package io.github.lukeeff.newssystem.commands;

import org.bukkit.entity.Player;

public class RemoveNews extends AbstractNews implements InterfaceNews{

    private static final int MINLENGTH = 2;

    public RemoveNews() {
        super(MINLENGTH);
    }


    @Override
    public void modifyNewsMap(Player player, String[] args) {
        final String newsMessageKey = args[getKeyIndex()];
        getConfigUtil().removeFromMessageMap(newsMessageKey);
    }

    @Override
    public int getMinLength() {
        return MINLENGTH;
    }
}
