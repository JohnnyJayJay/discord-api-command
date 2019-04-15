package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.hooks.SubscribeEvent

import java.util.HashMap;
import java.util.Map;


class CommandListener extends ListenerAdapter {

    private CommandSettings settings;
    private Map<Long, Long> cooldowns; // Long: User id, Long: last timestamp

    public CommandListener(CommandSettings settings) {
        this.settings = settings;
        this.cooldowns = new HashMap<>();
    }
    
    @SubscribeEvent
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
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
                    return;
                }
                cooldowns.put(userId, timestamp);
                CommandEvent.Command cmd = CommandEvent.parseCommand(raw, prefix, settings);
                if (cmd.getExecutor() != null) {
                    try {
                        cmd.getExecutor().onCommand(new CommandEvent(event.getJDA(), event.getResponseNumber(), event.getMessage(), cmd, settings),
                                event.getMember(), channel, cmd.getArgs());
                    } catch (Throwable t) {
                        CommandSettings.LOGGER.warn("One of the commands had an uncaught exception:", t);
                    }
                } else {
                    Message unknownCommand = settings.getUnknownCommandMessage();
                    if (unknownCommand != null && event.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
                        channel.sendMessage(unknownCommand).queue();
                }
            }
        }
    }
}
