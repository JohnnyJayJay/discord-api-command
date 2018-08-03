package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The default implementation for AbstractHelpCommand.
 * If you want to use this, add a new instance of this class as a command in your CommandSettings with the put-method.
 * This class is final. To create your own help command implementation, please refer to AbstractHelpCommand.
 * @author JohnnyJayJay
 * @version
 * @see AbstractHelpCommand
 */
public final class DefaultHelpCommand extends AbstractHelpCommand {

    /**
     * Lists all commands along with the information that more help can be received by adding the optional label parameter.
     */
    @Override
    public void provideGeneralHelp(CommandEvent event, String prefix, Map<String, ICommand> commands) {
        Member selfMember = event.getGuild().getSelfMember();
        if (!selfMember.hasPermission(event.getChannel(), Permission.MESSAGE_WRITE))
            return;

        EmbedBuilder embed = getBuilder(selfMember, event.getCommandSettings());
        // There may be problems here, since it is possible to add this command on another CommandSettings-instance than the attribute of this class.
        String helpLabels = "[" + String.join("|", event.getCommandSettings().getLabels(this)) + "]";
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

        CommandSettings settings = event.getCommandSettings();
        EmbedBuilder embed = getBuilder(selfMember, settings);
        embed.appendDescription("**Command Info for:** `").appendDescription("[")
                .appendDescription(String.join("|", settings.getLabels(command))).appendDescription("]`\n\n")
                .appendDescription(settings.getCommands().get(event.getArgs()[0]).info(event.getMember()));
        event.getChannel().sendMessage(embed.build()).queue();
    }

    private EmbedBuilder getBuilder(Member selfMember, CommandSettings settings) {
        return new EmbedBuilder().setColor(settings.getHelpColor() != null ? settings.getHelpColor() : selfMember.getColor());
    }

    /**
     * Returns the default info for this command.
     * @return "Shows all available commands or provides help for a specific command. Usage: `help <label>`"
     */
    @Override
    public String info(Member member) {
        return "Shows all available commands or provides help for a specific command. Usage: `help <label>`";
    }
}
