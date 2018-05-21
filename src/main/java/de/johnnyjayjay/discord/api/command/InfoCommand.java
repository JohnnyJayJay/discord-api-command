package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import static de.johnnyjayjay.discord.api.command.CommandSettings.getCommands;
import static de.johnnyjayjay.discord.api.command.CommandSettings.getPrefix;

public class InfoCommand implements ICommand {

    @Override
    public boolean canBeExecuted(GuildMessageReceivedEvent event, String label) {
        return true;
    }

    @Override
    public void onCommand(GuildMessageReceivedEvent event, String label, String[] args) {

        if (args.length == 1 && getCommands().containsKey(args[0])) {
            event.getChannel().sendMessage(getCommands().get(args[0]).info()).queue();
        } else
            event.getChannel().sendMessage(info()).queue();

    }

    @Override
    public MessageEmbed info() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription("Displays information about any available command.").setTitle("Usage: " + getPrefix() + "info <Command>");
        return eb.build();
    }


}
