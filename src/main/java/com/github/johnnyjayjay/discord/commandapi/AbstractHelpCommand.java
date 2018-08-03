package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class AbstractHelpCommand implements ICommand {

    protected final CommandSettings settings;

    protected AbstractHelpCommand(CommandSettings settings) {
        this.settings = settings;
    }

    @Override
    public final void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
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

    public abstract void provideGeneralHelp(CommandEvent event, String prefix, Map<String, ICommand> commands);

    public abstract void provideSpecificHelp(CommandEvent event, String prefix, ICommand command, Set<String> labels);

}
