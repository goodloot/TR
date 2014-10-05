/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.goodloot.tr.markets.pabots;

import ru.goodloot.tr.enums.TradeTypes;

/**
 * 
 * @author lol
 */
public class PABtceStamp {

    /*
    Btce btce;

    Logger logger = new Logger(getFolder());

    long timer = 0;

    final static int WAIT_DURATION = 10000;

    double totalInBtc = 0;

    double prevBtcAmount = 0;

    public static final double MIN_DIFF_BTC_BTCE = 0.015;

    public static final int WAIT_TIMER_MS_BITSTAMP = 100;

    @Override
    public boolean init() {

        if (super.init()) {
            btce = new Btce(secret, key);
            return true;
        }
        return false;
    }

    @Override
    public void doit() {

        double lastOrderId = btce.getLastOrderId();
        if (lastOrderId != 0) {
            if (!btce.cancelOrder()) {
                logger.writeAndOut("errs.txt", "Can't cancel order!");
            } else {
                logger.writeAndOut("errs.txt", "Order canceled!");
            }
            btce.setRemains(0);
            btce.setFundsAmount();
            double realRatio =
                    getRealRatio(btce.btcAmount
                            / btce.getTotalInBtc(slaveTickerBuy));
            if (btce.getLastTrade().equals(TradeTypes.Sell)) {
                lastSellRatio = realRatio;
                logger.write("sell.txt", realRatio);
                logger.writeAndOut("tradesUser.txt", "Real Sell",
                        btce.getLastDepthPrice(),
                        (btce.btcAmount - prevBtcAmount), masterTickerBuy,
                        slaveTickerSell, ratio);
            } else if (btce.getLastTrade().equals(TradeTypes.Buy)) {
                lastBuyRatio = realRatio;
                logger.write("buy.txt", realRatio);
                logger.writeAndOut("tradesUser.txt", "Real Buy",
                        btce.getLastDepthPrice(),
                        (btce.btcAmount - prevBtcAmount), masterTickerSell,
                        slaveTickerBuy, ratio);
            }
        }
    }*/
}
