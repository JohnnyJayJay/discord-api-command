import com.github.johnnyjayjay.discord.commandapi.CommandEvent;
import com.github.johnnyjayjay.discord.commandapi.CommandSettings;
import com.github.johnnyjayjay.discord.commandapi.ICommand;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.concurrent.TimeUnit;

/**
 * @author Johnny_JayJay
 * @version 0.1-SNAPSHOT
 */
public class KillCommand implements ICommand {

    private CommandSettings settings;

    public KillCommand(CommandSettings settings) {
        this.settings = settings;
    }

    @Override
    public void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
        channel.sendMessage("Are you sure?").queue();
        // Settings will now be deactivated. To reactivate them, call .activate(). This can be done by using another event listener.
        channel.sendMessage("Just kidding. It's too late. :smiling_imp:").queueAfter(3, TimeUnit.SECONDS, (msg) -> settings.deactivate());
        channel.sendMessage("Well played.").queueAfter(4, TimeUnit.SECONDS);
    }

    @Override
    public String info(Member member) {
        return String.format("Deactivates all commands. No return.\n\n ¯\\_(ツ)_/¯\n\nUsage: `%s[kill|deactivate|shutdown]`", settings.getPrefix(member.getGuild().getIdLong()));
    }
}
