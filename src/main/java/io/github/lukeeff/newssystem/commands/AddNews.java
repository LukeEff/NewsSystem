package io.github.lukeeff.newssystem.commands;

import org.bukkit.entity.Player;

import java.util.Arrays;

public class AddNews extends AbstractNews implements InterfaceNews {

    private static final int MINLENGTH = 3;

    public AddNews() {
        super(MINLENGTH);
    }


    public void modifyNewsMap(Player player, String[] args) {
        final int minLength = getMinLength();
        StringBuilder newsMessage = new StringBuilder();
        String newsID = args[getKeyIndex()];
        String[] newsMessageList = Arrays.copyOfRange(args, getKeyIndex() + 1, args.length);
        Arrays.stream(newsMessageList).forEach(word -> newsMessage.append(word + " "));
        getConfigUtil().addToMessageList(newsID, newsMessage.toString());
    }

    @Override
    public int getMinLength() {
        return MINLENGTH;
    }

}
