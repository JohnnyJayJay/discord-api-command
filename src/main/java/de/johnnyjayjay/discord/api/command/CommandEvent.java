package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * @author Johnny_JayJay
 * @version 2.5
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
