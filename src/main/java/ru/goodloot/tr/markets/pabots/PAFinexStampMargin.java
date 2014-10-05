/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.goodloot.tr.markets.pabots;

/**
 * 
 * @author lol
 */
public class PAFinexStampMargin {

    /*
    static private double ratio;

    static private double prevRatio;

    static private double lastSellRatio;

    static private double lastBuyRatio;

    static private double expectedBtc;

    static private double diffBtc;

    static private double stampTickerSell;

    static private double stampTickerBuy;

    static private double bitfinexTickerSell;

    static private double bitfinexTickerBuy;

    static private double remains = 0;

    private static double kMin;

    private static double kMax;

    private static double delta;

    private static boolean readLast;

    Bitfinex bitfinex;

    Thread t;

    public static final int WAIT_TIMER_MS_BITFINEX = 100;

    public static final double MIN_DIFF_BTC_BITFINEX = 0.1;

    @Override
    public void run() {

        doit();
    }

    public void threadStart() {

        t = new Thread(this);
        t.start();
    }

    public void doit() {

        long i = 0;
        int orderFlag = 0;
        String strTradeLog = "";

        if (!bitfinex.setFunsAmountMargin()) {
            LoggerUtils.writeAndOut("PAFinexStampMargin/errs.txt",
                    "Can't setFunsAmountMargin");
            return;
        }
        if (!bitfinex.setMarginBtcAmount()) {
            LoggerUtils.writeAndOut("PAFinexStampMargin/errs.txt",
                    "Can't setMarginBtcAmount");
            return;
        }

        if (orderFlag == 1) {

            if (bitfinex.isLastOrderComplete()) {
                orderFlag = 0;
                bitfinex.setMarginBtcAmount();
                LoggerUtils.writeAndOut("PAFinexStampMargin/tradesUser.txt",
                        strTradeLog + "\t" + bitfinex.getAvgExecutionPrice());

                if (remains > MIN_DIFF_BTC_BITFINEX) {
                    if (bitfinex.tradeBuyMargin(remains)) {

                        orderFlag = 1;
                        remains = 0;
                    }
                } else if (remains < -MIN_DIFF_BTC_BITFINEX) {
                    if (bitfinex.tradeSellMargin(remains)) {

                        orderFlag = 1;
                        remains = 0;
                    }
                }
                remains = 0;
            } else {
            }
        }

        bitfinex.setMarginBalance();

        double marginBtcAmount = bitfinex.getMarginBtcAmount();
        ratio = Math.log(stampTickerSell / bitfinexTickerBuy);
        double expBtc = getExpectedBtc(ratio);
        if (expBtc < 0) {
            if (marginBtcAmount > 0) {
                expectedBtc = 0;
                remains =
                        bitfinex.getTotalInBtcMargin(bitfinexTickerSell)
                                * expBtc * 0.98;
            } else {
                expectedBtc =
                        bitfinex.getTotalInBtcMargin(bitfinexTickerSell)
                                * expBtc * 0.98;
            }
        } else {
            if (marginBtcAmount < 0) {
                expectedBtc = 0;
                remains =
                        bitfinex.getTotalInBtcMargin(bitfinexTickerBuy)
                                * expBtc * 0.98;
            } else {
                expectedBtc =
                        bitfinex.getTotalInBtcMargin(bitfinexTickerBuy)
                                * expBtc * 0.98;
            }
        }

        if (expectedBtc < MIN_DIFF_BTC_BITFINEX
                && expectedBtc > -MIN_DIFF_BTC_BITFINEX) {
            expectedBtc = 0;
        }

        diffBtc = expectedBtc - marginBtcAmount;
        if (Math.abs(ratio - lastSellRatio) > delta
                && diffBtc > MIN_DIFF_BTC_BITFINEX) {

            if (bitfinex.tradeBuyMargin(diffBtc)) {

                lastBuyRatio = ratio;
                orderFlag = 1;
            }
        } else {
            prevRatio = ratio;
            ratio = Math.log(stampTickerBuy / bitfinexTickerSell);
            expBtc = getExpectedBtc(ratio);
            if (expBtc < 0) {
                if (marginBtcAmount > 0) {
                    expectedBtc = 0;
                    remains =
                            bitfinex.getTotalInBtcMargin(bitfinexTickerSell)
                                    * expBtc * 0.98;
                } else {
                    expectedBtc =
                            bitfinex.getTotalInBtcMargin(bitfinexTickerSell)
                                    * expBtc * 0.98;
                }
            } else {
                if (marginBtcAmount < 0) {
                    expectedBtc = 0;
                    remains =
                            bitfinex.getTotalInBtcMargin(bitfinexTickerBuy)
                                    * expBtc * 0.98;
                } else {
                    expectedBtc =
                            bitfinex.getTotalInBtcMargin(bitfinexTickerBuy)
                                    * expBtc * 0.98;
                }
            }

            if (expectedBtc < MIN_DIFF_BTC_BITFINEX
                    && expectedBtc > -MIN_DIFF_BTC_BITFINEX) {
                expectedBtc = 0;
            }

            diffBtc = expectedBtc - marginBtcAmount;
            if (Math.abs(lastBuyRatio - ratio) > delta
                    && diffBtc < -MIN_DIFF_BTC_BITFINEX) {

                if (bitfinex.tradeSellMargin(diffBtc)) {

                    lastSellRatio = ratio;
                    orderFlag = 1;
                }
            }
        }
    }

    static double getExpectedBtc(double k) {

        if (k <= kMin) {
            return -1;
        } else if (k >= kMax) {
            return 1;
        } else {
            return (2 * k - kMax - kMin) / (kMax - kMin);
        }
    }*/
}
