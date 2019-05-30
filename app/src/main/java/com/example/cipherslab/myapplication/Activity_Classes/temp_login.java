package com.example.cipherslab.myapplication.Activity_Classes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cipherslab.myapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class temp_login extends AppCompatActivity {

    private String personName = null, personGivenName= null, personFamilyName= null ,personEmail= null, personId= null;
    private GoogleSignInClient mGoogleSignInClient;
    private Button sign_out;
    private TextView nameTV;
    private TextView emailTV;
    private TextView idTV;
    private ImageView photoIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_login);
        ids ();
        google ();

         }
    private void ids(){
    sign_out = findViewById(R.id.log_out);
    nameTV = findViewById(R.id.name);
    emailTV = findViewById(R.id.email);
    idTV = findViewById(R.id.id);
    photoIV = findViewById(R.id.photo);

}
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(temp_login.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(temp_login.this, MainActivity.class));
                        finish();
                    }
                });
    }
    private void google(){
    //Google
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

    // Build a GoogleSignInClient with the options specified by gso.
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(temp_login.this);
    if (acct != null) {
        personName = acct.getDisplayName();
        personGivenName = acct.getGivenName();
        personFamilyName = acct.getFamilyName();
        personEmail = acct.getEmail();
        personId = acct.getId();
        Uri personPhoto = acct.getPhotoUrl();

        nameTV.setText("Name: "+personName);
        emailTV.setText("Email: "+personEmail);
        idTV.setText("ID: "+personId);
        Glide.with(this).load(personPhoto).into(photoIV);
    }

    sign_out.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            signOut();
        }
    });

}

}