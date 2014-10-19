/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.tickers;

import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.Anx;

/**
 * 
 * @author lol
 */
public class AnxTicker extends AbstractTicker {

    @Override
    protected void setCandidates() {

        JSONObject joTemp = jsonValue(Anx.callFunc("BTCUSD", "ticker"), "data");

        candidateBuy = Double.parseDouble(value(jsonValue(joTemp, "sell"), "value"));
        candidateSell = Double.parseDouble(value(jsonValue(joTemp, "buy"), "value"));
    }

    @Override
    protected double getTrustedPriceChange() {
        return 0.04;
    }
}
