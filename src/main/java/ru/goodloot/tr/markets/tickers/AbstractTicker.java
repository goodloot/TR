/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.goodloot.tr.markets.tickers;

/**
 * 
 * @author lol
 */
public abstract class AbstractTicker {

    protected volatile double tickerBuy = -1;

    protected volatile double tickerSell = -1;

    public abstract int setTicker();

    // public abstract TickerThread<?> getTickerThread();

    public double getTickerBuy() {

        return tickerBuy;
    }

    public double getTickerSell() {

        return tickerSell;
    }
}
