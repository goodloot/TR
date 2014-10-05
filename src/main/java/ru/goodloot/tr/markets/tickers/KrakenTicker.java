/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.goodloot.tr.markets.tickers;

import ru.goodloot.tr.utils.LoggerUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.Kraken;

/**
 * 
 * @author lol
 */
public class KrakenTicker extends AbstractTicker {

    private double prevTickerBuy = 0;

    private double prevTickerSell = 0;

    public final static double NOT_TRUSTED_PRICE_CHANGE = 0.03;

    @Override
    public int setTicker() {

        JSONObject jsonKraken =
                (JSONObject) Kraken.callFunc("XBTEUR", "Ticker");

        prevTickerBuy = tickerBuy;
        prevTickerSell = tickerSell;

        JSONObject joTemp =
                (JSONObject) ((JSONObject) jsonKraken.get("result"))
                        .get("XXBTZEUR");
        tickerBuy =
                Double.parseDouble(((JSONArray) joTemp.get("a")).get(0)
                        .toString());
        tickerSell =
                Double.parseDouble(((JSONArray) joTemp.get("b")).get(0)
                        .toString());
        if (prevTickerBuy == -1 && prevTickerSell == -1) {

            return 1;
        } else {

            if (Math.abs((tickerBuy + tickerSell - prevTickerBuy - prevTickerSell)
                    / (prevTickerBuy + prevTickerSell)) > NOT_TRUSTED_PRICE_CHANGE
                    || tickerBuy < tickerSell) {

                LoggerUtils.writeAndOut("Kraken/errs.txt",
                        "Kraken notTrustedPriceChange:\t" + tickerBuy + "\t"
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
