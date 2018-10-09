import com.github.johnnyjayjay.discord.commandapi.*;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * This is a sub command example
 */
public class CustomPrefixCommand extends AbstractCommand {


    @SubCommand(isDefault = true, botPerms = {Permission.MESSAGE_WRITE})
    public void everythingElse(CommandEvent event, Member member, TextChannel channel, String[] args) {
        event.respond("Correct usage: `" + event.getCommandSettings().getPrefix(event.getGuild().getIdLong()) + "prefix [get|set] <custom|default> <prefix>`");
    }

    @SubCommand(args = {"get"}, botPerms = {Permission.MESSAGE_WRITE})
    public void getPrefix(CommandEvent event, Member member, TextChannel channel, String[] args) {
        CommandSettings settings = event.getCommandSettings();
        event.respond("The prefix for this guild is: `" + settings.getPrefix(event.getGuild().getIdLong()) + "`\nThe default prefix is: `" + settings.getPrefix() + "`");
    }

    @SubCommand(args = {"set", "custom", ".*"}, moreArgs = true, botPerms = {Permission.MESSAGE_WRITE})
    public void setCustomPrefix(CommandEvent event, Member member, TextChannel channel, String[] args) {
        event.getCommandSettings().setCustomPrefix(event.getGuild().getIdLong(), args[2]);
        event.respond("Successfully set prefix for this guild to `" + args[2] + "`!");
    }

    @SubCommand(args = {"set", "default", ".*"}, moreArgs = true, botPerms = {Permission.MESSAGE_WRITE})
    public void setDefaultPrefix(CommandEvent event, Member member, TextChannel channel, String[] args) {
        event.getCommandSettings().setDefaultPrefix(args[2]);
        event.respond("Successfully set default prefix to `" + args[2] + "`!");
    }

}
