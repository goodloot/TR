/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.goodloot.tr.markets.tickers;

import ru.goodloot.tr.utils.LoggerUtils;
import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.Bitstamp;

/**
 * 
 * @author lol
 */
public class BitstampTicker extends AbstractTicker {

    private static final double BuySellTrustedDiff = 0.005;

    private static final double NOT_TRUSTED_PRICE_CHANGE = 0.0225;

    @Override
    public int setTicker() {

        double tempTickerBuy, tempTickerSell;
        JSONObject jsonObjBitstamp = Bitstamp.callFunc("ticker");

        tempTickerBuy =
                Double.parseDouble((jsonObjBitstamp.get("ask")).toString());
        tempTickerSell =
                Double.parseDouble((jsonObjBitstamp.get("bid")).toString());

        if (tickerBuy == -1 && tickerSell == -1) {

            if ((tempTickerBuy > tempTickerSell)
                    && (tempTickerBuy < tempTickerSell * 1.02)) {

                tickerBuy = tempTickerBuy;
                tickerSell = tempTickerSell;

                return 1;
            } else {
                return 0;
            }
        } else {

            if (Math.abs((tempTickerBuy + tempTickerSell - tickerBuy - tickerSell)
                    / (tickerBuy + tickerSell)) > NOT_TRUSTED_PRICE_CHANGE
                    || tempTickerBuy < tempTickerSell
                            * (1 - BuySellTrustedDiff)) {

                LoggerUtils.writeAndOut("log/errs",
                        "Bitstamp notTrustedPriceChange:\t" + tempTickerBuy
                                + "\t" + tempTickerSell);
                return 0;
            } else {

                tickerBuy = tempTickerBuy;
                tickerSell = tempTickerSell;

                return 1;
            }
        }
    }
}
