import com.github.johnnyjayjay.discord.commandapi.*;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * @author Johnny_JayJay
 * @version 0.1-SNAPSHOT
 */
public class PrefixCommand extends AbstractCommand {

    @SubCommand
    public void onEverythingElse(CommandEvent event, Member member, TextChannel channel, String[] args) {
        channel.sendMessage("Something else").queue();
    }

    @SubCommand
    public void onEverythingElse2(CommandEvent event, Member member, TextChannel channel, String[] args) {
        channel.sendMessage("This is another message").queue();
    }

}
