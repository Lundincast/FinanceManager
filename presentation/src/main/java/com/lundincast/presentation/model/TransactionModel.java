package com.lundincast.presentation.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Class that represents a transaction in the presentation layer.
 */
public class TransactionModel extends RealmObject {

    @PrimaryKey
    private int transactionId;

    public TransactionModel() {};

    public TransactionModel(int transactionId) {
        this.transactionId = transactionId;
    }

    private double price;
    private CategoryModel category;
    private Date date;
    private String comment;
    private boolean pending;
    private int dueToOrBy;
    private String dueName;


    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
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

    public String getDueName() {
        return dueName;
    }

    public void setDueName(String dueName) {
        this.dueName = dueName;
    }

    public int getDueToOrBy() {
        return dueToOrBy;
    }

    public void setDueToOrBy(int dueToOrBy) {
        this.dueToOrBy = dueToOrBy;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}
