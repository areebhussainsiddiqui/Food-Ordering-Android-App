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
import com.example.cipherslab.myapplication.Adapters.Deals_Adapter;
import com.example.cipherslab.myapplication.ItemList.Deals_Model_List;
import com.example.cipherslab.myapplication.R;
import com.example.cipherslab.myapplication.Sqlite.DB;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Deals_Activity extends AppCompatActivity {

    private ImageView backbtn,wifi_IV;
    private RelativeLayout cartbtn;

    private TextView counter_txt;
    private ArrayList <Deals_Model_List> ArrayLists;
    private Deals_Adapter Adapter;
    private RequestQueue requestQueue;
    private RecyclerView mRecyclerView;
    ProgressBar progressBar;
    Sprite doubleBounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_deals_);

        backbtn = findViewById (R.id.sub_menu_backbtn);
        cartbtn = findViewById (R.id.menu_cartbtn);
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        doubleBounce = new DoubleBounce ();
        wifi_IV = findViewById (R.id.wifi_IV);
        progressBar.setIndeterminateDrawable(doubleBounce);
        counter_txt = findViewById (R.id.counter_txt);

        DB db = new DB (this);
        if(db.Items_count ()<=0){
            counter_txt.setVisibility (View.GONE);
        }
        else {
            counter_txt.setVisibility (View.VISIBLE);
        db.Counter (counter_txt);
        }
        backbtn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                finish ( );

            }
        });
        cartbtn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                  startActivity(new Intent (Deals_Activity.this, Cart_Activity.class));
            }
        });



        mRecyclerView = findViewById (R.id.Deals_RecyclerView);
        mRecyclerView.setHasFixedSize (true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager (this));
        ArrayLists = new ArrayList <> ();
        requestQueue = Volley.newRequestQueue (this);


       // ParseJson();
        checkNetworkConnectionStatus ();
    }

    //RECYCLER METHODS

    private void ParseJson() {
        progressBar.isShown ( );
        progressBar.setVisibility (View.VISIBLE);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo (ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected ( )) {
            String url = "https://alshaikh-restaurant-server.herokuapp.com/api/deal/get";
            JsonObjectRequest request = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener <JSONObject> ( ) {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray array = response.getJSONArray ("data");
                        //.getJSONObject (0).getJSONArray ("subMenu");

                        for (int i = 0; i < array.length ( ); i++) {
                            JSONObject hits = array.getJSONObject (i);
                            String Product_ID = hits.getString ("_id");
                            String Product_Name = hits.getString ("name");
                            String Product_Descrip = hits.getString ("description");
                            String Product_Price = hits.getString ("price");
                            String Product_ExpiryDate = hits.getString ("expiryDate");
                            String Product_Image = hits.getString ("picture");

                            Log.d ("BLACK", "onResponse: "+Product_ID);
//                         SData(Product_ID,Product_Name,Product_Price,Product_Image);


                            ArrayLists.add (new Deals_Model_List (Product_ID,Product_Name,Product_Price, Product_Image,Product_ExpiryDate,Product_Descrip));
                        }
                        Adapter = new Deals_Adapter (Deals_Activity.this, ArrayLists);
                        Log.d ("TAG", "onResponse: " + ArrayLists);
                        mRecyclerView.setAdapter (Adapter);
                        progressBar.setVisibility (View.GONE);

                        //                   progressDialog.dismiss ();
                    } catch (JSONException e) {
                        e.printStackTrace ( );
                    }
                }
            }, new Response.ErrorListener ( ) {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace ( );
                }
            });
            requestQueue.add (request);

            // Do whatever
        }
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
            wifi_IV.setVisibility (View.VISIBLE);
            wifi_IV.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    recreate ();
                }});
        }
        }



}
