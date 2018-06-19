package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.JDA;

import javax.annotation.Nonnull;
import java.util.HashMap;

/**
 * To use this API, create a new object of this class and add your command classes by using add(...)
 * When you want your commands to become active, use activate()
 * @author Johnny_JayJay
 * @version 2.5
 */

public class CommandSettings {

    private String prefix;
    private HashMap<String, ICommand> commands;
    private JDA jda;
    private CommandListener listener;
    private boolean used;


    /**
     * This is the constructor.
     * The parameters are validated automatically. In case of any problems (if the prefix is empty), this will throw a CommandSetException.
     * @param jda Put your active JDA here. This is important for the activation of the CommandListener.
     * @param prefix The String you will have to put before every command in order to get your command execution registered. This can later be changed.
     */
    public CommandSettings(@Nonnull String prefix, @Nonnull JDA jda) {
        this.commands = new HashMap<>();
        this.listener = new CommandListener(this);
        this.used = false;
        this.setPrefix(prefix);
        this.jda = jda;
    }

    /**
     * This method is used to add commands from your project. Every command which is supposed to be active should be added by this.
     * <p>
     * The two parameters will be put in a HashMap which is used by the API to notice commands.
     * @param label The label which describes your command, i.e. the string after the prefix [prefix][label]. If it is empty or consists of multiple words,
     *              this will throw a CommandSetException.
     * @param command An instance of your command class which implements ICommand.
     * @return The current object. This is to use fluent interface.
     */
    public CommandSettings put(@Nonnull String label, @Nonnull ICommand command) {
        if (label.isEmpty() || label.contains(" "))
            throw new CommandSetException("Command label cannot be empty or consist of multiple words");
        else
            commands.put(label, command);

        return this;
    }

    /**
     * Use this method to remove existing commands. <p>
     * @param label The label of the command to remove.
     * @return true, if the command was removed. false, if the given label doesn't exist.
     */
    public boolean remove(@Nonnull String label) {
        boolean ret;
        if (commands.remove(label) != null)
            ret = true;
        else
            ret = false;
        return ret;
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
     * Sets the prefix and the command HashMap for the rest of the API. This is the last method to call when having finished setting up your commands.
     * <p>
     * Note that activating multiple CommandSettings may cause problems, because
     * To "save" your settings, using this is important, because otherwise your commands won't be registered.
     */
    public void activate() {
        if (!used) {
            jda.addEventListener(listener);
            used = true;
        } else
            throw new CommandSetException("CommandSettings already activated!");
    }

    /**
     * Deactivates the current CommandSettings by removing the listener from jda.
     * The CommandSettings can be activated again by using activate().
     */
    public void deactivate() {
        if (used) {
            jda.removeEventListener(listener);
            used = false;
        } else
            throw new CommandSetException("CommandSettings weren't activated yet and can therefore not be deactivated!");
    }

    String getPrefix() {
        return prefix;
    }

    HashMap<String, ICommand> getCommands() {
        return commands;
    }


}
