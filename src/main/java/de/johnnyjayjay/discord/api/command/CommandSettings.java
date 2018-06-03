package de.johnnyjayjay.discord.api.command;

import com.sun.istack.internal.NotNull;
import net.dv8tion.jda.core.JDA;

import java.util.HashMap;

/**
 * To use this API, create a new object of this class and add your command classes by using add(...)
 * When you want your commands to become active, use activate()
 * @author Johnny_JayJay
 * @version 1.6
 */

public class CommandSettings {

    private String prefix;
    private HashMap<String, ICommand> commands = new HashMap<>();
    private JDA jda;

    private static boolean used = false;
    private static String finalPrefix;
    private static HashMap<String, ICommand> finalCommands;


    /**
     * This is the constructor.
     * The parameters are validated automatically. In case of any problems, this will throw an IllegalArgumentException.
     * @param jda Put your active JDA here. This is important for the activation of the CommandListener.
     * @param prefix The String you will have to put before every command in order to get your command execution registered.
     */
    public CommandSettings(@NotNull String prefix, @NotNull JDA jda) {
        if (prefix.isEmpty()) {
            throw new IllegalArgumentException("Prefix cannot be empty");
        } else {
            this.prefix = prefix;
            this.jda = jda;
        }
    }

    /**
     * This method is used to add commands from your project. Every command which is supposed to be active should be added by this.
     * <p>
     * The two parameters will be put in a HashMap which is used by the API to notice commands.
     * @param label The label which describes your command, i.e. the string after the prefix [prefix][label]
     * @param command An instance of your command class which implements ICommand.
     * @return The current object. This is to use fluent interface.
     */
    public CommandSettings add(@NotNull String label, @NotNull ICommand command) {
        if (label.isEmpty() || label.contains(" "))
            throw new IllegalArgumentException("Command label cannot be empty or consist of multiple words");
        else
            commands.put(label, command);

        return this;
    }


    /**
     * Sets the prefix and the command HashMap for the rest of the API. This is the last method to call when having finished setting up your commands.
     * <p>
     * Note that you shouldn't create new CommandSettings and activate them after having already activated one. This may cause problems.
     * To "save" your settings, using this is important, because otherwise your commands won't be registered.
     */
    public void activate() {
        if (!used) {
            finalPrefix = this.prefix;
            finalCommands = this.commands;
            jda.addEventListener(new CommandListener());
            used = true;
        } else
            throw new CommandSetException("CommandSettings already activated!");
    }

    static String getPrefix() {
        return finalPrefix;
    }

    static HashMap<String, ICommand> getCommands() {
        return finalCommands;
    }




}
