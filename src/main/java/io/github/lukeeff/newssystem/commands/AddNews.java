package io.github.lukeeff.newssystem.commands;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * AddNews is an administrative command for adding
 * new news messages to the news feed. It will update
 * the messages into the configuration file and update
 * the action bar messages without usage of any sort of
 * reload.
 *
 * @author lukeeff
 * @since 4/25/2020
 */
public class AddNews extends AbstractNews implements InterfaceNews {

    @Getter private static final int MINLENGTH = 3;

    /**
     * Logic called after all checks are passed in each
     * respective superclass.<p> Built this way to avoid
     * as much duplicate code as possible when registering
     * commands.</p>
     * @param player the player that sent the command.
     * @param args the arguments in the command.
     */
    @Override
    public void modifyNewsMap(Player player, String[] args) {
        final String KEY = args[getKEYINDEX()];
        final String MESSAGE = toNewsMessage(args);
        getConfigUtil().addToMessageList(KEY, MESSAGE);
    }

    /**
     * Converts a players arguments to the valid news message
     * <p>A new array is made containing the actual words in
     * a given news message and appended to a StringBuilder object
     * with a space in between. The final character is a space, so
     * it gets removed from the StringBuilder object and the new
     * String is returned.</p>
     * @param args the arguments from the player.
     * @return A string representing the news message the player
     * will be adding.
     */
    private String toNewsMessage(String[] args) {
        final StringBuilder NEWSMESSAGE = new StringBuilder();
        final String[] MESSAGEWORDLIST = Arrays.copyOfRange(args, getKEYINDEX() + 1, args.length);

        Arrays.stream(MESSAGEWORDLIST).forEach(word -> NEWSMESSAGE.append(word + " "));
        NEWSMESSAGE.deleteCharAt(NEWSMESSAGE.length() - 1); //Removes extra space
        return NEWSMESSAGE.toString();
    }


    /**
     * A getter for a field variable in this class when
     * handling the InterfaceNews object superclass.
     * @return the minimum argument length for this sub command.
     */
    @Override
    public int getMINSUBCMDLENGTH() {
        return getMINLENGTH();
    }

}
