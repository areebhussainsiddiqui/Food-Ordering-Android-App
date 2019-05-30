package com.example.cipherslab.myapplication.Shared_Prefences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.cipherslab.myapplication.Activity_Classes.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class Logout {
    GoogleSignInClient mGoogleSignInClient;


    /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
*/
    // Build a GoogleSignInClient with the options specified by gso.
  /*  mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
*/
    public void Sign(Context context){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
        if (acct != null) {
            SignOut (context);
        }
        else {
            Toast.makeText (context,"YOU ARE NOT LOGGED IN",Toast.LENGTH_SHORT).show ();
        }
        }



    public void SignOut( final Context context){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void> () {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(context,"Successfully signed out",Toast.LENGTH_SHORT).show();
                        context.startActivity (new Intent (context, MainActivity.class));
                        //  context.finish();
                    }
                });

    }
    }

