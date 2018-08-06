import com.github.johnnyjayjay.discord.commandapi.CommandSettings;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

/**
 * @author Johnny_JayJay
 * @version 0.1-SNAPSHOT
 */
public class Main {

    private static CommandSettings settings;

    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA jda = new JDABuilder(AccountType.BOT).setToken(Secrets.TOKEN).buildBlocking();
        // default prefix shall be "!" and we want the labels to be case insensitive.
        settings = new CommandSettings("!", jda, true);
        settings.setCooldown(3000) // commands can only be executed every 3 seconds now
                .addHelpLabels("help", "helpme") // help can now be demanded by calling one of these labels.
                .put(new PingCommand(), "ping")
                .put(new PrefixCommand(settings), "setprefix")
                .put(new KillCommand(settings), "kill", "deactivate", "shutdown") // setting aliases
                .activate(); // Activating! Very important!
    }


    public static String getPrefix(long guildId) {
        return settings.getPrefix(guildId);
    }

}
