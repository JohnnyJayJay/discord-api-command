import com.github.johnnyjayjay.discord.commandapi.CommandSettings;
import com.github.johnnyjayjay.discord.commandapi.DefaultHelpCommand;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

/**
 * Exemplary main class
 */
// TODO: 04.08.2018 Write more examples
public class Main {

    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA jda = new JDABuilder(AccountType.BOT).setToken(Secrets.TOKEN).buildBlocking();
        // default prefix shall be "!" and we want the labels to be case insensitive.
        new CommandSettings("!!", jda, true).setCooldown(3000) // commands can only be executed every 3 seconds now
                .put(new PingCommand(), "ping", "p")
                .put(new CustomPrefixCommand(), "prefix")
                .activate(); // Activating! Very important!
    }

}
