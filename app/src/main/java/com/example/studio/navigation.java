package com.example.studio;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;

public class navigation extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigation;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        drawerLayout=findViewById(R.id.draw_lay);
        navigation=findViewById(R.id.navview);
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigation.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.home) {
                Intent ih = new Intent(navigation.this, navigation.class);
                startActivity(ih);
                finish();
            }
            if (id==R.id.python){
                Intent ip=new Intent(navigation.this, python_activity.class);
                startActivity(ip);
            }
            return true;
        }
        );}
}