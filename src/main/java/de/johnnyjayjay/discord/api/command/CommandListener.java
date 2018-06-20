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
                if (cmd.getArgs().length == 0) {
                    this.sendInfo(event.getChannel(), null, null);
                } else if (cmd.getArgs().length == 1 && settings.getCommands().containsKey(cmd.getArgs()[0])) {
                    this.sendInfo(event.getChannel(), settings.getCommands().get(cmd.getArgs()[0]), cmd.getArgs()[0]);
                }
            }
        }
    }

    private void sendInfo(TextChannel channel, ICommand command, String label) {
        var builder = new EmbedBuilder().setTitle("Help");
        if (command == null) {
            String helpLabels = format("[%s]", String.join("|", settings.getHelpLabels().toArray(new String[settings.getHelpLabels().size()])));
            builder.appendDescription(format("To learn more about a specific command, just call `%s%s <label>`.\n", settings.getPrefix(), helpLabels))
                    .appendDescription("The following commands are currently available:\n");
            var commandLabels = settings.getCommands().keySet().toArray(new String[settings.getCommands().keySet().size()]);
            builder.addField("Commands", format("```\n%s%s```", settings.getPrefix(), String.join(format("\n%s", settings.getPrefix()), commandLabels)), false);
            channel.sendMessage(builder.build()).queue();
        } else {
            builder.appendDescription(format("Command Info for: `%s`\n\n", label))
                    .appendDescription(command.info());
            channel.sendMessage(builder.build()).queue();
        }
    }
}
