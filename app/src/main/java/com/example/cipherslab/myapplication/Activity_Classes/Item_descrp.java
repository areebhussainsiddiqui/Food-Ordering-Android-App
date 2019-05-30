package com.example.cipherslab.myapplication.Activity_Classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cipherslab.myapplication.ItemList.SubItem_GS;
import com.example.cipherslab.myapplication.R;
import com.example.cipherslab.myapplication.Shared_Prefences.UserSessionManager;
import com.example.cipherslab.myapplication.Sqlite.DB;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Item_descrp extends AppCompatActivity {
   private      String SubmenuID ,MenuID,SubMenuItemName,SubMenuItemQty,SubMenuItemPrice;
   private      DB db_helper;
   private      ImageView image,add,remove,backbtn;
   private      RelativeLayout CartBtn;
   private      TextView productname,productprice,count_txt,Descrption,counter_txt;
   private      String prod_name,product_price,descrp,c,cImage,Deal_Name,Deal_id;
   private      Button add_btn;
   private      int prod_image , count=1,product_price_Int,P_mult_Q;
   private      SubItem_GS subItem_gs;
   private      ArrayList<SubItem_GS> allContacts=new ArrayList<>();
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_descrp);
          userSessionManager = new UserSessionManager (this);
            IDs ();
            dataFetch();
            Visibilty();


        CartBtn.setOnClickListener (new View.OnClickListener ( ) {
           @Override
           public void onClick(View v) { startActivity (new Intent (Item_descrp.this,Cart_Activity.class));}});
        add_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        DataAddOrUpdate();
     }
});
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Add ();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Remove();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void DataAddOrUpdate(){
        subItem_gs =new SubItem_GS (prod_name,product_price_Int,count,(count*product_price_Int));

        if(db_helper.hasObject (prod_name,subItem_gs)== true){
            Toast.makeText (getApplicationContext (),"Item Update",Toast.LENGTH_LONG).show ();
            finishAffinity();
            moveTaskToBack(false);
            startActivity (new Intent (Item_descrp.this,MainActivity.class));
        }
        else {
            db_helper.add_ (subItem_gs);
           // Toast.makeText (getApplicationContext ()," "+count+" "+P_mult_Q+"  "+product_price_Int+"  "+(count*product_price_Int), Toast.LENGTH_SHORT).show ( );
            userSessionManager.objOrderDet =OrderDetail ().toString ();
            Log.d ("BLACK", "dataFetch: "+OrderDetail ().toString ());

             Toast.makeText(Item_descrp.this, "Item Added", Toast.LENGTH_SHORT).show();
            finishAffinity();
            moveTaskToBack(false);
            startActivity (new Intent (Item_descrp.this,MainActivity.class));
//            Log.d ("BLACK", "dataFetch: "+OrderDetail ().toString ()+"\n"+Product_items ().toString ());
          /*  Intent intent = new Intent(getApplicationContext () , Cart_Activity.class);
             intent.putExtra("Data",DataParser ().toString ());
             getApplicationContext ().startActivity(intent);
        */}
    }
    private void Remove(){
        count=count-1;
        if(count<=1){
            String c = Integer.toString(1);
            count_txt.setText(c);
            count=1;
        }
        else{
            String c = Integer.toString(count);
            count_txt.setText(c);

        }

    }
    private void Add(){
        count=count+1;
        if(count<=1){
            String c = Integer.toString(1);
            count_txt.setText(c);
            count=1;
        }
        else{
            String c = Integer.toString(count);
            count_txt.setText(c);

        }

    }
    private void Visibilty(){
        db_helper = new DB (this);

        if(db_helper.Items_count ()<=0){
            counter_txt.setVisibility (View.GONE);
        }
        else {
            counter_txt.setVisibility (View.VISIBLE);
            counter_txt.setText (String.valueOf (db_helper.Items_count ( )));
        }
    }
    private void  IDs(){
        image = findViewById(R.id.image);
        productname = findViewById(R.id.detailed_product_name);
        productprice = findViewById(R.id.detailed_product_price);
        Descrption = findViewById (R.id.detailed_product_Descrip);
        add = findViewById(R.id.detailed_product_add);
        remove = findViewById(R.id.detailed_product_remove);
        backbtn = findViewById(R.id.detailed_product_backbtn);
        count_txt = findViewById(R.id.detailed_product_count);
        add_btn = findViewById(R.id.detailed_addcart_btn);
        CartBtn = findViewById (R.id.menu_cartbtn);
        counter_txt = findViewById (R.id.counter_txt);

    }
    private void dataFetch(){

        db_helper = new DB (this);
        prod_name =getIntent().getStringExtra("productname");
        product_price = getIntent().getStringExtra("productprice");
        prod_image = getIntent().getIntExtra("image",0);
        descrp = getIntent ().getStringExtra ("product_Descrip");
        cImage=  getIntent().getStringExtra ("ProductImage");

        //MENU SERVER IDS
        Deal_id     = getIntent ().getStringExtra ("DealMenuID");
        Deal_Name   = getIntent ().getStringExtra ("DealMenuName");
        //SUBMENU SERVER ITEMS
        SubmenuID       = getIntent ().getStringExtra ("subMenuID");
        MenuID          = getIntent ().getStringExtra ("menuId");
        SubMenuItemName = getIntent ().getStringExtra ("SubMenuItemName");
        SubMenuItemPrice  = getIntent ().getStringExtra ("SubMenuItemPrice");
        SubMenuItemQty  = getIntent ().getStringExtra ("SubMenuItemQty");


        Log.d ("BLACK", "onCreate: "+descrp+"\n"+Deal_id+"\n"+Deal_Name+"\n"+SubmenuID +"\n"+MenuID);
        productname.setText(prod_name);
        productprice.setText(product_price);
        Descrption.setText (descrp);
        c = Integer.toString(count);
        Picasso.with (this).load (cImage).fit ().centerInside ().into (image);
        count_txt.setText(c);
        product_price_Int =Integer.parseInt(product_price);
        allContacts = db_helper.listorder ();
       // Log.d ("BLACK", "dataFetch: "+OrderDetail ().toString ()+"\n"+Product_items ().toString ());
    }

    private  Object OrderDetail(){
    int prod_price = Integer.parseInt (product_price);
    String sub_tot =  String.valueOf (count*prod_price);
        JSONObject orderDetail = new JSONObject();
        try {
            orderDetail.put("product_id", Deal_id);
            orderDetail.put("product_name", Deal_Name);
            orderDetail.put("product_price", product_price);
            orderDetail.put("product_qty", count);
            orderDetail.put("product_img", cImage);
            orderDetail.put("product_sub_total",sub_tot );
            orderDetail.put ("product_items", Product_items ());
       //     orderDetail.put ("product_key","deal");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return orderDetail;
    }


    private Object Product_items(){
        JSONObject Obj_Product_item = new JSONObject();
        try {
            Obj_Product_item.put("menuId", MenuID);
            Obj_Product_item.put("subMenuId", SubmenuID);
            Obj_Product_item.put("subMenuName", SubMenuItemName);
            Obj_Product_item.put("quantity", SubMenuItemQty);
            Obj_Product_item.put("price", SubMenuItemPrice);
            Obj_Product_item.put ("totalPrice", product_price);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Obj_Product_item;
    }
}

