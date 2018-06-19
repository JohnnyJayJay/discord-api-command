package de.johnnyjayjay.discord.api.command;

import java.util.Arrays;

/**
 * Describes an executed Command.
 * Is used to parse a message which seems to be a command.
 * @author Johnny_JayJay
 * @version 2.5
 */
public class Command {

    private ICommand command;
    private String label;
    private String[] args;

    Command(String raw, CommandSettings settings) {
        var argsWithoutPrefix = raw.replaceFirst(settings.getPrefix(), "").split(" ");
        var commandLabel = argsWithoutPrefix[0];
        var argList = Arrays.asList(argsWithoutPrefix);
        var args = argList.subList(1, argList.size()).toArray(new String[argList.size() - 1]);
        if (settings.getCommands().containsKey(commandLabel)) {
            this.command = settings.getCommands().get(commandLabel);
            this.args = args;
            this.label = commandLabel;
        }
    }

    public String getLabel() {
        return label;
    }

    public String[] getArgs() {
        return args;
    }

    public ICommand getExecutor() {
        return command;
    }

}
