package data;

import android.provider.BaseColumns;

public class ItemConstant {

        public static abstract class ItemEntry implements BaseColumns {

            public static final String TABLE_NAME = "inventory";

            public static final String _ID = BaseColumns._ID;
            public static final String COLUMN_PRODUCT_NAME = "Product" + "Name";
            public static final String COLUMN_PRICE = "Price";
            public static final String COLUMN_QANTITY = "Quantity";
            public static final String COLUMN_SUPPLIERS_NAME = "SuppliersName";
            public static final String COLUMN_SUPPLIERS_PHONE = "SuppliersPhoneNumber";

        }
}
