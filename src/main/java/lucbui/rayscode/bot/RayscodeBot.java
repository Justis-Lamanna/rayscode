package lucbui.rayscode.bot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

/**
 * Entry point for rayscodeBot
 */
public class RayscodeBot {

    private static JDA bot;

    public static void main(String[] args) throws LoginException {
        bot = new JDABuilder(AccountType.BOT)
                .setToken(args[0])
                .addEventListener(new RayscodeBotMessageListener())
                .buildAsync();
    }

    public static JDA getBot(){
        return bot;
    }
}
