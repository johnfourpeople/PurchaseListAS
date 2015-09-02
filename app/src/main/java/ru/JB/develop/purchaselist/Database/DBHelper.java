package ru.JB.develop.purchaselist.Database;

import ru.JB.develop.purchaselist.R;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    Context context;
    public DBHelper(Context cntxt) {
          super(cntxt, "myDB", null, 1);
          context = cntxt;
        }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ Contract.Products.TABLE +" ("
                + Contract.Products._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Contract.Products.NAME +" TEXT" + ");");

        db.execSQL("CREATE TABLE "+ Contract.Purchase.TABLE +"("
                + Contract.Purchase._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.Purchase.IS_BOUGHT +" BOOLEAN,"
                + Contract.Purchase.PRODUCT_ID +" INTEGER" +");");
    }


    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Upgrade method
    }
}
