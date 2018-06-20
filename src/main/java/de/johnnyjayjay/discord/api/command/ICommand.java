package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * An interface used to describe a command class.
 * In order to use this API, every class which is supposed to execute commands must implement this interface.
 * @author Johnny_JayJay
 * @version 2.9
 */

public interface ICommand {

    /**
     * Everything that happens if the command is executed should be written here.
     * This is also the place for argument handling.
     * @param event By this, you are given access to the CommandEvent, in case you want to get its belongings or even modify it.
     * @param member The member who called the command.
     * @param channel The TextChannel in which the command has been called.
     * @param args The command arguments, i.e. everything behind the label ([prefix][label] [arg1] [arg2]...)
     */
    void onCommand(final CommandEvent event, final Member member, final TextChannel channel, final String[] args);

    /**
     * In case you are using the automated help command of this API, your command classes should override this method.
     * @return By default, this returns "No info, description or help set" which will be displayed if someone demands help for this command. An Override
     * should return info about the command, such as usage, description, permissions, aliases...
     */
    default String info() {
        return "No info, description or help set";
    }

}
