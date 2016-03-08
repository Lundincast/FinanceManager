package com.lundincast.presentation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lundincast on 1/03/16.
 */
public class CategoryModel extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private String color;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}