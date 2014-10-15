/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.parser.ParseException;
import ru.goodloot.tr.utils.AbstractUtils;

/**
 * 
 * @author lol
 */
public abstract class Exchange extends AbstractUtils {

    protected static final String USER_AGENT = "Mozilla/5.0";

    public static Object sendGet(String url) {

        try {

            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // add reuqest header
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            StringBuilder response;
            try (BufferedReader in =
                            new BufferedReader(
                                            new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            return parser.parse(response.toString());

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
