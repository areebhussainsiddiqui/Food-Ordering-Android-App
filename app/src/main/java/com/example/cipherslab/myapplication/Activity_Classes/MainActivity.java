package com.example.cipherslab.myapplication.Activity_Classes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cipherslab.myapplication.Adapters.Main_Adapater;
import com.example.cipherslab.myapplication.ItemList.Main_Model_List;
import com.example.cipherslab.myapplication.R;
import com.example.cipherslab.myapplication.Shared_Prefences.UserSessionManager;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    ProgressBar progressBar;
    Sprite doubleBounce;
    private ImageView wifi_IV;
    UserSessionManager session;
    TextView textView;
    private  String Bannner;
    private List<String> imageURLs;
    private int  index = 0;
    ArrayList <Main_Model_List> ArrayLists;
    Main_Adapater Adapter;
    RequestQueue requestQueue;
    private  Login_Activity log;
    RecyclerView mRecyclerView;
    RelativeLayout circleCart;
    private ViewFlipper vf;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        ids ( );

        ImageFlipper ( );
        setSupportActionBar (toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener (toggle);
        toggle.syncState ( );

        NavigationView navigationView = (NavigationView) findViewById (R.id.nav_view);
        navigationView.setNavigationItemSelectedListener (this);

        session = new UserSessionManager (getApplicationContext ( ));
        doubleBounce = new DoubleBounce ( );
        progressBar.setIndeterminateDrawable (doubleBounce);
        circleCart.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                startActivity (new Intent (MainActivity.this, Cart_Activity.class));
            }
        });
        mRecyclerView.setHasFixedSize (true);
        mRecyclerView.setLayoutManager (new LinearLayoutManager (this));
        ArrayLists = new ArrayList <> ( );
        requestQueue = Volley.newRequestQueue (this);
        checkNetworkConnectionStatus ( );


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
        if (drawer.isDrawerOpen (GravityCompat.START)) {
            drawer.closeDrawer (GravityCompat.START);
        } else {
            super.onBackPressed ( );
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId ( );

        if (id == R.id.nav_deals) {
            startActivity (new Intent (MainActivity.this, Deals_Activity.class));
        }
        if (id == R.id.nav_about) {
            Toast.makeText (getApplicationContext ( ), "ABOUT ", Toast.LENGTH_SHORT).show ( );
        } else if (id == R.id.nav_feedback) {
            startActivity (new Intent (MainActivity.this, FeedBack_Activity.class));

        } else if (id == R.id.nav_login) {
            Login ( );
        }
        DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
        drawer.closeDrawer (GravityCompat.START);
        return true;
    }

    private void ParseJson() {
        progressBar.isShown ( );
        progressBar.setVisibility (View.VISIBLE);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo (ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected ( )) {
            String url = "https://alshaikh-restaurant-server.herokuapp.com/api/menu/get";
            JsonObjectRequest request = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener <JSONObject> ( ) {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray array = response.getJSONArray ("data");
                        //.getJSONObject (0).getJSONArray ("subMenu");

                        for (int i = 0; i < array.length ( ); i++) {
                            JSONObject hits = array.getJSONObject (i);
                            String Product_Id = hits.getString ("_id");
                            String Product_Name = hits.getString ("name");
                            String Product_Image = hits.getString ("picture");
                            ArrayLists.add (new Main_Model_List (Product_Name, Product_Image));
                            Log.d ("TAG_ID", "onResponse: id " + Product_Id);
                        }
                        Adapter = new Main_Adapater (MainActivity.this, ArrayLists);
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

        }
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
            wifi_IV.setVisibility (View.VISIBLE);
            wifi_IV.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    recreate ();
                }
            });
            progressBar.setVisibility (View.GONE);
        }
    }

    private void ids() {
        wifi_IV = findViewById (R.id.wifi_IV);
        toolbar = findViewById (R.id.toolbar);
        vf = findViewById (R.id.main_viewfipper);
        progressBar = (ProgressBar) findViewById (R.id.spin_kit);
        circleCart = findViewById (R.id.Main_cartButton);

        mRecyclerView = findViewById (R.id.Main_RecyclerView);

    }



    private void signOut() {
        GoogleSignInClient mGoogleSignInClient;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder (GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail ( ).build ( );

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient (this, gso);
        mGoogleSignInClient.signOut ( ).addOnCompleteListener (this, new OnCompleteListener <Void> ( ) {
            @Override
            public void onComplete(@NonNull Task <Void> task) {
                Toast.makeText (MainActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show ( );
            }
        });
    }

    private void Login() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount (this);

        if (account != null) {
            DialogBox ( );
            //signOut ();
            //menuItem.setTitle ("LogOut");
            //   Toast.makeText (getApplicationContext (),"You are Already Logged In",Toast.LENGTH_SHORT).show ();
        } else if (session.isUserLoggedIn ( ) == true) {
            DialogBox ( );
            log = new Login_Activity ();
            log.SaveLoginID (null,null);
            //session.logoutUser ( );
        } else if (AccessToken.getCurrentAccessToken ( ) != null) {
            DialogBox ( );

            //       LoginManager.getInstance ().logOut ();
//        Toast.makeText (this,"YOU'RE LOG OUT Facebook..!",Toast.LENGTH_SHORT).show ();


        } else {
            startActivity (new Intent (MainActivity.this, Login_Activity.class));

        }

    }

    private void DialogBox() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder (MainActivity.this);
        View mview = getLayoutInflater ( ).inflate (R.layout.logout_dialog_confirm, null);
        mbuilder.setView (mview);
        final AlertDialog dialog = mbuilder.create ( );

        dialog.show ( );
        dialog.getWindow ( ).setBackgroundDrawable (new ColorDrawable (Color.TRANSPARENT));
        Button btnConfirm = (Button) mview.findViewById (R.id.dialog_confirm);
        btnConfirm.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                //  Toast.makeText (getApplicationContext (),"CHILD ADDED IN YOUR LIST",Toast.LENGTH_SHORT).show ();

                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount (MainActivity.this);

                if (account != null) {

                    signOut ( );
                    UserSessionManager.ID = null;
                    dialog.dismiss ( );
                    //menuItem.setTitle ("LogOut");
                    //   Toast.makeText (getApplicationContext (),"You are Already Logged In",Toast.LENGTH_SHORT).show ();
                } else if (session.isUserLoggedIn ( ) == true) {
                    session.logoutUser ( );
                    UserSessionManager.ID = null;
                    dialog.dismiss ( );
                } else if (AccessToken.getCurrentAccessToken ( ) != null) {
                    LoginManager.getInstance ( ).logOut ( );
                    UserSessionManager.ID = null;
                    dialog.dismiss ( );
                    //    Toast.makeText (this,"YOU'RE LOG OUT Facebook..!",Toast.LENGTH_SHORT).show ();


                }

            }
        });
        Button btnClose = (Button) dialog.findViewById (R.id.dialog_cancel);
        btnClose.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                dialog.dismiss ( );
            }
        });

    }

    private void ImageFlipper() {
        int image[] = {R.drawable.image3, R.drawable.image2, R.drawable.image1};

        for (int i = 0; i < image.length; i++) {
            ImageFlipper (image[i]);
        }
        for (int images : image) {
            ImageFlipper (images);
        }

    }

    private void ImageFlipper(int i) {
        ImageView imageView = new ImageView (this);
        imageView.setBackgroundResource (i);
        vf.addView (imageView);
        vf.setFlipInterval (3000);
        vf.setAutoStart (true);

        //Animation
        vf.setInAnimation (this, android.R.anim.slide_in_left);
        vf.setOutAnimation (this, android.R.anim.slide_out_right);
    }
}




