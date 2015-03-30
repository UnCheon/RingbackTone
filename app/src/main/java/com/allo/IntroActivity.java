package com.allo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
                String username = pref.getString("username", "");
                String password = pref.getString("password", "");

                if (username.equals("") || password.equals("")) {

                    go_register();

                } else {
                    go_main();
                }
            }

        }, 1000);
    }


    private void go_register() {
//        Intent intent = new Intent(this, RegisterActivity.class);
        Intent intent = new Intent(this, AgreeActivity.class);
        startActivity(intent);
        finish();
    }

    private void go_main() {
//        Intent intent = new Intent(this, FriendListActivity.class);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

