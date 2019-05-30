package com.example.cipherslab.myapplication.Activity_Classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cipherslab.myapplication.R;
import com.example.cipherslab.myapplication.Shared_Prefences.UserSessionManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    public   String IDs;
    private TextView signuptxt, alertLog;
    private EditText username, password;
    private Button googlebtn;
    private Button signIn_btn;
    private SharedPreferences sharedpreferences;
    private SignInButton signInButton;
    private LoginButton loginButton;
    private static final int RC_SIGN_IN = 420;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;
    private UserSessionManager userSessionManager;
    private JSONObject object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        FacebookSdk.sdkInitialize (getApplicationContext ( ));
        setContentView (R.layout.activity_login);

        username = findViewById (R.id.Login_usernameTxt);
        password = findViewById (R.id.Login_passwordTxt);
        signIn_btn = findViewById (R.id.login_btn);
        alertLog = findViewById (R.id.Alert_logged);
        alertLog.setVisibility (View.GONE);
        userSessionManager = new UserSessionManager (this);

        signIn_btn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                EmptyFields ( );

            }
        });


///FACEBOOK
        callbackManager = CallbackManager.Factory.create ( );
        loginButton = findViewById (R.id.facebook_login_button);

        Button btn = findViewById (R.id.fb);
        btn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                loginButton.performClick ( );

            }
        });

        List <String> permissionNeeds = Arrays.asList ("user_photos", "email", "user_birthday", "public_profile", "AccessToken");
        loginButton.registerCallback (callbackManager, new FacebookCallback <LoginResult> ( ) {

            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println ("onSuccess");
                String accessToken = loginResult.getAccessToken ( ).getToken ( );
                Toast.makeText (Login_Activity.this, "Login Successfully", Toast.LENGTH_SHORT).show ( );
                finish ( );
                Log.i ("accessToken", accessToken);


            /*    GraphRequest request = GraphRequest.newMeRequest (loginResult.getAccessToken ( ), new GraphRequest.GraphJSONObjectCallback ( ) {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.i ("LoginActivity", response.toString ( ));
                        try {
                            //  id = object.getString("id");
                            try {
                            finish ();
                                //    URL profile_pic = new URL ("http://graph.facebook.com/" + "id" + "/picture?type=large");
                            //    Log.i ("profile_pic", profile_pic + "");

                            } catch (Exception e){ //(MalformedURLException e) {
                                e.printStackTrace ( );
                            }
                            //   name = object.getString("name");
                            //   email = object.getString("email");
                            //   gender = object.getString("gender");
                            //  birthday = object.getString("birthday");
                        } catch (Exception e) {//(JSONException e) {
                            e.printStackTrace ( );
                        }
                    }
                });

                Bundle parameters = new Bundle ( );
                parameters.putString ("fields", "id,name,email,gender, birthday");
              request.setParameters (parameters);
                request.executeAsync ( );*/
            }

            @Override
            public void onCancel() {
                Toast.makeText (getApplicationContext ( ), "Login Cancel", Toast.LENGTH_SHORT).show ( );
                System.out.println ("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText (getApplicationContext ( ), "Some Thing Went Wrong" , Toast.LENGTH_LONG).show ( );
                System.out.println ("onError" + exception);

              }
        });

        //FACEBOOK END//


        signuptxt = findViewById (R.id.signUp_txt_login);
        signuptxt.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Login_Activity.this, RegisterActivity.class);
                startActivity (intent);


            }
        });


        //Google LOGIN
        googlebtn = findViewById (R.id.google);
        initializeControls ( );
        initializeGPlusSettings ( );
    }
    @Override
    public void onStart() {
        super.onStart ( );

        OptionalPendingResult <GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn (mGoogleApiClient);
        if (opr.isDone ( )) {
            Log.d ("TAG", "Got cached sign-in");
            GoogleSignInResult result = opr.get ( );
            handleGPlusSignInResult (result);
        } else {
            opr.setResultCallback (new ResultCallback <GoogleSignInResult> ( ) {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleGPlusSignInResult (googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d ("TAG", "onConnectionFailed:" + connectionResult);

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent (data);
            handleGPlusSignInResult (result);
        } else {
            super.onActivityResult (requestCode, responseCode, data);
            callbackManager.onActivityResult (requestCode, responseCode, data);
        }
    }

    private void initializeControls() {
        signInButton = (SignInButton) findViewById (R.id.btn_sign_in);
    /* signInButton.setOnClickListener (new View.OnClickListener ( ) {
         @Override
         public void onClick(View v) {
             signIn();
         }
     });*/
        signInButton.setOnClickListener (this);
    }
    private void initializeGPlusSettings() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder (GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail ( ).build ( );
        mGoogleApiClient = new GoogleApiClient.Builder (this).
                enableAutoManage (this, this).addApi (Auth.GOOGLE_SIGN_IN_API, gso).build ( );
        signInButton.setSize (SignInButton.SIZE_STANDARD);
        signInButton.setScopes (gso.getScopeArray ( ));
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent (mGoogleApiClient);
        startActivityForResult (signInIntent, RC_SIGN_IN);


    }
    private void handleGPlusSignInResult(GoogleSignInResult result) {
        Log.d ("TAG", "handleSignInResult:" + result.isSuccess ( ));
        if (result.isSuccess ( )) {


            GoogleSignInAccount acct = result.getSignInAccount ( );
            //Fetch values
            String personName = acct.getDisplayName ( );
            String personPhotoUrl = acct.getPhotoUrl ( ).toString ( );
            String email = acct.getEmail ( );
            String familyName = acct.getFamilyName ( );
            Log.e ("TAG", "Name: " + personName + ", email: " + email + ", Image: " + personPhotoUrl + ", Family Name: " + familyName);
            finish ( );
            updateUI (true);
        } else {
            updateUI (false);
        }
    }
    public void onClick(View v) {
        if (v == googlebtn) {
            // signInButton.performClick ();
            signIn ( );
        }
    }
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            signInButton.setVisibility (View.GONE);
        } else {
            signInButton.setVisibility (View.VISIBLE);
        }
    }
    private void EmptyFields() {
        String name, pass;
        name = username.getText ( ).toString ( );
        pass = password.getText ( ).toString ( );
     /*   if(name.isEmpty ()|| pass.isEmpty ()){
            Toast.makeText (Login_Activity.this,"Empty Fields",Toast.LENGTH_SHORT).show ();
        }
        else*/
        if (name.isEmpty ( )) {
            username.requestFocus ( );
            username.setError ("Field is Empty");
        }
        if (pass.isEmpty ( )) {
            password.requestFocus ( );
            password.setError ("Field is Empty");

        } else {
            userLogin ( );
            //Toast.makeText (Login_Activity.this,"Login",Toast.LENGTH_SHORT).show ();
        }
    }
    String  Us_name,Us_id;
    private void userLogin() {
        String URL = "https://alshaikh-restaurant-server.herokuapp.com/api/user/login";
        final String User = username.getText ( ).toString ( ).trim ( );
        final String Pass = password.getText ( ).toString ( ).trim ( );

        StringRequest stringRequest = new StringRequest (Request.Method.POST, URL, new Response.Listener <String> ( ) {
            @Override
            public void onResponse(String response) {
                if (!response.trim ( ).equals ("Failed")) {
                    Toast.makeText (Login_Activity.this, "Login SuccessFully" , Toast.LENGTH_SHORT).show ( );
//                    Toast.makeText (Login_Activity.this, "SuccessFully" + response, Toast.LENGTH_LONG).show ( );

                    try {
                       object = new JSONObject (response);
                        Us_name = object.getJSONObject ("data").getString("name");
                        Us_id = object.getJSONObject ("data").getString("_id");

                        Log.v("OBJECt",Us_id+""+Us_name);
                        SaveLoginID(Us_id,Us_name);
                    }
                       catch (JSONException e) {
                        e.printStackTrace ( );
                    }

                    userSessionManager.createUserLoginSession (User,Pass);

                    finish ();
                } else {
//                    Toast.makeText (Login_Activity.this, response, Toast.LENGTH_SHORT);

                    Toast.makeText (Login_Activity.this, " Login Failed", Toast.LENGTH_SHORT);
                    Log.d ("TAG", "onResponse: "+ response);
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText (Login_Activity.this, error.toString ( ), Toast.LENGTH_LONG).show ( );
                Toast.makeText (Login_Activity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show ( );
            }
        }) {
            @Override
            protected Map <String, String> getParams() throws AuthFailureError {
                Map <String, String> map = new HashMap <String, String> ( );
                map.put ("email", User);
                map.put ("password", Pass);

                return map;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue (this);
        requestQueue.add (stringRequest);
    }


    public void SaveLoginID(String id,String Name){

        SharedPreferences preferences = getApplicationContext ().getSharedPreferences ("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit ();
        editor.putString ("id",id);
        editor.putString ("name",Name);

        editor.apply ();
    }

    public String DisplayLoginID(Context context){
        SharedPreferences preferences = context.getSharedPreferences ("login",Context.MODE_PRIVATE);
        String ID  = preferences.getString ("id","");
        String Name = preferences.getString ("name","");
        return ID;
    }


}