/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.tickers;

import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.Bitfinex;

/**
 * 
 * @author lol
 */
public class BitfinexTicker extends AbstractTicker {

    @Override
    protected void setNewPrices() {

        JSONObject json = (JSONObject) Bitfinex.callFunc("btcusd", "ticker");

        tickerBuy = Double.parseDouble(json.get("ask").toString());
        tickerSell = Double.parseDouble(json.get("bid").toString());
    }
}
