package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


class CommandListener extends ListenerAdapter {

    private CommandSettings settings;

    public CommandListener(CommandSettings settings) {
        this.settings = settings;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        var raw = event.getMessage().getContentRaw();
        if (raw.startsWith(settings.getPrefix()) && !event.getAuthor().isBot()) {
            Command cmd = new Command(raw, settings);
            if (cmd.getExecutor() != null) {
                cmd.getExecutor().onCommand(new CommandEvent(event.getJDA(), event.getResponseNumber(), event.getMessage(), cmd), event.getChannel(), cmd.getArgs());
            }
        }
    }
}
