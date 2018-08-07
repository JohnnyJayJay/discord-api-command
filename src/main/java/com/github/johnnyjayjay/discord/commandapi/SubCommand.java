package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.Permission;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * This annotation may be used inside of a sub class of AbstractCommand to annotate methods as SubCommand-methods.
 * Methods annotated with this annotation must have the signature {@code void (com.github.johnnyjayjay.commandapi.CommandEvent, net.dv8tion.jda.core.entities.Member, net.dv8tion.jda.core.entities.TextChannel, java.lang.String[])}
 * in order to be registered (you will get a warning if a signature violates that).
 *
 * @author JohnnyJayJay
 * @version 3.2
 * @since 3.2
 * @see AbstractCommand
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {

    /**
     * Setting this to true marks a SubCommand as the default SubCommand. This means that it will be executed if no other SubCommand is triggered.
     * This might be useful for "incorrect usage" messages.
     * @return false by default.
     */
    boolean isDefault() default false;

    /**
     * An array of Strings, specifying a regex for each needed command argument. E.g.: if this is set to {@code {"get", "<@!?\\d+>"}}, the
     * method will only be triggered if the command matches: {@code [prefix][label] get [member mention]}. Of course, you don't have to use regex.
     * You may also use just normal words and then make your checks inside of the method.
     * Values specified in isDefault-SubCommands are ignored.
     * @return an empty array by default. Meaning, a method that does not specify a value will be called if there are no arguments.
     */
    String[] args() default {};

    /**
     * A boolean that indicates that there must be more arguments than specified in args in order to be triggered.
     * This might be useful for commands with an open argument length. If this is set to true, the command argument length must be greater than SubCommand#args().length.
     * This is ignored if specified in an isDefault-SubCommand.
     * @return false by default.
     */
    boolean moreArgs() default false;

    /**
     * An array of {@link net.dv8tion.jda.core.Permission} that specifies which Permissions the self member has to have in the event channel in order to trigger this method.
     * This is NOT ignored for isDefault-SubCommands.
     * @return an empty array by default.
     */
    Permission[] botPerms() default {};

}
