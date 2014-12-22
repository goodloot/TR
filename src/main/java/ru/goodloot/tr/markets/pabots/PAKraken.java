/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.pabots;

import ru.goodloot.tr.markets.TickerInfo;
import ru.goodloot.tr.markets.exchange.Kraken;

/**
 * @author lol
 */
public class PAKraken extends PAExchange {

    public PAKraken(String confName, TickerInfo master, TickerInfo slave) {

        super(confName, master, slave);

        exchange = new Kraken(getSecret(), getKey());
    }

    public double getUsdCource() {

        return Kraken.EUR_USD;
    }
}
