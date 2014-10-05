/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.goodloot.tr.markets.tickers;

import ru.goodloot.tr.utils.Logger;
import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.Bitfinex;

/**
 * 
 * @author lol
 */
public class BitfinexTicker extends AbstractTicker {

    private double prevTickerBuy = 0;

    private double prevTickerSell = 0;

    public final static double NOT_TRUSTED_PRICE_CHANGE = 0.03;

    private final static Logger logger = new Logger(BitfinexTicker.class);

    @Override
    public int setTicker() {

        JSONObject json = (JSONObject) Bitfinex.callFunc("btcusd", "ticker");

        prevTickerBuy = tickerBuy;
        prevTickerSell = tickerSell;

        tickerBuy = Double.parseDouble(json.get("ask").toString());
        tickerSell = Double.parseDouble(json.get("bid").toString());

        if (prevTickerBuy == -1 && prevTickerSell == -1) {

            return 1;
        } else {

            if (Math.abs((tickerBuy + tickerSell - prevTickerBuy - prevTickerSell)
                    / (prevTickerBuy + prevTickerSell)) > NOT_TRUSTED_PRICE_CHANGE
                    || tickerBuy < tickerSell) {

                logger.writeAndOut("errs.txt",
                        "Bitfinex notTrustedPriceChange:\t" + tickerBuy + "\t"
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
