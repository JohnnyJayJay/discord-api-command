package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Represents a command event. This is not much different from a GuildMessageReceivedEvent, though it gives access to the called command.
 * @author Johnny_JayJay
 * @version 3.0
 */
public class CommandEvent extends GuildMessageReceivedEvent {

    private final Command command;

    public CommandEvent(JDA api, long responseNumber, Message message, Command command) {
        super(api, responseNumber, message);
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

}
