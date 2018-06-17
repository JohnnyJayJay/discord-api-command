package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.entities.TextChannel;

/**
 * An interface used to describe a command class.
 * In order to use this API, every class which is supposed to execute commands must implement this interface.
 * @author Johnny_JayJay
 * @version 2.5
 */

public interface ICommand {

    /**
     * Everything that happens if the command is executed should be written here.
     * This is also the place for argument handling.
     * @param event By this, you are given access to the event, in case you want to get its belongings or even modify it.
     */
    void onCommand(final CommandEvent event, final TextChannel channel, final String[] args);

}
