package com.example.hp.babble.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hp.babble.R;
import com.example.hp.babble.sharedPreferences.LogIn_Pref;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String loggedInUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread=new Thread(){
            public void run()
            {
                super.run();
                try{
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    sharedPreferences = getSharedPreferences(LogIn_Pref.Pref_Name, Context.MODE_PRIVATE);
                    loggedInUserID = sharedPreferences.getString(LogIn_Pref.ApplicationUserId, null);
                    if (loggedInUserID!=null){
                        Intent intent=new Intent(SplashActivity.this,ChatTabs.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this, LogIn.class);
                        startActivity(intent);
                    }
                }
            }//End of Thread
        };

        thread.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
