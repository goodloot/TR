package ru.goodloot.tr.markets;

import org.json.simple.JSONObject;

public class Btce extends Exchange {
    /*
     private Map<String, String> argumentsSell;
     private Map<String, String> argumentsBuy;
     private Map<String, String> argumentsCancel;
     private double remains = 0;
     private int lastOrderId = 0;
     private TradeTypes lastTrade; // 1 - Sell 2 - Buy
     private String depthPrice;

     public Btce(String secret, String key) {

     super(secret, key);

     argumentsCancel = new HashMap<>();

     argumentsSell = new HashMap<>();
     argumentsSell.put("type", "sell");
     argumentsSell.put("pair", "btc_usd");

     argumentsBuy = new HashMap<>();
     argumentsBuy.put("type", "buy");
     argumentsBuy.put("pair", "btc_usd");
     }*/

    static public JSONObject callFunc(String pair, String func) {

        // Functions: fee, trades, ticker, depth
        String url = "https://btc-e.com/api/2/" + pair + "/" + func;

        return (JSONObject) sendGet(url);
    }
    /*
     protected String sendRequest(String method, Map<String, String> arguments) {

     Mac mac;
     SecretKeySpec key;

     if (arguments == null) { // If the user provided no arguments, just
     // create an empty argument array.
     arguments = new HashMap<>();
     }

     arguments.put("method", method); // Add the method to the post data.
     arguments.put("nonce", "" + getNonce()); // Add the dummy nonce.
     String postData = "";
     for (Iterator argumentIterator = arguments.entrySet().iterator(); argumentIterator
     .hasNext();) {
     Map.Entry argument = (Map.Entry) argumentIterator.next();

     if (postData.length() > 0) {
     postData += "&";
     }
     postData += argument.getKey() + "=" + argument.getValue();
     }

     // Create a new secret key
     try {
     key = new SecretKeySpec(getApiSecret().getBytes("UTF-8"), "HmacSHA512");
     } catch (UnsupportedEncodingException uee) {
     System.err.println("Unsupported encoding exception: " + uee.toString());
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
     con.setRequestProperty("Key", getApiKey());
     con.setRequestProperty("Sign",
     Hex.encodeHexString(mac.doFinal(postData.getBytes("UTF-8"))));

     con.setDoOutput(true);
     try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
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

     public boolean setFundsAmount() {

     JSONObject jsonObjBteTemp;
     try {
     JSONObject jsonObjBtce = callMethod("getInfo", null);
     jsonObjBteTemp =
     (JSONObject) ((JSONObject) jsonObjBtce.get("return"))
     .get("funds");
     btcAmount = Double.parseDouble(jsonObjBteTemp.get("btc").toString());
     usdAmount = Double.parseDouble(jsonObjBteTemp.get("usd").toString());
     return true;
     } catch (Exception e) {
     LoggerUtils.writeAndOut("btce/errs.txt",
     "SetFundsAmount Btce " + e.toString());
     return false;
     }
     }

     public boolean setFundsAmount(JSONObject jsonObjBtce) {

     JSONObject jsonObjBteTemp;
     try {
     jsonObjBteTemp =
     (JSONObject) ((JSONObject) jsonObjBtce.get("return"))
     .get("funds");
     btcAmount = Double.parseDouble(jsonObjBteTemp.get("btc").toString());
     usdAmount = Double.parseDouble(jsonObjBteTemp.get("usd").toString());
     return true;
     } catch (Exception e) {
     LoggerUtils.writeAndOut("btce/errs.txt",
     "SetFundsAmount Btce " + e.toString());
     return false;
     }
     }

     public boolean tradeBuyMargin(double diffBtc) {

     JSONObject tradeRes;
     try {
     JSONObject jo = callFunc("btc_usd", "depth");
     depthPrice =
     ((JSONArray) ((JSONArray) jo.get("asks")).get(0)).get(0)
     .toString();
     argumentsBuy.put("rate", depthPrice);
     argumentsBuy.put("amount", String.valueOf(Math.abs(Utils.round(diffBtc, 4))));
     tradeRes = callMethod("Trade", argumentsBuy);
     LoggerUtils.writeAndOut("btce/trades.txt", tradeRes.toString());
     } catch (Exception e) {
     LoggerUtils.writeAndOut("btce/errs.txt", "tradeBuy " + e.toString());
     return false;
     }
     if ("1".equals(tradeRes.get("success").toString())) {
     lastOrderId =
     Integer.parseInt(((JSONObject) tradeRes.get("return")).get(
     "order_id").toString());
     remains =
     Double.parseDouble(((JSONObject) tradeRes.get("return")).get(
     "remains").toString());
     lastTrade = TradeTypes.Buy;
     setFundsAmount(tradeRes);
     return true;
     } else {
     return false;
     }
     }

     public boolean tradeSellMargin(double diffBtc) {

     JSONObject tradeRes;
     try {
     JSONObject jo = callFunc("btc_usd", "depth");
     depthPrice =
     ((JSONArray) ((JSONArray) jo.get("bids")).get(0)).get(0)
     .toString();
     argumentsSell.put("rate", depthPrice);
     argumentsSell.put("amount", String.valueOf(Math.abs(Utils.round(diffBtc, 4))));
     tradeRes = callMethod("Trade", argumentsSell);
     LoggerUtils.writeAndOut("btce/trades.txt", tradeRes.toString());
     } catch (Exception e) {
     LoggerUtils.writeAndOut("btce/errs.txt", "tradeSell " + e.toString());
     return false;
     }
     if ("1".equals(tradeRes.get("success").toString())) {
     lastOrderId =
     Integer.parseInt(((JSONObject) tradeRes.get("return")).get(
     "order_id").toString());
     remains =
     Double.parseDouble(((JSONObject) tradeRes.get("return")).get(
     "remains").toString());
     lastTrade = TradeTypes.Sell;
     setFundsAmount(tradeRes);
     return true;
     } else {
     return false;
     }
     }

     public boolean cancelLastOrder() {

     if (lastOrderId < 1) {
     return false;
     }
     JSONObject jo;
     argumentsCancel.put("order_id", String.valueOf(lastOrderId));
     try {
     jo = callMethod("CancelOrder", argumentsCancel);
     } catch (Exception e) {
     LoggerUtils.writeAndOut("btce/errs.txt", "cancelOrder " + e.toString());
     return false;
     }
     LoggerUtils.writeAndOut("btce/trades.txt", jo.toString());
     if ("1".equals(jo.get("success").toString())) {
     lastOrderId = 0;
     setFundsAmount(jo);
     return true;
     } else {
     return false;
     }
     }

     public OrderInfo getOrderInfo() {

     return null;
     }

     @Override
     public double getFee() {

     return 0.002;
     }

     public double getRemains() {

     return remains;
     }

     public void setRemains(double r) {

     remains = r;
     }

     public TradeTypes getLastTrade() {

     return lastTrade;
     }

     public String getLastDepthPrice() {

     return depthPrice;
     }

     public int getLastOrderId() {

     return lastOrderId;
     }*/
}
