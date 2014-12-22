/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package ru.goodloot.tr.markets.pabots;

/**
 * 
 * @author lol
 */
public class PAExchangeMargin {

    static double getExpectedBtc(double k, double kMin, double kMax) {

        if (k <= kMin) {
            return -1;
        } else if (k >= kMax) {
            return 1;
        } else {
            return (2 * k - kMax - kMin) / (kMax - kMin);
        }
    }
}
