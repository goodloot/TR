/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.tickers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.Btce;

/**
 * 
 * @author lol
 */
public class BtceTicker extends AbstractTicker {

    protected void setNewPrices(boolean oldTicker) {

        JSONObject jsonObjBtce = Btce.callFunc("btc_usd", "ticker");

        candidateBuy =
                        Double.parseDouble(((JSONObject) jsonObjBtce.get("ticker")).get(
                                        "buy").toString());
        candidateSell =
                        Double.parseDouble(((JSONObject) jsonObjBtce.get("ticker")).get(
                                        "sell").toString());
    }

    @Override
    protected void setCandidates() {

        JSONObject depth = Btce.callFunc("btc_usd", "depth");

        candidateBuy =
                        Double.parseDouble(((JSONArray) ((JSONArray) depth.get("asks"))
                                        .get(0)).get(0).toString());
        candidateSell =
                        Double.parseDouble(((JSONArray) ((JSONArray) depth.get("bids"))
                                        .get(0)).get(0).toString());
    }
}
