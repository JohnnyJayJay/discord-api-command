package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * A template for own help command implementations.
 * This class implements ICommand, therefore each sub class can be added as a normal command with CommandSettings#put.
 * The framework provides a default implementation of this class, DefaultHelpCommand.
 * @author JohnnyJayJay
 * @version 3.2
 * @since 3.2
 * @see DefaultHelpCommand
 */
public abstract class AbstractHelpCommand implements ICommand {

    
    @Override
    public final void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
        CommandSettings settings = event.getCommandSettings();
        String prefix = settings.getPrefix(event.getGuild().getIdLong());
        Map<String, ICommand> unmodifiableCommands = Collections.unmodifiableMap(settings.getCommands());
        if (args.length == 1) {
            String label = settings.isLabelIgnoreCase() ? args[0].toLowerCase() : args[0];
            if (settings.getLabelSet().contains(label)) {
                ICommand command = settings.getCommands().get(label);
                this.provideSpecificHelp(event, prefix, command, settings.getLabels(command));
            } else {
                this.provideGeneralHelp(event, prefix, unmodifiableCommands);
            }
        } else {
            this.provideGeneralHelp(event, prefix, unmodifiableCommands);
        }
    }

    /**
     * This method is called if someone uses this command with no parameters or doesn't give a valid label as an argument.
     * The best use would be to display all commands or provide general help in another way.
     * @param event the CommandEvent as for usual commands.
     * @param prefix The prefix of the guild this command was called on.
     * @param commands an unmodifiable Map that contains all the prefixes with their corresponding commands that are registered for these CommandSettings.
     */
    public abstract void provideGeneralHelp(CommandEvent event, String prefix, Map<String, ICommand> commands);

    /**
     * This method is called if someone calls the help command and provides a valid label as the first argument.
     * @param event the CommandEvent as for usual commands.
     * @param prefix The prefix of the guild this command was called on.
     * @param command The ICommand that the help was requested for.
     * @param labels All the labels that are associated with the ICommand instance.
     */
    public abstract void provideSpecificHelp(CommandEvent event, String prefix, ICommand command, Set<String> labels);

}
