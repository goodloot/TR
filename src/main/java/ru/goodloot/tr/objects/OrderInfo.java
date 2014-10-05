/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.goodloot.tr.objects;

/**
 *
 * @author lol
 */
public class OrderInfo {

    private boolean orderComplete;
    private double volExecuted;
    private boolean success = true;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isOrderComplete() {
        return orderComplete;
    }

    public void setOrderComplete(boolean orderComplete) {
        this.orderComplete = orderComplete;
    }

    public double getVolExecuted() {
        return volExecuted;
    }

    public void setVolExecuted(double volExecuted) {
        this.volExecuted = volExecuted;
    }
}
