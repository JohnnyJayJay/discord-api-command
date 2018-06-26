package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * To use this API, create a new object of this class and add your command classes by using add(...)<p>
 * When you want your commands to become active, use activate()
 * @author Johnny_JayJay
 * @version 3.0
 */

public class CommandSettings {

    private final String INVALID_PREFIX_MESSAGE = "Prefix cannot be empty or contain the characters +*^|$\\?";
    private final String INVALID_LABEL_MESSAGE = "Label cannot be empty, consist of multiple words or contain new lines!";
    // Regex which only matches valid prefixes
    public static final String VALID_PREFIX = "([^\\\\+*^|$?])+";
    // Regex which only matches valid command labels
    public static final String VALID_LABEL = "([^\\s\\n\\t])+";

    private String defaultPrefix;

    private Set<String> helpLabels; // labels which trigger the auto-generated help command
    private Map<Long, String> prefixMap; // Long: GuildID, String: prefix

    private Map<String, ICommand> commands; // String: command label, ICommand: command class

    private Object jda;

    private CommandListener listener;

    private boolean activated; // ...is this instance activated?
    private boolean useShardManager;


    private boolean useHelpCommand;
    private boolean labelIgnoreCase;


    /**
     * This is the optional constructor in case you are sharding your bot.
     * The parameters are validated automatically. In case of any problems (if the prefix is empty), this will throw a CommandSetException.
     * @param shardManager Put your active ShardManager here. This is important for the activation of the CommandListener.
     * @param defaultPrefix The String you will have to put before every command in order to get your command execution registered. This can later be changed.
     * @param useHelpCommand Set this to true, if you want to use the auto-generated help command of this API. You can configure this by setting the help
     *                       labels with setHelpLabel(String...) and by overriding the method info() in your command classes.
     * @param labelIgnoreCase Set this to true, if you want deactivate case sensitivity for the recognition of labels. E.g.: there will be no difference between the labels "foo",
     *                       "FOO", "FoO" and so on.
     */
    public CommandSettings(@Nonnull String defaultPrefix, @Nonnull ShardManager shardManager, boolean useHelpCommand, boolean labelIgnoreCase) {
        this(defaultPrefix, useHelpCommand, labelIgnoreCase);
        this.jda = shardManager;
        this.useShardManager = true;
    }

    /**
     * This is the constructor.
     * The parameters are validated automatically. In case of any problems (if the prefix is empty), this will throw a CommandSetException.
     * @param jda Put your active JDA here. This is important for the activation of the CommandListener.
     * @param defaultPrefix The String you will have to put before every command in order to get your command execution registered. This can later be changed.
     * @param useHelpCommand Set this to true, if you want to use the auto-generated help command of this API. You can configure this by setting the help
     *                       labels with setHelpLabel(String...) and by overriding the method info() in your command classes.
     * @param labelIgnoreCase Set this to true, if you want deactivate case sensitivity for the recognition of labels. E.g.: there will be no difference between the labels "foo",
     *      *                 "FOO", "FoO" and so on.
     */
    public CommandSettings(@Nonnull String defaultPrefix, @Nonnull JDA jda, boolean useHelpCommand, boolean labelIgnoreCase) {
        this(defaultPrefix, useHelpCommand, labelIgnoreCase);
        this.jda = jda;
        this.useShardManager = false;
    }

    private CommandSettings(@Nonnull String defaultPrefix, boolean useHelpCommand, boolean labelIgnoreCase) {
        this.commands = new HashMap<>();
        this.listener = new CommandListener(this);
        this.activated = false;
        this.setDefaultPrefix(defaultPrefix);
        this.useHelpCommand = useHelpCommand;
        this.labelIgnoreCase = labelIgnoreCase;
        this.helpLabels = new HashSet<>();
        this.prefixMap = new HashMap<>();
    }

    /**
     * Use this method to add help labels. This will only work if you instantiated this class with the parameter useHelpCommand as true.
     * @param labels One or more labels which may later be called by members to list all commands or to show info about one specific command.
     * @return The current object. This is to use fluent interface.
     */
    public CommandSettings setHelpLabel(String... labels) {
        for (String label : labels) {
            if (label.matches(VALID_LABEL))
                helpLabels.add(label);
            else
                throw new CommandSetException(INVALID_LABEL_MESSAGE);
        }
        return this;
    }

