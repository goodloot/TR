/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.markets.pabots;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import ru.goodloot.tr.TickerThread;
import ru.goodloot.tr.markets.TradableExchange;
import ru.goodloot.tr.utils.LoggerUtils;

/**
 * 
 * @author lol
 */
public abstract class AbstractPABot<T extends TradableExchange> implements Runnable {

    public T tradeExchange;

    protected double ratio;

    protected double prevRatio;

    protected double lastSellRatio;

    protected double lastBuyRatio;

    protected double expectedBtc;

    protected double diffBtc;

    protected double masterTickerBuy;

    protected double masterTickerSell;

    protected double slaveTickerBuy;

    protected double slaveTickerSell;

    protected double prevMasterTickerBuy;

    protected double prevMasterTickerSell;

    protected double prevSlaveTickerBuy;

    protected double prevSlaveTickerSell;

    protected double kMin;

    protected double kMax;

    protected double delta;

    protected boolean readLast;

    protected Thread t;

    protected String key;

    protected String secret;

    protected TradableExchange exchange;

    protected TickerThread master;

    protected TickerThread slave;

    protected String confName;

    abstract public void doit();

    abstract public String getFolder();

    abstract protected String getApiConfName();

    protected boolean init() {

        setKeySecret();

        String str = LoggerUtils.readLast(confName);
        String args[] = str.split(" ");

        kMin = Double.valueOf(args[0]);
        kMax = Double.valueOf(args[1]);
        delta = Double.valueOf(args[2]);
        readLast = Boolean.parseBoolean(args[3]);

        if (readLast) {
            initLastsRatio();
        } else {

            lastSellRatio = Double.parseDouble(args[4]);
            lastBuyRatio = Double.parseDouble(args[5]);
        }

        if (!master.isRunned()) {
            master.doit();
        }

        if (!slave.isRunned()) {
            slave.doit();
        }

        return true;
    }

    private void setKeySecret() {

        try (BufferedReader f = new BufferedReader(new FileReader(getApiConfName()))) {

            String str = f.readLine();
            String[] args = str.split(" ");

            key = args[0];
            secret = args[1];

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initLastsRatio() {

        String str = LoggerUtils.readLast(getFolder() + "/buy.txt");

        lastBuyRatio = Double.parseDouble(str.substring(str.lastIndexOf('\t')));

        str = LoggerUtils.readLast(getFolder() + "/sell.txt");

        lastSellRatio = Double.parseDouble(str.substring(str.lastIndexOf('\t')));
    }

    protected boolean isWriteInLog() {

        return !(masterTickerBuy == prevMasterTickerBuy
                        && masterTickerSell == prevMasterTickerSell
                        && slaveTickerBuy == prevSlaveTickerBuy && slaveTickerSell == prevSlaveTickerSell);
    }

    @Override
    public void run() {

        doit();
    }

    public void threadStart() {

        t = new Thread(this);
        t.start();
    }
}
