/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr;

import ru.goodloot.tr.markets.tickers.AbstractTicker;
import ru.goodloot.tr.utils.LoggerUtils;
import ru.goodloot.tr.utils.Utils;

/**
 * 
 * @author lol
 */
public class TickerThread implements Runnable {

    private final int period;

    private volatile boolean runned = false;

    private final AbstractTicker ticker;

    public TickerThread(AbstractTicker ticker, int period) {
        this.ticker = ticker;
        this.period = period;
    }

    public TickerThread(AbstractTicker ticker) {
        this(ticker, 1000);
    }

    @Override
    public void run() {

        while (true) {

            long timePassed = System.currentTimeMillis();

            try {

                ticker.setTicker();

            } catch (RuntimeException e) {
                LoggerUtils.out("Exception setting ticker "
                                + ticker.getClass().getSimpleName());
                e.printStackTrace();
            }

            timePassed = System.currentTimeMillis() - timePassed;

            if (timePassed < period) {
                Utils.sleep(period - timePassed);
            }
        }
    }

    public void startThread() {

        if (!runned) {
            runned = true;
            new Thread(this).start();
        } else {
            throw new RuntimeException("That ticker thread already started "
                            + ticker.getClass().getSimpleName());
        }
    }

    public boolean isRunned() {
        return runned;
    }

    public AbstractTicker getTicker() {
        return ticker;
    }
}
