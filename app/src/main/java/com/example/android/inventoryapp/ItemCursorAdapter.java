package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ItemConstant;

public class ItemCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ItemCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = view.findViewById(R.id.price);

        Button sellButton = view.findViewById(R.id.sellButton);

        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ItemConstant.ItemEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ItemConstant.ItemEntry.COLUMN_QANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ItemConstant.ItemEntry.COLUMN_PRICE);

        // Read the pet attributes from the Cursor for the current item
        String productName = cursor.getString(nameColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);

        // Update the TextViews with the attributes for the current item
        nameTextView.setText(productName);
        quantityTextView.setText(productQuantity);
        priceTextView.setText(productPrice);

        //Sell button functionality
        String currentQuantity = cursor.getString(quantityColumnIndex);
        final int quantityCurrent = Integer.valueOf(currentQuantity);
        final int itemID = cursor.getInt(cursor.getColumnIndex(ItemConstant.ItemEntry._ID));

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantityCurrent > 0) {
                    int newQantity = quantityCurrent -1;
                    Uri itemQuantityUri = ContentUris.withAppendedId(ItemConstant.ItemEntry.CONTENT_URI, itemID);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ItemConstant.ItemEntry.COLUMN_QANTITY, newQantity);
                    context.getContentResolver().update(itemQuantityUri, contentValues, null, null);
                }else {
                    Toast.makeText(context, "No more items!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
