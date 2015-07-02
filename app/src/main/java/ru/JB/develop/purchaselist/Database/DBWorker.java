package ru.JB.develop.purchaselist.Database;

import java.util.ArrayList;

import ru.JB.develop.purchaselist.Model.ProductItem;
import ru.JB.develop.purchaselist.Model.ProductItems;
import ru.JB.develop.purchaselist.Model.PurchaseItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBWorker {
	
	DBHelper helper;
	SQLiteDatabase database;
	
	public DBWorker(Context context){
		
		helper = new DBHelper(context);
		database = helper.getWritableDatabase();
		
	}

    private boolean exist( String name){
        String[] selectionArgs = {name};
		Cursor c = database.query(Contract.Products.TABLE, null, Contract.Products.NAME+ " = ?", selectionArgs, null, null, null);
        Log.d("debug db exist",String.valueOf(c.getCount()));
        return (c.getCount()>0);
    }
	
	public ArrayList<ProductItem> readFromProducts(){
		
		ArrayList<ProductItem> items = new ArrayList<ProductItem>();
		
		String sqlQuery = "SELECT * FROM "+ Contract.Products.TABLE +" JOIN "+ Contract.Unit.TABLE +
                " ON "+ Contract.Products.TABLE +"."+ Contract.Products.UNIT_ID
                +" = "+ Contract.Unit.TABLE +"." + Contract.Unit._ID +
                " ORDER BY " + Contract.Products._ID;

		Cursor c = database.rawQuery(sqlQuery, null);
		if (c != null) {
			  if (c.moveToFirst()) {
		        int indexProductName = c.getColumnIndex(Contract.Products.NAME);
		        int indexUnitName= c.getColumnIndex(Contract.Unit.NAME);
		        int indexProductPrice = c.getColumnIndex(Contract.Products.PRICE);
		        int indexProductId = c.getColumnIndex(Contract.Products._ID);
		        do{
		        	
		        		String text = c.getString(indexProductName);
				    	String unit = c.getString(indexUnitName);
		        		Double price = c.getDouble(indexProductPrice);
				    	Integer id = c .getInt(indexProductId);
				    	items.add(new ProductItem(text,unit,price,id));        	
		        }while(c.moveToNext());
		      }
		}
		return items;
	}
	//TODO refactor methods of DB using( with ?)
	public void deleteFromProductsByName(String productName){
		database.delete(Contract.Products.TABLE, Contract.Products.NAME + " = '" + productName + "'", null);
	}
	
	public void deleteFromProductsById(int id){
		database.delete(Contract.Products.TABLE, Contract.Products._ID + " = " + id, null);
	}

	public long writeToProducts(ProductItem product){
        if(!exist(product.getName())) {
            ContentValues values = new ContentValues();
            values.put(Contract.Products.NAME, product.getName());
            values.put(Contract.Products.PRICE, product.getPrice());
            int unitId = findUnitIdByName(product.getUnit());
            if (unitId < 0)
                unitId = writeToUnits(product.getUnit());
            values.put(Contract.Products.UNIT_ID, unitId);
            long id = database.insert(Contract.Products.TABLE, null, values);
            return id;
        }
        return -1;
	}
	
	//TODO find alternative (method have one usage)
	public int getProductId(String name){
		String[] selectionArgs = {name};
		String[] columns = {Contract.Products._ID};
        Cursor c = database.query(Contract.Products.TABLE, columns, Contract.Products.NAME + " = ?", selectionArgs,null,null,null);
        if (c != null){
			 if (c.moveToFirst()){
				 Integer indexID = c.getColumnIndex(Contract.Products._ID);
				 return c.getInt(indexID);
			 }
		}
		return -1;
	}
	
	public void editProduct(int id, String newName,String newPrice){
		ContentValues values = new ContentValues();
		values.put("ProductName", newName);
		values.put("ProductPrice", newPrice);
		String whereClause = "ProductsId=?";
		String[] whereArgs = new String[] {String.valueOf(id)};
		database.update("Products", values, whereClause , whereArgs );
	}
	
	public int writeToUnits(String newName){
		ContentValues values = new ContentValues();
		values.put("UnitsName", newName);
		return (int) database.insert("Units", null, values);
		
	}
	
	public int findUnitIdByName(String unitName){
        String[] selectionArgs = {unitName};
        String[] columns = {Contract.Unit._ID};
        Cursor c = database.query(Contract.Unit.TABLE, columns, Contract.Unit.NAME +" = ?", selectionArgs, null, null, null);

        if(c != null){
			if(c.moveToFirst()){
				Integer indexId = c.getColumnIndex(Contract.Unit._ID);
				return c.getInt(indexId);
			}
		}
		return -1;
	}

	public ArrayList<PurchaseItem> readFromPurchases(){
		
		ArrayList<PurchaseItem> items = new ArrayList<PurchaseItem>();
		
		String sqlQuery = "SELECT "+ Contract.Products.NAME +", "
                + Contract.Purchase.NUMBER + ", " +
                Contract.Purchase.IS_BOUGHT + ", " +
				Contract.Purchase._ID + ", " +
				Contract.Products.TABLE +"."+ Contract.Products._ID +
				" FROM "+ Contract.Products.TABLE +
                " JOIN "+ Contract.Purchase.TABLE +
				" ON "+ Contract.Purchase.TABLE +"."+ Contract.Purchase.PRODUCT_ID +" == "+
                Contract.Products.TABLE +"."+ Contract.Products._ID;
		Cursor c = database.rawQuery(sqlQuery, null);
		if (c != null) {
			if (c.moveToFirst()) {
				Integer indexProductName = c.getColumnIndex(Contract.Products.NAME);
		        Integer indexNumberOfPurchase = c.getColumnIndex(Contract.Purchase.NUMBER);
		        Integer indexPurchaseIsBought = c.getColumnIndex(Contract.Purchase.IS_BOUGHT);
		        Integer indexPurchasesId = c.getColumnIndex(Contract.Purchase._ID);
		        Integer indexProductsId = c.getColumnIndex(Contract.Products._ID);
		        do{
			    	String PurchaseName = c.getString(indexProductName);
			    	int NumberOfPurchase = c.getInt(indexNumberOfPurchase);
			    	boolean PurchaseIsBought = (c.getInt(indexPurchaseIsBought)==1);
			    	int PurchasesId = c.getInt(indexPurchasesId);
			    	int ProductsId = c.getInt(indexProductsId); 
			    	PurchaseItem item = new PurchaseItem(PurchasesId, PurchaseName, NumberOfPurchase, PurchaseIsBought, ProductsId);
		        	items.add(item);
		        }while(c.moveToNext());
		      }
		}
		return items;
	}
		

	
	public void deleteFormPurchases(String productName){
        String[] whereArgs = {String.valueOf(getProductId(productName))};
		database.delete(Contract.Purchase.TABLE, Contract.Products._ID + " = ?", whereArgs);
	}

	public void writeToPurchases(ArrayList<Integer> productsIds){
		ContentValues values = new ContentValues();
		for(Integer prodId : productsIds){
			values.clear();
			values.put(Contract.Purchase.NUMBER, 1);
			values.put(Contract.Purchase.IS_BOUGHT, false);
			values.put("ProductsId", prodId);
			Long idd = database.insert("Purchases", null, values);
		}
	}
	
	public void close(){
		database.close();
	}


}
