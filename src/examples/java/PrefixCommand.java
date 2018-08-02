import com.github.johnnyjayjay.discord.commandapi.CommandEvent;
import com.github.johnnyjayjay.discord.commandapi.CommandSettings;
import com.github.johnnyjayjay.discord.commandapi.ICommand;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * @author Johnny_JayJay
 * @version 0.1-SNAPSHOT
 */
public class PrefixCommand implements ICommand {

    private CommandSettings settings;

    public PrefixCommand(CommandSettings settings) {
        this.settings = settings;
    }

    @Override
    public void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
        if (args.length == 1) {
            if (args[0].matches(CommandSettings.VALID_PREFIX)) { // checking whether the given prefix fits the rules
                settings.setCustomPrefix(event.getGuild().getIdLong(), args[0]);
                channel.sendMessage(String.format("This guild's prefix was successfully set to `%s`", args[0])).queue();
            } else {
                channel.sendMessage("Invalid prefix!").queue();
            }
        } else {
            channel.sendMessage("Must provide a prefix as an argument! Info:\n" + this.info(event.getMember())).queue();
        }
    }

    @Override
    public String info(Member member) {
        return String.format("Sets the prefix for the this guild.\nUsage: `%ssetprefix <prefix>`", settings.getPrefix(member.getGuild().getIdLong()));
    }
}
