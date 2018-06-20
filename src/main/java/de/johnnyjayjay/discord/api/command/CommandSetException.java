package de.johnnyjayjay.discord.api.command;

/**
 * Exception that is thrown in case of any problems concerning the CommandSettings. <p>
 * CommandSetExceptions are RuntimeExceptions.
 * @author Johnny_JayJay
 * @version 2.9
 */
public class CommandSetException extends RuntimeException {
    public CommandSetException(String msg) {
        super(msg);
    }
}
