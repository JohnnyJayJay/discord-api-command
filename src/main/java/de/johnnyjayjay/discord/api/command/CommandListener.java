package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import static java.lang.String.format;


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
                cmd.getExecutor().onCommand(new CommandEvent(event.getJDA(), event.getResponseNumber(), event.getMessage(), cmd),
                        event.getMember(), event.getChannel(), cmd.getArgs());
            } else if (settings.useHelpCommand() && settings.getHelpLabels().contains(cmd.getLabel())) {
                this.sendInfo(event.getChannel(), cmd.getArgs());
            }
        }
    }

    private void sendInfo(TextChannel channel, String[] args) {
        var builder = new EmbedBuilder().setTitle("Help");
        if (args.length == 0) {
            String helpLabels = format("[%s]", String.join("|", settings.getHelpLabels().toArray(new String[settings.getHelpLabels().size()])));
            builder.appendDescription(format("To learn more about a specific command, just call `%s%s <label>`.\n", settings.getPrefix(), helpLabels))
                    .appendDescription("The following commands are currently available:\n");
            var commandLabels = settings.getCommands().keySet().toArray(new String[settings.getCommands().keySet().size()]);
            builder.addField("Commands", format("```\n%s%s```", settings.getPrefix(), String.join(format("\n%s", settings.getPrefix()), commandLabels)), false);
            channel.sendMessage(builder.build()).queue();
        } else if (args.length == 1 && settings.getCommands().containsKey(args[0])) {
            builder.appendDescription(format("Command Info for: `%s`\n\n", args[0]))
                    .appendDescription(settings.getCommands().get(args[0]).info());
            channel.sendMessage(builder.build()).queue();
        }
    }
}

