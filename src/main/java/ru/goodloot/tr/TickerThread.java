/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr;

import ru.goodloot.tr.markets.tickers.AbstractTicker;
import ru.goodloot.tr.utils.Logger;
import ru.goodloot.tr.utils.Utils;

/**
 * 
 * @author lol
 */
public class TickerThread implements Runnable {

    private static final Logger logger = new Logger(TickerThread.class);

    private final int period;

    private volatile boolean runned = false;

    private volatile boolean correct;

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
                // 1 - trusted price
                correct = ticker.setTicker() == 1;
            } catch (RuntimeException e) {
                correct = false;
                logger.out("Exception setting ticker "
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

    public boolean isCorrect() {
        return correct;
    }

    public AbstractTicker getTicker() {
        return ticker;
    }
}
