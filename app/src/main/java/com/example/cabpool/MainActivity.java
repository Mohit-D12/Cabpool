package com.example.cabpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button googleSignInButton_loginScreen_java, loginButton_loginScreen_java;
    EditText username_loginScreen_java, password_loginScreen_java;
    TextView signUp_loginScreen_java;
    String tag="abc";
    int RC_SignIn=5;
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    ProgressDialog mProgress;
    DatabaseReference mDatabase;
    String name="";
    TextView forgotPassword_loginScreen_java;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleSignInButton_loginScreen_java=findViewById(R.id.signInWithGoogle_loginScreen_xml);
        mAuth=FirebaseAuth.getInstance();
        signUp_loginScreen_java=findViewById(R.id.signUp_loginScreen_xml);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mProgress= new ProgressDialog(this);
        username_loginScreen_java=findViewById(R.id.username_loginScreen_xml);
        password_loginScreen_java=findViewById(R.id.password_loginScreen_xml);
        loginButton_loginScreen_java=findViewById(R.id.loginButton_loginScreen_xml);
        forgotPassword_loginScreen_java=findViewById(R.id.forgotPassword_loginScreen_xml);

        forgotPassword_loginScreen_java.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this, forgotPassword_activity.class);
                startActivity(i);
            }
        });


        signUp_loginScreen_java.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this, register_activity.class);
                startActivity(i);
            }
        });

        loginButton_loginScreen_java.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();
            }
        });



        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(getApplicationContext(), gso);

        googleSignInButton_loginScreen_java.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SignIn);

    }
    public void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                firebaseAuthWithGoogle(account);
            }
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Log.w(tag, "Google sign in failed", e);
            // ...
        }

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.d(tag, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(tag, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid= mAuth.getCurrentUser().getUid();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                            databaseReference.child(uid).child("name").setValue(user.getDisplayName());
                            sharedPreferences = getSharedPreferences("Users",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId",uid);
                            editor.commit();

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(tag, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent i = new Intent(MainActivity.this, mainApp_activity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(user != null) {
            mProgress.setMessage("Signing In, please Wait");
            mProgress.show();
            startActivity(i);

        }

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    void startSignIn()
    {
        //Getting fields from user
        String email=username_loginScreen_java.getText().toString();
        String password=password_loginScreen_java.getText().toString();
        if(email.isEmpty()||password.isEmpty()) Toast.makeText(MainActivity.this,"Fields cannot be Empty, please enter credentials", Toast.LENGTH_SHORT).show();
        else {//Signing in E-mail and Password

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser=mAuth.getCurrentUser();
                        String uid= mAuth.getCurrentUser().getUid();
                        sharedPreferences = getSharedPreferences("Users",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId",uid);
                        editor.commit();
                        if(currentUser.isEmailVerified())
                            updateUI(currentUser);
                        else Toast.makeText(getApplicationContext(),"Please vdrify your E-mail", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(getApplicationContext(), "Sign-in Error, please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
