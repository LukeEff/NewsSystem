package io.github.lukeeff.newssystem.commands;

import io.github.lukeeff.newssystem.NewsSystem;

/**
 * News is the primary command string for all
 * "news" sub commands.
 *
 * @author lukeeff
 * @since 4/25/2020
 */
public class News extends AbstractNews {

    /**
     * Default constructor for news.
     * Initializes field variables in super
     * class.
     *
     * News is the module that gets
     * constructed. Following that,
     * its instance is used to register
     * new sub commands.
     *
     * @param instance the main class instance.
     */
    public News(NewsSystem instance) {
        super(instance);
    }

}
