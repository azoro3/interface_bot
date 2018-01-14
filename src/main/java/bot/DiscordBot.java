package bot;

import io.sgr.urlshortener.google.GoogleURLShortener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

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

    JSONObject result = new JSONObject();
    MessageChannel channel;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        User author = event.getAuthor();
        Message message = event.getMessage();
        channel = event.getChannel();
        String msg = message.getContentDisplay();
        Boolean isCallingBot = msg.toUpperCase().startsWith("PRICE ");

        if (msg.length() > 6) {
            msg = msg.substring(6, msg.length());
        }
        if (!event.getAuthor().isBot() && isCallingBot) {

            try {
                //JSON here
                String ur = "http://localhost:8080/pricebot-0.0.1-SNAPSHOT/price/" + msg;

                OkHttpClient okhttpClient = new OkHttpClient();
                Request getRequest = new Request.Builder().url(ur).build();
                okhttpClient.newCall(getRequest).enqueue(new Callback() {
                    public void onFailure(Call call, IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }

                    public void onResponse(Call call, Response rspns) throws IOException {
                        String body = rspns.body().string();

                        result = new JSONObject(body);

                        JSONArray tab = result.getJSONArray("products");

                        String prices = "";
                        for (int i = 0; i < tab.length(); i++) {
                            JSONObject obj = (JSONObject) tab.get(i);
                            JSONArray tabOffers = obj.getJSONArray("offers");
                            for (int j = 0; j < tabOffers.length(); j++) {
                                JSONObject article = (JSONObject) tabOffers.get(j);
                                Object res = article.get("url");
                                GoogleURLShortener shortener = new GoogleURLShortener();
                                String shortUrl = shortener.shortenURL("<some_origin>", "AIzaSyAjSBCl4HtD6yy-2n-pi0AX3w-S87EauxI", res.toString());
                                prices = " Prix : " + article.get("price") + " " + article.get("currency") + " lien : " + shortUrl + "\n";
                                channel.sendMessage(prices).queue();
                            }

                        }

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
