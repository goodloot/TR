/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.anx;

import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.tickers.AbstractTicker;
import ru.goodloot.tr.utils.LoggerUtils;

/**
 * 
 * @author lol
 */
public class AnxTicker extends AbstractTicker {

    private double prevTickerBuy = 0;

    private double prevTickerSell = 0;

    public final static double NOT_TRUSTED_PRICE_CHANGE = 0.03;

    @Override
    public int setTicker() {

        prevTickerBuy = tickerBuy;
        prevTickerSell = tickerSell;

        JSONObject json = (JSONObject) Anx.callFunc("BTCHKD", "ticker");
        JSONObject joTemp = (JSONObject) json.get("data");

        tickerBuy =
                        Double.parseDouble(((JSONObject) joTemp.get("sell")).get("value")
                                        .toString());
        tickerSell =
                        Double.parseDouble(((JSONObject) joTemp.get("buy")).get("value")
                                        .toString());

        if (prevTickerBuy == -1 && prevTickerSell == -1) {
            return 1;
        } else {
            if (Math.abs((tickerBuy + tickerSell - prevTickerBuy - prevTickerSell)
                            / (prevTickerBuy + prevTickerSell)) > NOT_TRUSTED_PRICE_CHANGE
                            || tickerBuy < tickerSell) {

                LoggerUtils.writeAndOut("anx/errs.txt", "Anx notTrustedPriceChange:\t"
                                + tickerBuy + "\t" + tickerSell);

                tickerBuy = prevTickerBuy;
                tickerSell = prevTickerSell;
                return 0;
            } else {
                return 1;
            }
        }
    }
}
