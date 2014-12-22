/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.goodloot.tr.markets.exchange;

import com.xeiam.xchange.utils.Base64;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * @author lol
 */
public class Bitfinex extends Exchange {

    private String _secret;

    private String _key;

    private long _nonce;

    static public Object callFunc(String pair, String func) {

        // Functions: fee, trades, ticker, depth
        String url = "https://api.bitfinex.com/v1/" + func + "/" + pair;

        return sendGet(url);
    }

    private Object callMethod(String method, Map<String, String> arguments)
            throws Exception {

        javax.crypto.Mac mac;
        javax.crypto.spec.SecretKeySpec key = null;

        if (arguments == null) { // If the user provided no arguments, just
            // create an empty argument array.
            arguments = new java.util.HashMap<>();
        }

        arguments.put("request", "/v1" + method); // Add the method to the post
        // data.
        arguments.put("nonce", "" + ++_nonce); // Add the dummy nonce.
        String postData = "";
        for (Iterator argumentIterator =
                     arguments.entrySet().iterator(); argumentIterator
                     .hasNext(); ) {
            java.util.Map.Entry argument = (java.util.Map.Entry) argumentIterator.next();

            if (postData.length() > 0) {
                postData += ",";
            }
            if (argument.getValue() instanceof Integer) {
                postData +=
                        "\"" + argument.getKey() + "\"" + ":"
                                + argument.getValue();
            } else {
                postData +=
                        "\"" + argument.getKey() + "\"" + ":" + "\""
                                + argument.getValue() + "\"";
            }
        }
        postData = "{" + postData + "}";
        String encodedPostData =
                Base64.encodeBytes(postData.getBytes());
        // Create a new secret key
        try {
            key = new javax.crypto.spec.SecretKeySpec(
                    _secret.getBytes("UTF-8"), "HmacSHA384");
        } catch (java.io.UnsupportedEncodingException uee) {
            System.err.println("Unsupported encoding exception: "
                    + uee.toString());
            return null;
        }

        // Create a new mac
        try {
            mac = javax.crypto.Mac.getInstance("HmacSHA384");
        } // catch( NoSuchAlgorithmException nsae) {
        catch (Exception e) {
            System.err.println("No such algorithm exception: " + e.toString());
            return null;
        }

        // Init mac with key.
        try {
            mac.init(key);

        } catch (Exception e) {
            System.err.println("Invalid key exception: " + e.toString());
            return null;
        }

        String url = "https://api.bitfinex.com/v1" + method;
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("X-BFX-APIKEY", _key);
        con.setRequestProperty("X-BFX-PAYLOAD", (encodedPostData));
        con.setRequestProperty("X-BFX-SIGNATURE", new String(mac
                .doFinal(encodedPostData.getBytes("UTF-8"))));

        con.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(
                con.getOutputStream())) {
            wr.writeBytes(postData);
            wr.flush();
        }

        java.io.BufferedReader in =
                new java.io.BufferedReader(
                        new java.io.InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONParser parser = new JSONParser();
        return parser.parse(response.toString());
    }
}
