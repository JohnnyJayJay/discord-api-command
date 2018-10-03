package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This abstract class is an alternative implementation of ICommand.
 * Instead of implementing ICommand directly, you may create sub classes of this class and register them as commands.
 * This class provides the possibility to use sub commands, making it possible to annotate methods as SubCommand methods.
 * To learn more about using sub commands, please refer to the readme on github.
 * @author JohnnyJayJay
 * @version 3.2
 * @since 3.2
 * @see ICommand
 */
public abstract class AbstractCommand implements ICommand {

    // TODO: 03.10.2018 Regexes? 
    /**
     * A regex to match member mentions.
     */
    protected final String MEMBER_MENTION = "<@!?\\d+>";
    /**
     * A regex to match role mentions.
     */
    protected final String ROLE_MENTION = "<&\\d+>";
    /**
     * A regex to match text channel mentions.
     */
    protected final String CHANNEL_MENTION = "<#\\d+>";

    private final Map<SubCommand, Method> subCommands;

    /**
     * Iterates over the SubCommand-annotated methods and saves them in a cache.
     */
    protected AbstractCommand() {
        // This has to be changed as soon as onCommand changes
        final Class<?>[] parameterTypes = {CommandEvent.class, Member.class, TextChannel.class, String[].class};
        this.subCommands = new HashMap<>();
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(SubCommand.class)) {
                if (method.getReturnType().equals(Void.TYPE) && Modifier.isPublic(method.getModifiers()) && Arrays.equals(method.getParameterTypes(), parameterTypes)) {
                    subCommands.put(method.getAnnotation(SubCommand.class), method);
                } else {
                    CommandSettings.LOGGER.warn("You are using an invalid method signature for the SubCommand-annotation on method" + getClass().getName() + "#" + method.getName()
                            + ".\nExpected: void (com.github.johnnyjayjay.commandapi.CommandEvent, net.dv8tion.jda.core.entities.Member, net.dv8tion.jda.core.entities.TextChannel, java.lang.String[])\nFound: "
                            + method.getReturnType().getName() + " (" + Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.joining(", ")) + ")\nThis method will therefore be ignored.");
                }
            }
        }
    }

    /**
     * This overrides the method declared in {@link com.github.johnnyjayjay.discord.commandapi.ICommand ICommand}. It is final, thus it may not be overwritten.
     * To define own command methods in a sub class of this class, refer to the {@link com.github.johnnyjayjay.discord.commandapi.SubCommand SubCommand-annotation}.<br>
     * See the examples and the readme on github for further information.
     * @see SubCommand
     */
    @Override
    public final void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
        CommandSettings settings = event.getCommandSettings();
        Optional<SubCommand> matchesArgs = subCommands.keySet().stream()
                .filter((sub) -> !sub.isDefault())
                .filter((sub) -> sub.args().length == args.length || (sub.moreArgs() && args.length > sub.args().length)) // FIXME: 03.10.2018 
                .filter((sub) -> {
                    String regex;
                    for (int i = 0; i < sub.args().length; i++) {
                        regex = settings.isLabelIgnoreCase() ? "(?i)" + sub.args()[i] : sub.args()[i];
                        if (!args[i].matches(regex))
                            return false;
                    }
                    return true;
                })
                .filter((sub) -> event.checkBotPermissions(sub.botPerms())).findFirst();
        if (matchesArgs.isPresent()) {
            this.invokeMethod(subCommands.get(matchesArgs.get()), event, member, channel, args);
        } else {
            subCommands.keySet().stream().filter(SubCommand::isDefault).filter((sub) -> event.checkBotPermissions(sub.botPerms()))
                    .findFirst().map(subCommands::get)
                    .ifPresent((method) -> this.invokeMethod(method, event, member, channel, args));
        }
    }

    private void invokeMethod(Method method, CommandEvent event, Member member, TextChannel channel, String[] args) {
        try {
            method.invoke(this, event, member, channel, args);
        } catch (IllegalAccessException e) {
            CommandSettings.LOGGER.error("An unexpected Exception occurred while trying to invoke sub command method; Please report this in a github issue. https://github.com/JohnnyJayJay/discord-api-command/issues", e);
        } catch (InvocationTargetException e) {
            CommandSettings.LOGGER.warn("Command " + event.getCommand().getExecutor().getClass().getName() + " had an uncaught Exception in SubCommand " + method.getName() + ":", e.getCause());
        }
    }

    // algorithm to find the best fitting method for a specified name. Currently it's not used
    /*private Optional<SubCommand> bestMethod(Set<SubCommand> possible, String joinedArgs) {
        Optional<SubCommand> ret = Optional.empty();
        if (possible.size() == 1) {
            ret = possible.stream().findFirst();
        } else if (!possible.isEmpty()) {
            SubCommand longest = null;
            int longestPrefixLength = 0;
            for (SubCommand command : possible) {
                String name = command.name();

                if (joinedArgs.equals(name)) {
                    longest = command;
                    break;
                }

                int minLength = Math.min(name.length(), joinedArgs.length());
                if (minLength <= longestPrefixLength) {
                    if (name.isEmpty() && longest == null)
                        longest = command;
                    continue;
                }

                for (int local = 0; local < minLength; local++) {
                    if (name.charAt(local) != joinedArgs.charAt(local)) {
                        if (local > longestPrefixLength) {
                            longest = command;
                            longestPrefixLength = local;
                        }
                        break;
                    } else if (local == minLength - 1 && local > longestPrefixLength) {
                        longest = command;
                        longestPrefixLength = local;
                        break;
                    }
                }
            }
            if (longest != null) {
                ret = Optional.of(longest);
            }
        }
        return ret;
    }*/

}
