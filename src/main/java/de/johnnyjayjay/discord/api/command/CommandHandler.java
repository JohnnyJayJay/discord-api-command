package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

import static de.johnnyjayjay.discord.api.command.CommandSettings.getPrefix;
import static de.johnnyjayjay.discord.api.command.CommandSettings.getCommands;


class CommandHandler {


    public static void handleCommand(String raw, GuildMessageReceivedEvent event) {
        CommandContainer cmd = new CommandContainer(raw);
        if (cmd.command != null) {
            cmd.command.onCommand(event, cmd.label, cmd.args);
        }
    }


    private static class CommandContainer {

        private final ICommand command;
        private final String label;
        private final String[] args;

        CommandContainer(String raw) {

            String[] beheadedSplit = raw.replaceFirst(getPrefix(), "").split(" ");
            String label = beheadedSplit[0];
            ArrayList<String> argList = new ArrayList<>();
            Arrays.asList(beheadedSplit).stream().forEach((s) -> argList.add(s));
            String[] args = argList.subList(1, argList.size()).toArray(new String[argList.size() - 1]);
            this.args = args;
            this.label = label;
            if (getCommands().containsKey(label)) {
                this.command = getCommands().get(label);
            }
        }

    }

}




