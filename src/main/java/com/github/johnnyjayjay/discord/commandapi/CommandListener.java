package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;


class CommandListener extends ListenerAdapter {

    private CommandSettings settings;
    private Map<Long, Long> cooldowns; // Long: User id, Long: last timestamp

    public CommandListener(CommandSettings settings) {
        this.settings = settings;
        this.cooldowns = new HashMap<>();
    }

    // TODO: 03.08.2018 clean this stuff
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!settings.getBlacklistedChannels().contains(event.getChannel().getIdLong()) && (!event.getAuthor().isBot() || settings.botsMayExecute())) {
            String raw = event.getMessage().getContentRaw();
            String prefix = settings.getPrefix(event.getGuild().getIdLong());
            if (raw.startsWith(prefix)) {
                long timestamp = System.currentTimeMillis();
                long userId = event.getAuthor().getIdLong();
                if (cooldowns.containsKey(userId) && (timestamp - cooldowns.get(userId)) < settings.getCooldown()) {
                    if (settings.isResetCooldown())
                        cooldowns.put(userId, timestamp);
                    return;
                }
                cooldowns.put(userId, timestamp);
                CommandEvent.Command cmd = CommandEvent.parseCommand(raw, prefix, settings);
                if (cmd.getExecutor() != null) {
                    cmd.getExecutor().onCommand(new CommandEvent(event.getJDA(), event.getResponseNumber(), event.getMessage(), cmd, settings),
                            event.getMember(), event.getChannel(), cmd.getArgs());
                }
            }
        }
    }
}
