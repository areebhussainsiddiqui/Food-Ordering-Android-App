package com.example.cipherslab.myapplication.Activity_Classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    private ImageView btnback;
    private EditText name_txt, email_txt, phone_txt, password_txt;
    private Button btn_SignUp;
    private TextView SignIn_txt;

    String Url = "https://alshaikh-restaurant-server.herokuapp.com/api/user/register/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_register);

        ids ();
        SignIn_txt.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {startActivity (new Intent (RegisterActivity.this,Login_Activity.class));}
        });
        btn_SignUp.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                EmptyFields();
            }


        });
        btnback.setOnClickListener (new View.OnClickListener ( ) {

            @Override
            public void onClick(View v) {
                finish ( );
            }
        });

    }

    private  void ids(){
        btnback = findViewById (R.id.btn_back);
        name_txt = findViewById (R.id.Reg_Name);
        email_txt = findViewById (R.id.Reg_Email);
        phone_txt = findViewById (R.id.Reg_phone);
        password_txt = findViewById (R.id.Reg_Password);
        btn_SignUp = findViewById (R.id.Reg_btn_SignUp);
        SignIn_txt = findViewById (R.id.SignIn_txt);

    }
    private void PostRequest(){
        StringRequest stringRequest = new StringRequest (Request.Method.POST, Url, new Response.Listener <String> ( ) {
            @Override
            public void onResponse(String response) {
                Toast.makeText (getApplicationContext (),"Registered Successfully",Toast.LENGTH_SHORT).show ();
                finish ();
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText (getApplicationContext (),"User Already Registered",Toast.LENGTH_SHORT).show ();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params  = new HashMap<String,String> ();
                String Name = name_txt.getText ( ).toString ( ).trim ( );
                String email = email_txt.getText ( ).toString ( ).trim ( );
                String phone = phone_txt.getText ( ).toString ( ).trim ( );
                String password = password_txt.getText ( ).toString ( ).trim ( );
                params.put ("name",Name);
                params.put ("email",email);
                params.put ("number",phone);
                params.put ("password",password);



                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue (this);
        requestQueue.add (stringRequest);
    }
    private void EmptyFields() {
    String name, email, phone, password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    name = name_txt.getText ( ).toString ( );
    email = email_txt.getText ( ).toString ( );
    phone = phone_txt.getText ( ).toString ( );
    password = password_txt.getText ( ).toString ( );

    if (name.isEmpty ( )) {
        name_txt.requestFocus ( );
        name_txt.setError ("Field is Empty");

    }

    if (email.isEmpty ( )) {
        email_txt.requestFocus ( );
        email_txt.setError ("Field is Empty");
    }


    if (!email.matches (emailPattern) || email_txt.length ( ) <= 0) {
        email_txt.requestFocus ( );
        email_txt.setError ("Invalid Email");
    }
     /*else {
        Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
        // or
        textView.setText("valid email");
    }*/


    if (phone.isEmpty ( )) {
        phone_txt.requestFocus ( );
        phone_txt.setError ("Field is Empty");

    }
    if ((phone.length ( ) < 11)||(phone.length ( ) > 12)) {
        phone_txt.requestFocus ( );
        phone_txt.setError ("Invalid Phone");

    }
    if (password.isEmpty ( )) {
        password_txt.requestFocus ( );
        password_txt.setError ("Field is Empty");

    }
    if (password.length ( ) < 6) {
        password_txt.requestFocus ( );
        password_txt.setError ("Password Must be Atleast 6 Digits");

    } else {
        PostRequest ( );
    }
}

    /*

    private void sendpostreq() throws IOException {
        MediaType MEDIA_TYPE = MediaType.parse ("application/json");
        OkHttpClient client = new OkHttpClient ( );
        JSONObject postdata = new JSONObject ( );

        try {
            Toast.makeText (this,name+"ACCEPTED",Toast.LENGTH_SHORT).show ();
            postdata.put ("name", name);
            postdata.put ("email", email);
            postdata.put ("password", password);
            postdata.put ("number", phone);

            //   postdata.put ("name", "aneh");
        //    postdata.put ("email", "ads@hotmail.com");
        //    postdata.put ("password", "12345");
      //      postdata.put ("number", "12345234");


        } catch (JSONException e) {
            Toast.makeText (this,name,Toast.LENGTH_SHORT).show ();
            // TODO Auto-generated catch block
            e.printStackTrace ( );
        }

        RequestBody body = RequestBody.create (MEDIA_TYPE, postdata.toString ( ));

        Request request = new Request.Builder ( ).url (Url).post (body).header ("Accept", "application/json").header ("Content-Type", "application/json").build ( );

        client.newCall (request).enqueue (new Callback ( ) {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage ( ).toString ( );
                Log.w ("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body ( ).string ( );
                Log.e ("TAG", mMessage);
            }
        });
    }
*/


}







