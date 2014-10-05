/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.goodloot.tr.markets.tickers;

import ru.goodloot.tr.utils.LoggerUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.Btce;

/**
 * 
 * @author lol
 */
public class BtceTicker extends AbstractTicker {

    private double prevTickerBuy = 0;

    private double prevTickerSell = 0;

    private final static double NOT_TRUSTED_PRICE_CHANGE = 0.03;

    public int setTicker(boolean oldTicker) {

        JSONObject jsonObjBtce = Btce.callFunc("btc_usd", "ticker");
        prevTickerBuy = tickerBuy;
        prevTickerSell = tickerSell;
        tickerBuy =
                Double.parseDouble(((JSONObject) jsonObjBtce.get("ticker"))
                        .get("buy").toString());
        tickerSell =
                Double.parseDouble(((JSONObject) jsonObjBtce.get("ticker"))
                        .get("sell").toString());
        if (prevTickerBuy == -1 && prevTickerSell == -1) {
            return 1;
        } else {
            if (Math.abs((tickerBuy + tickerSell - prevTickerBuy - prevTickerSell)
                    / (prevTickerBuy + prevTickerSell)) > NOT_TRUSTED_PRICE_CHANGE
                    || tickerBuy < tickerSell) {
                LoggerUtils.writeAndOut("btce/errs.txt",
                        "Btce notTrustedPriceChange:\t" + tickerBuy + "\t"
                                + tickerSell);
                tickerBuy = prevTickerBuy;
                tickerSell = prevTickerSell;
                return 0;
            } else {
                return 1;
            }
        }
    }

    @Override
    public int setTicker() {

        JSONObject depth = Btce.callFunc("btc_usd", "depth");
        prevTickerBuy = tickerBuy;
        prevTickerSell = tickerSell;
        tickerBuy =
                Double.parseDouble(((JSONArray) ((JSONArray) depth.get("asks"))
                        .get(0)).get(0).toString());
        tickerSell =
                Double.parseDouble(((JSONArray) ((JSONArray) depth.get("bids"))
                        .get(0)).get(0).toString());
        if (prevTickerBuy == -1 && prevTickerSell == -1) {
            return 1;
        } else {
            if (Math.abs((tickerBuy + tickerSell - prevTickerBuy - prevTickerSell)
                    / (prevTickerBuy + prevTickerSell)) > NOT_TRUSTED_PRICE_CHANGE
                    || tickerBuy < tickerSell) {
                LoggerUtils.writeAndOut("btce/errs.txt",
                        "Btce notTrustedPriceChange:\t" + tickerBuy + "\t"
                                + tickerSell);
                tickerBuy = prevTickerBuy;
                tickerSell = prevTickerSell;
                return 0;
            } else {
                return 1;
            }
        }
    }
}
