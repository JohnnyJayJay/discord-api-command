package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.IMentionable;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// TODO: 31.08.2018 GenericCommandEvent, CommandFailureEvent (mit reason), CommandOnCooldownEvent, CommandUnknownEvent 
/**
 * Represents a command event. This is not much different from a GuildMessageReceivedEvent, though it gives access to the called command
 * and provides several utilities to work with, such as the getFirstMention-methods
 * @author Johnny_JayJay
 * @version 3.2
 * @see GuildMessageReceivedEvent
 */
public class CommandEvent extends GuildMessageReceivedEvent {

    private final Command command;
    private final CommandSettings settings;

    public CommandEvent(JDA api, long responseNumber, Message message, Command command, CommandSettings settings) {
        super(api, responseNumber, message);
        this.command = command;
        this.settings = settings;
    }

    /**
     * Sends a message to the event channel. This only sends a message if the self member has permission to do so.
     * @param msg The message to respond with as a String.
     */
    public void respond(String msg) {
        if (checkBotPermissions(Permission.MESSAGE_WRITE))
            this.getChannel().sendMessage(msg).queue();
    }

    /**
     * Sends a message to the event channel. This only sends a message if the self member has permission to do so.
     * @param msg The message to respond with as a MessageEmbed.
     */
    public void respond(MessageEmbed msg) {
        if (checkBotPermissions(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
            this.getChannel().sendMessage(msg).queue();
    }

    /**
     * Sends a message to the event channel. This only sends a message if the self member has permission to do so.
     * @param msg The message to respond with as a Message object.
     */
    public void respond(Message msg) {
        if (checkBotPermissions(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
            this.getChannel().sendMessage(msg).queue();
    }

    /**
     * @return the CommandEvent.Command instance of this event. This instance works like a container with further
     * information and utility methods about the command that was executed.
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Returns the CommandSettings instance this command was called for.
     * @return an instance of CommandSettings.
     * @see CommandSettings
     */
    public CommandSettings getCommandSettings() {
        return settings;
    }

    /**
     * Returns the arguments of the command.
     * @return the arguments of the command that has been executed.
     */
    public String[] getArgs() {
        return command.args;
    }

    /**
     * Returns the first mention in the event message as an Optional.
     * @return An Optional of the first mentioned entity in the event message.
     * @param mentionTypes The MentionTypes to look for.
     * @see Optional
     */
    public Optional<? extends IMentionable> getFirstMention(Message.MentionType... mentionTypes) {
        Optional<? extends IMentionable> ret;
        List<IMentionable> mentions = this.getMessage().getMentions(mentionTypes);
        if (mentions.isEmpty())
            ret = Optional.empty();
        else
            ret = Optional.of(mentions.get(0));
        return ret;
    }

    /**
     * Returns the first member mention in the event message as an Optional.
     * @return An Optional of the first mentioned Member in the event message.
     * @see Optional
     */
    public Optional<User> getFirstUserMention() {
        return (Optional<User>) getFirstMention(Message.MentionType.USER);
    }

    /**
     * Returns the first role mention in the event message as an Optional.
     * @return An Optional of the first mentioned Role in the event message.
     * @see Optional
     */
    public Optional<Role> getFirstRoleMention() {
        return (Optional<Role>) getFirstMention(Message.MentionType.ROLE);
    }

    /**
     * Returns the first channel mention in the event message as an Optional.
     * @return An Optional of the first mentioned TextChannel in the event message.
     * @see Optional
     */
    public Optional<TextChannel> getFirstChannelMention() {
        return (Optional<TextChannel>) getFirstMention(Message.MentionType.CHANNEL);
    }

    /**
     * Returns whether the Member who executed the command has the given permissions in the event channel.
     * @param permissions One or more Checks to check for.
     * @return true: Member has the permissions in this channel, false: they have not
     */
    public boolean checkMemberPermissions(Permission... permissions) {
        return this.getMember().hasPermission(this.channel, permissions);
    }

    /**
     * Returns whether the self member has the given permissions in the event channel.
     * @param permissions One or more Checks to check for.
     * @return true: self member has the permissions in this channel, false: it has not
     */
    public boolean checkBotPermissions(Permission... permissions) {
        return this.guild.getSelfMember().hasPermission(this.channel, permissions);
    }

    protected static Command parseCommand(String raw, String prefix, CommandSettings settings) {
        return new Command(raw, prefix, settings);
    }

    /**
     * Describes an executed Command. <p>
     * Is used to parse a message which seems to be a command.
     * @author Johnny_JayJay
     * @version 3.2
     */
    public static class Command {

        private final ICommand command;
        private final String joinedArgs;
        private final String rawArgs;
        private final String rawMessage;
        private final String prefix;
        private final String label;
        private final String[] args;

        // TODO: 31.08.2018 performanteste und best accessible methode finden
        private Command(String raw, String prefix, CommandSettings settings) {
            String[] argsWithoutPrefix = raw.replaceFirst(prefix, "").split("\\s+");
            this.label = settings.isLabelIgnoreCase() ? argsWithoutPrefix[0].toLowerCase() : argsWithoutPrefix[0];;
            this.command = settings.getCommands().getOrDefault(this.label, null);
            this.rawMessage = raw;
            this.prefix = prefix;
            this.args = Arrays.copyOfRange(argsWithoutPrefix, 1, argsWithoutPrefix.length);
            this.joinedArgs = String.join(" ", this.args);
            this.rawArgs = raw.replaceFirst(prefix + this.label + "\\s+", "");
        }

        /**
         * Returns the prefix used to call this command.
         * @return The prefix.
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * Returns the label of the command.
         * @return The label of the called command, e.g. "foo" if someone calls the command "!foo" (if the prefix is "!")
         */
        public String getLabel() {
            return label;
        }

        /**
         * Returns the arguments of the command.
         * @return The command arguments. In most cases, this is not of importance, because you get these already explicitly in the onCommand-method of ICommand.
         */
        public String[] getArgs() {
            return args;
        }

        /**
         * Returns the {@code ICommand} instance that executed this command.
         * @return The object that calls the onCommand-method. Might be useful in some special cases.
         */
        public ICommand getExecutor() {
            return command;
        }

        /**
         * Returns the raw content of the event message.
         * @return The raw Message that can also be retrieved with CommandEvent#getMessage#getContentRaw
         */
        public String getRawMessage() {
            return rawMessage;
        }

        /**
         * Gets the unmodified arguments of this Command.
         * @return the raw message without the prefix and the label.
         */
        public String getRawArgs() {
            return rawArgs;
        }

        /**
         * Returns the command arguments not as an array, but as a List.
         * @return the arguments as an immutable List
         */
        public List<String> getArgsAsList() {
            return Collections.unmodifiableList(Arrays.asList(args));
        }

        /**
         * Returns the arguments as a single String.
         * @return the arguments joined with a space
         */
        public String getJoinedArgs() {
            return joinedArgs;
        }

        /**
         * Returns the arguments as a single String.
         * @param fromIndex from which argument index the Strings will be joined.
         * @return the arguments joined with a space
         * @throws IllegalArgumentException if the given index is invalid (higher than the argument length or lower than 0.
         * @deprecated This method was deprecated and replaced by CommandEvent.Command#joinArgs.
         */
        @Deprecated
        public String getJoinedArgs(int fromIndex) {
            if (fromIndex >= args.length || fromIndex < 0)
                throw new IllegalArgumentException("invalid index! The arguments array only has a total length of " + args.length);
            return String.join(" ", Arrays.asList(args).subList(fromIndex, args.length));
        }

        /**
         * Joins the command arguments from a specific index on.
         * @param fromIndex a start index which may not be out of bounds.
         * @return the arguments, joined with a space from a specific index on
         * @throws IllegalArgumentException if a parameter does not apply to the requirements.
         */
        public String joinArgs(int fromIndex) {
            return joinArgs(fromIndex, args.length);
        }

        /**
         * Joins the command arguments from and to a specific index.
         * @param fromIndex a start index which may not be out of bounds.
         * @param toIndex an end index which may not be smaller than fromIndex nor out of bounds.
         * @return the arguments, joined with a space within a specific range
         * @throws IllegalArgumentException if a parameter does not apply to the requirements.
         */
        public String joinArgs(int fromIndex, int toIndex) {
            return joinArgs(" ", fromIndex, toIndex);
        }

        /**
         * Joins the command arguments with a specific delimiter from and to a specific index.
         * @param delimiter the delimiter that is supposed to join the arguments.
         * @param fromIndex a start index which may not be out of bounds.
         * @param toIndex an end index which may not be smaller than fromIndex nor out of bounds.
         * @return the arguments, joined with the given delimiter within a specific range
         * @throws IllegalArgumentException if a parameter does not apply to the requirements.
         */
        public String joinArgs(@Nonnull CharSequence delimiter, int fromIndex, int toIndex) {
            if (fromIndex >= args.length || fromIndex < 0 || toIndex < fromIndex || toIndex > args.length)
                throw new IllegalArgumentException("Invalid index! The indexes are either out of bounds or toIndex is smaller than fromIndex.");
            return String.join(delimiter, Arrays.copyOfRange(args, fromIndex, toIndex));
        }
    }
}
