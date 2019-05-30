package com.example.cipherslab.myapplication.Activity_Classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cipherslab.myapplication.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FeedBack_Activity extends AppCompatActivity {

  private  EditText name_user,descrp;
  private  Button btn;
  private ImageView backbtn;
  private  String Name,DESCRIP,Date;
  private  String Url = "https://alshaikh-restaurant-server.herokuapp.com/api/feedback/post";
  private  Date date = new Date ();
  private  DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_feed_back);
        ids ();

        dateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
        Date =  dateFormat.format(date).toString (); //2016/11/16 12:08:43

        btn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
        checkNetworkConnectionStatus ();
            }

        });
        backbtn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
             finish ();
            }
        });
        name_user.setText ("");
        descrp.setText ("");
    }

    private void ids(){
        backbtn = findViewById (R.id.cart_backbtn);
        name_user = findViewById (R.id.name);
        descrp = findViewById (R.id.descript);
        btn = findViewById (R.id.submit);


    }
    private void PostRequest(){
        StringRequest stringRequest = new StringRequest (Request.Method.POST, Url, new Response.Listener <String> ( ) {
            @Override
            public void onResponse(String response) {
            Toast.makeText (getApplicationContext (),""+response,Toast.LENGTH_SHORT).show ();
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText (getApplicationContext (),""+error,Toast.LENGTH_SHORT).show ();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError{

                Map<String,String> params  = new HashMap<String,String> ();
                String Name = name_user.getText ().toString ();
                String FeedBack = descrp.getText ().toString ();
                String Date =  dateFormat.format(date).toString (); //2016/11/16 12:08:43
                params.put ("name",Name);
                params.put ("message",FeedBack);
                params.put ("date",Date);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue (this);
        requestQueue.add (stringRequest);
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
                PostRequest ( );
            }
            else if (mobileConnected){ //mobile data connected
                PostRequest ();
            }
        }
        else { //no internet connection
            Toast.makeText(getApplicationContext (),"INTERNET NOT AVAILABLE",Toast.LENGTH_SHORT).show ();
               }
    }

    }
