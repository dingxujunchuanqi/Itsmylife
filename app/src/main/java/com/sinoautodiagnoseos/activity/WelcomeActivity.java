package com.sinoautodiagnoseos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sinoautodiagnoseos.R;

/**
 * Created by HQ_Demos on 2017/5/8.
 */

public class WelcomeActivity extends BaseFragmentActivity {
    private final long SPLASH_LENGTH = 2000;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_LENGTH);
    }
}
