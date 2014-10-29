/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.pabots;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import ru.goodloot.tr.TickerThread;
import ru.goodloot.tr.markets.TradableExchange;
import ru.goodloot.tr.markets.tickers.AbstractTicker;
import ru.goodloot.tr.objects.PAPriceValue;
import ru.goodloot.tr.utils.Logger;
import ru.goodloot.tr.utils.LoggerUtils;

/**
 * 
 * @author lol
 */
public abstract class AbstractPABot<T extends TradableExchange> implements Runnable {

    protected final Logger logger;

    protected double ratio;

    protected double prevRatio;

    protected double lastSellRatio;

    protected double lastBuyRatio;

    protected double expectedBtc;

    protected double diffBtc;

    protected final PAPriceValue price = new PAPriceValue();

    protected final PAPriceValue prevPrice = new PAPriceValue();

    protected double kMin;

    protected double kMax;

    protected double delta;

    protected boolean readLastsRatio;

    private String key;

    private String secret;

    protected TradableExchange exchange;

    protected final TickerThread master;

    protected final TickerThread slave;

    protected final AbstractTicker masterTicker;

    protected final AbstractTicker slaveTicker;

    private final String confName;

    private final String folder;

    abstract public void doit();

    protected AbstractPABot(String confName, TickerThread master, TickerThread slave) {

        this.confName = confName;
        this.master = master;
        this.slave = slave;

        this.masterTicker = master.getTicker();
        this.slaveTicker = slave.getTicker();

        this.folder = getSlaveName() + getMasterName();
        this.logger = new Logger(this.folder);

        init();
    }

    private void init() {

        setKeySecret();

        LoggerUtils.out("Reading " + confName + " for configurating");

        String str = LoggerUtils.readLast(confName);
        String args[] = str.split(" ");

        kMin = Double.valueOf(args[0]);
        kMax = Double.valueOf(args[1]);
        delta = Double.valueOf(args[2]);
        readLastsRatio = Boolean.parseBoolean(args[3]);

        if (readLastsRatio) {
            initLastsRatio(folder);
        } else {

            lastSellRatio = Double.parseDouble(args[4]);
            lastBuyRatio = Double.parseDouble(args[5]);
        }

        if (!master.isRunned()) {
            master.startThread();
        }

        if (!slave.isRunned()) {
            slave.startThread();
        }
    }

    private void setKeySecret() {

        try (BufferedReader f = new BufferedReader(new FileReader(getApiConfName()))) {

            String str = f.readLine();
            String[] args = str.split(" ");

            key = args[0];
            secret = args[1];

        } catch (IOException e) {
            throw new RuntimeException("Can't read key and secret from "
                            + getApiConfName(), e);
        }
    }

    public void initLastsRatio(String folder) {

        LoggerUtils.out("Start init lasts ratio from " + folder);

        try {
            String str = LoggerUtils.readLast(folder + "/buy.txt");
            lastBuyRatio = Double.parseDouble(str.substring(str.lastIndexOf('\t')));
        } catch (RuntimeException e) {
            LoggerUtils.out("Exception reading buy.txt, set lastBuyRatio = 10", e);
            lastBuyRatio = 10;
        }

        try {
            String str = LoggerUtils.readLast(folder + "/sell.txt");
            lastSellRatio = Double.parseDouble(str.substring(str.lastIndexOf('\t')));
        } catch (RuntimeException e) {
            LoggerUtils.out("Exception reading sell.txt, set lastSellRatio = 10", e);
            lastSellRatio = 10;
        }
    }

    protected final boolean isWriteInLog() {
        return !(price.isSame(prevPrice));
    }

    @Override
    public void run() {
        doit();
    }

    public void startThread() {
        new Thread(this).start();
    }

    private String getApiConfName() {
        return getSlaveName() + ".conf";
    }

    private String getMasterName() {
        return getTickerExchangeName(masterTicker.getClass());
    }

    private String getSlaveName() {
        return getTickerExchangeName(slaveTicker.getClass());
    }

    private <S extends AbstractTicker> String getTickerExchangeName(Class<S> clazz) {
        String s = clazz.getSimpleName();
        return s.substring(0, s.indexOf("Ticker"));
    }

    public String getFolder() {
        return folder;
    }

    public String getConfName() {
        return confName;
    }

    public String getKey() {
        return key;
    }

    public String getSecret() {
        return secret;
    }
}
