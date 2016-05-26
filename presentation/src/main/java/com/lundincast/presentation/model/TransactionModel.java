package com.lundincast.presentation.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Class that represents a transaction in the presentation layer.
 */
public class TransactionModel extends RealmObject {

    @PrimaryKey
    private int transactionId;

    public TransactionModel() {}

    public TransactionModel(int transactionId) {
        this.transactionId = transactionId;
    }

    @Required
    private String transactionType;
    private double price;
    private CategoryModel category;
    private Date date;
    private int day;
    private int month;
    private int year;
    private String comment;
    private AccountModel fromAccount;
    private boolean pending;
    private int dueToOrBy;
    private String dueName;


    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public AccountModel getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(AccountModel fromAccount) {
        this.fromAccount = fromAccount;
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
