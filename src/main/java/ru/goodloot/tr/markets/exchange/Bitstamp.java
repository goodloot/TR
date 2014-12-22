package ru.goodloot.tr.markets.exchange;

import org.json.simple.JSONObject;

public class Bitstamp extends Exchange {

    static public JSONObject callFunc(String func) {

        String url = "https://www.bitstamp.net/api/" + func + "/";

        return (JSONObject) sendGet(url);
    }
}
