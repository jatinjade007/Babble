package com.example.hp.babble.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hp.babble.R;
import com.example.hp.babble.sharedPreferences.LogIn_Pref;

public class UserProfileActivity extends AppCompatActivity {
    private TextView name,emailId;
    private SharedPreferences sharedPreferences;
    private String profileName,profileEmail;
    private Toolbar toolbar;
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name= (TextView) findViewById(R.id.textName);
        emailId= (TextView) findViewById(R.id.textEmailId);
        toolbar= (Toolbar) findViewById(R.id.userProfileToolbar);
        btnHome= (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, ChatTabs.class);
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //TO Set Tittle of Toolbar
        getSupportActionBar().setTitle("Profile");



        //Getting data from Shared Preferences
        sharedPreferences = getSharedPreferences(LogIn_Pref.Pref_Name, Context.MODE_PRIVATE);
        profileName=sharedPreferences.getString(LogIn_Pref.Name, null);
        profileEmail=sharedPreferences.getString(LogIn_Pref.EmailId,null);
        name.setText(profileName);
        emailId.setText(profileEmail);
    }
    //To go back on Previous Screen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
