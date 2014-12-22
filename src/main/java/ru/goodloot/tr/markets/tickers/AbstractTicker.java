/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.tickers;

import ru.goodloot.tr.markets.MarketInfoReceiver;
import ru.goodloot.tr.utils.AbstractUtils;
import ru.goodloot.tr.utils.Logger;

/**
 * 
 * @author lol
 */
public abstract class AbstractTicker extends AbstractUtils implements
        MarketInfoReceiver {

    private final static Logger logger = new Logger(AbstractTicker.class);

    private final static double DEFAULT_NOT_TRUSTED_PRICE_CHANGE = 0.03;

    private double tickerBuy = 0;

    private double tickerSell = 0;

    private volatile boolean changed;

    private volatile boolean correct;

    protected double candidateBuy = 0;

    protected double candidateSell = 0;

    public void getInfo() {

        setCandidates();

        correct = checkCorrect();

        if (correct) {

            if (checkPriceChanged()) {

                synchronized (this) {

                    tickerBuy = candidateBuy;
                    tickerSell = candidateSell;
                }
                // Удостоверяемся, что цена поменялась
                changed = true;
            } else {
                changed = false;
            }
        } else {
            logger.writeAndOut("errs.txt", getClass().getSimpleName()
                            + " notTrustedPriceChange", candidateBuy, candidateSell);
        }

    }

    abstract protected void setCandidates();

    /**
     * @return true - цена поменялась
     */
    private boolean checkPriceChanged() {
        return candidateBuy != tickerBuy || candidateSell != tickerSell;
    }

    private boolean checkCorrect() {

        // Возможно для первой итерации надо тоже сделать какую-то проверку
        boolean firstIteration = tickerBuy == 0 && tickerSell == 0;

        if (firstIteration) {
            return true;
        }

        boolean isTrusted = isTrustedPriceChange();

        return isTrusted;
    }

    protected boolean isTrustedPriceChange() {
        return !(Math.abs((candidateBuy + candidateSell - tickerBuy - tickerSell)
                / (tickerBuy + tickerSell)) > getTrustedPriceChange()
                || candidateBuy < candidateSell);
    }

    protected double getTrustedPriceChange() {
        return DEFAULT_NOT_TRUSTED_PRICE_CHANGE;
    }

    synchronized public double getTickerBuy() {
        return tickerBuy;
    }

    synchronized public double getTickerSell() {
        return tickerSell;
    }

    public boolean isCorrect() {
        return correct;
    }

    public boolean isChanged() {
        return changed;
    }
}
