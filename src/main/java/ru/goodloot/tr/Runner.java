/**
 *
 */
package ru.goodloot.tr;

import ru.goodloot.tr.markets.pabots.PAAnx;
import ru.goodloot.tr.markets.tickers.AnxTicker;
import ru.goodloot.tr.markets.tickers.BitfinexTicker;
import ru.goodloot.tr.markets.tickers.BitstampTicker;
import ru.goodloot.tr.markets.tickers.KrakenTicker;

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

    private TickerThread krakenTicker;

    private TickerThread bitfinexTicker;

    private TickerThread bitstampTicker;

    private TickerThread anxTicker;

    // public void runKrakenBitfinex() {
    //
    // PAKraken paKraken =
    // new PAKraken("krakenBitfinex.conf", getBitfinexTicker(),
    // getKrakenTicker());
    //
    // paKraken.startThread();
    // }
    //
    // public void runKrakenBitstamp() {
    //
    // PAKraken paKraken =
    // new PAKraken("krakenBitstamp.conf", getBitstampTicker(),
    // getKrakenTicker());
    //
    // paKraken.startThread();
    // }
    public void runAnxBitfinex() {

        PAAnx paAnx = new PAAnx("anxBitfinex.conf", getBitfinexTicker(), getAnxTicker());
        paAnx.startThread();
    }

    public TickerThread getBitfinexTicker() {
        if (bitfinexTicker == null) {
            bitfinexTicker = new TickerThread(new BitfinexTicker());
        }
        return bitfinexTicker;
    }

    public TickerThread getKrakenTicker() {
        if (bitstampTicker == null) {
            bitstampTicker = new TickerThread(new BitstampTicker());
        }
        return bitstampTicker;
    }

    public TickerThread getBitstampTicker() {
        if (krakenTicker == null) {
            krakenTicker = new TickerThread(new KrakenTicker());
        }
        return krakenTicker;
    }

    public TickerThread getAnxTicker() {
        if (anxTicker == null) {
            anxTicker = new TickerThread(new AnxTicker());
        }
        return anxTicker;
    }
}
