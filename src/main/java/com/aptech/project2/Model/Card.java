package com.aptech.project2.Model;

import java.time.LocalDate;
import java.util.Date;

public class Card {
    private int id;
    private Staff staff;
    private Product product;
    private Integer proQuantity;
    private Double proPrice;

    public Card() {
    }

    public Card(int id, Staff staff, Product product, Integer proQuantity, Double proPrice) {
        this.id = id;
        this.staff = staff;
        this.product = product;
        this.proQuantity = proQuantity;
        this.proPrice = proPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getProQuantity() {
        return proQuantity;
    }

    public void setProQuantity(Integer proQuantity) {
        this.proQuantity = proQuantity;
    }

    public Double getProPrice() {
        return proPrice;
    }

    public void setProPrice(Double proPrice) {
        this.proPrice = proPrice;
    }
}