    /**
     * Use this method to add commands from your project. Every command which is supposed to be active should be added by this.
     * <p>
     * The two parameters will be put in a HashMap which is used by the API to notice commands.
     * @param label The label which describes your command, i.e. the string after the prefix [prefix][label].
     * @param command An instance of your command class which implements ICommand.
     * @return The current object. This is to use fluent interface.
     * @throws CommandSetException If the label is empty or consists of multiple words.
     */
    public CommandSettings put(@Nonnull ICommand command, @Nonnull String label) {
        if (label.matches(VALID_LABEL))
            commands.put(labelIgnoreCase ? label.toLowerCase() : label, command);
        else
            throw new CommandSetException(INVALID_LABEL_MESSAGE);

        return this;
    }

    /**
     * Use this method to add commands with aliases. <p>
     * Works like put(ICommand, String) but adds multiple labels to the same command.
     * @param command An instance of your command class which implements ICommand.
     * @param labels One or more labels. This will throw a CommandSetException, if the label is empty or contains spaces.
     * @return The current object. This is to use fluent interface.
     */
    public CommandSettings put(@Nonnull ICommand command, @Nonnull String... labels) {
        for (String label : labels)
            this.put(command, label);
        return this;
    }

    /**
     * Use this method to remove existing commands. <p>
     * @param label The label of the command to remove.
     * @return true, if the label was successfully removed. false, if the given label doesn't exist.
     */
    public boolean remove(@Nonnull String label) {
        return commands.remove(labelIgnoreCase ? label.toLowerCase() : label) != null;
    }

    /**
     * Use this method to remove more than one command at a time. <p>
     * @param labels One or more labels to remove
     * @return true, if every label was successfully removed. false, if one of the given labels does not exist and thus was not removed.
     */
    public boolean remove(@Nonnull String... labels) {
        boolean success = true;
        for (String label : labels) {
            if (!this.remove(label))
                success = false;
        }
        return success;
    }

    /**
     * Use this method to set the default prefix.
     * @param prefix The prefix to set. In case the given String is empty, this will throw a CommandSetException.
     * @throws CommandSetException if the prefix is empty.
     */
    public void setDefaultPrefix(@Nonnull String prefix) {
        if (prefix.matches(VALID_PREFIX))
            this.defaultPrefix = prefix;
        else
            throw new CommandSetException(INVALID_PREFIX_MESSAGE);
    }

    /**
     * Use this method to add a custom command prefix to a guild. This will only work if you instantiated this class with useCustomPrefixes set to true.
     * You can remove the custom prefix from a guild by setting its prefix to null.
     * @param guildId The guild id as a long.
     * @param prefix The prefix to be set.
     * @throws CommandSetException if a non-null prefix is empty.
     */
    public void setCustomPrefix(long guildId, String prefix) {
        if (prefix != null && !prefix.matches(VALID_PREFIX))
            throw new CommandSetException(INVALID_PREFIX_MESSAGE);
        prefixMap.put(guildId, prefix);
    }

    /**
     * Sets the prefix and the command HashMap for the rest of the API. This is the last method to call when having finished setting up your commands.<p>
     * Note that activating multiple CommandSettings may cause problems. You can do this to use multiple prefixes, but it is not recommended.<p>
     * This method is important to call because otherwise no command will be registered by the internal command listener.
     * @throws CommandSetException if you already activated this instance.
     */
    public void activate() {
        if (!activated) {
            if (useShardManager)
                ((ShardManager)jda).addEventListener(listener);
            else
                ((JDA)jda).addEventListener(listener);
            activated = true;
        } else
            throw new CommandSetException("CommandSettings already activated!");
    }

    /**
     * Deactivates the current CommandSettings by removing the command listener from jda.
     * The CommandSettings can be activated again by using activate().
     * @throws CommandSetException if you either did not activate this instance or already deactivated it.
     */
    public void deactivate() {
        if (activated) {
            if (useShardManager)
                ((ShardManager)jda).removeEventListener(listener);
            else
                ((JDA)jda).removeEventListener(listener);
            activated = false;
        } else
            throw new CommandSetException("CommandSettings weren't activated yet and can therefore not be deactivated!");
    }

    /**
     * Use this method to get the prefix for a specific guild.
     * @param guildId The id of the guild to check.
     * @return the default prefix, if there is no custom prefix set for the given guild id. Otherwise, it returns the custom prefix.
     */
    public String getPrefix(long guildId) {
        return prefixMap.get(guildId) != null ? prefixMap.get(guildId) : defaultPrefix;
    }

    /**
     * Use this method to get the default prefix.
     * @return default prefix set in the constructor or the method setDefaultPrefix(String).
     */
    public String getPrefix() {
        return defaultPrefix;
    }

    protected boolean labelIgnoreCase() {
        return labelIgnoreCase;
    }

    protected Set<String> getHelpLabels() {
        return helpLabels;
    }

    protected boolean useHelpCommand() {
        return useHelpCommand;
    }

    protected Map<String, ICommand> getCommands() {
        return commands;
    }

}
