/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.goodloot.tr;

import ru.goodloot.tr.markets.tickers.AbstractTicker;
import ru.goodloot.tr.utils.Utils;

/**
 * 
 * @author lol
 */
public class TickerThread implements Runnable {

    private Thread t;

    private int timeWait;

    private boolean runned = false;

    private boolean correct;

    private AbstractTicker ticker;

    public TickerThread(AbstractTicker ticker) {

        timeWait = 1000;
        this.ticker = ticker;
    }

    public TickerThread(AbstractTicker ticker, int period) {

        timeWait = period;
        this.ticker = ticker;
    }

    public void run() {

        while (true) {

            long timePassed = System.currentTimeMillis();

            try {
                // 1 - trusted price
                correct = ticker.setTicker() == 1;
            } catch (RuntimeException e) {

                System.out.println("Exception setting ticker: "
                        + e.getMessage());
                correct = false;
            }

            timePassed = System.currentTimeMillis() - timePassed;

            if (timePassed < timeWait) {
                Utils.sleep(timeWait - timePassed);
            }
        }
    }

    public void doit() {

        t = new Thread(this);
        runned = true;
        t.start();
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
