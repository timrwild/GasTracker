package com.twild.gastracker;

/*
 * This class instantiates the login screen. The user is able to login using their Google account.
 */

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseUser;

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
    protected void onCreate(Bundle savedInstanceState) {
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
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(ActivityLoginScreen.this, "Connection Failed", Toast.LENGTH_LONG).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        buttonGoogleSignIn.setOnClickListener(this);
        buttonOtherGoogleSignIn.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    /* If the user is not null, that means the user is signed in and
                     * we can go to the next screen
                     */
                    finish();
                    // startActivity(new Intent(ActivityLoginScreen.this, CLASS THAT HAS THE CARS LIST.class));
                }
                else {
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
    public void onClick(View view) {

    }
}
