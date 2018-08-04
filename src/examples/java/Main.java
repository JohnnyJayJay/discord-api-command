import com.github.johnnyjayjay.discord.commandapi.CommandSettings;
import com.github.johnnyjayjay.discord.commandapi.DefaultHelpCommand;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

/**
 * @author Johnny_JayJay
 * @version 0.1-SNAPSHOT
 */
// TODO: 04.08.2018 Write proper examples that make sense 
public class Main {

    private static CommandSettings settings;

    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA jda = new JDABuilder(AccountType.BOT).setToken("NDQwMjIyMTc1NjkwMDk2NjYx.DkZHIg.Fu6w20j9htc0Z_-G7W6S1k9Yumw").buildBlocking();
        // default prefix shall be "!" and we want the labels to be case insensitive.
        settings = new CommandSettings("!", jda, true);
        settings.setCooldown(3000) // commands can only be executed every 3 seconds now
                //.addHelpLabels("help", "helpme") // help can now be demanded by calling one of these labels.
                .put(new DefaultHelpCommand(), "help", "helpme")
                .put(new PingCommand(), "ping")
                .put(new PrefixCommand(settings), "setprefix")
                .put(new KillCommand(settings), "kill", "deactivate", "shutdown") // setting aliases
                .activate(); // Activating! Very important!
    }

    public static String getPrefix(long guildId) {
        return settings.getPrefix(guildId);
    }

}
