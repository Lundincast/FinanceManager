package com.lundincast.presentation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Class that represents an overhead in the presentation layer.
 */
public class OverheadModel extends RealmObject {

    @PrimaryKey
    private int overheadId;

    private double price;
    private CategoryModel category;
    private short dayOfMonth;
    private String comment;

    public OverheadModel() {}

    public OverheadModel(int overheadId) {
        this.overheadId = overheadId;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public short getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(short dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getOverheadId() {
        return overheadId;
    }

    public void setOverheadId(int overheadId) {
        this.overheadId = overheadId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
