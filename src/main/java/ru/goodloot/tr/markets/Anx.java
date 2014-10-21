/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.NotAvailableFromExchangeException;
import com.xeiam.xchange.NotYetImplementedForExchangeException;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.service.polling.PollingTradeService;
import com.xeiam.xchange.utils.Base64;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONObject;
import ru.goodloot.tr.exceptions.AnxExchangeException;
import ru.goodloot.tr.objects.OrderInfo;
import ru.goodloot.tr.utils.Logger;
import ru.goodloot.tr.utils.Utils;

/**
 * 
 * @author lol
 */
public class Anx extends TradableExchange {

    private static final Logger logger = new Logger(Anx.class);

    public final static double HKD_USD = 0.129;

    private Exchange exchange;

    private PollingTradeService tradeService;

    private Map<String, String> argsOrderResult;

    private static final String API_V2 = "2";

    private static final String API_V3 = "3";

    public Anx(String secret, String key) {

        super(secret, key);

        ExchangeSpecification spec = new ExchangeSpecification(getExchangeClassName());
        spec.setApiKey(key);
        spec.setSecretKey(secret);

        exchange = ExchangeFactory.INSTANCE.createExchange(spec);
        tradeService = exchange.getPollingTradeService();

        argsOrderResult = new HashMap<>();
    }

    static public JSONObject callFunc(String pair, String func) {

        String url = "https://anxpro.com/api/2/" + pair + "/money/" + func;
        return (JSONObject) sendGet(url);
    }

    @Override
    protected String sendRequest(String method, Map<String, String> arguments) {
        return sendRequest(method, arguments, API_V2);
    }

