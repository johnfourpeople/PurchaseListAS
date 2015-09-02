package ru.JB.develop.purchaselist.Model;

import java.text.NumberFormat;
import java.text.ParseException;

import android.util.Log;

public class ProductItem {

    private String name;
    private long id;

    public ProductItem(String newName) {
        name = newName;
    }

    public ProductItem(String newName, int newId) {
        name = newName;

        id = newId;
    }

    public final int getId() {
        return (int)id;
    }

    public void setId(long newID) {
        id = newID;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }
}