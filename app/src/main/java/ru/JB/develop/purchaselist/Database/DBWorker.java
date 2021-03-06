package ru.JB.develop.purchaselist.Database;

import java.util.ArrayList;
import java.util.List;

import ru.JB.develop.purchaselist.Model.ProductItem;
import ru.JB.develop.purchaselist.Model.PurchaseItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBWorker {

    private String TAG = "DBWorker";

    DBHelper helper;
    SQLiteDatabase database;

    public DBWorker(Context context) {
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();
    }

    private boolean nameExistProducts(String name) {
        String[] selectionArgs = {name};
        Cursor c = database.query(Contract.Products.TABLE, null,
                Contract.Products.NAME + " = ?", selectionArgs, null,
                null, null);
        Log.d("debug db exist",String.valueOf(c.getCount()));
        return (c.getCount()>0);
    }

    private boolean idExistPurchases(int id) {
        String[] selectionArgs = {String.valueOf(id)};
        Cursor c = database.query(Contract.Purchase.TABLE, null,
                Contract.Purchase.PRODUCT_ID + " = ?", selectionArgs,
                null, null, null);
        return (c.getCount()>0);
    }

    public int getIdByName(String name) {

        String[] selectionArgs = {name};
        Cursor c = database.query(Contract.Products.TABLE, null,
                Contract.Products.NAME + " = ?", selectionArgs, null,
                null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                int indexId = c.getColumnIndex(Contract.Products._ID);
                return -c.getInt(indexId);
            }
        }
        return 1;
    }

    public List<ProductItem> readFromProducts() {

        List<ProductItem> items = new ArrayList<ProductItem>();

        String sqlQuery = "SELECT * FROM "+ Contract.Products.TABLE
                + " ORDER BY " + Contract.Products.NAME;

        Cursor c = database.rawQuery(sqlQuery, null);
        if (c != null) {
            if (c.moveToFirst()) {
                int indexProductName = c.getColumnIndex(Contract.Products.NAME);
                int indexProductId = c.getColumnIndex(Contract.Products._ID);
                do {
                    String text = c.getString(indexProductName);
                    Integer id = c .getInt(indexProductId);
                    items.add(new ProductItem(text, id));
                } while(c.moveToNext());
            }
        }
        return items;
    }

    public void deleteFromProductsById(int id) {
        String[] whereArgs = {String.valueOf(id)};
        database.delete(Contract.Products.TABLE, Contract.Products._ID
                + " =  ?", whereArgs);
    }

    public int writeToProducts(ProductItem product) {
        int existingId = getIdByName(product.getName());
        Log.d(TAG, "Id of prod: " + String.valueOf(existingId));
        if (existingId > 0) {
            ContentValues values = new ContentValues();
            values.put(Contract.Products.NAME, product.getName());
            return (int)database.insert(Contract.Products.TABLE, null, values);
        }
        return existingId;
    }

    public void editProduct(int id, String newName) {
        ContentValues values = new ContentValues();
        values.put(Contract.Products.NAME, newName);
        String whereClause = Contract.Products._ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(id)};
        database.update(Contract.Products.TABLE, values,
                whereClause, whereArgs);
    }

    public List<PurchaseItem> readFromPurchases() {
        List<PurchaseItem> items = new ArrayList<PurchaseItem>();

        String sqlQuery = "SELECT " + Contract.Products.NAME +", "
                + Contract.Purchase.IS_BOUGHT + ", "
                + Contract.Purchase._ID + ", "
                + Contract.Products.TABLE +"." + Contract.Products._ID
                + " FROM " + Contract.Purchase.TABLE
                + " JOIN " + Contract.Products.TABLE
                + " ON " + Contract.Purchase.TABLE +"."
                + Contract.Purchase.PRODUCT_ID + " == "
                + Contract.Products.TABLE + "." + Contract.Products._ID;
        Cursor c = database.rawQuery(sqlQuery, null);
        if (c != null) {
            if (c.moveToFirst()) {
                Integer indexProductName = c.getColumnIndex(
                    Contract.Products.NAME);
                Integer indexPurchaseIsBought = c.getColumnIndex(
                        Contract.Purchase.IS_BOUGHT);
                Integer indexPurchasesId = c.getColumnIndex(
                        Contract.Purchase._ID);
                Integer indexProductsId = c.getColumnIndex(
                        Contract.Purchase.PRODUCT_ID);
                do {
                    String PurchaseName = c.getString(indexProductName);
                    boolean PurchaseIsBought = (c.getInt(indexPurchaseIsBought)==1);
                    int PurchasesId = c.getInt(indexPurchasesId);
                    int ProductsId = c.getInt(indexProductsId);
                    PurchaseItem item = new PurchaseItem(PurchasesId,
                            PurchaseName, PurchaseIsBought, ProductsId);
                    items.add(item);
                } while(c.moveToNext());
              }
        }
        return items;
    }

    public void deleteFromPurchasesById(int ID) {
        String[] whereArgs = { String.valueOf(ID) };
        database.delete(Contract.Purchase.TABLE,
                Contract.Purchase.PRODUCT_ID + " = ?", whereArgs);
    }

    public void writeToPurchases(List<Integer> productsIds) {
        ContentValues values = new ContentValues();
        for(Integer prodId : productsIds){
            String[] selectionArgs = {String.valueOf(prodId)};
            Cursor c = database.query(Contract.Purchase.TABLE, null,
                    Contract.Purchase.PRODUCT_ID + " = ?", selectionArgs,
                    null, null, null);
            if (c.getCount()>0) {
                if (c.moveToFirst()) {
                    Integer indexPurchaseIsBought = c.getColumnIndex(
                            Contract.Purchase.IS_BOUGHT);
                    Integer indexPurchasesId = c.getColumnIndex(
                            Contract.Purchase._ID);
                    Integer indexProductsId = c.getColumnIndex(
                            Contract.Purchase.PRODUCT_ID);

                    values.put(Contract.Purchase.IS_BOUGHT,
                            c.getInt(indexPurchaseIsBought)==1);
                    values.put(Contract.Purchase._ID,
                            c.getInt(indexPurchasesId));
                    values.put(Contract.Purchase.PRODUCT_ID,
                            c.getInt(indexProductsId));

                    String whereClause = Contract.Purchase.PRODUCT_ID + " = ?";
                    String[] whereArgs = new String[] {String.valueOf(prodId)};

                    database.update(Contract.Purchase.TABLE, values,
                            whereClause, whereArgs);
                }
            } else {
                values.clear();
                values.put(Contract.Purchase.IS_BOUGHT, false);
                values.put("ProductsId", prodId);
                Long idd = database.insert(Contract.Purchase.TABLE,
                        null, values);
            }
        }
    }

    public void close() {
        database.close();
    }

}
