package ru.JB.develop.purchaselist.Model;

import java.text.NumberFormat;
import java.text.ParseException;

import android.util.Log;

public class ProductItem {

    private String name;
    private String unit;
    private double price;
    private long id;

    public ProductItem(String newName, double newPrice) {
        name = newName;
        price = newPrice;
    }

    public ProductItem(String newName, double newPrice, int newId) {
        name = newName;
        price = newPrice;
        id = newId;
    }

    public ProductItem(String newName, String newUnit,
                       double newPrice, int newId) {
        name = newName;
        unit = newUnit;
        price = newPrice;
        id = newId;
    }

    public ProductItem(String newName, String newUnit, double newPrice) {
        name = newName;
        unit = newUnit;
        price = newPrice;
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

    public void setUnit(String newUnit) {
        unit = newUnit;
    }

    public String getUnit() {
        return unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double newPrice) {
        price = newPrice;
    }

    public void setFormatedPrice(String formatedPrice) {
        setPrice(parsePrice(formatedPrice));
    }

    public double parsePrice(String formatedPrice) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        try {
            return nf.parse(formatedPrice).doubleValue();
        } catch (ParseException e) {
            return -1;
        }
    }

    public String getFormatedPrice() {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(price);
    }
}