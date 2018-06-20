package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.JDA;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * To use this API, create a new object of this class and add your command classes by using add(...)<p>
 * When you want your commands to become active, use activate()
 * @author Johnny_JayJay
 * @version 2.9
 */

public class CommandSettings {


    private String prefix;

    private Set<String> helpLabels;
    private HashMap<String, ICommand> commands;

    private JDA jda;
    private CommandListener listener;

    private boolean used;
    private boolean useHelpCommand;


    /**
     * This is the constructor.
     * The parameters are validated automatically. In case of any problems (if the prefix is empty), this will throw a CommandSetException.
     * @param jda Put your active JDA here. This is important for the activation of the CommandListener.
     * @param prefix The String you will have to put before every command in order to get your command execution registered. This can later be changed.
     * @param useHelpCommand Set this to true, if you want to use the auto-generated help command of this API. You can configure this by setting the help
     *                       labels with setHelpLabel(String...) and by overriding the method info() in your command classes.
     */
    public CommandSettings(@Nonnull String prefix, @Nonnull JDA jda, boolean useHelpCommand) {
        this.commands = new HashMap<>();
        this.listener = new CommandListener(this);
        this.used = false;
        this.setPrefix(prefix);
        this.jda = jda;
        this.useHelpCommand = useHelpCommand;
        if (useHelpCommand) {
            this.helpLabels = new HashSet<>();
        }
    }

    /**
     * Use this method to add help labels. This, of course, only makes sense if you instantiated this class with the parameter useHelpCommand as true.
     * @param labels One or more labels which may later be called by members to list all commands or to show info about one specific command.
     * @return The current object. This is to use fluent interface.
     */
    public CommandSettings setHelpLabel(String... labels) {
        for (String label : labels) {
            helpLabels.add(label);
        }
        return this;
    }

    /**
     * Use this method to add commands from your project. Every command which is supposed to be active should be added by this.
     * <p>
     * The two parameters will be put in a HashMap which is used by the API to notice commands.
     * @param label The label which describes your command, i.e. the string after the prefix [prefix][label]. If it is empty or consists of multiple words,
     *              this will throw a CommandSetException.
     * @param command An instance of your command class which implements ICommand.
     * @return The current object. This is to use fluent interface.
     */
    public CommandSettings put(@Nonnull ICommand command, @Nonnull String label) {
        if (label.isEmpty() || label.contains(" "))
            throw new CommandSetException("Command label cannot be empty or consist of multiple words");
        else
            commands.put(label, command);

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
        for (String label : labels) {
            this.put(command, label);
        }
        return this;
    }

    /**
     * Use this method to remove existing commands. <p>
     * @param label The label of the command to remove.
     * @return true, if the label was successfully removed. false, if the given label doesn't exist.
     */
    public boolean remove(@Nonnull String label) {
        boolean success = false;
        if (commands.remove(label) != null)
            success = true;
        return success;
    }

    /**
     * Use this method to remove more than one command at a time. <p>
     * @param labels One or more labels to remove
     * @return true, if every label was successfully removed. false, if one of the given labels does not exist.
     */
    public boolean remove(@Nonnull String... labels) {
        boolean success = true;
        for (String label : labels) {
            if (!this.remove(label)) {
                success = false;
            }
        }
        return success;
    }

    /**
     * Use this method to set the prefix.
     * @param prefix The prefix to set. In case the given String is empty, this will throw a CommandSetException.
     */
    public void setPrefix(@Nonnull String prefix) {
        if (prefix.isEmpty())
            throw new CommandSetException("Prefix cannot be empty");
        else
            this.prefix = prefix;
    }

    /**
     * Sets the prefix and the command HashMap for the rest of the API. This is the last method to call when having finished setting up your commands.<p>
     * Note that activating multiple CommandSettings may cause problems. You can do this to use multiple prefixes, but it is not recommended.<p>
     * This method is important to call because otherwise no command will be registered by the internal command listener.
     */
    public void activate() {
        if (!used) {
            jda.addEventListener(listener);
            used = true;
        } else
            throw new CommandSetException("CommandSettings already activated!");
    }

    /**
     * Deactivates the current CommandSettings by removing the command listener from jda.
     * The CommandSettings can be activated again by using activate().
     */
    public void deactivate() {
        if (used) {
            jda.removeEventListener(listener);
            used = false;
        } else
            throw new CommandSetException("CommandSettings weren't activated yet and can therefore not be deactivated!");
    }

    Set<String> getHelpLabels() {
        return helpLabels;
    }

    boolean useHelpCommand() {
        return useHelpCommand;
    }

    String getPrefix() {
        return prefix;
    }

    HashMap<String, ICommand> getCommands() {
        return commands;
    }


}
