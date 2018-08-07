package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Set;

/**
 * An interface used to describe a command class.
 * In order to use this API, every class which isDefault supposed to execute commands must implement this interface.
 * @author Johnny_JayJay
 * @version 3.2
 * @since 1.1
 * @see AbstractCommand
 * @see AbstractHelpCommand
 */
@FunctionalInterface
public interface ICommand {

    Message defaultInfo = new MessageBuilder().setContent("No info, description or help set for this command.").build();

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
     * <p>This method is deprecated. Please use the other info-method instead as this is no longer supported.
     */
    @Deprecated
    default String info(Member member) {
        return "No info, description or help set for this command";
    }

    /**
     * In case you are using DefaultHelpCommand of this framework, your command classes should override this method.
     * It will be displayed if someone requests help for this command.
     * This returns the Message that will be displayed.
     * @param member The Member that called the command. Might be helpful to check for permissions or make mentions possible.
     * @param prefix The prefix of the guild the help command was called on.
     * @param labels The registered labels of this ICommand instance. Useful to display aliases.
     * @return the Message object, that, if DefaultHelpCommand is used, will be sent to the channel the help was requested in.
     * @see DefaultHelpCommand
     * @see AbstractHelpCommand
     * @see MessageBuilder
     */
    default Message info(Member member, String prefix, Set<String> labels) {
        return defaultInfo;
    }

}
