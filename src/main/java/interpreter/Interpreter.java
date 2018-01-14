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

public class Interpreter {



   public String getResult(String message){
       String result = "NOK";

       Boolean isCallingBot = message.toUpperCase().startsWith("PRICE ");

       if (message.length() > 6) {
           message = message.substring(6, message.length());
       }



       return "";
   }

}
