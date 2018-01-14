package bot;

import interpreter.Interpreter;
import io.sgr.urlshortener.google.GoogleURLShortener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

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
public class TelegramBot extends TelegramLongPollingBot implements Observer {

    JSONObject result = new JSONObject();
    Interpreter interpreter = new Interpreter();
    String msg;
    long chat_id;

    @Override
    public String getBotToken() {
        return "525473043:AAFSuGYoGnMKY8g-403B7RlEhMA3NebhPT4";
    }

    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            chat_id = update.getMessage().getChatId();
            msg = update.getMessage().getText();

            if(interpreter.isCallingBot(msg)){
                interpreter.addObserver(this);
                interpreter.parse(msg);
            }

        }
    }

    public String getBotUsername() {
        return "compare-bot";
    }

    @Override
    public void update(Observable o, Object arg) {
        String prices = (String)arg;
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
