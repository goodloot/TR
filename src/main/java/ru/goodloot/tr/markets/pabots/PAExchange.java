package ru.goodloot.tr.markets.pabots;

import ru.goodloot.tr.TickerThread;
import ru.goodloot.tr.enums.TradeTypes;
import ru.goodloot.tr.exceptions.AnxExchangeException;
import ru.goodloot.tr.objects.OrderInfo;
import ru.goodloot.tr.utils.Utils;

/**
 * @author Artur M.
 * @created Oct 5, 2014
 * 
 * @Description ...
 */
public abstract class PAExchange extends AbstractPABot {

    private int orderFlag = 0;

    protected String strTradeLog = "";

    private TradeTypes lastTrade;

    public static final double MIN_DIFF_BTC = 0.015;

    public static final int WAIT_TIMER_MS = 200;

    protected PAExchange(String confName, TickerThread master, TickerThread slave) {
        super(confName, master, slave);
    }

    @Override
    public void doit() {

        long i = 0;

        exchange.setFundsAmount();

        logger.writeAndOut("tradesUser.txt", "Runned", kMin, kMax, delta, readLastsRatio,
                        lastSellRatio, lastBuyRatio);

        while (true) {

            i++;
            Utils.sleep(WAIT_TIMER_MS);

            try {

                if (orderFlag == 1) {
                    processExistOrder();
                }

                if (slave.isCorrect() && master.isCorrect()) {
                    processTrading();
                }

            } catch (AnxExchangeException e) {
                logger.out("Anx exception occurs", e);
                e.printStackTrace();
            }

            if (i % 600 == 1) {
                logger.out(price, prevRatio, ratio, exchange.getBtcAmount(),
                                exchange.getUsdAmount());
            }

            if (i % 5 == 1 && isWriteInLog()) {

                logger.writeWithoutDate("logNew.txt", Utils.getNonce(), price);
                // Записываем предыдущую цену чтобы знать, поменялась ли она
                prevPrice.setPrice(price);
            }
        }
    }

    private void processExistOrder() {

        OrderInfo info = exchange.getOrderInfo();

        processExistOrderSpecifics(info);

        orderFlag = 0;

        setLastTrade();
    }

    /**
     * Эту реализацию лучше переопередлять в предках
     * 
     * @param info
     *        информация об ордере
     */
    protected void processExistOrderSpecifics(OrderInfo info) {

        /**
         * TODO: Доработать надо концепцию
         */
        if (info.isOrderComplete()) {

            strTradeLog = "Complete  " + strTradeLog;
        } else {
            if (exchange.cancelLastOrder()) {

                strTradeLog = "Cancelled " + strTradeLog;
            } else {

                strTradeLog = "NOT Cancelled " + strTradeLog;
            }
        }

        exchange.setFundsAmount();

        setRatioFromReal();

        logger.writeAndOut("tradesUser.txt", strTradeLog);
    }

    protected final void setRatioFromReal() {
        ratio =
                        getRealRatio(exchange.getBtcAmount()
                                        / exchange.getTotalInBtc(price.getSlaveBuy()));
    }

    protected OrderInfo updateOrderInfo(OrderInfo info) {
        return info;
    }

    private void setLastTrade() {

        if (lastTrade == TradeTypes.Buy) {

            lastBuyRatio = ratio;
            logger.write("buy.txt", ratio);

        } else if (lastTrade == TradeTypes.Sell) {

            lastSellRatio = ratio;
            logger.write("sell.txt", ratio);
        }
    }

    private void processTrading() {

        double masterBuy = master.getTicker().getTickerBuy();
        double masterSell = master.getTicker().getTickerSell();
        double slaveBuy = slave.getTicker().getTickerBuy() * getUsdCource();
        double slaveSell = slave.getTicker().getTickerSell() * getUsdCource();

        price.setMasterBuy(masterBuy);
        price.setMasterSell(masterSell);
        price.setSlaveBuy(slaveBuy);
        price.setSlaveSell(slaveSell);

        double totalInBtc = exchange.getTotalInBtc(slaveBuy);

        ratio = Math.log(masterSell / slaveBuy);
        expectedBtc = totalInBtc * getExpectedBtc(ratio);
        diffBtc = expectedBtc - exchange.getBtcAmount();

        if (Math.abs(ratio - lastSellRatio) > delta && diffBtc > MIN_DIFF_BTC) {

            logger.out("Buy sign", price, prevRatio, ratio, exchange.getBtcAmount(),
                            exchange.getUsdAmount());

            if (exchange.tradeBuyMargin(diffBtc)) {

                lastTrade = TradeTypes.Buy;
                orderFlag = 1;

                strTradeLog =
                                logger.getFullStrWithoutDate("Buy",
                                                exchange.getDepthPrice(), diffBtc,
                                                masterSell, slaveBuy, ratio);
            }

        } else {

            prevRatio = ratio;
            ratio = Math.log(masterBuy / slaveSell);
            expectedBtc = totalInBtc * getExpectedBtc(ratio);
            diffBtc = expectedBtc - exchange.getBtcAmount();

            if (Math.abs(lastBuyRatio - ratio) > delta && diffBtc < -MIN_DIFF_BTC) {

                logger.out("Sell sign", price, prevRatio, ratio, exchange.getBtcAmount(),
                                exchange.getUsdAmount());

                if (exchange.tradeSellMargin(diffBtc)) {

                    lastTrade = TradeTypes.Sell;
                    orderFlag = 1;

                    strTradeLog =
                                    logger.getFullStrWithoutDate("Sell",
                                                    exchange.getDepthPrice(), diffBtc,
                                                    masterBuy, slaveSell, ratio);
                }
            }
        }
    }

    public double getUsdCource() {
        return exchange.getUsdCource();
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
}
