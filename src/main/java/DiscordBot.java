
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
public class DiscordBot extends ListenerAdapter {

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
            JSONObject result = new JSONObject();
            try {
                //JSON here
                String ur = "http://localhost:8080/priceapi/price/" + msg;
                System.out.println(ur);
                OkHttpClient okhttpClient = new OkHttpClient();
                Request getRequest =new Request.Builder().url(ur).build();
                okhttpClient.newCall(getRequest).enqueue(new Callback() {
                    public void onFailure(Call call, IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }

                    public void onResponse(Call call, Response rspns) throws IOException {
                        String res =rspns.body().string();
                        System.out.println(res);
                    }
                });
                
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
