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
    protected void setNewPrices() {

        JSONObject joTemp = jsonValue(Anx.callFunc("BTCHKD", "ticker"), "data");

        tickerBuy = Double.parseDouble(value(jsonValue(joTemp, "sell"), "value"));
        tickerSell = Double.parseDouble(value(jsonValue(joTemp, "buy"), "value"));
    }
}
