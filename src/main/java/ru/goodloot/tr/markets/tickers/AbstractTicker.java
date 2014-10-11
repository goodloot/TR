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

    private volatile double tickerBuy = 0;

    private volatile double tickerSell = 0;

    protected double candidateBuy = 0;

    protected double candidateSell = 0;

    public int setTicker() {

        setCandidates();

        if (isTrustedPriceChange()) {
            synchronized (this) {
                tickerBuy = candidateBuy;
                tickerSell = candidateSell;
            }
            return 1;
        } else {
            logger.writeAndOut("errs.txt", getClass().getSimpleName()
                            + " notTrustedPriceChange", candidateBuy, candidateSell);
            return 0;
        }

    }

    abstract protected void setCandidates();

    /**
     * @return true - not trusted price change, false - ok
     */
    protected boolean isTrustedPriceChange() {

        // Возможно для первой итерации надо тоже сделать какую-то проверку
        boolean firstIteration = tickerBuy == 0 && tickerSell == 0;

        boolean notTrustedChanges =
                        Math.abs((candidateBuy + candidateSell - tickerBuy - tickerSell)
                                        / (tickerBuy + tickerSell)) > DEFAULT_NOT_TRUSTED_PRICE_CHANGE
                                        || candidateBuy < candidateSell;

        return firstIteration || !notTrustedChanges;
    }

    public double getTickerBuy() {
        return tickerBuy;
    }

    public double getTickerSell() {
        return tickerSell;
    }
}
