package interpreter;

import io.sgr.urlshortener.google.GoogleURLShortener;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Arthur
 * @author Alexandre
 * @author Alexandre
 * @author Magalie
 */
public class Interpreter extends Observable {

    JSONObject result = new JSONObject();

    public Boolean isCallingBot(String message) {
        return message.toUpperCase().startsWith("PRICE ");
    }

    public void parse(String message) {
        String errors = "";
        int limit = 1000;
        String[] listToken = message.split(" ");

        ArrayList<String> availableCommands = new ArrayList<>(Arrays.asList("price", "--limit"));

        String keywords = "";
        int index = 0;
        while (index < listToken.length) {

            String token = listToken[index];

            switch (token) {
                case "price":
                    Boolean finish = false;
                    while (!finish) {
                        index++;
                        Boolean outOfBand = index >= listToken.length;

                        String key = "";
                        if (!outOfBand) {
                            key = listToken[index];

                            if (!availableCommands.contains(key)) {

                                keywords += key + " ";
                            } else {
                                finish = true;
                            }

                        } else {
                            finish = true;
                        }

                    }
                    index--;
                    break;
                case "--limit":
                    index++;
                    Boolean outOfBand = index >= listToken.length;
                    if (!outOfBand) {
                        String key = listToken[index];

                        limit = Integer.parseInt(key);
                    } else {
                        errors += "aucun paramètre spécifié après l'identificateur limit\n\n";
                    }
                    index--;

            }
            index++;

            if (keywords.isEmpty()) {
                errors += "aucun mot-clés spécifié\n\n";
            }
        }

        if (errors.isEmpty()) {
            getResults(keywords.trim(), limit);
        } else {
            setChanged();
            this.notifyObservers(errors);
        }

    }

    private void getResults(String msg, int limit) {
        try {
            String ur = "http://localhost:8080/priceapi/price/" + msg;

            OkHttpClient okhttpClient = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
                    .writeTimeout(60000, TimeUnit.MILLISECONDS)
                    .readTimeout(60000, TimeUnit.MILLISECONDS)
                    .build();
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
                        int end = tabOffers.length() > limit ? limit : tabOffers.length();
                        for (int j = 0; j < end; j++) {
                            JSONObject article = (JSONObject) tabOffers.get(j);
                            Object res = article.get("url");
                            GoogleURLShortener shortener = new GoogleURLShortener();
                            String shortUrl = shortener.shortenURL("<some_origin>", "AIzaSyAjSBCl4HtD6yy-2n-pi0AX3w-S87EauxI", res.toString());
                            prices = " Prix : " + article.get("price") + " " + article.get("currency") + " lien : " + shortUrl + "\n";
                            //channel.sendMessage(prices).queue();
                            setChanged();
                            notifyObservers(prices);
                        }

                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
