package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import data.ItemConstant.ItemEntry;
import data.ItemDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText mProductName;

    private EditText mPrice;

    private EditText mQuantity;

    private EditText mSuppliersName;

    private EditText mSuppliersPhone;

   @Override
    protected void onCreate (Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_editor);

       mProductName = findViewById(R.id.edit_name);
       mPrice = findViewById(R.id.edit_price);
       mQuantity = findViewById(R.id.edit_quantity);
       mSuppliersName = findViewById(R.id.edit_suppliers_name);
       mSuppliersPhone = findViewById(R.id.edit_suppliers_phone_number);
   }

    // Get user input from editor and save new item info
    private void insertItem(){
        String nameString = mProductName.getText().toString().trim();
        String priceString = mPrice.getText().toString().trim();
        String quantityString = mQuantity.getText().toString().trim();
        String suppliersNameString = mSuppliersName.getText().toString().trim();
        String suppliersPhoneString = mSuppliersPhone.getText().toString().trim();

        // converts string to integer
        int price = Integer.parseInt(priceString);
        int quantity = Integer.parseInt(quantityString);

        // converts string to long
        long suppliersPhone = Long.parseLong(suppliersPhoneString);

        // Create database helper
        ItemDbHelper mDbHelper = new ItemDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_PRODUCT_NAME,nameString);
        values.put(ItemEntry.COLUMN_PRICE,price);
        values.put(ItemEntry.COLUMN_QANTITY,quantity);
        values.put(ItemEntry.COLUMN_SUPPLIERS_NAME,suppliersNameString);
        values.put(ItemEntry.COLUMN_SUPPLIERS_PHONE,suppliersPhone);

        // Insert a new row in the database, returning the ID of that new row.
        // The first argument for db.insert() is the pets table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
        long newRowId = db.insert(ItemEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving pet", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Pet saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet in database
                insertItem();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
