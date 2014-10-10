/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.tickers;

import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.Bitstamp;

/**
 * 
 * @author lol
 */
public class BitstampTicker extends AbstractTicker {

    @Override
    protected void setNewPrices() {

        JSONObject jsonObjBitstamp = Bitstamp.callFunc("ticker");

        tickerBuy = Double.parseDouble((jsonObjBitstamp.get("ask")).toString());
        tickerSell = Double.parseDouble((jsonObjBitstamp.get("bid")).toString());
    }
}
