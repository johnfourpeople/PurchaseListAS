package ru.JB.develop.purchaselist.Database;

/**
 * Created by JB on 02.07.2015.
 */
public final class Contract {
    public static final class Products{
        public static final String TABLE ="Products";
        public static final String _ID ="ProductsId";
        public static final String NAME ="ProductName";
        public static final String PRICE ="ProductPrice";
        public static final String UNIT_ID ="UnitsId";
    }
    public static final class Purchase {

        public static final String TABLE ="Purchases";
        public static final String _ID="PurchaseId";
        public static final String NUMBER ="NumberOfPurchases";
        public static final String IS_BOUGHT ="PurchaseIsBought";
        public static final String PRODUCT_ID ="ProductsId";
    }
    public static final class Unit {
        public static final String TABLE ="Units";
        public static final String _ID ="UnitsId";
        public static final String NAME ="UnitsName";
    }

}
