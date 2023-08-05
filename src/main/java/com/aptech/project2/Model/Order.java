package com.aptech.project2.Model;

import java.time.LocalDateTime;

public class Order {
    private String id;
    private Staff staff;
    private double subTotal;
    private double discount;
    private double total;
    private LocalDateTime createDate;

    public Order() {
    }

    public Order(String id, Staff staff, double subTotal, double discount, double total, LocalDateTime createDate) {
        this.id = id;
        this.staff = staff;
        this.subTotal = subTotal;
        this.discount = discount;
        this.total = total;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
