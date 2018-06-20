package de.johnnyjayjay.discord.api.command;

import java.util.Arrays;

/**
 * Describes an executed Command. <p>
 * Is used to parse a message which seems to be a command.
 * @author Johnny_JayJay
 * @version 2.9
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
        this.args = args;
        this.label = commandLabel;
        if (settings.getCommands().containsKey(commandLabel)) {
            this.command = settings.getCommands().get(commandLabel);
        }
    }


    /**
     * @return The label of the called command, e.g. "foo" if someone calls the command "!foo" (if the prefix is "!")
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return The command arguments. In most cases, this is not of importance, because you get these already explicitly in the onCommand-method of ICommand.
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * @return The object that calls the onCommand-method. Might be useful in some special cases.
     */
    public ICommand getExecutor() {
        return command;
    }

}
