package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The default implementation for AbstractHelpCommand.
 * If you want to use this, add a new instance of this class as a command in your CommandSettings with the put-method.
 * This class is final. To create your own help command implementation, please refer to AbstractHelpCommand.
 * @author JohnnyJayJay
 * @version 3.2
 * @see AbstractHelpCommand
 */
public final class DefaultHelpCommand extends AbstractHelpCommand {

    private final Message info = new MessageBuilder().setContent("Command info:\nShows all available commands or provides help for a specific command.").build();

    /**
     * Lists all commands along with the information that more help can be received by adding the optional label parameter.
     */
    @Override
    public void provideGeneralHelp(CommandEvent event, String prefix, Map<String, ICommand> commands) {
        Member selfMember = event.getGuild().getSelfMember();
        if (!selfMember.hasPermission(event.getChannel(), Permission.MESSAGE_WRITE))
            return;

        CommandSettings settings = event.getCommandSettings();
        EmbedBuilder embed = new EmbedBuilder().setColor(settings.getHelpColor() != null ? settings.getHelpColor() : selfMember.getColor());
        String helpLabels = "[" + String.join("|", settings.getLabels(this)) + "]";
        embed.appendDescription("To learn more about a specific command, just call `").appendDescription(prefix)
                .appendDescription(helpLabels).appendDescription(" <label>`.\nThe following commands are currently available:\n");
        String commandsList = commands.keySet().stream().map((label) -> prefix + label).collect(Collectors.joining(", "));
        if (commandsList.length() < 1010)
            embed.addField("Commands", "```\n" + commandsList + "```", false);
        else
            embed.addField("Warning", "Too many commands to show.", false);
        event.getChannel().sendMessage(embed.build()).queue();
    }

    /**
     * Shows the command info based on the method ICommand#info in an embed.
     */
    @Override
    public void provideSpecificHelp(CommandEvent event, String prefix, ICommand command, Set<String> labels) {
        Member selfMember = event.getGuild().getSelfMember();
        if (!selfMember.hasPermission(event.getChannel(), Permission.MESSAGE_WRITE))
            return;

        event.getChannel().sendMessage(command.info(event.getMember(), prefix, labels)).queue();
    }

    /**
     * Returns the default info for this command.
     * @return A message with content "Shows all available commands or provides help for a specific command."
     */
    @Override
    public Message info(Member member, String prefix, Set<String> labels) {
        return info;
    }
}
