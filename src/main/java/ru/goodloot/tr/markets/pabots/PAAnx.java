package ru.goodloot.tr.markets.pabots;

import ru.goodloot.tr.TickerThread;
import ru.goodloot.tr.markets.Anx;
import ru.goodloot.tr.objects.OrderInfo;
import ru.goodloot.tr.utils.LoggerUtils;
import ru.goodloot.tr.utils.Utils;

/**
 * @author Artur M.
 * @created Oct 5, 2014
 * 
 * @Description ...
 */
public class PAAnx extends PAExchange {

    public PAAnx(String confName, TickerThread master, TickerThread slave) {

        super(confName, master, slave);

        exchange = new Anx(getSecret(), getKey());
    }

    @Override
    protected OrderInfo updateOrderInfo(OrderInfo info) {
        return exchange.getOrderInfo();
    }

    @Override
    protected void processExistOrderSpecifics(OrderInfo info) {

        // Записываем, чтобы узнать разницу
        double prevBtc = exchange.getBtcAmount();

        boolean cancelled = false;

        if (info.isOrderComplete()) {

            strTradeLog = "Complete  " + strTradeLog;
        } else {
            if (exchange.cancelLastOrder()) {

                cancelled = true;
                strTradeLog = "Cancelled " + strTradeLog;
            } else {
                /**
                 * Если отменить не смогли, считаем, что он полностью выполнился
                 */
                strTradeLog = "NOT Cancelled " + strTradeLog;
            }
        }

        exchange.setFundsAmount();

        /**
         * Устанавливаем баланс и, если он поменялся, пересчитываем ратио
         */
        double realBtcDiff = exchange.getBtcAmount() - prevBtc;
        double tempRatio = ratio;

        if (realBtcDiff != 0) {

            setRatioFromReal();
            LoggerUtils.out("Btc amount WAS changed, real ratio setted", tempRatio, ratio);
        } else {
        	LoggerUtils.out("Btc amount NOT changed");

            if (!cancelled) {
                for (int i = 0; i < 15; i++) {

                    exchange.setFundsAmount();
                    realBtcDiff = exchange.getBtcAmount() - prevBtc;
                    if (realBtcDiff != 0) {
                    	LoggerUtils.out("Btc amount WAS changed in loop, real ratio setted",
                                        tempRatio, ratio, i);
                        break;
                    }
                    Utils.sleep(1500);
                }
            }
        }

        logger.writeAndOut("tradesUser.txt", strTradeLog, realBtcDiff);
    }

    /*
     * Будем переодически вызывать, опыт показывает, что надо
     */
    @Override
    protected void peredoicalTask(long i) {

        if (i % 1200 == 600) {
            exchange.setFundsAmount(true);
        }
    }
}
