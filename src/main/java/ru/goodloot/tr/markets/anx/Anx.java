/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.anx;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.NotAvailableFromExchangeException;
import com.xeiam.xchange.NotYetImplementedForExchangeException;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.utils.Base64;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import ru.goodloot.tr.markets.TradableExchange;
import ru.goodloot.tr.objects.OrderInfo;

/**
 * 
 * @author lol
 */
public class Anx extends TradableExchange {

    public final static double HKD_USD = 0.129;

    Exchange anx;

    public Anx(String secret, String key) {

        super(secret, key);

        ExchangeSpecification spec = new ExchangeSpecification(getExchangeClassName());
        spec.setApiKey(key);
        spec.setSecretKey(secret);

        anx = ExchangeFactory.INSTANCE.createExchange(spec);
    }

    static public Object callFunc(String pair, String func) {

        String url = "https://anxpro.com/api/2/" + pair + "/money/" + func;
        return sendGet(url);
    }

    protected String sendRequest(String method, Map<String, String> arguments) {

        Mac mac;
        SecretKeySpec key;

        if (arguments == null) { // If the user provided no arguments, just
            // create an empty argument array.
            arguments = new HashMap<>();
        }

        long nonce = getNonce();

        arguments.put("nonce", "" + nonce); // Add the dummy nonce.
        String postData = "";
        for (Map.Entry<String, String> argument : arguments.entrySet()) {

            if (postData.length() > 0) {
                postData += "&";
            }
            postData += argument.getKey() + "=" + argument.getValue();
        }

        // postData = "Test" + '\0' + "Message";
        String apiPath = "api" + method;

        // String secretData = apiPath + '\0' + postData;
        try {
            String secret = getApiSecret();
            // String secret = "7pgj8Dm6";

            key = new SecretKeySpec(Base64.decode(secret), "HmacSHA512");
            mac = Mac.getInstance("HmacSHA512");
            mac.init(key);

            mac.update(apiPath.getBytes());
            // mac.update("Test".getBytes());
            mac.update(new byte[] {0});
            // mac.update("Message".getBytes());
            mac.update(postData.getBytes());

            String url = "https://anxpro.com/api" + method;
            // String url =
            // "https://private-anon-487d5f9e8-anxv3.apiary-mock.com/api"
            // + method;

            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            String sign = Base64.encodeBytes(mac.doFinal());
            String apiKey = getApiKey();

            con.setRequestMethod("POST");
            con.setRequestProperty("Rest-Key", apiKey);
            con.setRequestProperty("Rest-Sign", sign);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // con.setRequestProperty("User-Agent", null);

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
            throw new RuntimeException(e);
        }
    }

    private String getExchangeClassName() {
        return "com.xeiam.xchange.anx.v2.ANXExchange";
    }

    public boolean setFundsAmount() {

        // JSONObject obj = callMethod("/2/money/info", null);

        try {
            AccountInfo ai = anx.getPollingAccountService().getAccountInfo();
            System.out.println(ai);
            setBtcAmount(ai.getBalance("BTC").doubleValue());
            setUsdAmount(ai.getBalance("USD").doubleValue());

        } catch (ExchangeException | IOException | NotAvailableFromExchangeException
                        | NotYetImplementedForExchangeException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public OrderInfo getOrderInfo() {
        return null;
    }

    public boolean cancelLastOrder() {
        return true;
    }

    public boolean tradeBuyMargin(double diffBtc) {
        return true;
    }

    public boolean tradeSellMargin(double diffBtc) {
        return true;
    }

    public double getFee() {
        return 0;
    }
}
