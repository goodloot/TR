/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.tickers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.Kraken;

/**
 * 
 * @author lol
 */
public class KrakenTicker extends AbstractTicker {

    @Override
    protected void setNewPrices() {

        JSONObject joTemp =
                        jsonValue(jsonValue(Kraken.callFunc("XBTEUR", "Ticker"), "result"),
                                        "XXBTZEUR");

        tickerBuy = Double.parseDouble(((JSONArray) joTemp.get("a")).get(0).toString());
        tickerSell = Double.parseDouble(((JSONArray) joTemp.get("b")).get(0).toString());
    }
}
