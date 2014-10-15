package ru.goodloot.tr.objects;

import ru.goodloot.tr.utils.LoggerUtils;

/**
 * @author Artur M.
 * @created Oct 15, 2014
 * 
 * @Description ...
 */
public class PAPriceInfo {

    private double masterBuy;

    private double masterSell;

    private double slaveBuy;

    private double slaveSell;

    public boolean isSame(PAPriceInfo i) {
        return masterBuy == i.getMasterBuy() && masterSell == i.getMasterSell()
                        && slaveBuy == i.getSlaveBuy() && slaveSell == i.getSlaveSell();
    }

    public void setPrice(PAPriceInfo i) {

        masterBuy = i.getMasterBuy();
        masterSell = i.getMasterSell();
        slaveBuy = i.getSlaveBuy();
        slaveSell = i.getSlaveSell();
    }

    @Override
    public String toString() {
        return LoggerUtils.getFullStrWithoutDate(masterBuy, masterSell, slaveBuy,
                        slaveSell);
    }

    public double getMasterBuy() {
        return masterBuy;
    }

    public void setMasterBuy(double masterBuy) {
        this.masterBuy = masterBuy;
    }

    public double getMasterSell() {
        return masterSell;
    }

    public void setMasterSell(double masterSell) {
        this.masterSell = masterSell;
    }

    public double getSlaveBuy() {
        return slaveBuy;
    }

    public void setSlaveBuy(double slaveBuy) {
        this.slaveBuy = slaveBuy;
    }

    public double getSlaveSell() {
        return slaveSell;
    }

    public void setSlaveSell(double slaveSell) {
        this.slaveSell = slaveSell;
    }
}
