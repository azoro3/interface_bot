package interpreter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

public class Interpreter {
    public Boolean isCallingBot(String message){
        return message.toUpperCase().startsWith("PRICE ");
    }

    public String parse(String message){
        String errors = "";
        int limit = 1000;
        String[] listToken = message.split(" ");

        ArrayList<String> availableCommands = new ArrayList<>(Arrays.asList("price", "--limit"));

        String keywords = "";
        int index = 0;
        while (index < listToken.length){
            String token = listToken[index];
            switch (token){
                case "price":
                    Boolean finish = false;
                    while (!finish){
                        index++;
                        Boolean outOfBand = index >= listToken.length;
                        String key = "";
                        if(!outOfBand) {
                            key = listToken[index];
                            if (!availableCommands.contains(key)){

                                keywords += key + " ";
                            }else{
                                finish = true;
                            }

                        }else{
                            finish = true;
                        }
                        index --;
                    }
                    break;
                case "--limit" :
                    index ++;
                    Boolean outOfBand = index >= listToken.length;
                    if(!outOfBand){
                        String key = listToken[index];

                        limit = Integer.parseInt(key);
                    }else{
                        errors += "aucun paramètre spécifié après l'identificateur limit\n\n";
                    }


            }
            index++;

        }
        if(message.isEmpty())
            errors += "aucun mot-clé spécifié\n\n";

        if(errors.isEmpty()){
            return message;
        }else{
            return errors;
        }


    }

}
