package bot;

import io.sgr.urlshortener.google.GoogleURLShortener;
import java.io.IOException;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Arthur
 */
public class TelegramBot extends TelegramLongPollingBot {

    JSONObject result = new JSONObject();
    String msg;

    @Override
    public String getBotToken() {
        return "525473043:AAFSuGYoGnMKY8g-403B7RlEhMA3NebhPT4";
    }

    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            final long chat_id = update.getMessage().getChatId();
            msg = update.getMessage().getText();

            Boolean isCallingBot = msg.toUpperCase().startsWith("PRICE ");

            if (msg.length() > 6) {
                msg = msg.substring(6, msg.length());
            }

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
                        System.out.println(body);

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
                                SendMessage message = new SendMessage() // Create a message object object
                                        .setChatId(chat_id)
                                        .setText(prices);
                                try {
                                    execute(message); // Sending our message object to user
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public String getBotUsername() {
        return "compare-bot";
    }
}
