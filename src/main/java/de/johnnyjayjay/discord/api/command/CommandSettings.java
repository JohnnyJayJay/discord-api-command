package de.johnnyjayjay.discord.api.command;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import java.util.HashMap;

/**
 * To use this API, create a new object of this class and add your command classes by using add(...)
 * When you want your commands to become active, use activate()
 * @author Johnny_JayJay
 * @version 1.2
 */

public class CommandSettings {

    private String prefix;
    private HashMap<String, ICommand> commands = new HashMap<String, ICommand>();
    private JDA jda;

    private static String finalPrefix;
    private static HashMap<String, ICommand> finalCommands;


    /**
     * This is the constructor.
     * The parameters are validated automatically. In case of any problems, this will throw an IllegalArgumentException.
     * @param jda Put your active JDA here. This is important for the activation of the CommandListener.
     * @param prefix The String you will have to put before every command in order to get your command execution registered.
     */
    public CommandSettings(String prefix, JDA jda) {
        if (prefix.isEmpty() || prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be empty or null");
        } else
            this.prefix = prefix;

        if (jda == null) {
            throw new IllegalArgumentException("JDA cannot be null");
        } else
            this.jda = jda;
    }


    /**
     * This method is used to add commands from your project. Every command which is supposed to be active should be added by this.
     * <p>
     * The two parameters will be put in a HashMap which is used by the API to notice commands.
     * @param label The label which describes your command, i.e. the string after the prefix [prefix][label]
     * @param command An instance of your command class which implements ICommand.
     * @return The current object. This is to use fluent interface.
     */
    public CommandSettings add(String label, ICommand command) {
        if (label.isEmpty() || label == null || label.contains(" ")) {
            throw new IllegalArgumentException("Command label cannot be empty, null or consist of multiple words");
        } else if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        } else
            commands.put(label, command);

        return this;
    }


    /**
     * Sets the prefix and the command HashMap for the rest of the API.
     * <p>
     * Note that you shouldn't create new CommandSettings and activate them after having already activated one. This can cause problems.
     * To "save" your settings, this is important, because otherwise your commands won't be registered.
     */
    public void activate() {
        
        finalPrefix = this.prefix;
        finalCommands = this.commands;

        jda.addEventListener(new CommandListener());

    }

    static String getPrefix() {
        return finalPrefix;
    }

    static HashMap<String, ICommand> getCommands() {
        return finalCommands;
    }




}
