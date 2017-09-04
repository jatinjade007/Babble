package com.example.hp.babble.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.babble.R;
import com.example.hp.babble.adapter.MyPageAdapter;
import com.example.hp.babble.sharedPreferences.LogIn_Pref;

import java.util.ArrayList;

public class ChatTabs extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyPageAdapter myPageAdapter;
  //  private ListView drawerList;
    private ArrayAdapter<String> arrayAdapter;
    //private ActionBarDrawerToggle actionBarDrawerToggle;
   // private DrawerLayout drawerLayout;
    private TextView txtName;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_tabs);
        //findViewByd
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        viewPager= (ViewPager) findViewById(R.id.viewPager);

        //Add Tabs in the Layout
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Requests"));
        tabLayout.addTab(tabLayout.newTab().setText("Members"));
        myPageAdapter=new MyPageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        //For Swiping The Fragments In Activity
        viewPager.setAdapter(myPageAdapter);
        //By Clicking on Tabs
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*
        //Navigation Drawer
        txtName= (TextView) findViewById(R.id.txtName);
        sharedPreferences = getSharedPreferences(LogIn_Pref.Pref_Name, Context.MODE_PRIVATE);
        name=sharedPreferences.getString(LogIn_Pref.Name, null);
        txtName.setText(name);
        */


     //   drawerList = (ListView) findViewById(R.id.navList);
       // drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
       setSupportActionBar(toolbar);



        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);


        switch (item.getItemId()) {

            case R.id.profile:
               // Toast.makeText(MainActivity.this, "Item 1 is clicked", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ChatTabs.this,UserProfileActivity.class));
                return true;
            case R.id.logout:
                //Toast.makeText(MainActivity.this, "Item 2 is clicked", Toast.LENGTH_LONG).show();
                //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure, Do you want to logout?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent=new Intent(ChatTabs.this,LogIn.class);
                                startActivity(intent);
                                finish();
                                sharedPreferences = getSharedPreferences(LogIn_Pref.Pref_Name, Context.MODE_PRIVATE);
                                sharedPreferencesEditor=sharedPreferences.edit();
                                sharedPreferencesEditor.clear();
                                sharedPreferencesEditor.commit();
                                Toast.makeText(ChatTabs.this,"User Logged Out",Toast.LENGTH_LONG).show();

                                return;

                                // Toast.makeText(com.example.admin.splashscreen.activity.LogoutActivity.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            case R.id.exit:
                alertDialogBuilder.setMessage("Are you sure, Do you want to Exit?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog1 = alertDialogBuilder.create();
                alertDialog1.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
