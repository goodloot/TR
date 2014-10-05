package ru.goodloot.tr.markets.hit;

import java.util.Map;
import ru.goodloot.tr.objects.OrderInfo;
import static ru.goodloot.tr.markets.Exchange.sendGet;
import ru.goodloot.tr.markets.TradableExchange;

/**
 * @author Artur M.
 * @created Sep 2, 2014
 * 
 * @Description ...
 */
public class Hitbtc extends TradableExchange {

    public Hitbtc(String secret, String key) {

        super(secret, key);
    }

    static public Object callFunc(String pair, String func) {

        String url = "https://api.kraken.com/0/public/" + func + "?pair=" + pair;
        return sendGet(url);
    }

    protected String sendRequest(String method, Map<String, String> arguments) {

        return null;
    }

    public boolean setFundsAmount() {
        return true;
    }

    public OrderInfo getOrderInfo() {
        return null;
    }

    public boolean cancelLastOrder() {
        return true;
    }

    public boolean tradeBuyMargin(double diffBtc) {
        return true;
    }

    public boolean tradeSellMargin(double diffBtc) {
        return true;
    }

    public double getFee() {
        return 0.001;
    }
}
