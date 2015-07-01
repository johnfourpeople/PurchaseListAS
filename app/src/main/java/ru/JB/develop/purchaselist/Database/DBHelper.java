package ru.JB.develop.purchaselist.Database;

import ru.JB.develop.purchaselist.R;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	Context context;
	public DBHelper(Context cntxt)  {
	      super(cntxt, "myDB", null, 1);
	      context = cntxt;
	    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE Products ("
    			+ "ProductsId INTEGER PRIMARY KEY AUTOINCREMENT," 
    			+ "ProductName TEXT," 
    			+ "ProductPrice TEXT," 
    			+ "UnitsId INTEGER" + ");");
		
		db.execSQL("CREATE TABLE Purchases("
				+ " PurchaseId INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " NumberOfPurchases INTEGER," 
				+ " PurchaseIsBought BOOLEAN," 
				+ " ProductsId INTEGER" +");");
		
		db.execSQL("CREATE TABLE Units( " +
				"UnitsId INTEGER PRIMARY KEY AUTOINCREMENT," +
				" UnitsName TEXT);");
		
		String[] units = context.getResources().getStringArray(R.array.units);
		ContentValues values = new ContentValues();
		for(int i=0; i<units.length; i++){
			values.clear();
			values. put("UnitsName",units[i]);
			db.insert("Units", null, values);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

}
