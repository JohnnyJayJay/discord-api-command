package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * An interface used to describe a command.
 * In order to use this API, every class which is supposed to execute commands must implement this interface.
 * @author Johnny_JayJay
 * @version 1.3
 */

public interface ICommand {

    /**
     * Everything that happens if the command is executed should be written here.
     * This is also the place for argument handling.
     * @param event By this, you are given access to the event, in case you want to get its belongings or even modify it.
     * @param label In case you use one class for multiple commands, you can tell them apart by their label.
     * @param args Everything after the label. [prefix][label] [arg1] [arg2] ... [argN]
     */
    void onCommand(GuildMessageReceivedEvent event, String label, String[] args);




}
