/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.tickers;

import ru.goodloot.tr.utils.AbstractUtils;
import ru.goodloot.tr.utils.Logger;

/**
 * 
 * @author lol
 */
public abstract class AbstractTicker extends AbstractUtils {

    private final static Logger logger = new Logger(BitfinexTicker.class);

    private final static double DEFAULT_NOT_TRUSTED_PRICE_CHANGE = 0.03;

    protected volatile double tickerBuy = -1;

    protected volatile double tickerSell = -1;

    protected double prevTickerBuy = 0;

    protected double prevTickerSell = 0;

    public int setTicker() {

        prevTickerBuy = tickerBuy;
        prevTickerSell = tickerSell;

        setNewPrices();

        if (prevTickerBuy == -1 && prevTickerSell == -1) {
            /**
             * В этом случае возможно тоже надо проверять тикер на вшивость
             */
            return 1;
        } else {
            if (isBadPriceChange()) {

                logger.writeAndOut("errs.txt", getClass().getSimpleName()
                                + " notTrustedPriceChange", tickerBuy, tickerSell);

                tickerBuy = prevTickerBuy;
                tickerSell = prevTickerSell;
                return 0;
            } else {
                return 1;
            }
        }
    }

    abstract protected void setNewPrices();

    /**
     * @return true - not trusted price change, false - ok
     */
    protected boolean isBadPriceChange() {
        return Math.abs((tickerBuy + tickerSell - prevTickerBuy - prevTickerSell)
                        / (prevTickerBuy + prevTickerSell)) > DEFAULT_NOT_TRUSTED_PRICE_CHANGE
                        || tickerBuy < tickerSell;
    }

    public double getTickerBuy() {
        return tickerBuy;
    }

    public double getTickerSell() {
        return tickerSell;
    }
}
