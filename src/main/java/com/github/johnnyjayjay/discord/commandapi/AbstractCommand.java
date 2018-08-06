package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This abstract class isDefault an alternative implementation of ICommand.
 * Instead of implementing ICommand directly, you may create sub classes of this class and register them as commands.
 * This class provides the possibility to use sub commands, making it possible to annotate methods as SubCommand methods.
 * To learn more about using sub commands, please refer to the readme on github.
 * @author JohnnyJayJay
 * @version 3.3
 * @see ICommand
 */
// TODO: 06.08.2018 changelog 
public abstract class AbstractCommand implements ICommand {

    private final Map<SubCommand, Method> subCommands;

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

    @Override
    public final void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
        String joinedArgs = event.getCommandSettings().isLabelIgnoreCase() ? event.getCommand().getJoinedArgs().toLowerCase() : event.getCommand().getJoinedArgs();
        Member selfMember = event.getGuild().getSelfMember();
        // FIXME: 06.08.2018 default method is always chosen
        subCommands.keySet().stream()
                .filter((sub) -> joinedArgs.matches(sub.regex() + ".*"))
                .filter((sub) -> sub.argsLength() == args.length || sub.argsLength() == -1)
                .filter((sub) -> event.checkMemberPermissions(sub.memberPerms()) && event.checkBotPermissions(sub.botPerms())).findFirst()
                .map(Optional::of).orElseGet(
                        () -> subCommands.keySet().stream().filter((sub) -> event.checkMemberPermissions(sub.memberPerms()) && event.checkBotPermissions(sub.botPerms())).filter(SubCommand::isDefault).findFirst())
                .map(subCommands::get).ifPresent((method) -> {
                    try {
                        method.invoke(this, event, member, channel, args);
                    } catch (IllegalAccessException e) {
                        CommandSettings.LOGGER.error("An Exception occurred while trying to invoke sub command method; Please report this in a github issue. https://github.com/JohnnyJayJay/discord-api-command/issues", e);
                    } catch (InvocationTargetException e) {
                        CommandSettings.LOGGER.warn("One of the commands had an uncaught exception:", e.getCause());
                        e.printStackTrace();
                    }
        });
    }

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