    @Override
    protected String sendRequest(String method, Map<String, String> arguments,
                    String apiVersion) {

        Mac mac;
        SecretKeySpec key;

        if (arguments == null) {
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

        String apiPath;

        if (API_V3.equals(apiVersion)) {
            apiPath = "api/3/" + method;
        } else {
            apiPath = method;
        }

        try {
            String secret = getApiSecret();

            key = new SecretKeySpec(Base64.decode(secret), "HmacSHA512");
            mac = Mac.getInstance("HmacSHA512");
            mac.init(key);

            mac.update(apiPath.getBytes());
            mac.update(new byte[] {0});
            mac.update(postData.getBytes());

            String url = "https://anxpro.com/api/" + apiVersion + "/" + method;

            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            String sign = Base64.encodeBytes(mac.doFinal());
            String apiKey = getApiKey();

            con.setRequestMethod("POST");
            con.setRequestProperty("Rest-Key", apiKey);
            con.setRequestProperty("Rest-Sign", sign);

            con.setRequestProperty("Content-Type",
                            API_V3.equals(apiVersion) ? "application/json"
                                            : "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", USER_AGENT);

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
            throw new AnxExchangeException(e);
        }
    }

    private String getExchangeClassName() {
        return "com.xeiam.xchange.anx.v2.ANXExchange";
    }

    @Override
    public void setFundsAmount() {
        setFundsAmount(false);
    }

    @Override
    public void setFundsAmount(boolean suppressLog) {

        try {

            AccountInfo ai = exchange.getPollingAccountService().getAccountInfo();

            setBtcAmount(ai.getBalance("BTC").doubleValue());
            setUsdAmount(ai.getBalance("USD").doubleValue() * getUsdCource());

            if (!suppressLog) {
                logger.out("SetFundsAmount called", "btc: " + getBtcAmount(), "usd: "
                                + getUsdAmount());
            }

        } catch (ExchangeException | IOException | NotAvailableFromExchangeException
                        | NotYetImplementedForExchangeException e) {
            throw new AnxExchangeException(e);
        }
    }

    @Override
    public OrderInfo getOrderInfo() {

        LimitOrder order = null;

        try {
            for (LimitOrder o : tradeService.getOpenOrders().getOpenOrders()) {
                if (o.getId().equals(lastOrderId)) {
                    order = o;
                    break;
                }
            }

            if (order != null) {
                logger.out("Order " + lastOrderId + " opened");
                return new OrderInfo(lastOrderId, false, null);
            } else {
                logger.out("Order " + lastOrderId + " closed");
                return new OrderInfo(lastOrderId, true, null);
            }

        } catch (ExchangeException | IOException | NotAvailableFromExchangeException
                        | NotYetImplementedForExchangeException e) {
            throw new AnxExchangeException(e);
        }
    }

    public OrderInfo getClosedOrderInfo(String orderId) {

        argsOrderResult.put("orderId", orderId);

        JSONObject json =
                        callMethod("BTCUSD/money/order/result", argsOrderResult, API_V2);
        String exec = value(jsonValue(jsonValue(json, "data"), "total_amount"), "value");

        return new OrderInfo(orderId, true, exec == null ? null : Double.valueOf(exec));
    }

    @Override
    public boolean cancelLastOrder() {

        try {
            logger.out("Call cancelLastOrder " + lastOrderId);
            return tradeService.cancelOrder(lastOrderId);
        } catch (ExchangeException | IOException | NotAvailableFromExchangeException
                        | NotYetImplementedForExchangeException e) {
            throw new AnxExchangeException(e);
        }
    }

    @Override
    public boolean tradeBuyMargin(double diffBtc) {

        try {

            Ticker t =
                            exchange.getPollingMarketDataService().getTicker(
                                            CurrencyPair.BTC_USD, null);

            BigDecimal price = t.getAsk();
            depthPrice = price.toString();
            // BigDecimal price = new BigDecimal("2000");

            double usdAmount = getUsdAmount();

            if (diffBtc * price.doubleValue() * getUsdCource() > usdAmount
                            * feeMultiplier()) {
                diffBtc =
                                usdAmount / getUsdCource() / price.doubleValue()
                                                * feeMultiplier();
            }

            BigDecimal volume = new BigDecimal(diffBtc, new MathContext(6));

            LimitOrder lo =
                            new LimitOrder(OrderType.BID, volume, CurrencyPair.BTC_USD,
                                            null, Utils.now(), price);

            logger.out("Placing limit buy order", "vol: " + volume, "price: " + price);

            String res = tradeService.placeLimitOrder(lo);

            logger.out("Limit buy order responce: " + res);

            lastOrderId = parseOrderId(res);

            return true;

        } catch (ExchangeException | IOException | NotAvailableFromExchangeException
                        | NotYetImplementedForExchangeException e) {
            throw new AnxExchangeException(e);
        }
    }

    @Override
    public boolean tradeSellMargin(double diffBtc) {

        try {

            Ticker t =
                            exchange.getPollingMarketDataService().getTicker(
                                            CurrencyPair.BTC_USD, null);

            BigDecimal price = t.getBid();
            depthPrice = price.toString();
            // BigDecimal price = new BigDecimal("4000");

            double btcAmount = getBtcAmount();

            if (Math.abs(diffBtc) > btcAmount) {
                diffBtc = btcAmount;
            }

            BigDecimal volume = new BigDecimal(Math.abs(diffBtc), new MathContext(6));

            LimitOrder lo =
                            new LimitOrder(OrderType.ASK, volume, CurrencyPair.BTC_USD,
                                            null, Utils.now(), price);

            logger.out("Placing limit sell order", "vol: " + volume, "price: " + price);

            String res = tradeService.placeLimitOrder(lo);

            logger.out("Limit sell order responce: " + res);

            lastOrderId = parseOrderId(res);

            return true;

        } catch (ExchangeException | IOException | NotAvailableFromExchangeException
                        | NotYetImplementedForExchangeException e) {
            throw new AnxExchangeException(e);
        }
    }

    protected String parseOrderId(String responce) {
        return responce;
    }

    @Override
    public double getFee() {
        return 0.003;
    }

    @Override
    public double getUsdCource() {
        return 1;
    }
}
