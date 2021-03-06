package io.github.lukeeff.newssystem.commands;

import lombok.Getter;
import lombok.NonNull;
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

    @Getter private static final int MIN_LENGTH = 3;

    /**
     * Logic called after all checks are passed in each
     * respective superclass.
     *
     * Built this way to avoid
     * as much duplicate code as possible when registering
     * commands.
     *
     * @param player the player that sent the command.
     * @param args the arguments in the command.
     */
    @Override
    public void modifyNewsMap(@NonNull final Player player, @NonNull final String[] args) {
        final String key = args[getKEY_INDEX()];
        final String message = toNewsMessage(args);
        getConfigUtil().addToMessageList(key, message);
    }

    /**
     * Converts a players arguments to the valid news message.
     *
     * A new array is made containing the actual words in
     * a given news message and appended to a StringBuilder object
     * with a space in between. The final character is a space, so
     * it gets removed from the StringBuilder object and the new
     * String is returned.
     *
     * @param args the arguments from the player.
     * @return A string representing the news message the player
     * will be adding.
     */
    private String toNewsMessage(String[] args) {
        final StringBuilder newsMessage = new StringBuilder();
        final String[] messageWordList = Arrays.copyOfRange(args, getKEY_INDEX() + 1, args.length);

        Arrays.stream(messageWordList).forEach(word -> newsMessage.append(word + " "));
        newsMessage.deleteCharAt(newsMessage.length() - 1); //Removes extra space
        return newsMessage.toString();
    }


    /**
     * A getter for a field variable in this class when
     * handling the InterfaceNews object superclass.
     *
     * @return the minimum argument length for this sub command.
     */
    @Override
    public int getMIN_SUB_CMD_LENGTH() {
        return getMIN_LENGTH();
    }

}
