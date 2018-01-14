
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Arthur
 */
public class DiscordBot extends ListenerAdapter{

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String msg = message.getContentDisplay();
        Boolean isCallingBot = msg.toUpperCase().startsWith("PRICE ");
        
        if (msg.length() > 6) {
            msg = msg.substring(6, msg.length());
        }
        if (!event.getAuthor().isBot() && isCallingBot) {
            JSONObject result = null;
            try {
                //JSON here
                //result = priceApi.getPrice(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONArray tab = result.getJSONArray("products");

            String prices = "";
            for (int i = 0; i < tab.length(); i++) {
                JSONObject obj = (JSONObject) tab.get(i);
                JSONArray tabOffers = obj.getJSONArray("offers");
                for (int j = 0; j < tabOffers.length(); j++) {
                    JSONObject article = (JSONObject) tabOffers.get(j);
                    prices = " Prix : " + article.get("price") + " " + article.get("currency") + " lien : " + article.get("url") + "\n";
                    channel.sendMessage(prices).queue();
                }

            }
            // System.out.println(prices);

        }
    }
}
