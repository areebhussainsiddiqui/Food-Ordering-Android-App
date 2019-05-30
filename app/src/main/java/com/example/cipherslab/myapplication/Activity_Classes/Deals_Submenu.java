package com.example.cipherslab.myapplication.Activity_Classes;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cipherslab.myapplication.Adapters.sub_deals_Adapter;
import com.example.cipherslab.myapplication.ItemList.Deals_Model_List;
import com.example.cipherslab.myapplication.R;
import com.example.cipherslab.myapplication.Sqlite.DB;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Deals_Submenu extends AppCompatActivity {
    private ImageView backbtn,Wifi_IV;
    private RelativeLayout cartbtn;
    private TextView counter_txt;
    private ArrayList <Deals_Model_List> ArrayList;
    private sub_deals_Adapter mAdapter;
    private RequestQueue requestQueue;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private Sprite doubleBounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_deals__submenu);
        ids ();
        setCounter_txt ();
        doubleBounce = new DoubleBounce ();
        progressBar.setIndeterminateDrawable(doubleBounce);
        backbtn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                finish ( );

            }
        });
        cartbtn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) { startActivity(new Intent (Deals_Submenu.this, Cart_Activity.class));     }
        });
        setmRecyclerView();



        //  bindViews ( );


    }
    private void setCounter_txt(){

        DB db = new DB (this);

        if(db.Items_count ()<=0){
            counter_txt.setVisibility (View.GONE);
        } else {
            counter_txt.setVisibility (View.VISIBLE);
            counter_txt.setText (String.valueOf (db.Items_count ( ))); }

    }
    private void setmRecyclerView(){

        mRecyclerView = findViewById (R.id.Deals_sub_menu_recyclerview);
        mRecyclerView.setHasFixedSize (true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager (this));

        ArrayList = new ArrayList <> ();

        requestQueue = Volley.newRequestQueue (this);
        checkNetworkConnectionStatus();

    }
    private void ids(){
    backbtn = findViewById (R.id.sub_menu_backbtn);
    cartbtn = findViewById (R.id.menu_cartbtn);
    progressBar = (ProgressBar)findViewById(R.id.spin_kit);
    counter_txt = findViewById (R.id.counter_txt);
    Wifi_IV = findViewById (R.id.wifi_IV);

}
    private void ParseJson() {
        final int position_Array =getIntent().getIntExtra ("position",0);
        progressBar.isShown();
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://alshaikh-restaurant-server.herokuapp.com/api/deal/get";
        JsonObjectRequest request = new JsonObjectRequest (
                Request.Method.GET, url,
                null,
                new Response.Listener <JSONObject> ( ) {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray ("data").getJSONObject (position_Array).getJSONArray ("items");
                            //.getJSONObject (0).getJSONArray ("subMenu");

                            for(int i =0;i<array.length ();i++)
                            {
                                JSONObject hits = array.getJSONObject (i);
                                Log.d ("TAGGE", "onResponse: "+hits);
                                String menu_Id = hits.getString("menuId");
                                String Sub_menu_ID = hits.getString ("subMenuId");
                                String Product_Name = hits.getString ("subMenuName");
                                int Product_Price = hits.getInt ("price");
                                int Product_Total_Price = hits.getInt ("totalPrice");
                                String Product_Quantitiy = hits.getString ("quantity");

                         //       SubData(Product_ID,Product_Name,Product_Price,Product_Total_Price,Product_Quantitiy);
                                ArrayList.add (new Deals_Model_List (Product_Quantitiy,Product_Name,Product_Price,Product_Total_Price));

                            }
                            mAdapter = new sub_deals_Adapter (Deals_Submenu.this,ArrayList);
                            mRecyclerView.setAdapter (mAdapter);
                            progressBar.setVisibility(View.GONE);

                            //                progressDialog.dismiss ();
                        } catch (JSONException e) {
                            e.printStackTrace ( );
                        }
                    }
                }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace ();
            }
        });
        requestQueue.add (request);
    }
    private void checkNetworkConnectionStatus() {
        boolean wifiConnected;
        boolean mobileConnected;
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()){ //connected with either mobile or wifi
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected ) { //wifi connected
                ParseJson ( );
            }
            else if (mobileConnected){ //mobile data connected
                ParseJson ();
            }
        }
        else { //no internet connection
            Toast.makeText(getApplicationContext (),"INTERNET NOT AVAILABLE",Toast.LENGTH_SHORT).show ();
            progressBar.setVisibility (View.GONE);
            Wifi_IV.setVisibility (View.VISIBLE);
            Wifi_IV.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    recreate ();
                }});
        }
    }


   /* private void SubData(String Product_ID,String Productname,int Product_price,int Product_totalPrice,String quantitiy){
        Intent intent = new Intent(getApplicationContext () , Item_descrp.class);
        intent.putExtra("subMenuId",Product_ID);
        intent.putExtra("quantity",quantitiy);//2
        intent.putExtra("subMenuName",Productname);//Chicken Biryani (Double)
        intent.putExtra("price",Product_price);// 170,
        intent.putExtra("totalPrice",Product_totalPrice);//340

    }
*/
    /*
    private void NestedJson(){
        JSONObject student1 = new JSONObject();
        try {
            student1.put("id", "3");
            student1.put("name", "NAME OF STUDENT");
            student1.put("year", "3rd");
            student1.put("curriculum", "Arts");
            student1.put("birthday", "5/5/1993");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject student2 = new JSONObject();
        try {
            student2.put("id", "2");
            student2.put("name", "NAME OF STUDENT2");
            student2.put("year", "4rd");
            student2.put("curriculum", "scicence");
            student2.put("birthday", "5/5/1993");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        JSONArray jsonArray = new JSONArray();

        jsonArray.put(student1);
        jsonArray.put(student2);

        JSONObject studentsObj = new JSONObject();
        studentsObj.put("Students", jsonArray);



        String jsonStr = studentsObj.toString();

        System.out.println("jsonString: "+jsonStr);

    }
*/
}
