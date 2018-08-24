package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;


class CommandListener implements EventListener {

    private CommandSettings settings;
    private Map<Long, Long> cooldowns; // Long: User id, Long: last timestamp

    public CommandListener(CommandSettings settings) {
        this.settings = settings;
        this.cooldowns = new HashMap<>();
    }

    @Override
    public void onEvent(Event e) {
        if (!(e instanceof GuildMessageReceivedEvent))
            return;

        GuildMessageReceivedEvent event = (GuildMessageReceivedEvent) e;
        TextChannel channel = event.getChannel();
        if (!settings.getBlacklistedChannels().contains(channel.getIdLong()) && (!event.getAuthor().isBot() || settings.botsMayExecute())) {
            String raw = event.getMessage().getContentRaw();
            String prefix = settings.getPrefix(event.getGuild().getIdLong());
            if (raw.startsWith(prefix)) {
                long timestamp = System.currentTimeMillis();
                long userId = event.getAuthor().getIdLong();
                if (cooldowns.containsKey(userId) && (timestamp - cooldowns.get(userId)) < settings.getCooldown()) {
                    if (settings.isResetCooldown())
                        cooldowns.put(userId, timestamp);
                    Message cooldownMessage = settings.getCooldownMessage();
                    if (cooldownMessage != null && event.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
                        channel.sendMessage(cooldownMessage).queue();
                    return;
                }
                cooldowns.put(userId, timestamp);
                CommandEvent.Command cmd = CommandEvent.parseCommand(raw, prefix, settings);
                // TODO: 24.08.2018 Optimize Exception handling
                settings.execute(() -> {
                    if (cmd.getExecutor() != null) {
                        try {
                            CommandEvent commandEvent = new CommandEvent(event.getJDA(), event.getResponseNumber(), event.getMessage(), cmd, settings);
                            if (settings.mayCall(commandEvent)) {
                                cmd.getExecutor().onCommand(commandEvent, event.getMember(), channel, cmd.getArgs());
                            }
                        } catch (Throwable t) {
                            CommandSettings.LOGGER.warn("Command " + cmd.getExecutor().getClass().getName() + " had an uncaught exception:", t);
                        }
                    } else {
                        Message unknownCommand = settings.getUnknownCommandMessage();
                        if (unknownCommand != null && event.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
                            channel.sendMessage(unknownCommand).queue();
                    }
                });
            }
        }
    }
}
