package com.lundincast.presentation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Class that represents an account in the presentation layer.
 */
public class AccountModel extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private int color;
    private double balance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
