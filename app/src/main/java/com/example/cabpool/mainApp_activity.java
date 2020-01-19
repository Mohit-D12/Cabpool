package com.example.cabpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class mainApp_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    Fragment defaultFragment;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    SharedPreferences sharedPreferences;

    TextView username_navigationdrawer;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app_activity);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(getApplicationContext(), gso);



        defaultFragment = new CabpoolFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                defaultFragment).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationItemSelectedListener);


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);



        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.string_open, R.string.string_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);                  //displaying button to open navigation drawer
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

    }

    //switching between fragments via bottom navigation bar
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //switching fragments via bottomNavigationBar
                    Fragment selectedFragment = new CabpoolFragment();

                    switch(menuItem.getItemId()) {
                        case R.id.bottomNavigationBar:
                            selectedFragment = new CabpoolFragment();
                            break;
                        case R.id.drivers_bottomNavigationBar:
                            selectedFragment = new DriversFragment();
                            break;
                        case R.id.upcoming_bottomNavigationBar:
                            selectedFragment = new UpcomingFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;

                }
            };

    //for allowing drawer to open when we click the button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        username_navigationdrawer = drawerLayout.findViewById(R.id.username_navigationDrawer);
        sharedPreferences = getSharedPreferences("Users",MODE_PRIVATE);
        username_navigationdrawer.setText(sharedPreferences.getString("userName","defaultUser"));


        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    

    //for switching between fragments via navigation drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();


        if (id == R.id.home_navigationBar)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CabpoolFragment()).commit();
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        else if (id == R.id.profile_naviagationBar)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
            bottomNavigationView.setVisibility(View.INVISIBLE);
        }
        else if (id == R.id.about_navigationBar) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AboutFragment()).commit();
            bottomNavigationView.setVisibility(View.INVISIBLE);
        }
        else if (id == R.id.logOut_navigationBar)
        {
            FirebaseAuth mAuth= FirebaseAuth.getInstance();
            mAuth.signOut();
            revokeAccess();
            sharedPreferences = getSharedPreferences("Users",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userId",null);
            editor.commit();
            startActivity(new Intent(mainApp_activity.this,MainActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Toast.makeText(getApplicationContext(), "Logged out of Google successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
