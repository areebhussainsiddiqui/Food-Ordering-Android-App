package com.example.cipherslab.myapplication.Activity_Classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cipherslab.myapplication.Adapters.cart_Adapter;
import com.example.cipherslab.myapplication.ItemList.SubItem_GS;
import com.example.cipherslab.myapplication.R;
import com.example.cipherslab.myapplication.Shared_Prefences.UserSessionManager;
import com.example.cipherslab.myapplication.Sqlite.DB;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Cart_Activity extends AppCompatActivity {
    private int total = 0;
    DB database;
    cart_Adapter recycler;
    List <SubItem_GS> subItem_gs;
    UserSessionManager userSessionManager;
    private ImageView backbtn;
    private Button OrderPlaced_btn;
    private TextView totalAmount;
    private EditText Address_txt,nearby_txt,Phone_txt;
    Login_Activity activity = new Login_Activity ();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_cart);
        Log.d ("BAT", "onCreate: "+UserSessionManager.ID);
        ids ();
        db ( );
        userSessionManager = new UserSessionManager (this);
        totalAmount.setText (database.DisplayInfo ());
        OrderPlaced_btn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
           LoginStatus ( );
            }
        });
        backbtn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Backbtn ( );
            }
        });

    }

    private  void ids(){
        backbtn = findViewById (R.id.cart_backbtn);
        OrderPlaced_btn = findViewById (R.id.cart_place_order);
        totalAmount = findViewById (R.id.bill_total_amount);


    }
    public void db() {
        RecyclerView recyclerView;
        subItem_gs = new ArrayList <> ( );
        recyclerView = findViewById (R.id.cart_recyclerview);
        database = new DB (Cart_Activity.this);
        subItem_gs = database.listorder ( );
        recycler = new cart_Adapter (subItem_gs, new cart_Adapter.OnTotalPriceChanger ( ) {
            @Override
            public void onPriceChange(int change) {
                int subtract = Integer.parseInt (database.DisplayInfo ())-change;
                database.SaveInfo (String.valueOf (subtract));
                totalAmount.setText (database.DisplayInfo ());
            /*   int  a =  database.tot ();
               a = a -change;
               int cartBill = database.tot ()-change;
               database.SaveInfo (String.valueOf (total));
               totalAmount.setText (String.valueOf (a));
               database.SaveInfo (String.valueOf (a));
               totalAmount.setText ( database.DisplayInfo ());
            */
            }
        });

        final RecyclerView.LayoutManager reLayoutManager = new LinearLayoutManager (getApplicationContext ( ));
        recyclerView.setLayoutManager (reLayoutManager);
        recyclerView.setItemAnimator (new DefaultItemAnimator ( ));
        recyclerView.setAdapter (recycler);
        recycler.notifyDataSetChanged ( );

    }
    private void LoginStatus() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount (Cart_Activity.this);


        if (acct != null) {
            //    Toast.makeText (Cart_Activity.this,"YOU'RE ALREADY LOGGED IN..!",Toast.LENGTH_SHORT).show ();
            if(Integer.parseInt (database.DisplayInfo ()) <=0){
                Toast.makeText (getApplicationContext (),"YOUR CART IS EMPTY",Toast.LENGTH_SHORT).show ();
            }else {
                DialogBox ( );
            }
        } else if (userSessionManager.isUserLoggedIn ( ) == true) {
            //        Toast.makeText (Cart_Activity.this,"YOU'RE ALREADY shared LOGGED IN..!",Toast.LENGTH_SHORT).show ();
            if(Integer.parseInt (database.DisplayInfo ()) <=0){
                Toast.makeText (getApplicationContext (),"YOUR CART IS EMPTY",Toast.LENGTH_SHORT).show ();
            }else {
                DialogBox ( );
            }   //       finish ();
        } else if (AccessToken.getCurrentAccessToken ( ) != null) {
            //     LoginManager.getInstance ().logOut ();
            //      Toast.makeText (Cart_Activity.this,"YOU'RE ALREADY Facebook LOGGED IN..!",Toast.LENGTH_SHORT).show ();
            if(Integer.parseInt (database.DisplayInfo ()) <=0){
                Toast.makeText (getApplicationContext (),"YOUR CART IS EMPTY",Toast.LENGTH_SHORT).show ();
            }else {
                DialogBox ( );
            }
        } else {
            Intent intent = new Intent (Cart_Activity.this, Login_Activity.class);
            startActivity (intent);
        }

    }
    private void Backbtn() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount (Cart_Activity.this);
        if (acct != null) {
            Intent intent = new Intent (Cart_Activity.this, MainActivity.class);
            startActivity (intent);
        } else {
            finish ( );
        }

    }
    private void DialogBox() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder (Cart_Activity.this);
        View mview = getLayoutInflater ( ).inflate (R.layout.dialog_for_address, null);
        mbuilder.setView (mview);
        final AlertDialog dialog = mbuilder.create ( );
        dialog.show ( );
        dialog.setCanceledOnTouchOutside (false);
        dialog.getWindow ( ).setBackgroundDrawable (new ColorDrawable (Color.TRANSPARENT));
        Address_txt = dialog.findViewById (R.id.address_txt);
        nearby_txt = dialog.findViewById (R.id.nearby_txt);
        Phone_txt = dialog.findViewById (R.id.phone);
        RelativeLayout btnConfirm = dialog.findViewById (R.id.button_proceed);
        btnConfirm.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
            String address = Address_txt.getText ().toString ();
            String nearby = nearby_txt.getText ().toString ();
            String phone = Phone_txt.getText ().toString ();


                if (phone.isEmpty ( )) {
                    Phone_txt.requestFocus ( );
                    Phone_txt.setError ("Field is Empty");
                } if ((phone.length ( ) < 11)||(phone.length ( ) > 12)) {
                    Phone_txt.requestFocus ( );
                    Phone_txt.setError ("Invalid Phone");

                }
                if (address.isEmpty ( )) {
                    Address_txt.requestFocus ( );
                    Address_txt.setError ("Field is Empty");
                }if (nearby.isEmpty ( )) {
                    nearby_txt.requestFocus ( );
                    nearby_txt.setError ("Field is Empty");
                }

                else{

                    Details (address,nearby,phone);
                    Toast.makeText (getApplicationContext ( ), "ORDER PLACED", Toast.LENGTH_SHORT).show ( );
                    dialog.dismiss ();
                     }


            }
        });
        CircleImageView btnClose = dialog.findViewById (R.id.circle_cancel);
        btnClose.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                dialog.dismiss ( );
            }
        });
    }

    String order = userSessionManager.objOrderDet;
    JSONObject objectDetail;
    private void Details(final String address, final String Nearby, final String phone_No) {


        final String Address = address.trim ( );
        final String Near = Nearby.trim ( );
        final String phone = phone_No.trim ();

        String url = "https://alshaikh-restaurant-server.herokuapp.com/api/order/create";
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url, new Response.Listener <String> ( ) {
            @Override
            public void onResponse(String response) {
                Log.d ("BLACK", "onResponse: Success"+response);
           Toast.makeText (getApplicationContext (),"Order Placed Successfully",Toast.LENGTH_SHORT).show ();
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d ("BLACK", "onErrorResponse: "+error);
                Toast.makeText (getApplicationContext (),""+error,Toast.LENGTH_SHORT).show ();
            }
        }) {
            protected Map <String, String> getParams() throws AuthFailureError {

       SharedPreferences preferences = getApplicationContext ().getSharedPreferences ("login", Context.MODE_PRIVATE);
                String ID  = preferences.getString ("id","");
                String Name = preferences.getString ("name","");
                JSONArray jsonArray = null;

                try {
                     objectDetail = new JSONObject (getResults ().toString ());
                } catch (JSONException e) {
                    e.printStackTrace ( );
                }
              //  String Order = getResults ().toString ();
                int totalPrice = database.tot();
                Map <String, String> params = new HashMap <String, String> ( );
                params.put ("userID",ID);//4
                params.put ("name",Name);
                params.put ("phoneNumber",phone);//1
             //   params.put ("orderDetail",DataParser().toString ());//3
                params.put ("orderDetail",getResults().toString ());
                params.put ("address", Address);//2
                params.put ("nearBy", Near);//5
                params.put ("totalPrice", String.valueOf (totalPrice));//3
            //    params.put ("orderDetail", String.valueOf (obj));

                Log.d ("BLACK", "getParams: "
                        +activity.DisplayLoginID (Cart_Activity.this)+"\n"
                        +params+"\n");
              DB db = new DB (Cart_Activity.this);
              db.Delete_AllData ();
              db.SaveInfo ("0");
              Cart_Activity.this.finish ();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue (this);
        requestQueue.add (stringRequest);
    }
    public Object getResults() {

        String myPath = "/data/data/com.example.cipherslab.myapplication/databases/Order.db";// Set path to your database
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        String searchQuery = "SELECT  * FROM _OrderTable ";
        Cursor cursor = myDataBase.rawQuery(searchQuery, null);

        JSONArray resultSet = new JSONArray();
        JSONObject returnObj = new JSONObject();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {

                    try {

                        if (cursor.getString(i) != null) {
                            Log.d("TAG_NAME", cursor.getString(i));
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }

            }

            resultSet.put(rowObject);
            cursor.moveToNext();
        }


        cursor.close();
        Log.d("black", resultSet.toString());
        return resultSet;
    }


    private String submenu (){
        //SubMenu
        String sub_prod_id =getIntent().getStringExtra("subMenuId");
        String sub_prod_qty =getIntent().getStringExtra("quantity");
        String sub_prod_name =getIntent().getStringExtra("subMenuName");
        String sub_product_price = getIntent().getStringExtra("price");
        String sub_prod_total = getIntent().getStringExtra ("totalPrice");

        JSONObject SubMenu = new JSONObject();
        try {
            SubMenu.put("subMenuId", sub_prod_id);
            SubMenu.put("subMenuName", sub_prod_name);
            SubMenu.put("price", sub_product_price);
            SubMenu.put("quantity", sub_prod_qty);
            SubMenu.put("totalPrice", sub_prod_total);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
return String.valueOf (SubMenu);
    }
    private JSONObject  DataParser(){
//Product
    String prod_id =getIntent().getStringExtra("product_id");
    String prod_name =getIntent().getStringExtra("product_name");
    String  product_price = getIntent().getStringExtra("product_price");
    String prod_image = getIntent().getStringExtra ("product_img");
    String count = getIntent ().getStringExtra ("product_qty");
        String subTotal = getIntent ().getStringExtra ("product_sub_total");

        //Product_SubTitle Need to be Add...

        JSONObject orderDetail = new JSONObject();
        try {
            orderDetail.put("product_id", prod_id);
            orderDetail.put("product_name", prod_name);
            orderDetail.put("product_price", product_price);
           orderDetail.put("product_qty", count);
            orderDetail.put("product_img", prod_image);
            orderDetail.put("product_sub_total", subTotal);
            orderDetail.put ("product_items", submenu());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
return orderDetail;
    }
}