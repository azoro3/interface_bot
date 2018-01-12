import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class mainBot extends ListenerAdapter {
    static String token = "MzkzMDM2MTI4MTUzNzYzODQw.DS4ecQ.4VFa7Yi3ElJecQXHu4r8tmMsJ64";
    static PriceApi priceApi;

    public static void main(String[] args) {
        try {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .addEventListener(new mainBot())
                    .buildBlocking();
            priceApi = new PriceApi();

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
        Boolean isCallingBot = msg.toUpperCase().startsWith("PRICE ");
        if(msg.length() > 6) {
            msg = msg.substring(6, msg.length());
        }
        //System.out.println(msg);
       // System.out.printf("Author: %s, channel: %s, message: %s\n", author.getName(), channel.getName(), msg);
        if (!event.getAuthor().isBot() && isCallingBot) {
            JSONObject result = null;
            try {
               result = priceApi.getPrice(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONArray tab = result.getJSONArray("products");

            String prices = "";
            for (int i = 0; i < tab.length() ; i++) {
                JSONObject obj = (JSONObject)tab.get(i);
                JSONArray tabOffers = obj.getJSONArray("offers");
                for (int j = 0; j < tabOffers.length(); j++) {
                    JSONObject article = (JSONObject)tabOffers.get(j);
                    prices = " Prix : " + article.get("price") + " " + article.get("currency") + " lien : " + article.get("url") + "\n";
                    channel.sendMessage(prices).queue();
                }


            }
           // System.out.println(prices);

        }
    }
}
