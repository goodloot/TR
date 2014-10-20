/**
 *
 */
package ru.goodloot.tr;

import ru.goodloot.tr.markets.pabots.PAAnx;
import ru.goodloot.tr.markets.tickers.AnxTicker;
import ru.goodloot.tr.markets.tickers.BitfinexTicker;

/**
 * Runner class
 * 
 * Description ...
 * 
 * 
 * @author Artur M.
 * @created 20 авг. 2014 г.
 * 
 */
public class Runner {

    private final TickerThread bitfinexTicker = new TickerThread(new BitfinexTicker());

    private final TickerThread anxTicker = new TickerThread(new AnxTicker());

    public void runAnxBitfinex() {

        PAAnx paAnx = new PAAnx("anxBitfinex.conf", bitfinexTicker, anxTicker);
        paAnx.startThread();
    }
}
