package ru.goodloot.tr.markets.exchange;

import org.json.simple.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Btce extends Exchange {

    private String _secret;

    private String _key;

    private long _nonce;

    static public JSONObject callFunc(String pair, String func) {

        // Functions: fee, trades, ticker, depth
        String url = "https://btc-e.com/api/2/" + pair + "/" + func;

        return (JSONObject) sendGet(url);
    }

    protected String sendRequest(String method,
            Map<String, String> arguments) {

        Mac mac;
        SecretKeySpec key;

        if (arguments == null) { // If the user provided no arguments, just
            // create an empty argument array.
            arguments = new HashMap<>();
        }

        arguments.put("method", method); // Add the method to the post data.
        arguments.put("nonce", "" + _nonce); // Add the dummy nonce.
        String postData = "";
        for (Iterator argumentIterator =
                     arguments.entrySet().iterator(); argumentIterator
                     .hasNext(); ) {
            Map.Entry argument = (Map.Entry) argumentIterator.next();

            if (postData.length() > 0) {
                postData += "&";
            }
            postData += argument.getKey() + "=" + argument.getValue();
        }

        // Create a new secret key
        try {
            key = new SecretKeySpec(_secret.getBytes("UTF-8"),
                    "HmacSHA512");
        } catch (UnsupportedEncodingException uee) {
            System.err.println(
                    "Unsupported encoding exception: " + uee.toString());
            return null;
        }

        // Create a new mac
        try {
            mac = Mac.getInstance("HmacSHA512");
        } // catch( NoSuchAlgorithmException nsae) {
        catch (Exception e) {
            System.err.println("No such algorithm exception: " + e.toString());
            return null;
        }

        // Init mac with key.
        try {
            mac.init(key);
            // } catch( InvalidKeyException ike) {
        } catch (Exception e) {
            System.err.println("Invalid key exception: " + e.toString());
            return null;
        }

        String url = "https://btc-e.com/tapi";

        try {

            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Key", _key);
            con.setRequestProperty("Sign",
                    new String(
                            mac.doFinal(postData.getBytes("UTF-8"))));

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(
                    con.getOutputStream())) {
                wr.writeBytes(postData);
                wr.flush();
            }

            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
