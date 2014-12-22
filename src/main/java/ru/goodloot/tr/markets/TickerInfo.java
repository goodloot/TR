/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets;

import ru.goodloot.tr.markets.tickers.AbstractTicker;

/**
 * 
 * @author lol
 */
public class TickerInfo extends PeriodicalInfoReceiver {

    public TickerInfo(AbstractTicker ticker) {
        super(ticker, 1000);
    }

    public AbstractTicker getTicker() {
        return (AbstractTicker) marketInfoReceiver;
    }
}
