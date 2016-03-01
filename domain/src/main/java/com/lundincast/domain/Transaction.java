package com.lundincast.domain;

import java.util.Date;

/**
 * Class that represents a Transaction in the domain layer.
 */
public class Transaction {

    private final int transactionId;

    public Transaction(int transactionId) {
        this.transactionId = transactionId;
    }

    private double price;
    private Category category;
    private Date date;
    private String comment;

    public long getTransactionId() {
        return transactionId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
