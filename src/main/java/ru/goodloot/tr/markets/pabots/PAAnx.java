package ru.goodloot.tr.markets.pabots;

import ru.goodloot.tr.TickerThread;
import ru.goodloot.tr.markets.Anx;
import ru.goodloot.tr.objects.OrderInfo;

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

        if (info.isOrderComplete()) {

            strTradeLog = "Complete  " + strTradeLog;
        } else {
            if (exchange.cancelLastOrder()) {

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

        if (realBtcDiff != 0) {

            double tempRatio = ratio;
            setRatioFromReal();
            logger.out("Btc amount was changed, real ratio setted", tempRatio, ratio);
        }

        logger.writeAndOut("tradesUser.txt", strTradeLog, realBtcDiff);
    }
}
