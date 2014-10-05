/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.pabots;

import ru.goodloot.tr.TickerThread;
import ru.goodloot.tr.markets.Kraken;
import ru.goodloot.tr.utils.Logger;

/**
 * 
 * @author lol
 */
public class PAKraken extends PAExchange {

    public PAKraken(String confName, TickerThread master, TickerThread slave) {

        this.confName = confName;
        this.master = master;
        this.slave = slave;

        init();
    }

    @Override
    protected boolean init() {

        Logger logger = new Logger(getFolder());
        setLogger(logger);

        super.init();

        exchange = new Kraken(secret, key);

        return true;
    }

    @Override
    public String getFolder() {

        return "kraken" + getMasterName();
    }

    @Override
    protected String getApiConfName() {

        return "kraken.conf";
    }

    public double getUsdCource() {
        return Kraken.USD_EUR;
    }
}
