package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * An interface used to describe a command.
 * In order to use this API, every class which is supposed to execute commands must implement this interface.
 * @author Johnny_JayJay
 * @version 1.2
 */

public interface ICommand {

    /**
     * This method returns whether the command can be executed in the first place.
     * For instance, you can check on permissions by this.
     * This is not supposed to distinguish between different arguments.
     * @param event To decide whether your command will be executed, you get the event to work with.
     * @param label In case you use one class for multiple commands, you can tell them apart by their label.
     * @return true: execute the command if it's called. false: do not execute it.
     */
    boolean canBeExecuted(GuildMessageReceivedEvent event, String label);

    /**
     * Everything that happens if the command is executed should be written here.
     * This is also the place for argument handling.
     * @param event By this, you are given access to the event, in case you want to get its belongings or even modify it.
     * @param label In case you use one class for multiple commands, you can tell them apart by their label.
     * @param args Everything after the label. [prefix][label] [arg1] [arg2] ... [argN]
     */
    void onCommand(GuildMessageReceivedEvent event, String label, String[] args);


    /**
     * This method is called if canBeExecuted() is false.
     * Its purpose is to inform the user of the correct execution of the command or why he is not allowed to use it.
     * @return null, if you don't want to use it. Though if you want to, return a MessageEmbed with the information you'd like to provide.
     */
    MessageEmbed info();


}
