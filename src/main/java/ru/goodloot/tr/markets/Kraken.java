/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets;

import com.xeiam.xchange.utils.Base64;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.goodloot.tr.objects.OrderInfo;
import ru.goodloot.tr.utils.Utils;

/**
 * 
 * @author lol
 */
public class Kraken extends TradableExchange {

    private Map<String, String> argsBalance = new HashMap<>();

    private Map<String, String> argsMarginBuy = new HashMap<>();

    private Map<String, String> argsMarginSell = new HashMap<>();

    private Map<String, String> argsCancel = new HashMap<>();

    private Map<String, String> argsStatus = new HashMap<>();

    protected double eurAmout;

    public static final double EUR_USD = 1.38;

    public Kraken(String secret, String key) {

        super(secret, key);

        argsBalance.put("asset", "ZUSD");

        argsMarginBuy.put("pair", "XBTEUR");
        argsMarginBuy.put("type", "buy");
        argsMarginBuy.put("ordertype", "limit");

        argsMarginSell.put("pair", "XBTEUR");
        argsMarginSell.put("type", "sell");
        argsMarginSell.put("ordertype", "limit");
    }

    static public JSONObject callFunc(String pair, String func) {

        String url = "https://api.kraken.com/0/public/" + func + "?pair=" + pair;
        return (JSONObject) sendGet(url);
    }

    @Override
    public String sendRequest(String method, Map<String, String> arguments) {

        Mac mac;
        SecretKeySpec key;

        if (arguments == null) { // If the user provided no arguments, just
            // create an empty argument array.
            arguments = new HashMap<>();
        }

        long nonce = getNonce() * 1000 + 635311245321481394L;

        arguments.put("nonce", "" + nonce); // Add the dummy nonce.
        String postData = "";
        for (Map.Entry<String, String> argument : arguments.entrySet()) {

            if (postData.length() > 0) {
                postData += "&";
            }
            postData += argument.getKey() + "=" + argument.getValue();
        }

        String secretData = nonce + (char) 0 + postData;

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(secretData.getBytes("UTF-8"));
            byte[] secretDataByteArray = md.digest();

            // Create a new secret key
            key = new SecretKeySpec(Base64.decode(getApiSecret()), "HmacSHA512");

            // Create a new mac
            mac = Mac.getInstance("HmacSHA512");

            // Init mac with key.
            mac.init(key);

            String path = "/0/private/" + method;
            byte[] pathBytesArray = path.getBytes("UTF-8");
            byte[] fullBytesArray = Utils.concat(pathBytesArray, secretDataByteArray);

            String url = "https://api.kraken.com" + path;
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            String sign = Base64.encodeBytes(mac.doFinal(fullBytesArray));
            String apiKey = getApiKey();

            con.setRequestMethod("POST");
            con.setRequestProperty("API-Key", apiKey);
            con.setRequestProperty("API-Sign", sign);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", null);

            con.setDoOutput(true);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(postData);
                wr.flush();
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader in =
                            new BufferedReader(
                                            new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            return response.toString();

        } catch (NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void setFundsAmount() {

        JSONObject jsonObj = callMethod("Balance", null);

        JSONObject jsonObjTemp = (JSONObject) jsonObj.get("result");

        setBtcAmount(Double.parseDouble(jsonObjTemp.get("XXBT").toString()));
        setUsdAmount(Double.parseDouble(jsonObjTemp.get("ZEUR").toString()) * EUR_USD);
    }

    public boolean tradeBuyMargin(double diffBtc) {

        double volume;

        JSONObject jo = callFunc("XBTEUR", "Ticker");

        depthPrice =
                        ((JSONArray) ((JSONObject) ((JSONObject) jo.get("result"))
                                        .get("XXBTZEUR")).get("a")).get(0).toString();

        // depthPrice = "10";
        argsMarginBuy.put("price", depthPrice);

        double usdAmount = getUsdAmount();

        if (diffBtc * Double.valueOf(depthPrice) * EUR_USD > usdAmount) {
            volume = usdAmount / EUR_USD / Double.valueOf(depthPrice);
        } else {
            volume = diffBtc;
        }

        argsMarginBuy.put("volume", String.valueOf((Utils.round(Math.abs(volume), 6))));

        JSONObject tradeRes = (JSONObject) callMethod("AddOrder", argsMarginBuy);

        lastOrderId =
                        ((JSONArray) ((JSONObject) tradeRes.get("result")).get("txid"))
                                        .get(0).toString();

        return true;
    }

    @Override
    public boolean tradeSellMargin(double diffBtc) {

        double volume;
        JSONObject tradeRes;
        JSONObject jo = callFunc("XBTEUR", "Ticker");

        depthPrice =
                        ((JSONArray) ((JSONObject) ((JSONObject) jo.get("result"))
                                        .get("XXBTZEUR")).get("b")).get(0).toString();

        // depthPrice = "1000";
        argsMarginSell.put("price", depthPrice);

        double btcAmount = getBtcAmount();

        if (Math.abs(diffBtc) > btcAmount) {
            volume = btcAmount;
        } else {
            volume = diffBtc;
        }

        argsMarginSell.put("volume", String.valueOf((Utils.round(Math.abs(volume), 6))));

        tradeRes = (JSONObject) callMethod("AddOrder", argsMarginSell);

        lastOrderId =
                        ((JSONArray) ((JSONObject) tradeRes.get("result")).get("txid"))
                                        .get(0).toString();

        return true;
    }

    @Override
    public boolean cancelLastOrder() {

        argsCancel.put("txid", lastOrderId);

        JSONObject jo = (JSONObject) callMethod("CancelOrder", argsCancel);

        return "1".equals(((JSONObject) jo.get("result")).get("count").toString());
    }

    @Override
    public OrderInfo getOrderInfo() {

        OrderInfo i = new OrderInfo();

        argsStatus.put("txid", lastOrderId);

        JSONObject jo = (JSONObject) callMethod("QueryOrders", argsStatus);

        JSONObject res = (JSONObject) ((JSONObject) jo.get("result")).get(lastOrderId);

        i.setVolExecuted(Double.parseDouble(res.get("vol_exec").toString()));
        i.setOrderComplete("closed".equals(res.get("status").toString()));

        return i;
    }

    @Override
    public double getFee() {

        return 0.002;
    }

    public String getLastOrderId() {

        return lastOrderId;
    }
}
