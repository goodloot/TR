package ru.goodloot.tr.markets.pabots;

import ru.goodloot.tr.enums.TradeTypes;
import ru.goodloot.tr.objects.OrderInfo;
import ru.goodloot.tr.utils.Logger;
import ru.goodloot.tr.utils.Utils;

/**
 * @author Artur M.
 * @created Oct 5, 2014
 * 
 * @Description ...
 */
public abstract class PAExchange extends AbstractPABot {

    private Logger logger;

    private int orderFlag = 0;

    private String strTradeLog = "";

    private TradeTypes lastTrade;

    public static final double MIN_DIFF_BTC = 0.015;

    public static final int WAIT_TIMER_MS = 200;

    @Override
    public void doit() {

        long i = 0;

        if (!exchange.setFundsAmount()) {
            logger.writeAndOut("errs.txt", "Can't setFundsAmount!");
            return;
        }

        logger.writeAndOut("tradesUser.txt", "Runned", kMin, kMax, delta, readLast,
                        lastSellRatio, lastBuyRatio);

        while (true) {

            i++;
            Utils.sleep(WAIT_TIMER_MS);

            if (orderFlag == 1) {
                processExistOrder();
            }

            if (slave.isCorrect() && master.isCorrect()) {
                processTrading();
            }

            if (i % 600 == 1) {
                logger.out(masterTickerBuy, masterTickerSell, slaveTickerBuy,
                                slaveTickerSell, Utils.round(prevRatio, 6),
                                Utils.round(ratio, 6), exchange.getBtcAmount(),
                                exchange.getUsdAmount());
            }

            if (i % 5 == 1 && isWriteInLog()) {

                logger.write("logNew.txt", Utils.getNonce(), masterTickerBuy,
                                masterTickerSell, slaveTickerBuy, slaveTickerSell);

                prevMasterTickerBuy = masterTickerBuy;
                prevMasterTickerSell = masterTickerSell;
                prevSlaveTickerBuy = slaveTickerBuy;
                prevSlaveTickerSell = slaveTickerSell;
            }
        }
    }

    private boolean processExistOrder() {

        OrderInfo info = exchange.getOrderInfo();

        if (info.isOrderComplete()) {

            logger.writeAndOut("tradesUser.txt", "Real " + strTradeLog,
                            info.getVolExecuted());

        } else {

            ratio =
                            getRealRatio(exchange.getBtcAmount()
                                            / exchange.getTotalInBtc(slaveTickerBuy));

            if (exchange.cancelLastOrder()) {

                logger.writeAndOut("tradesUser.txt", "Cancelled " + strTradeLog,
                                info.getVolExecuted(), ratio);

            } else {

                logger.writeAndOut("tradesUser.txt", "NOT Cancelled " + strTradeLog,
                                info.getVolExecuted(), ratio);

            }
        }

        orderFlag = 0;

        exchange.setFundsAmount();

        if (lastTrade == TradeTypes.Buy) {

            lastBuyRatio = ratio;
            logger.write("buy.txt", ratio);

        } else if (lastTrade == TradeTypes.Sell) {

            lastSellRatio = ratio;
            logger.write("sell.txt", ratio);
        }

        return true;
    }

    private void processTrading() {

        masterTickerBuy = master.getTicker().getTickerBuy();
        masterTickerSell = master.getTicker().getTickerSell();

        slaveTickerBuy = slave.getTicker().getTickerBuy() * getUsdCource();
        slaveTickerSell = slave.getTicker().getTickerSell() * getUsdCource();

        double totalInBtc = exchange.getTotalInBtc(slaveTickerBuy);

        ratio = Math.log(masterTickerSell / slaveTickerBuy);
        expectedBtc = totalInBtc * getExpectedBtc(ratio);
        diffBtc = expectedBtc - exchange.getBtcAmount();

        if (Math.abs(ratio - lastSellRatio) > delta && diffBtc > MIN_DIFF_BTC) {

            if (exchange.tradeBuyMargin(diffBtc)) {

                lastTrade = TradeTypes.Buy;
                orderFlag = 1;

                strTradeLog =
                                logger.getFullStrWithoutDate("Buy",
                                                exchange.getDepthPrice(), diffBtc,
                                                masterTickerSell, slaveTickerBuy, ratio);
            }

        } else {

            prevRatio = ratio;
            ratio = Math.log(masterTickerBuy / slaveTickerSell);
            expectedBtc = totalInBtc * getExpectedBtc(ratio);
            diffBtc = expectedBtc - exchange.getBtcAmount();

            if (Math.abs(lastBuyRatio - ratio) > delta && diffBtc < -MIN_DIFF_BTC) {

                if (exchange.tradeSellMargin(diffBtc)) {

                    lastTrade = TradeTypes.Sell;
                    orderFlag = 1;

                    strTradeLog =
                                    logger.getFullStrWithoutDate("Sell",
                                                    exchange.getDepthPrice(), diffBtc,
                                                    masterTickerBuy, slaveTickerSell,
                                                    ratio);
                }
            }
        }
    }

    public double getUsdCource() {
        return 1;
    }

    protected double getExpectedBtc(double k) {

        if (k <= kMin) {
            return 0;
        } else if (k >= kMax) {
            return 1;
        } else {
            return (k - kMin) / (kMax - kMin);
        }
    }

    protected double getRealRatio(double realBtc) {

        return (kMax - kMin) * expectedBtc + kMin;
    }

    protected String getMasterName() {

        String s = master.getClass().getSimpleName();
        return s.substring(0, s.indexOf("Ticker"));
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
