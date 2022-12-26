package com.example.revision2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    //TextView tv;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //ActionBAr and its title
        actionBar=getSupportActionBar();
        actionBar.setTitle("Profile");
        firebaseAuth =firebaseAuth.getInstance();
        BottomNavigationView navigationView=findViewById(R.id.navigation);

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#545352"));
        // Set BackgroundDrawable
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(colorDrawable);

        navigationView.setOnItemSelectedListener(selectedListener);

// deafault on start
        actionBar.setTitle ("Start") ; //ch
        ProfileFragment fragment1 = new ProfileFragment ();
        FragmentTransaction ft1 = getSupportFragmentManager () .beginTransaction();
        ft1.replace(R.id.fragment_container, fragment1,  "");
        ft1.commit ();
//tv=findViewById(R.id.Textv);
    }

 public NavigationBarView.OnItemSelectedListener selectedListener =
          new NavigationBarView.OnItemSelectedListener(){

        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//handle item clicks
                    switch (menuItem.getItemId()) {

                      /*  case R.id.nav_home:
//home fragment transaction
                            actionBar.setTitle ("Home") ; //ch
                            Toast.makeText(DashboardActivity.this, "home", Toast.LENGTH_SHORT).show();
                            HomeFragment fragment1 = new HomeFragment ();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment1).commit();

                            return true;*/
                        case R.id.nav_profile:
//profile fragment transaction
                            actionBar.setTitle ("Profile") ; //ch
                            ProfileFragment fragment2 = new ProfileFragment ();
                            FragmentTransaction ft2 = getSupportFragmentManager () .beginTransaction();
                            ft2.replace(R.id.fragment_container, fragment2,  "");
                            ft2.commit ();
                            return true;
                        case R.id.nav_users:

                            actionBar.setTitle ("Users") ; //ch
                            UsersFragment fragment3 = new UsersFragment ();
                            FragmentTransaction ft3 = getSupportFragmentManager () .beginTransaction();
                            ft3.replace(R.id.fragment_container, fragment3,  "");
                            ft3.commit ();
                            return true;

                    }
                    return false;
                }};

    public boolean onCreateOptionsMenu (Menu menu) {
//inflating menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id=item.getItemId();
        if(id==R.id.action_logout){
          firebaseAuth.getInstance().signOut();
            checkUserStatus();

        }
        return super.onOptionsItemSelected(item);
    }
    private void checkUserStatus(){
        //get Current user
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){

        }else{
            startActivity(new Intent(DashboardActivity.this,MainActivity.class));
            finish();;
        }
    }
}