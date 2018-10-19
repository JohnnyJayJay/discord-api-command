package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.HashMap;
import java.util.Map;


class CommandListener implements EventListener {

    private String globalPrefix; // The Global Prefix
    private CommandSettings settings;
    private Map<Long, Long> cooldowns; // Long: User id, Long: last timestamp

    public CommandListener(CommandSettings settings, String globalPrefix) {
        this.settings = settings;
        this.cooldowns = new HashMap<>();
        this.globalPrefix = globalPrefix;
    }

    @Override
    public void onEvent(Event e) {
        if (!(e instanceof GuildMessageReceivedEvent))
            return;

        GuildMessageReceivedEvent event = (GuildMessageReceivedEvent) e;
        TextChannel channel = event.getChannel();
        // if channel is not blacklisted and author is human (or bot execution is enabled)
        if (!settings.getBlacklistedChannels().contains(channel.getIdLong()) && (!event.getAuthor().isBot() || settings.botsMayExecute())) {
            // if custom thread pool is configured: run async
            settings.execute(() -> {
                String raw = event.getMessage().getContentRaw();
                String prefix = settings.getPrefix(event.getGuild().getIdLong());
                if (raw.startsWith(prefix) || raw.startsWith(globalPrefix)) {
                    CommandEvent.Command cmd = CommandEvent.parseCommand(raw, prefix, settings); // parse command
                    CommandEvent commandEvent = new CommandEvent(event.getJDA(), event.getResponseNumber(), event.getMessage(), cmd, settings); // create event
                    if (cmd.getExecutor() != null) { // if command exists
                        // care about cooldowns
                        long timestamp = System.currentTimeMillis();
                        long userId = event.getAuthor().getIdLong();
                        if (cooldowns.containsKey(userId) && (timestamp - cooldowns.get(userId)) < settings.getCooldown()) {
                            if (settings.isResetCooldown())
                                cooldowns.put(userId, timestamp);
                            Message cooldownMessage = settings.getCooldownMessage();
                            if (cooldownMessage != null)
                                commandEvent.respond(cooldownMessage);
                            return;
                        }
                        cooldowns.put(userId, timestamp);
                        // execute command
                        try {
                            if (settings.mayCall(commandEvent)) {
                                cmd.getExecutor().onCommand(commandEvent, event.getMember(), channel, cmd.getArgs());
                            }
                        } catch (Throwable t) {
                            settings.onException(commandEvent, t);
                            if (settings.isLogExceptions()) {
                                CommandSettings.LOGGER.warn("Command " + cmd.getExecutor().getClass().getName() + " had an uncaught exception:", t);
                            }
                        }
                    } else { // command is unknown
                        settings.onUnknownCommand(commandEvent);
                        // TODO: 03.10.2018 das entfernen
                        Message unknownCommand = settings.getUnknownCommandMessage();
                        if (unknownCommand != null)
                            commandEvent.respond(unknownCommand);
                    }
                }
            });
        }
    }
}
