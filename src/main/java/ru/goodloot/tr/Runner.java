/**
 * 
 */
package ru.goodloot.tr;

import ru.goodloot.tr.markets.pabots.PAKraken;
import ru.goodloot.tr.markets.tickers.BitstampTicker;
import ru.goodloot.tr.markets.tickers.BitfinexTicker;
import ru.goodloot.tr.markets.tickers.KrakenTicker;

/**
 * Runner class
 * 
 * Description ...
 * 
 * 
 * @author Mukhametyanov Artur
 * @created 20 авг. 2014 г.
 * 
 */

public class Runner {

    // private Exchange exchange;
    //
    // private AbstractTicker ticker;

    private TickerThread krakenTicker = new TickerThread(new KrakenTicker());

    private TickerThread bitfinexTicker = new TickerThread(
            new BitfinexTicker());

    private TickerThread bitstampTicker = new TickerThread(
            new BitstampTicker());

    public void runKrakenBitfinex() {

        PAKraken paKraken =
                new PAKraken("krakenBitfinex", bitfinexTicker, krakenTicker);

        paKraken.threadStart();
    }

    public void runKrakenBitstamp() {

        PAKraken paKraken =
                new PAKraken("krakenBitfinex", bitstampTicker, krakenTicker);

        paKraken.threadStart();
    }
}
