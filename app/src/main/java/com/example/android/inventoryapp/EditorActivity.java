package com.example.android.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ItemConstant.ItemEntry;
import com.example.android.inventoryapp.data.ItemDbHelper;

public class EditorActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_ITEM_LOADER = 0;

    private Uri mCurrentItemUri;

    private EditText mProductName;

    private EditText mPrice;

    private EditText mQuantity;

    private EditText mSuppliersName;

    private EditText mSuppliersPhone;

    private boolean mhasItemChanged = false;

    // Variables for + and - buttons
    String quantityString;
    int quantity = 0;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch (View view, MotionEvent motionEvent) {
            mhasItemChanged=true;
            return false;
        }
    };

   @Override
    protected void onCreate (Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_editor);

       Button minusItemButton;
       Button plusItemButton;
       Button callButton;

       Intent intent = getIntent();
       mCurrentItemUri = intent.getData();

       if (mCurrentItemUri == null) {
           setTitle(getString(R.string.add_an_item));
           invalidateOptionsMenu();
       }else {
           setTitle(getString(R.string.edit_item));
           getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
       }

       mProductName = findViewById(R.id.edit_name);
       mPrice = findViewById(R.id.edit_price);
       mQuantity = findViewById(R.id.edit_quantity);
       mSuppliersName = findViewById(R.id.edit_suppliers_name);
       mSuppliersPhone = findViewById(R.id.edit_suppliers_phone_number);

       mProductName.setOnTouchListener(mTouchListener);
       mPrice.setOnTouchListener(mTouchListener);
       mQuantity.setOnTouchListener(mTouchListener);
       mSuppliersName.setOnTouchListener(mTouchListener);
       mSuppliersPhone.setOnTouchListener(mTouchListener);

       minusItemButton = findViewById(R.id.minusButton);
       plusItemButton = findViewById(R.id.plusButton);
       callButton = findViewById(R.id.callButton);

       //Decrease items
       minusItemButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               quantityString = mQuantity.getText().toString();
               if (TextUtils.isEmpty(quantityString)) {
                   quantity = 0;
               }else {
                   quantity = Integer.parseInt(quantityString);
               }

               if (quantity > 0) {
                   quantity -=1;
                   quantityString = Integer.toString(quantity);
                   mQuantity.setText(quantityString);
               }else {
                   Toast.makeText(getBaseContext(), "Can't have less than 1 item!",
                           Toast.LENGTH_LONG).show();
               }
           }
       });

       //Increase items
       plusItemButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               quantityString = mQuantity.getText().toString();
               if (TextUtils.isEmpty(quantityString)) {
                   quantity = 0;
               }else {
                   quantity = Integer.parseInt(quantityString);
                   quantity += 1;
                   quantityString = Integer.toString(quantity);
                   mQuantity.setText(quantityString);
               }
           }
       });

       //Call to supplier intent

       callButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String suppliersPhone = mSuppliersPhone.getText().toString().trim();
               Intent intent = new Intent(Intent.ACTION_DIAL);
               intent.setData(Uri.parse("tel:" + suppliersPhone));
               if (intent.resolveActivity(getPackageManager()) != null){
                   startActivity(intent);
               }
           }
       });
   }

   private void saveItem() {
       String nameString = mProductName.getText().toString().trim();
       String priceString = mPrice.getText().toString().trim();
       String quantityString = mQuantity.getText().toString().trim();
       String suppliersName = mSuppliersName.getText().toString().trim();
       String suppliersPhone = mSuppliersPhone.getText().toString().trim();

       if (mCurrentItemUri == null && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString)
               && TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(suppliersName) &&
               TextUtils.isEmpty(suppliersPhone)) {
           return;
       }

       // Create a ContentValues object where column names are the keys
       ContentValues values = new ContentValues();
       values.put(ItemEntry.COLUMN_PRODUCT_NAME,nameString);
       values.put(ItemEntry.COLUMN_PRICE,priceString);
       values.put(ItemEntry.COLUMN_QANTITY,quantityString);
       values.put(ItemEntry.COLUMN_SUPPLIERS_NAME,suppliersName);
       values.put(ItemEntry.COLUMN_SUPPLIERS_PHONE,suppliersPhone);

       if (mCurrentItemUri == null) {
           Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);

           if (newUri == null) {
               Toast.makeText(this, getString(R.string.error_save_item),
                       Toast.LENGTH_SHORT).show();
           }else {
               Toast.makeText(this, getString(R.string.item_save), Toast.LENGTH_SHORT).show();
           }
       }else {
           int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);

           if (rowsAffected == 0) {
               Toast.makeText(this, getString(R.string.error_save_item),
                       Toast.LENGTH_SHORT).show();
           }else{
               Toast.makeText(this, getString(R.string.item_save), Toast.LENGTH_SHORT).show();
           }
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
    public boolean onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                saveItem();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mhasItemChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the item hasn't changed, continue with handling back button press
        if (!mhasItemChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all item attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_PRODUCT_NAME,
                ItemEntry.COLUMN_PRICE,
                ItemEntry.COLUMN_QANTITY,
                ItemEntry.COLUMN_SUPPLIERS_NAME,
                ItemEntry.COLUMN_SUPPLIERS_PHONE };

        return new CursorLoader(this,
                mCurrentItemUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_QANTITY);
            int suppliersNameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_SUPPLIERS_NAME);
            int suppliersPhoneColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_SUPPLIERS_PHONE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String suppliersName = cursor.getString(suppliersNameColumnIndex);
            long suppliersPhone = cursor.getLong(suppliersPhoneColumnIndex);

            // Update the views on the screen with the values from the database
            mProductName.setText(name);
            mPrice.setText(Integer.toString(price));
            mQuantity.setText(Integer.toString(quantity));
            mSuppliersName.setText(suppliersName);
            mSuppliersPhone.setText(Long.toString(suppliersPhone));
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mProductName.setText("");
        mPrice.setText("");
        mQuantity.setText("");
        mSuppliersName.setText("");
        mSuppliersPhone.setText("");

    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem() {
        // Only perform the delete if this is an existing item.
        if (mCurrentItemUri != null) {
            // Call the ContentResolver to delete the item at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}
