package com.orgwork.renewed;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Acitivity.Fragment.AgendaFragment;
import Acitivity.Fragment.HomeFragment;
import Acitivity.Fragment.ProfileFragment;
import Class.Conexao;

public class Menu extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

    }


    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
         new BottomNavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 Fragment selectedFragment = null;

                 switch (item.getItemId()){
                     case R.id.nav_inicio:
                         selectedFragment = new HomeFragment();
                         break;
                     case R.id.nav_perfil:
                         selectedFragment = new ProfileFragment();
                         break;
                     case R.id.nav_agenda:
                         selectedFragment = new AgendaFragment();
                         break;
                 }
                 getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                         selectedFragment).commit();

                 return true;
             }
         };

    @Override
    public void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();

    }
}