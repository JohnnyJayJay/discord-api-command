import com.github.johnnyjayjay.discord.commandapi.CommandEvent;
import com.github.johnnyjayjay.discord.commandapi.ICommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

/**
 * @author Johnny_JayJay
 * @version 0.1-SNAPSHOT
 */
public class PingCommand implements ICommand {

    @Override
    public void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
        event.getFirstUserMention().ifPresent((user) -> event.respond("You got pong'd! " + user.getAsMention()));
    }

    @Override
    public String info(Member member) {
        return String.format("Tag another member to pong him. Or let it be and get the bot's ping.\nUsage: `%sping <member>`", Main.getPrefix(member.getGuild().getIdLong()));
    }
}
