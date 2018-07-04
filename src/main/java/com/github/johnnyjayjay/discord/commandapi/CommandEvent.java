package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a command event. This is not much different from a GuildMessageReceivedEvent, though it gives access to the called command.
 * @author Johnny_JayJay
 * @version 3.0
 */
public class CommandEvent extends GuildMessageReceivedEvent {

    private final Command command;

    public CommandEvent(JDA api, long responseNumber, Message message, Command command) {
        super(api, responseNumber, message);
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public String[] getArgs() {
        return command.args;
    }

    /**
     * Describes an executed Command. <p>
     * Is used to parse a message which seems to be a command.
     * @author Johnny_JayJay
     * @version 3.0
     */
    public static class Command {

        private ICommand command;
        private String label;
        private String[] args;

        Command(String raw, String prefix, CommandSettings settings) {
            String[] argsWithoutPrefix = raw.replaceFirst(prefix, "").split("\\s+");
            String commandLabel = settings.labelIgnoreCase() ? argsWithoutPrefix[0].toLowerCase() : argsWithoutPrefix[0];
            List<String> argList = Arrays.asList(argsWithoutPrefix);
            String[] args = argList.subList(1, argList.size()).toArray(new String[argList.size() - 1]);
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

        /**
         * @return the arguments as an immutable List
         */
        public List<String> getArgsAsList() {
            return List.of(args);
        }

        /**
         * @return the arguments joined with a space
         */
        public String getArgsAsString() {
            return String.join(" ", args);
        }

        /**
         * @param fromIndex from which argument index the Strings will be joined.
         * @return the arguments joined with a space
         */
        public String getArgsAsString(int fromIndex) {
            if (fromIndex >= args.length)
                throw new IllegalArgumentException("invalid index! The arguments array only has a total length of " + args.length);
            return String.join(" ", Arrays.asList(args).subList(fromIndex, args.length));
        }
    }
}
