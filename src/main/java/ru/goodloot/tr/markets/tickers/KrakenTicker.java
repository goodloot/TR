/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.tickers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.exchange.Kraken;

/**
 * 
 * @author lol
 */
public class KrakenTicker extends AbstractTicker {

    @Override
    protected void setCandidates() {

        JSONObject joTemp =
                        jsonValue(jsonValue(Kraken.callFunc("XBTEUR", "Ticker"), "result"),
                                        "XXBTZEUR");

        candidateBuy =
                        Double.parseDouble(((JSONArray) joTemp.get("a")).get(0)
                                        .toString());
        candidateSell =
                        Double.parseDouble(((JSONArray) joTemp.get("b")).get(0)
                                        .toString());
    }
}
