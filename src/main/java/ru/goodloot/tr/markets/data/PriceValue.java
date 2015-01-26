package ru.goodloot.tr.markets.data;

import ru.goodloot.tr.utils.LoggerUtils;

/**
 * @author Artur M.
 * @created Oct 17, 2014
 * 
 * @Description ...
 */
public class PriceValue {

    private double buy;

    private double sell;

    public boolean equals(PriceValue v) {
        return buy == v.getBuy() && sell == v.getSell();
    }

    @Override
    public String toString() {
        return LoggerUtils.getFullStrWithoutDate(buy, sell);
    }

    public double getBuy() {
        return buy;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }

    public double getSell() {
        return sell;
    }

    public void setSell(double sell) {
        this.sell = sell;
    }
}
