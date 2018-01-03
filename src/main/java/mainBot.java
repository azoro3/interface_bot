import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class mainBot extends ListenerAdapter {
    static String token = "MzkzMDM2MTI4MTUzNzYzODQw.DS4ecQ.4VFa7Yi3ElJecQXHu4r8tmMsJ64";

    public static void main(String[] args) {
        try {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .addEventListener(new mainBot())
                    .buildBlocking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String msg = message.getContentDisplay();
        System.out.printf("Author: %s, channel: %s, message: %s\n", author.getName(), channel.getName(), msg);
        if (!event.getAuthor().isBot()) {
            channel.sendMessage("WOW, can't believe you just said that !!!!").queue();
        }
    }
}
