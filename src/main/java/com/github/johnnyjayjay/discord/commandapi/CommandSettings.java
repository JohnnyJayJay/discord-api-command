package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * To use this API, create a new object of this class and add your command classes by using add(...)<p>
 * When you want your commands to become active, use activate()
 * @author Johnny_JayJay
 * @version 3.0
 */

public class CommandSettings {

    // Logger
    public static final Logger logger = LoggerFactory.getLogger("CommandAPI");

    // Regex that only matches valid prefixes
    public static final String VALID_PREFIX = "([^\\\\+*^|$?])+";
    // Regex that only matches valid command labels
    public static final String VALID_LABEL = "([^\\s])+";

    private final String INVALID_PREFIX_MESSAGE = "Prefix cannot be empty or contain the characters +*^|$\\?";
    private final String INVALID_LABEL_MESSAGE = "Label cannot be empty, consist of multiple words or contain new lines!";

    private String defaultPrefix;
    private long cooldown;

    private Set<String> helpLabels; // labels which trigger the auto-generated help command
    private Map<Long, String> prefixMap; // Long: GuildID, String: prefix

    private Map<String, ICommand> commands; // String: command label, ICommand: command class

    private Object jda; // The JDA or ShardManager

    private CommandListener listener;

    private boolean activated; // ...is this instance activated?
    private boolean useShardManager;

    private boolean labelIgnoreCase;


    /**
     * This is the optional constructor in case you are sharding your bot.
     * The parameters are validated automatically. In case of any problems (if the prefix is empty), this will throw a CommandSetException.
     * @param shardManager Put your active ShardManager here. This is important for the activation of the CommandListener.
     * @param defaultPrefix The String you will have to put before every command in order to get your command execution registered. This can later be changed.
     * @param labelIgnoreCase Set this to true, if you want deactivate case sensitivity for the recognition of labels. E.g.: there will be no difference between the labels "foo",
     *                       "FOO", "FoO" and so on.
     */
    public CommandSettings(@Nonnull String defaultPrefix, @Nonnull ShardManager shardManager, boolean labelIgnoreCase) {
        this(defaultPrefix, labelIgnoreCase, 0);
        this.jda = shardManager;
        this.useShardManager = true;
    }

    /**
     * This is the constructor.
     * The parameters are validated automatically. In case of any problems (if the prefix is empty), this will throw a CommandSetException.
     * @param jda Put your active JDA here. This is important for the activation of the CommandListener.
     * @param defaultPrefix The String you will have to put before every command in order to get your command execution registered. This can later be changed.
     * @param labelIgnoreCase Set this to true, if you want deactivate case sensitivity for the recognition of labels. E.g.: there will be no difference between the labels "foo",
     *      *                 "FOO", "FoO" and so on.
     */
    public CommandSettings(@Nonnull String defaultPrefix, @Nonnull JDA jda, boolean labelIgnoreCase) {
        this(defaultPrefix, labelIgnoreCase, 0);
        this.jda = jda;
        this.useShardManager = false;
    }

    /**
     * This is the optional constructor in case you are sharding your bot.
     * The parameters are validated automatically. In case of any problems (if the prefix is empty), this will throw a CommandSetException.
     * @param shardManager Put your active ShardManager here. This is important for the activation of the CommandListener.
     * @param defaultPrefix The String you will have to put before every command in order to get your command execution registered. This can later be changed.
     * @param labelIgnoreCase Set this to true, if you want deactivate case sensitivity for the recognition of labels. E.g.: there will be no difference between the labels "foo",
     *                       "FOO", "FoO" and so on.
     * @param msCooldown How much time it takes until a member is able to use a command again. This is to prevent spamming.
     * @throws CommandSetException if the given prefix is invalid.
     */
    public CommandSettings(@Nonnull String defaultPrefix, @Nonnull ShardManager shardManager, boolean labelIgnoreCase, long msCooldown) {
        this(defaultPrefix, labelIgnoreCase, msCooldown);
        this.jda = shardManager;
        this.useShardManager = true;
    }

    /**
     * This is the constructor.
     * The parameters are validated automatically. In case of any problems (if the prefix is empty), this will throw a CommandSetException.
     * @param jda Put your active JDA here. This is important for the activation of the CommandListener.
     * @param defaultPrefix The String you will have to put before every command in order to get your command execution registered. This can later be changed.
     * @param labelIgnoreCase Set this to true, if you want deactivate case sensitivity for the recognition of labels. E.g.: there will be no difference between the labels "foo",
     *      *                 "FOO", "FoO" and so on.
     * @param msCooldown How much time it takes until a member is able to use a command again. This is to prevent spamming.
     * @throws CommandSetException if the given prefix is invalid.
     */
    public CommandSettings(@Nonnull String defaultPrefix, @Nonnull JDA jda, boolean labelIgnoreCase, long msCooldown) {
        this(defaultPrefix, labelIgnoreCase, msCooldown);
        this.jda = jda;
        this.useShardManager = false;
    }

    private CommandSettings(@Nonnull String defaultPrefix, boolean labelIgnoreCase, long msCooldown) {
        this.commands = new HashMap<>();
        this.listener = new CommandListener(this);
        this.activated = false;
        this.cooldown = msCooldown;
        this.setDefaultPrefix(defaultPrefix);
        this.labelIgnoreCase = labelIgnoreCase;
        this.helpLabels = new HashSet<>();
        this.prefixMap = new HashMap<>();
    }

