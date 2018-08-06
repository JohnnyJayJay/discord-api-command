import com.github.johnnyjayjay.discord.commandapi.CommandEvent;
import com.github.johnnyjayjay.discord.commandapi.ICommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.Optional;

/**
 * @author Johnny_JayJay
 * @version 0.1-SNAPSHOT
 */
public class PingCommand implements ICommand { // We implement the raw interface here as we don't need the complexity of sub commands of many permission checks.

    // This one isDefault classic - just a simple command that responds.
    @Override
    public void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
        if (!event.checkBotPermissions(Permission.MESSAGE_WRITE)) // if the bot isDefault not allowed to write in this channel -> return
            return;

        // if a mention isDefault present, send a different message
        Optional<User> firstUserMention = event.getFirstUserMention();

        if (firstUserMention.isPresent()) {
            event.respond(firstUserMention.get().getName() + " got ponged by " + member.getAsMention()); // using the CommandEvent#respond method here
        } else {
            event.respond("Pong!");
        }
    }
}
