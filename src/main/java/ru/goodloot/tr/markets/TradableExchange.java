/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets;

import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import ru.goodloot.tr.objects.OrderInfo;
import ru.goodloot.tr.utils.Logger;
import ru.goodloot.tr.utils.Utils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * 
 * @author lol
 */
public abstract class TradableExchange extends Exchange {

    private String apiSecret;

    private String apiKey;

    private long nonce;

    private double btcAmount = 0;

    private double usdAmount = 0;

    protected String depthPrice;

    protected String lastOrderId;

    private boolean printMethodCallResponce = true;

    private boolean printMethodCallRequest = true;

    private static final Logger logger = new Logger(TradableExchange.class);

    public TradableExchange(String secret, String key) {

        this.nonce = Utils.getNonce();
        this.apiSecret = secret;
        this.apiKey = key;
    }

    public double getTotalInBtc(double Price) {

        return btcAmount + (usdAmount / Price) * (1 - getFee());
    }

    /*
     * 0.01 - 1% fee
     */
    protected final JSONObject callMethod(String method, Map<String, String> arguments) {

        return callMethod(method, arguments, null);
    }

    protected final JSONObject callMethod(String method, Map<String, String> arguments,
                    String apiVersion) {

        if (printMethodCallRequest) {
            logger.out("Call method: " + method, "Args: " + arguments);
        }

        String response;

        if (apiVersion == null) {
            response = sendRequest(method, arguments);
        } else {
            response = sendRequest(method, arguments, apiVersion);
        }

        if (printMethodCallResponce) {
            logger.out("Method: " + method, "Response: " + response);
        }

        try {
            return response.isEmpty() ? null : (JSONObject) parser.parse(response
                            .toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    abstract protected String sendRequest(String method, Map<String, String> arguments);

    protected String sendRequest(String method, Map<String, String> arguments,
                    String apiVersion) {
        throw new NotImplementedException();
    }

    abstract public double getFee();

    public double feeMultiplier() {
        return 1 - getFee();
    }

    abstract public void setFundsAmount();

    abstract public OrderInfo getOrderInfo();

    abstract public boolean cancelLastOrder();

    abstract public boolean tradeBuyMargin(double diffBtc);

    abstract public boolean tradeSellMargin(double diffBtc);

    public double getUsdCource() {
        throw new NotImplementedException();
    }

    public String getApiSecret() {

        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {

        this.apiSecret = apiSecret;
    }

    public String getApiKey() {

        return apiKey;
    }

    public void setApiKey(String apiKey) {

        this.apiKey = apiKey;
    }

    public long getNonce() {

        return nonce++;
    }

    public double getBtcAmount() {

        return btcAmount;
    }

    public void setBtcAmount(double btcAmount) {

        this.btcAmount = btcAmount;
    }

    public double getUsdAmount() {

        return usdAmount;
    }

    public void setUsdAmount(double usdAmount) {

        this.usdAmount = usdAmount;
    }

    public String getDepthPrice() {

        return depthPrice;
    }
}
