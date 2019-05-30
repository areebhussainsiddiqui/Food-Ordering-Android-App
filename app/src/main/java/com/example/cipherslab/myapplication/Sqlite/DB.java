package com.example.cipherslab.myapplication.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cipherslab.myapplication.ItemList.SubItem_GS;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


public class DB extends SQLiteOpenHelper {

    private	static final int DATABASE_VERSION =	5;

    public static String DATABASE = "Order.db";
    //table Order


    public static String TableName               = "_OrderTable";
    private static String Col_Product_ID         = "product_id";
    private static String Col_Product_Name       = "product_name";
    private static String Col_Product_Price      = "product_price";
    private static String Col_Product_Quantity   = "product_qty";
    private static String Col_TotalPrice         = "product_sub_total";


    public DB(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String	CREATE_CONTACTS_TABLE = "CREATE	TABLE " + TableName + "(" + Col_Product_ID + " INTEGER PRIMARY KEY," + Col_Product_Name + " TEXT NOT NULL,"+ Col_Product_Price + " INTEGER NOT NULL,"+ Col_Product_Quantity + " INTEGER NOT NULL," + Col_TotalPrice + " INTEGER NOT NULL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }


    int iHits;
    int counter;
    int i=0;
    public ArrayList<SubItem_GS> listorder(){
        String sql = "select * from " + TableName;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SubItem_GS> Store = new ArrayList<>();
        SubItem_GS dataModel = null;
        Cursor cursor = db.rawQuery(sql, null);
        Cursor cur = db.rawQuery("SELECT SUM("+ Col_TotalPrice +") FROM _OrderTable", null);
        cur.moveToFirst ();
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String    _name = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                int _price = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(String.valueOf ("product_price"))));
                String    _quantity = cursor.getString(cursor.getColumnIndexOrThrow(String.valueOf ("product_qty")));
                int total = (Integer.parseInt(_quantity))*(_price);
                String    TotalPrice = cursor.getString(cursor.getColumnIndexOrThrow("product_sub_total"));
                iHits =cur.getInt(0);
                i = i+1;
                SaveInfo(String.valueOf (iHits));
       Store.add(new SubItem_GS (id,_name, _price ,Integer.parseInt(_quantity),Integer.parseInt(TotalPrice), total));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return Store;
    }

    public  int Items_count(){
        int count_items = 0;

        String sql = "SELECT COUNT(*) FROM "+TableName;

        Cursor cursor = getReadableDatabase ().rawQuery (sql,null);
        if(cursor.getCount ()>0){
            cursor.moveToFirst ();
            count_items = cursor.getInt (0);
        }
        cursor.close ();
        return count_items;
    }

    public void Counter(TextView textView){
        textView.setText (String.valueOf (Items_count ()));
    }




    public void totalmount(TextView textView) {
        try {
            textView.setText (String.valueOf (iHits));

        }
        catch (Exception ex){
            Toast.makeText (getApplicationContext (),iHits,Toast.LENGTH_SHORT).show ();
        }
}


    public int tot() {
        return iHits; }


    public void add_(SubItem_GS item){
        ContentValues values = new ContentValues();
        //values.put (Col_Product_ID,item.getId ());
        values.put(Col_Product_Name, item.getItemName ());
        values.put(Col_Product_Price, item.getItemPrice ());//1350
        values.put(Col_Product_Quantity, item.getItemQuantity ());
        values.put(Col_TotalPrice, item.getTotalItemPrice ());//13500

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TableName, null, values);
    }

    public void update_(SubItem_GS item){
        ContentValues values = new ContentValues();
        values.put(Col_Product_Name, item.getItemName ());
        values.put(Col_Product_Price, item.getItemPrice ());
        values.put(Col_Product_Quantity, item.getItemQuantity ());
        values.put(Col_TotalPrice, item.getTotalItemPrice ());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TableName, values, Col_Product_Name	+ "	= ?", new String[] { String.valueOf(item.getItemName ())});
    }


  public SQLiteDatabase delete_(int id  )
    {
        SQLiteDatabase db = this.getWritableDatabase ();
        db.delete(TableName, Col_Product_ID + " = ?",new String[] {String.valueOf(id)});
        return db;
    }
    public SQLiteDatabase Delete_AllData()
    {
        SQLiteDatabase db = this.getWritableDatabase ();
        db.execSQL("delete from "+ TableName);
        return db;
    }



    public boolean hasObject(String Product_Name,SubItem_GS item) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TableName + " WHERE " + Col_Product_Name + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {Product_Name});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;
            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            update_ (item);
            //here, count is records found
            Log.d("TAG", String.format("%d records found", count));

            //endregion

        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }


    public void SaveInfo(String IHits){

        SharedPreferences preferences = getApplicationContext ().getSharedPreferences ("AmountInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit ();
        editor.putString ("TotalAmount",IHits);
        editor.apply ();
    }

    public String DisplayInfo(){
        SharedPreferences preferences = getApplicationContext ().getSharedPreferences ("AmountInfo",Context.MODE_PRIVATE);
        String Amount  = preferences.getString ("TotalAmount","");
        return Amount;
    }


}
