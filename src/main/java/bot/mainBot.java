package bot;

import bot.DiscordBot;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class mainBot extends ListenerAdapter {

    static String token = "MzkzMDM2MTI4MTUzNzYzODQw.DS4ecQ.4VFa7Yi3ElJecQXHu4r8tmMsJ64";


    public static void main(String[] args) {
        //Launch Discord
        try {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .addEventListener(new DiscordBot())
                    .buildBlocking();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Launch telegram
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
