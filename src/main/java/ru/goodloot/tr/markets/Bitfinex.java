/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.goodloot.tr.markets;

import ru.goodloot.tr.utils.Utils;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lol
 */
public class Bitfinex extends Exchange {

    private String _secret;
    private String _key;
    private Map argumentsSell;
    private Map argumentsBuy;
    private Map argumentsMarginSell;
    private Map argumentsMarginBuy;
    private Map argumentsCancel;
    private double btcAmount = 0;
    private double usdAmount = 0;
    // private double prevBtcAmount = -1;
    // private double prevUsdAmount = -1;
    static private double ltcTickerMid = -1;
    // static private double prevLtcTickerMid;
    static private double ltcAmountTrade;
    static private String avgExecutionPrice = "";
    // static private double prevTickerBuy = 0;
    // static private double prevTickerSell = 0;
    static public double fee = 0.002;
    private double remains = 0;
    private int lastOrderId = 0;
    private int lastTrade = 0; // 1 - Sell 2 - Buy
    private String depthPrice;
    private long _nonce;
    private double marginBalance;
    private double marginBtcAmount;

    public Bitfinex(String secret, String key) {

        _nonce = Utils.getNonce();
        _secret = secret;
        _key = key;
        argumentsCancel = new HashMap<>();
        argumentsSell = new HashMap<>();
        argumentsBuy = new HashMap<>();
        argumentsMarginSell = new HashMap<>();
        argumentsMarginBuy = new HashMap<>();
        argumentsSell.put("symbol", "btcusd");
        argumentsSell.put("exchange", "bitfinex");
        argumentsSell.put("side", "sell");
        argumentsSell.put("type", "exchange limit");
        argumentsBuy.put("symbol", "btcusd");
        argumentsBuy.put("exchange", "bitfinex");
        argumentsBuy.put("side", "buy");
        argumentsBuy.put("type", "exchange limit");
        argumentsMarginSell.put("symbol", "btcusd");
        argumentsMarginSell.put("exchange", "all");
        argumentsMarginSell.put("side", "sell");
        argumentsMarginSell.put("type", "limit");
        argumentsMarginBuy.put("symbol", "btcusd");
        argumentsMarginBuy.put("exchange", "all");
        argumentsMarginBuy.put("side", "buy");
        argumentsMarginBuy.put("type", "limit");
    }

