package ru.JB.develop.purchaselist.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.Log;

import ru.JB.develop.purchaselist.Database.DBWorker;

public class PurchaseItems {

    final String TAG = "PurchaseItems";

    private List<PurchaseItem> purchases;
    private DBWorker database;

    public PurchaseItems(Context context) {
        database = new DBWorker(context);
        purchases = database.readFromPurchases();
    }

    // Must staying before adapter initialization.
    // Cause - notifying that data set changing
    public void add(List<Integer> productsIds) {
        database.writeToPurchases(productsIds);
        purchases = database.readFromPurchases();
    }

    public List<PurchaseItem> getAll() {
        List<PurchaseItem> copyPurchases = new ArrayList<>(purchases);
        return copyPurchases;
    }


    public int size() {
        return purchases.size();
    }

    public PurchaseItem get(int index) {
        return purchases.get(index);
    }

    private int findIndexById(int id) {
        Comparator<PurchaseItem> comparator = new Comparator<PurchaseItem>() {
            @Override
            public int compare(PurchaseItem lhs, PurchaseItem rhs) {
                return lhs.getProductId() - rhs.getProductId();
            }
        };
        Collections.sort(purchases, comparator);
        return Collections.binarySearch(purchases,
                new PurchaseItem(1, "", true, id),comparator);
    }

    public PurchaseItem getById(int id) {
        int index = findIndexById(id);
        Log.d("PurchaseItems", String.valueOf(index));
        return purchases.get(index);
    }

    public void delete(List<Integer> idsForDelete) {
        for(int ID : idsForDelete ){
            database.deleteFromPurchasesById(ID);
            purchases.remove(findIndexById(ID));
        }
    }

}
