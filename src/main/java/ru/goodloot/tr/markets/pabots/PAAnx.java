package ru.goodloot.tr.markets.pabots;

import ru.goodloot.tr.TickerThread;
import ru.goodloot.tr.markets.Anx;
import ru.goodloot.tr.objects.OrderInfo;
import ru.goodloot.tr.utils.Logger;

/**
 * @author Artur M.
 * @created Oct 5, 2014
 * 
 * @Description ...
 */
public class PAAnx extends PAExchange {

    public PAAnx(String confName, TickerThread master, TickerThread slave) {

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

        exchange = new Anx(secret, key);

        return true;
    }

    @Override
    protected OrderInfo updateOrderInfo(OrderInfo info) {
        return exchange.getOrderInfo();
    }

    @Override
    public String getFolder() {

        return "anx" + getMasterName();
    }

    @Override
    protected String getApiConfName() {

        return "anx.conf";
    }

    @Override
    public double getUsdCource() {
        return Anx.HKD_USD;
    }
}
