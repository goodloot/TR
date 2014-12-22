/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.tickers;

import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.exchange.Bitstamp;

/**
 * 
 * @author lol
 */
public class BitstampTicker extends AbstractTicker {

    @Override
    protected void setCandidates() {

        JSONObject jsonObjBitstamp = Bitstamp.callFunc("ticker");

        candidateBuy = Double.parseDouble((jsonObjBitstamp.get("ask")).toString());
        candidateSell = Double.parseDouble((jsonObjBitstamp.get("bid")).toString());
    }
}