    static public Object callFunc(String pair, String func) {

        // Functions: fee, trades, ticker, depth
        String url = "https://api.bitfinex.com/v1/" + func + "/" + pair;

        return sendGet(url);
    }
    /*
     private Object callMethod(String method, Map<String, String> arguments)
     throws Exception {

     Mac mac;
     SecretKeySpec key = null;

     if (arguments == null) { // If the user provided no arguments, just
     // create an empty argument array.
     arguments = new HashMap<>();
     }

     arguments.put("request", "/v1" + method); // Add the method to the post
     // data.
     arguments.put("nonce", "" + ++_nonce); // Add the dummy nonce.
     String postData = "";
     for (Iterator argumentIterator = arguments.entrySet().iterator(); argumentIterator
     .hasNext();) {
     Map.Entry argument = (Map.Entry) argumentIterator.next();

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
     Base64.encodeBase64String(postData.getBytes());
     // Create a new secret key
     try {
     key = new SecretKeySpec(_secret.getBytes("UTF-8"), "HmacSHA384");
     } catch (UnsupportedEncodingException uee) {
     System.err.println("Unsupported encoding exception: "
     + uee.toString());
     return null;
     }

     // Create a new mac
     try {
     mac = Mac.getInstance("HmacSHA384");
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
     con.setRequestProperty("X-BFX-SIGNATURE", Hex.encodeHexString(mac
     .doFinal(encodedPostData.getBytes("UTF-8"))));

     con.setDoOutput(true);
     try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
     wr.writeBytes(postData);
     wr.flush();
     }

     BufferedReader in =
     new BufferedReader(new InputStreamReader(con.getInputStream()));
     String inputLine;
     StringBuilder response = new StringBuilder();

     while ((inputLine = in.readLine()) != null) {
     response.append(inputLine);
     }
     in.close();

     JSONParser parser = new JSONParser();
     return parser.parse(response.toString());
     }

     static public int setLtcTicker() {

     JSONObject ltcTickerJSON;
     // double BuySellTrustedDiff = 0.1;
     try {
     // prevLtcTickerMid = ltcTickerMid;
     ltcTickerJSON = (JSONObject) callFunc("ltcusd", "ticker");
     ltcTickerMid =
     Double.parseDouble((ltcTickerJSON.get("mid").toString()));
     return 1;
     } catch (Exception e) {
     System.err.println("setLtcTicker Bitfinex " + e.toString());
     return 0;
     }
     }

     public boolean setFundsAmount() {

     try {
     JSONArray jsonArrayBitfinex =
     (JSONArray) callMethod("/balances", null);

     // prevBtcAmount = btcAmount;
     // prevUsdAmount = usdAmount;
     btcAmount =
     Double.parseDouble(((JSONObject) (jsonArrayBitfinex.get(6)))
     .get("amount").toString());
     usdAmount =
     Double.parseDouble(((JSONObject) (jsonArrayBitfinex.get(8)))
     .get("amount").toString());
     return true;
     } catch (Exception e) {
     LoggerUtils.writeAndOut("bitfinex/errs.txt",
     "SetFundsAmount Bitfinex " + e.toString());
     return false;
     }
     }

     public boolean tradeBuy(double diffBtc) {

     JSONObject tradeRes;
     argumentsBuy.put("amount", Math.abs(Utils.round(diffBtc, 4)));
     try {
     JSONObject jo =
     (JSONObject) callFunc("btcusd?limit_bids=0&limit_asks=1",
     "book");
     depthPrice =
     ((JSONObject) ((JSONArray) jo.get("asks")).get(0)).get(
     "price").toString();
     // argumentsBuy.put("price", BitfinexTicker.getTickerBuy());
     tradeRes = (JSONObject) callMethod("/order/new", argumentsBuy);
     } catch (Exception e) {
     LoggerUtils.writeAndOut("bitfinex/errs.txt",
     "tradeBuy " + e.toString());
     return false;
     }
     LoggerUtils.writeAndOut("bitfinex/trades.txt", tradeRes.toString());
     lastOrderId = Integer.valueOf(tradeRes.get("order_id").toString());
     return true;
     }

     public boolean tradeSell(double diffBtc) {

     JSONObject tradeRes;
     argumentsSell.put("amount", Math.abs(Utils.round(diffBtc, 4)));
     try {
     JSONObject jo =
     (JSONObject) callFunc("btcusd?limit_bids=1&limit_asks=0",
     "book");
     depthPrice =
     ((JSONObject) ((JSONArray) jo.get("bids")).get(0)).get(
     "price").toString();
     // argumentsSell.put("price", BitfinexTicker.getTickerSell());
     tradeRes = (JSONObject) callMethod("/order/new", argumentsSell);
     } catch (Exception e) {
     LoggerUtils.writeAndOut("bitfinex/errs.txt",
     "tradeSell " + e.toString());
     return false;
     }
     LoggerUtils.writeAndOut("bitfinex/trades.txt", tradeRes.toString());
     lastOrderId = Integer.valueOf(tradeRes.get("order_id").toString());
     return true;
     }

     public boolean tradeBuyMargin(double diffBtc) {

     JSONObject tradeRes;
     argumentsMarginBuy.put("amount", Math.abs(Utils.round(diffBtc, 4)));
     try {
     JSONObject jo =
     (JSONObject) callFunc("btcusd?limit_bids=0&limit_asks=1",
     "book");
     depthPrice =
     ((JSONObject) ((JSONArray) jo.get("asks")).get(0)).get(
     "price").toString();
     argumentsMarginBuy.put("price", depthPrice);
     tradeRes =
     (JSONObject) callMethod("/order/new", argumentsMarginBuy);
     } catch (Exception e) {
     LoggerUtils.writeAndOut("bitfinex/errs.txt",
     "tradeBuyMargin " + e.toString());
     return false;
     }
     LoggerUtils.writeAndOut("bitfinex/tradesMargin.txt",
     tradeRes.toString());
     lastOrderId = Integer.valueOf(tradeRes.get("order_id").toString());
     return true;
     }

     public boolean tradeSellMargin(double diffBtc) {

     JSONObject tradeRes;
     argumentsMarginSell.put("amount", Math.abs(Utils.round(diffBtc, 4)));
     try {
     JSONObject jo =
     (JSONObject) callFunc("btcusd?limit_bids=1&limit_asks=0",
     "book");
     depthPrice =
     ((JSONObject) ((JSONArray) jo.get("bids")).get(0)).get(
     "price").toString();
     argumentsMarginSell.put("price", depthPrice);
     tradeRes =
     (JSONObject) callMethod("/order/new", argumentsMarginSell);
     } catch (Exception e) {
     LoggerUtils.writeAndOut("bitfinex/errs.txt", "tradeSellMargin "
     + e.toString());
     return false;
     }
     LoggerUtils.writeAndOut("bitfinex/tradesMargin.txt",
     tradeRes.toString());
     lastOrderId = Integer.valueOf(tradeRes.get("order_id").toString());
     return true;
     }

     boolean isLastOrderComplete() {

     Map argumentsStatus = new HashMap<>();
     argumentsStatus.put("order_id", lastOrderId);
     try {
     JSONObject jo =
     (JSONObject) callMethod("/order/status", argumentsStatus);
     double rem =
     Double.parseDouble(jo.get("remaining_amount").toString());
     if (rem == 0) {
     avgExecutionPrice = jo.get("avg_execution_price").toString();
     return true;
     } else {
     return false;
     }
     } catch (Exception e) {
     LoggerUtils.writeAndOut("bitfinrx/errs.txt",
     "isLastOrderComplete Error " + e.toString());
     return false;
     }
     }

     public boolean setFunsAmountMargin() {

     try {
     JSONArray jsonArrayBitfinex =
     (JSONArray) callMethod("/balances", null);
     ltcAmountTrade =
     Double.parseDouble(((JSONObject) (jsonArrayBitfinex.get(1)))
     .get("amount").toString());
     return true;
     } catch (Exception e) {
     LoggerUtils.writeAndOut("bitfinex/errs.txt",
     "setFunsAmountMargin Bitfinex " + e.toString());
     return false;
     }
     }

     public boolean setMarginBalance() {

     try {
     marginBalance = ltcTickerMid * ltcAmountTrade / (1 + fee);
     return true;
     } catch (Exception e) {
     LoggerUtils.writeAndOut("bitfinex/errs.txt",
     "setMarginBalance Bitfinex " + e.toString());
     return false;
     }
     }

     public boolean setMarginBtcAmount() {

     try {
     JSONArray positions = (JSONArray) callMethod("/positions", null);
     if (positions.isEmpty()) {
     marginBtcAmount = 0;
     } else {
     marginBtcAmount =
     Double.parseDouble(((JSONObject) positions.get(0))
     .get("amount").toString());
     }
     return true;
     } catch (Exception e) {
     LoggerUtils.writeAndOut("bitfinex/errs.txt",
     "setMarginBtcAmount Bitfinex " + e.toString());
     return false;
     }
     }

     public boolean cancelLastOrder() {

     JSONObject jo;
     argumentsCancel.put("order_id", lastOrderId);
     try {
     jo = (JSONObject) callMethod("/order/cancel", argumentsCancel);
     if (jo.isEmpty()) {
     return false;
     } else {
     return true;
     }
     } catch (Exception e) {
     LoggerUtils.writeAndOut("bitfinex/errs.txt",
     "cancelLastOrder Bitfinex " + e.toString());
     return false;
     }
     }

     public double getMarginBalance() {

     return marginBalance;
     }

     public double getMarginBtcAmount() {

     return marginBtcAmount;
     }

     public double getTotalInBtcMargin(double price) {

     return marginBalance / price / (1 + fee);
     }

     double getTotalInBtc(double Price) {

     return getBtcAmount() + (getUsdAmount() / Price) * (1 - Bitfinex.fee);
     }

     double getBtcAmount() {

     return btcAmount;
     }

     double getUsdAmount() {

     return usdAmount;
     }

     public double getRemains() {

     return remains;
     }

     public void setRemains(double r) {

     remains = r;
     }

     public int getLastTrade() {

     return lastTrade;
     }

     public String getLastDepthPrice() {

     return depthPrice;
     }

     public int getLastOrderId() {

     return lastOrderId;
     }

     String getAvgExecutionPrice() {

     return avgExecutionPrice;
     }*/
}
