package ru.JB.develop.purchaselist.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.util.Log;

import ru.JB.develop.purchaselist.Database.DBWorker;

public class ProductItems {

    private List<ProductItem> products;
    private DBWorker database;

    public ProductItems(Context context) {
        database = new DBWorker(context);
        products = database.readFromProducts();
    }

    public int size() {
        return products.size();
    }

    // refactor delete purchases with no existing Id

    public int findIndexOfProductById(Integer id) {
        Log.d("debug items id",id.toString());
        Comparator<ProductItem> comparator = new Comparator<ProductItem>() {

            @Override
            public int compare(ProductItem prod0, ProductItem prod1) {
                return prod0.getId()-prod1.getId();
            }
        };
        Collections.sort(products, comparator);
        int index = Collections.binarySearch(products,
                new ProductItem("", 0, id), comparator);
        Log.d("debug items index", String.valueOf(index));
        return index;
    }

    public void deleteItemById(int idForDelete) {
        int index = findIndexOfProductById(idForDelete);
        if (index >= 0) {
            products.remove(index);
            database.deleteFromProductsById(idForDelete);
        }
    }

    // refactor
    public ProductItem add(ProductItem newItem) {
        long ID = database.writeToProducts(newItem);
        newItem.setId(ID);
        if (ID > 0) {
            products.add(newItem);
            return newItem;
        }
        return null;
    }

    public void edit(int id, String newName, String newPrice) {
        int index = findIndexOfProductById(id);
        if (index <0) {
            return;
        }
        products.get(index).setName(newName);
        products.get(index).setFormatedPrice(newPrice);
        database.editProduct(id, newName,
                String.valueOf(products.get(index).getPrice()));
    }

    public List<ProductItem> getAll() {
        List<ProductItem> copyProducts = new ArrayList<ProductItem>();
        Comparator<ProductItem> comparatorByName = new Comparator<ProductItem>() {
            @Override
            public int compare(ProductItem lhs, ProductItem rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        };
        copyProducts.addAll(products);
        Collections.sort(copyProducts, comparatorByName);
        return copyProducts;
    }

    public ProductItem get(int index) {
        return products.get(index);
    }

    public void close() {
        database.close();
    }
}