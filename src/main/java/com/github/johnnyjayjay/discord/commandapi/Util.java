package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.entities.Message;

import java.util.function.BiConsumer;

/**
 * A util class with Strings that may be used as regular expressions (e.g. for {@link SubCommand SubCommands}).
 * @author Johnny_JayJay
 * @version 3.2_01
 * @since 3.2_01
 */
public class Util {

    /**
     * A regex that matches a raw Discord member (or user) mention
     */
    public static final String MEMBER_MENTION = "<@!?\\d+>";
    /**
     * A regex that matches a raw Discord text channel mention
     */
    public static final String CHANNEL_MENTION = "<#\\d+>";
    /**
     * A regex that matches a raw Discord role mention
     */
    public static final String ROLE_MENTION = "<&\\d+>";
    /**
     * A regex that matches one digit (0-9)
     */
    public static final String DIGIT = "\\d";
    /**
     * A regex that matches everything.
     */
    public static final String ANYTHING = ".*";

    /**
     * A regex that only matches valid labels. Can be used to check user input.
     */
    public static final String VALID_LABEL = "[^\\s]+";

    /**
     * Gets the stack trace of a Throwable as if it was displayed via {@code Throwable#printStackTrace()}.
     * This might be useful in {@link CommandSettings#onException(BiConsumer)}.
     * @param throwable The throwable to get the stack trace of
     * @return the stack trace as a String or at least a part of the stack trace if it is too long.
     */
    public static String getStackTraceAsString(Throwable throwable) {
        StringBuilder builder = new StringBuilder().append("Exception in thread ").append(Thread.currentThread().getName()).append(" - ")
                .append(throwable.getClass().getName()).append(": ").append(throwable.getMessage());
        StackTraceElement[] elements = throwable.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            if (builder.length() > Message.MAX_CONTENT_LENGTH - 200) {
                builder.append("\n\t...").append(elements.length - i).append(" more");
                return builder.toString();
            }
            builder.append("\n\tat: ").append(elements[i]);
        }

        causes:
        while ((throwable = throwable.getCause()) != null) {
            builder.append("\nCaused by: ").append(throwable.getClass().getName()).append(": ").append(throwable.getMessage());
            elements = throwable.getStackTrace();
            for (int i = 0; i < elements.length; i++) {
                if (builder.length() > Message.MAX_CONTENT_LENGTH - 200) {
                    builder.append("\n\t...").append(elements.length - i).append(" more");
                    break causes;
                }
                builder.append("\n\tat: ").append(elements[i]);
            }
        }
        return builder.toString();
    }

}
