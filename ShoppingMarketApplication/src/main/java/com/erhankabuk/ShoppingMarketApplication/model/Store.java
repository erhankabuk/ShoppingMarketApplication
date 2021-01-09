package com.erhankabuk.ShoppingMarketApplication.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "marketList")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "item")
    private String item;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "quantity")
    private int quantity;

    public Store(Long id, String item, BigDecimal price, int quantity) {
        this.id = id;
        this.item = item;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {return quantity; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Store() {
    }
}
