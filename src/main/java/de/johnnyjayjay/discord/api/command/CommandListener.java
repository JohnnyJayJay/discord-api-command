package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import static de.johnnyjayjay.discord.api.command.CommandSettings.getPrefix;

class CommandListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().startsWith(getPrefix()) && !event.getAuthor().isBot()) {
            CommandHandler.handleCommand(event.getMessage().getContentRaw(), event);
        }

    }

}
