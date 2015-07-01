package ru.JB.develop.purchaselist.Database;

import java.util.ArrayList;

import ru.JB.develop.purchaselist.Model.ProductItem;
import ru.JB.develop.purchaselist.Model.ProductItems;
import ru.JB.develop.purchaselist.Model.PurchaseItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBWorker {
	
	DBHelper helper;
	SQLiteDatabase database;
	
	public DBWorker(Context context){
		
		helper = new DBHelper(context);
		database = helper.getWritableDatabase();
		
	}
	
	public ArrayList<ProductItem> readFromProducts(){
		
		ArrayList<ProductItem> items = new ArrayList<ProductItem>();
		
		String sqlQuery = "SELECT * FROM Products JOIN Units ON Products.UnitsId = Units.UnitsId ORDER BY ProductsId";
		Cursor c = database.rawQuery(sqlQuery, null);
		if (c != null) {
			  if (c.moveToFirst()) {
		        int indexProductName = c.getColumnIndex("ProductName");
		        int indexUnitName= c.getColumnIndex("UnitsName");
		        int indexProductPrice = c.getColumnIndex("ProductPrice");
		        int indexProductId = c.getColumnIndex("ProductsId");
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
	
	public void deleteFromProductsByName(String productName){
		database.delete("Products", "ProductName = '" + productName + "'", null);
	}
	
	public void deleteFromProductsById(int id){
		database.delete("Products", "ProductsId = " + id, null);
	}
		
	public long writeToProducts(ProductItem product){
		ContentValues values = new ContentValues();
		values.put("ProductName", product.getName());
		values.put("ProductPrice", product.getPrice());
		int unitId = findUnitIdByName(product.getUnit());
		if (unitId<0)
			unitId = writeToUnits(product.getUnit());
		values.put("UnitsId", unitId);
		long id = database.insert("Products", null, values);
		return id;
	}
	
	//refactor  "-1" - when id not found, if more then one picked first.
	public int getProductId(ContentValues values){
		String sql = "SELECT ProductsId FROM PRODUCTS WHERE ProductName = '" + values.getAsString("ProductName") + "'";
		Cursor c = database.rawQuery(sql, null);
		if (c != null){
			 if (c.moveToFirst()){
				 Integer indexID = c.getColumnIndex("ProductsId");
				 return c.getInt(indexID);
			 }
		}
		return -1;
	}
	//refactor database.insert returning what we need
	public int getProductId(ProductItem item){
		String sql = "SELECT ProductsId FROM PRODUCTS WHERE ProductName = '" + item.getName() + "'";
		Cursor c = database.rawQuery(sql, null);
		if (c != null){
			 if (c.moveToFirst()){
				 Integer indexID = c.getColumnIndex("ProductsId");
			 return c.getInt(indexID);
			 }
		}
		return -1;
	}
	
	public int getProductId(String name){
		String sql = "SELECT ProductsId FROM PRODUCTS WHERE ProductName = '" + name + "'";
		Cursor c = database.rawQuery(sql, null);
		if (c != null){
			 if (c.moveToFirst()){
				 Integer indexID = c.getColumnIndex("ProductsId");
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
		String sql = "SELECT UnitsId FROM Units WHERE UnitsName = ?";
		String[] selectionArgs = {unitName};
		Cursor c = database.rawQuery(sql, selectionArgs );
		if(c != null){
			if(c.moveToFirst()){
				Integer indexId = c.getColumnIndex("UnitsId");
				return c.getInt(indexId);
			}
		}
		return -1;
	}

	public ArrayList<PurchaseItem> readFromPurchases(){
		
		ArrayList<PurchaseItem> items = new ArrayList<PurchaseItem>();
		
		String sqlQuery = "SELECT ProductName, NumberOfPurchases, PurchaseIsBought," +
				" PurchaseId, Purchases.ProductsId"+
				" FROM Purchases JOIN Products" +
				" ON Purchases.ProductsId == Products.ProductsId";
		Cursor c = database.rawQuery(sqlQuery, null);
		if (c != null) {
			if (c.moveToFirst()) {
				Integer indexProductName = c.getColumnIndex("ProductName");
		        Integer indexNumberOfPurchase = c.getColumnIndex("NumberOfPurchases");
		        Integer indexPurchaseIsBought = c.getColumnIndex("PurchaseIsBought");
		        Integer indexPurchasesId = c.getColumnIndex("PurchaseId");
		        Integer indexProductsId = c.getColumnIndex("ProductsId");
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
		

	
	public void deleteFormPurchases(String purchaseName){
		database.delete("Purchases", "ProductsId = " + ((Integer)getProductId(purchaseName)).toString(), null);
	}
	
	public void writeToPurchases(PurchaseItem purchase, int productsId){
		ContentValues values = new ContentValues();
		values.put("NumberOfPurchase", purchase.getNumberOfPurchases());
		values.put("PurchaseIsBought", purchase.getPurchaseIsBought());
		values.put("ProductsId", productsId);
		database.insert("Purchases", null, values);
	}
	
	public void writeToPurchases(ArrayList<Integer> productsIds){
		ContentValues values = new ContentValues();
		for(Integer prodId : productsIds){
			values.clear();
			values.put("NumberOfPurchases", 1);
			values.put("PurchaseIsBought", false);
			values.put("ProductsId", prodId);
			Long idd = database.insert("Purchases", null, values);
		}
	}
	
	public void close(){
		database.close();
	}
}
