/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.objects;

/**
 * 
 * @author lol
 */
public class OrderInfo {

    private boolean orderComplete;

    private Double volExecuted;

    private boolean success = true;

    private String orderId;

    public OrderInfo() {}

    public OrderInfo(String orderId, boolean orderComplete, Double volExecuted) {

        this.orderId = orderId;
        this.orderComplete = orderComplete;
        this.volExecuted = volExecuted;
    }

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

    public Double getVolExecuted() {
        return volExecuted;
    }

    public void setVolExecuted(Double volExecuted) {
        this.volExecuted = volExecuted;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
