package com.twild.gastracker;

/*
 * This class instantiates the login screen. The user is able to login using their Google account.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class ActivityLoginScreen extends AppCompatActivity implements View.OnClickListener {

    private Button buttonOtherGoogleSignIn;
    private SignInButton buttonGoogleSignIn;

    private EditText editTextEmailText;
    private EditText editTextPasswordText;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private GoogleApiClient googleApiClient;

    private static final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        // set the content view to the login screen

        buttonGoogleSignIn = (SignInButton) findViewById(R.id.button_sign_in_google);
        buttonOtherGoogleSignIn = (Button) findViewById(R.id.button_sign_in_email);

        editTextEmailText = (EditText) findViewById(R.id.edit_text_email);
        editTextPasswordText = (EditText) findViewById(R.id.edit_text_password);

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener()
                {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
                    {
                        Toast.makeText(ActivityLoginScreen.this, "Connection Failed", Toast.LENGTH_LONG).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        buttonGoogleSignIn.setOnClickListener(this);
        buttonOtherGoogleSignIn.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        firebaseAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    /* If the user is not null, that means the user is signed in and
                     * we can go to the next screen
                     */
                    finish();
                    // startActivity(new Intent(ActivityLoginScreen.this, CLASS THAT HAS THE CARS LIST.class));
                }
                else
                {
                    /*
                     * If we get here, then the user is not logged in, and since the state changed,
                     * then the user must have logged out
                     */
                    Log.d("logout", "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    @Override
    public void onStart()
    {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(firebaseAuthListener != null)
        {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view == buttonGoogleSignIn)
        {
            signInGoogle();
        }
        else if (view == buttonOtherGoogleSignIn)
        {
            registerUser();
        }

    }

    private void registerUser()
    {

        /*
         * First, we need to get the text from the boxes the user typed in.
         * After that, we need to make sure that there's actually text in the boxes.
         */
        String textEmail = editTextEmailText.getText().toString().trim();
        String textPassword = editTextPasswordText.getText().toString().trim();

        if (TextUtils.isEmpty(textEmail))
        {
            Toast.makeText(this, "Enter valid email address", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(textPassword))
        {
            Toast.makeText(this, "Enter password", Toast.LENGTH_LONG).show();
            return;
        }

        /*
         * If there's text in both of the boxes, put up a progress box telling the
         * user that we're registering them.
         */

        progressDialog.setMessage("Registering User...");
        progressDialog.show();


        /*
         * Start a thing to create the new user, and if it's successful, tell them that
         * they successfully registered.
         *
         *
         *
         * ADD ANOTHER WAY TO SIMPLY LOGIN WITH AN EMAIL AND PASSWORD, NOT JUST CREATE AN ACCOUNT
         * ALSO, ADD ANOTHER PASSWORD BOX TO CONFIRM THE PASSWORD
         *
         */

        firebaseAuth.createUserWithEmailAndPassword(textEmail, textPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(ActivityLoginScreen.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                            progressDialog.hide();
                        }

                    }
                });
    }

    private void signInGoogle()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else
            {
                // do something to tell the user that they didn't sign in correctly or something
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        Log.d("Authentication", "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Log.d("Authentication", "signInWithCredential:onComplete: " + task.isSuccessful());

                        if (!task.isSuccessful())
                        {
                            Log.w("Authentication", "signInWithCredential" + task.getException());
                            Toast.makeText(ActivityLoginScreen.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
