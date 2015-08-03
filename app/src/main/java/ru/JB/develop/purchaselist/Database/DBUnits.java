package ru.JB.develop.purchaselist.Database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUnits {

    DBHelper helper;
    SQLiteDatabase database;

    public DBUnits(Context context) {
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();
    }

    public List<String> readUnits() {
        String sql = "SELECT * FROM "+ Contract.Unit.TABLE;
        List<String> list = new ArrayList<String>();
        Cursor c = database.rawQuery(sql, null);
        if (c != null) {
            if (c.moveToFirst()) {
                Integer indexUnitsName = c.getColumnIndex(Contract.Unit.NAME);
                do {
                    list.add(c.getString(indexUnitsName));
                } while (c.moveToNext());
            }
        }
        return list;
    }

    public void writeUnit(String newName) {
        ContentValues values = new ContentValues();
        values.put("UnitsName", newName);
        database.insert("Units", null, values);
    }

    public void close() {
        database.close();
    }
}
