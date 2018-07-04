package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.join;
import static java.lang.String.format;


class CommandListener extends ListenerAdapter {

    private CommandSettings settings;
    private Map<Long, Long> cooldowns; // Long: User id, Long: last timestamp

    public CommandListener(CommandSettings settings) {
        this.settings = settings;
        this.cooldowns = new HashMap<>();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String raw = event.getMessage().getContentRaw();
        String prefix = settings.getPrefix(event.getGuild().getIdLong());
        if (!event.getAuthor().isBot()) {
            if (raw.startsWith(prefix)) {
                long timestamp = System.currentTimeMillis();
                long userId = event.getAuthor().getIdLong();
                if (cooldowns.containsKey(userId) && (timestamp - cooldowns.get(userId)) < settings.getCooldown())
                    return;
                cooldowns.put(userId, timestamp);
                CommandEvent.Command cmd = new CommandEvent.Command(raw, prefix, settings);
                if (cmd.getExecutor() != null) {
                    cmd.getExecutor().onCommand(new CommandEvent(event.getJDA(), event.getResponseNumber(), event.getMessage(), cmd),
                            event.getMember(), event.getChannel(), cmd.getArgs());
                } else if (settings.getHelpLabels().contains(cmd.getLabel()) && event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_WRITE)) {
                    this.sendInfo(event.getMember(), event.getChannel(), prefix, cmd.getArgs());
                }
            }
        }
    }

    private void sendInfo(Member member, TextChannel channel, String prefix, String[] args) {
        var builder = new EmbedBuilder().setTitle("Help");
        if (args.length == 0) {
            String helpLabels = format("[%s]", join("|", settings.getHelpLabels().toArray(new String[settings.getHelpLabels().size()])));
            builder.appendDescription(format("To learn more about a specific command, just call `%s%s <label>`.\n", prefix, helpLabels))
                    .appendDescription("The following commands are currently available:\n");
            var commandLabels = settings.getCommands().keySet().toArray(new String[settings.getCommands().keySet().size()]);
            builder.addField("Commands", format("```\n%s%s```", prefix, join(format(", %s", prefix), commandLabels)), false);
            channel.sendMessage(builder.build()).queue();
        } else if (args.length == 1 && settings.getCommands().containsKey(args[0])) {
            builder.appendDescription(format("**Command Info for:** `%s`\n\n", args[0]))
                    .appendDescription(settings.getCommands().get(args[0]).info(member));
            channel.sendMessage(builder.build()).queue();
        }
    }
}
