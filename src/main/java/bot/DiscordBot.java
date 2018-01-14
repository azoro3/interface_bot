package bot;

import interpreter.Interpreter;
import io.sgr.urlshortener.google.GoogleURLShortener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Observer;

import javafx.beans.Observable;
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
public class DiscordBot extends ListenerAdapter implements Observer {


    MessageChannel channel;
    Interpreter interpreter = new Interpreter();

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
        String message = (String)arg;
        channel.sendMessage(message).queue();
    }
}
