package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

import static de.johnnyjayjay.discord.api.command.CommandSettings.getPrefix;
import static de.johnnyjayjay.discord.api.command.CommandSettings.getCommands;


class CommandHandler {


    public static void handleCommand(String raw, GuildMessageReceivedEvent event) {

        CommandContainer cmd = new CommandContainer(raw);

        if (cmd.command != null) {
            if (cmd.command.canBeExecuted(event)) {
                cmd.command.onCommand(event, cmd.args);
            } else
                event.getMessage().getTextChannel().sendMessage(cmd.command.info(event.getAuthor())).queue();
        }
    }


    static class CommandContainer {

        ICommand command;
        String[] args;

        CommandContainer(String raw) {

            String beheaded = raw.replaceFirst(getPrefix(), "");
            String[] beheadedSplit = beheaded.split(" ");
            String label = beheadedSplit[0];
            ArrayList<String> argList = new ArrayList<>();

            for (String s : beheadedSplit) {
                argList.add(s);
            }

            String[] args = new String[argList.size() - 1];
            argList.subList(1, argList.size()).toArray(args);

            this.args = args;
            if (getCommands().containsKey(label)) {
                this.command = getCommands().get(label);
            }
        }

    }

}




