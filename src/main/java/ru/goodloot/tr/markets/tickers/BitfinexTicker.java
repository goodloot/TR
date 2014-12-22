/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.tickers;

import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.exchange.Bitfinex;

/**
 * 
 * @author lol
 */
public class BitfinexTicker extends AbstractTicker {

    @Override
    protected void setCandidates() {

        JSONObject json = (JSONObject) Bitfinex.callFunc("btcusd", "ticker");

        candidateBuy = Double.parseDouble(json.get("ask").toString());
        candidateSell = Double.parseDouble(json.get("bid").toString());
    }
}