    /**
     * Method to add one help label.
     * @param label The label to add.
     * @return The current object. This is to use fluent interface.
     * @throws CommandSetException if the given label is invalid (contains spaces)
     */
    public CommandSettings addHelpLabel(String label) {
        if (label.matches(VALID_LABEL))
            helpLabels.add(labelIgnoreCase ? label.toLowerCase() : label);
        else
            throw new CommandSetException(INVALID_LABEL_MESSAGE);

        return this;
    }

    /**
     * Use this method to add help labels. This will only work if you instantiated this class with the parameter useHelpCommand as true.
     * @param labels One or more labels which may later be called by members to list all commands or to show info about one specific command.
     * @return The current object. This is to use fluent interface.
     */
    public CommandSettings addHelpLabels(@Nonnull String... labels) {
        for (String label : labels)
            this.addHelpLabel(label);

        return this;
    }

    /**
     * Adds multiple labels from a String Set.
     * @param labels A Set which contains the labels you want to add.
     * @return The current object. This is to use fluent interface.
     */
    public CommandSettings addHelpLabels(Set<String> labels) {
        var labelStream = labels.stream();
        if (labelStream.allMatch((label) -> label.matches(VALID_LABEL)))
            helpLabels.addAll(labelIgnoreCase ? labelStream.map(String::toLowerCase).collect(Collectors.toSet()) : labels);
        else
            throw new CommandSetException(INVALID_LABEL_MESSAGE);

        return this;
    }

    /**
     * This can be used to remove some help labels, but not all of them.
     * @param labels The help labels to remove.
     * @return true, if every label was successfully removed. false, if one of the given labels does not exist and thus was not removed.
     */
    public boolean removeHelpLabels(String... labels) {
        return helpLabels.removeAll(Set.of(labels));
    }

    /**
     * Removes all labels from a Set.
     * @param labels The Set of labels that are to be removed.
     * @return true, if every label was successfully removed. false, if one of the given labels does not exist and thus was not removed.
     */
    public boolean removeHelpLabels(Set<String> labels) {
        return helpLabels.removeAll(labels);
    }

    /**
     * This can be used to deactivate the help labels. Removes every help label.
     * @return The current object. This is to use fluent interface.
     */
    public CommandSettings clearHelpLabels() {
        helpLabels.clear();
        return this;
    }

    /**
     * Use this method to add commands from your project. Every command which is supposed to be active should be added by this.
     * <p>
     * The two parameters will be put in a HashMap which is used by the API to notice commands.
     * @param label The label which describes your command, i.e. the string after the prefix [prefix][label].
     * @param executor An instance of your command class which implements ICommand.
     * @return The current object. This is to use fluent interface.
     * @throws CommandSetException If the label is empty or consists of multiple words.
     */
    public CommandSettings put(@Nonnull ICommand executor, @Nonnull String label) {
        if (label.matches(VALID_LABEL))
            commands.put(labelIgnoreCase ? label.toLowerCase() : label, executor);
        else
            throw new CommandSetException(INVALID_LABEL_MESSAGE);

        return this;
    }

    /**
     * Use this method to add commands with aliases. <p>
     * Works like put(ICommand, String) but adds multiple labels to the same command.
     * @param executor An instance of your command class which implements ICommand.
     * @param labels One or more labels. This will throw a CommandSetException, if the label is empty or contains spaces.
     * @return The current object. This is to use fluent interface.
     */
    public CommandSettings put(@Nonnull ICommand executor, @Nonnull String... labels) {
        for (String label : labels)
            this.put(executor, label);
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
     * Removes every label that is in the given Collection.
     * @param labels The labels to remove.
     * @return true, if every label was successfully removed. False, if not (e.g. the label didn't exist)
     */
    public boolean remove(Set<String> labels) {
        return this.remove(labels.toArray(new String[labels.size()]));
    }

    /**
     * Removes each entry from the commands map. After that, only the help commands are still registered.
     */
    public CommandSettings clear() {
        commands.clear();
        return this;
    }

    /**
     * Use this method to set the default prefix.
     * @param prefix The prefix to set. In case the given String is empty, this will throw a CommandSetException.
     * @throws CommandSetException if a non-null prefix does not match the requirements for a valid prefix.
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
     * @param prefix The nullable prefix to be set.
     * @throws CommandSetException if a non-null prefix does not match the requirements for a valid prefix.
     */
    public void setCustomPrefix(long guildId, @Nullable String prefix) {
        if (prefix != null && !prefix.matches(VALID_PREFIX))
            throw new CommandSetException(INVALID_PREFIX_MESSAGE);
        prefixMap.put(guildId, prefix);
    }

    /**
     * Sets the cooldown for this instance of settings.
     * @param msCooldown the cooldown in milliseconds.
     */
    public CommandSettings setCooldown(long msCooldown) {
        this.cooldown = msCooldown;
        return this;
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

    /**
     * Returns every registered label in a Set. Note that in case you activated labelIgnoreCase, every label in there will be in lower case.
     * Adding or removing something will not have any effect. This can primarily be used to iterate over the labels.
     * @return a Set of labels.
     */
    public Set<String> getLabelSet() {
        return commands.keySet();
    }

    /**
     * Returns all of the registered help labels.
     * @return an unmodifiable Set of Strings that are registered as help labels.
     */
    public Set<String> getHelpLabelSet() {
        return Collections.unmodifiableSet(helpLabels);
    }

    /**
     * Returns whether this instance is activated or not.
     * @return true, if it is, false, if not.
     */
    public boolean isActivated() {
        return activated;
    }

    protected long getCooldown() {
        return cooldown;
    }

    protected boolean labelIgnoreCase() {
        return labelIgnoreCase;
    }

    protected Set<String> getHelpLabels() {
        return helpLabels;
    }

    protected Map<String, ICommand> getCommands() {
        return commands;
    }

}
