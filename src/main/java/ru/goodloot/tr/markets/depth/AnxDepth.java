package ru.goodloot.tr.markets.depth;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.goodloot.tr.markets.exchange.Anx;

/**
 * Created by artur on 22.12.14.
 */
public class AnxDepth extends AbstractDepth {

    public void getInfo() {

        super.getInfo();

        JSONObject json = Anx.callFunc("btcusd", "depth/full");
        for (Object obj : value(jsonValue(json, "data"), "asks", JSONArray.class)) {
            ((JSONObject) obj).get("price");
            ((JSONObject) obj).get("amount");
        }
    }
}
