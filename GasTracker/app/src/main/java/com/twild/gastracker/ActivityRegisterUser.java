package com.twild.gastracker;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import static com.twild.gastracker.ActivityLoginScreen.firebaseAuth;

public class ActivityRegisterUser extends AppCompatActivity
{


    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextConfirmPassword;

    Button buttonRegisterUser;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        editTextEmail = (EditText) findViewById(R.id.edit_text_register_user_email);
        editTextPassword = (EditText) findViewById(R.id.edit_text_register_user_password);
        editTextConfirmPassword = (EditText) findViewById(R.id.edit_text_register_user_confirm_password);

        buttonRegisterUser = (Button) findViewById(R.id.button_register_user);
        buttonRegisterUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerUser();
            }
        });

        progressDialog = new ProgressDialog(this);

    }

    public void registerUser()
    {
        /*
        * First, we need to get the text from the boxes the user typed in.
        * After that, we need to make sure that there's actually text in the boxes
        * and that the passwords match
        */
        String textEmail = editTextEmail.getText().toString().trim();
        String textPassword = editTextPassword.getText().toString().trim();
        String textConfirmPassword = editTextConfirmPassword.getText().toString().trim();

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
        if (!textPassword.equals(textConfirmPassword))
        {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
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
         * they successfully registered
         */

        firebaseAuth.createUserWithEmailAndPassword(textEmail, textPassword)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    progressDialog.dismiss();

                    if (task.isSuccessful())
                    {
                        Toast.makeText(ActivityRegisterUser.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(ActivityRegisterUser.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                });
    }

}
