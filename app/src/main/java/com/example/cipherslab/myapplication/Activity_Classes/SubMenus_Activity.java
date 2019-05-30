package com.example.cipherslab.myapplication.Activity_Classes;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.cipherslab.myapplication.Adapters.Sub_Menu_Adapter;
import com.example.cipherslab.myapplication.ItemList.SubMenu;
import com.example.cipherslab.myapplication.R;
import com.example.cipherslab.myapplication.Sqlite.DB;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubMenus_Activity extends AppCompatActivity {
    private ImageView backbtn,Wifi_IV;
    private RelativeLayout cartbtn;
    private TextView counter_txt;
    private ArrayList <SubMenu> ArrayList;
    private Sub_Menu_Adapter mAdapter;
    private RequestQueue requestQueue;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private Sprite doubleBounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_sub_menus_);
        ids ( );
        setmRecyclerView ( );
        setCounter_txt ( );
        backbtn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                finish ( );

            }
        });

        cartbtn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                startActivity (new Intent (SubMenus_Activity.this, Cart_Activity.class));
            }
        });


    }

    private void setCounter_txt() {
        DB db = new DB (this);
        if (db.Items_count ( ) <= 0) {
            counter_txt.setVisibility (View.GONE);
        } else {

            counter_txt.setVisibility (View.VISIBLE);
            counter_txt.setText (String.valueOf (db.Items_count ( )));
        }


    }

    private void setmRecyclerView() {
        mRecyclerView.setHasFixedSize (true);
        mRecyclerView.setLayoutManager (new LinearLayoutManager (this));
        ArrayList = new ArrayList <> ( );
        requestQueue = Volley.newRequestQueue (this);
        checkNetworkConnectionStatus();
    }

    private void ids() {
        mRecyclerView = findViewById (R.id.sub_menu_recyclerview);
        backbtn = findViewById (R.id.sub_menu_backbtn);
        cartbtn = findViewById (R.id.menu_cartbtn);
        progressBar = (ProgressBar) findViewById (R.id.spin_kit);
        doubleBounce = new DoubleBounce ( );
        progressBar.setIndeterminateDrawable (doubleBounce);
        counter_txt = findViewById (R.id.counter_txt);
        Wifi_IV =findViewById (R.id.wifi_IV);

    }

    private void ParseJson() {
        final int position_Array = getIntent ( ).getIntExtra ("position", 0);
        progressBar.isShown ( );
        progressBar.setVisibility (View.VISIBLE);

        //final ProgressDialog progressDialog = new ProgressDialog (this);
        //progressDialog.setMessage ("Data Fetching..");
        //progressDialog.show ();
        String url = "https://alshaikh-restaurant-server.herokuapp.com/api/menu/get";
        JsonObjectRequest request = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener <JSONObject> ( ) {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray ("data").getJSONObject (position_Array).getJSONArray ("subMenu");
                    //.getJSONObject (0).getJSONArray ("subMenu");

                    for (int i = 0; i < array.length ( ); i++) {
                        JSONObject hits = array.getJSONObject (i);
                        String Product_Name = hits.getString ("name");
                        int Product_Price = hits.getInt ("price");
                        String Product_Image = hits.getString ("picture");
                        String Product_Descrip = hits.getString ("description");
                        ArrayList.add (new SubMenu (Product_Name, Product_Image, Product_Price, Product_Descrip));
                    }
                    mAdapter = new Sub_Menu_Adapter (SubMenus_Activity.this, ArrayList);
                    mRecyclerView.setAdapter (mAdapter);
                    progressBar.setVisibility (View.GONE);

                    //                progressDialog.dismiss ();
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
    }

    private void checkNetworkConnectionStatus() {
        boolean wifiConnected;
        boolean mobileConnected;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo ( );
        if (activeInfo != null && activeInfo.isConnected ( )) { //connected with either mobile or wifi
            wifiConnected = activeInfo.getType ( ) == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType ( ) == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected) { //wifi connected
                ParseJson ( );
            } else if (mobileConnected) { //mobile data connected
                ParseJson ( );
            }
        } else { //no internet connection
            Toast.makeText (getApplicationContext ( ), "INTERNET NOT AVAILABLE", Toast.LENGTH_SHORT).show ( );
            progressBar.setVisibility (View.GONE);
            Wifi_IV.setVisibility (View.VISIBLE);
            Wifi_IV.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    recreate ( );
                }
            });
        }
    }
}
    /*private void bindViews() {
        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = (RecyclerView) findViewById (R.id.sub_menu_recyclerview);
        mRecyclerView.setHasFixedSize (true);
        mLayoutManager = new LinearLayoutManager (SubMenus_Activity.this);
        ArrayList = new ArrayList <> ();

        requestQueue = Volley.newRequestQueue (this);
        ParseJson();

        List <SubItem_GS> rowListItem = getAllItemList ( );
        mRecyclerView.setLayoutManager (mLayoutManager);
        mAdapter = new Sub_Menu_Adapter (SubMenus_Activity.this, ArrayList);
        mRecyclerView.setAdapter (mAdapter);
        mRecyclerView.setNestedScrollingEnabled (false);


    }*/
//RECYCLER METHODS