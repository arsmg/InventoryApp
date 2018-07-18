package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.inventoryapp.data.ItemConstant.ItemEntry;

public class ItemDbHelper extends SQLiteOpenHelper {

   public static final String DATABASE_NAME = "store.db";

   public static final int DATABASE_VERSION = 1;

   public ItemDbHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_ITEMS_TABLE =  "CREATE TABLE " + ItemEntry.TABLE_NAME + " ("
                + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemEntry.COLUMN_PRODUCT_NAME + " REAL NOT NULL, "
                + ItemEntry.COLUMN_PRICE + " INTEGER DEFAULT 0, "
                + ItemEntry.COLUMN_QANTITY + " INTEGER NOT NULL DEFAULT 0,"
                + ItemEntry.COLUMN_SUPPLIERS_NAME + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_SUPPLIERS_PHONE + " LONG NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
