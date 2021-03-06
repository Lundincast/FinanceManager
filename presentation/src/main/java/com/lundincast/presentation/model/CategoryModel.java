package com.lundincast.presentation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Class that represents a category in the presentation layer.
 */
public class CategoryModel extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private int color;

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
}
