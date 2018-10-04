package com.github.johnnyjayjay.discord.commandapi;

/**
 * A util class with Strings that may be used as regular expressions (e.g. for {@link SubCommand SubCommands}).
 * @author Johnny_JayJay
 * @version 3.2_01
 * @since 3.2_01
 */
public class Regex {

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
     * A regex that only matches valid prefixes. Can be used to check user input.
     */
    public static final String VALID_PREFIX = "[^\\\\+*^|$?]+";
    /**
     * A regex that only matches valid labels. Can be used to check user input.
     */
    public static final String VALID_LABEL = "[^\\s]+";

}
