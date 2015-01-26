package ru.goodloot.tr.markets.pabots;

import ru.goodloot.tr.markets.TickerInfo;
import ru.goodloot.tr.markets.exchange.Anx;
import ru.goodloot.tr.markets.data.OrderInfo;
import ru.goodloot.tr.utils.LoggerUtils;
import ru.goodloot.tr.utils.Utils;

/**
 * @author Artur M.
 * @created Oct 5, 2014
 * 
 * @Description ...
 */
public class PAAnx extends PAExchange {

    public PAAnx(String confName, TickerInfo master, TickerInfo slave) {

        super(confName, master, slave);

        exchange = new Anx(getSecret(), getKey());
    }

    protected OrderInfo updateOrderInfo(OrderInfo info) {
        return exchange.getOrderInfo();
    }

    @Override
    protected void processExistOrderSpecifics(OrderInfo info) {

        // Записываем, чтобы узнать разницу
        double prevBtc = exchange.getBtcAmount();

        boolean complete = false;
        boolean cancelled = false;

        if (info.isOrderComplete()) {

        	complete = true;
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

        if (realBtcDiff != 0) {
            LoggerUtils.out("Btc amount WAS changed, real ratio", ratio);
        } else {
        	
        	LoggerUtils.out("Btc amount NOT changed");

            if (!cancelled) {
                for (int i = 0; i < 15; i++) {

                    exchange.setFundsAmount();
                    realBtcDiff = exchange.getBtcAmount() - prevBtc;
                    if (realBtcDiff != 0) {
                    	LoggerUtils.out("Btc amount WAS changed in loop, ratio", ratio, i);
                        break;
                    }
                    Utils.sleep(1500);
                }
            }
        }
        
        // Если ордер не завершился, ратио высчитываем сами
        if (!complete) {
        	double temp = ratio;
        	ratio = getRealRatio();
        	LoggerUtils.out("Order not complete, set real ratio", ratio, "Prev:", temp);
        }

        logger.writeAndOut("tradesUser.txt", strTradeLog, realBtcDiff);
    }

    /*
     * Будем переодически вызывать, опыт показывает, что надо
     */
    @Override
    protected void periodicalTask(long i) {

        if (i % 1200 == 600) {
            exchange.setFundsAmount(true);
        }
    }
}
