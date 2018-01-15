package bot;

import interpreter.Interpreter;
import java.util.Observer;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Arthur
 * @author Alexandre
 * @author Alexandre
 * @author Magalie
 */
public class DiscordBot extends ListenerAdapter implements Observer {

    MessageChannel channel;
    Interpreter interpreter = new Interpreter();

    /**
     *
     * @param event message receive on the channel
     *
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        User author = event.getAuthor();
        Message message = event.getMessage();
        channel = event.getChannel();
        String msg = message.getContentDisplay();

        if (!event.getAuthor().isBot() && interpreter.isCallingBot(msg)) {

            interpreter.addObserver(this);
            interpreter.parse(msg);

        }
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        String message = (String) arg;
        channel.sendMessage(message).queue();
    }
}
